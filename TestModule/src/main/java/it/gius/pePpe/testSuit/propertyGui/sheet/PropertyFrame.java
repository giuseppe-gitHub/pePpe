package it.gius.pePpe.testSuit.propertyGui.sheet;

import it.gius.pePpe.testSuit.property.IProperties;
import it.gius.pePpe.testSuit.propertyGui.editors.PropertyEditorConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditor;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyValue;

public class PropertyFrame extends JFrame {

	private static final long serialVersionUID = 7831453593502794249L;

	private BeanWrapper beanWrapper;
	private IProperties bean;
	private JPanel propertyContainers[];
	private PropertyValue[] propertyValues;
	private PropertyEditor[] editors;
	private int propertySize;
	
	private Color defaultPanelColor;

	public PropertyFrame(IProperties bean, BeanWrapper beanWrapper, PropertyValue[] propertyValues, PropertyEditor[] editors, int propertySize) {
		this.bean = bean;
		this.beanWrapper = beanWrapper;
		this.propertyValues = propertyValues;
		this.editors = editors;
		this.propertySize = propertySize;

	}

	public void init(String simName, int x, int y) throws FrameInitializationException
	{
		this.setTitle(simName + " properties");
		this.setLocation(x, y);

		//this.add(propertyComponent);
		JPanel macroPanel = new JPanel();
		macroPanel.setLayout(new BoxLayout(macroPanel, BoxLayout.Y_AXIS));

		propertyContainers = new JPanel[propertySize];

		for(int i=0; i<propertySize; i++)
		{
			propertyContainers[i] = new JPanel(new BorderLayout(PropertyEditorConstants.HGAP,PropertyEditorConstants.VGAP));
			JPanel panel = propertyContainers[i];
			panel.setBorder(new TitledBorder(propertyValues[i].getName()));
			panel.add(editors[i].getCustomEditor());
			macroPanel.add(panel);
		}
		
		defaultPanelColor = propertyContainers[0].getBackground();

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton defaultButton = new JButton("Default");
		BeanToDefault toDefault = new BeanToDefault();
		defaultButton.addActionListener(toDefault);
		buttonPanel.add(defaultButton);

		BeanApplyModification beanApplyModification = null;
		try {
			beanApplyModification = new BeanApplyModification();
		} catch (Exception e) {
			throw new FrameInitializationException("Fail to initialize frame", e);
		} 

		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(beanApplyModification);
		buttonPanel.add(applyButton);

		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new FrameCloser(this));
		buttonPanel.add(doneButton);


		macroPanel.add(buttonPanel);

		this.add(macroPanel);

		
		for(int i=0; i<propertySize; i++)
		{
			try {
				Object value = beanWrapper.getPropertyValue(propertyValues[i].getName());
				editors[i].setValue(value);
				propertyValues[i].setConvertedValue(value);
				updateSuccessful(i);
			} catch (BeansException e) {
				errorOnUpdate(i);
			}
		}

	}
	

	private static class FrameCloser implements ActionListener{

		private JFrame frame;

		public FrameCloser(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
	
	
	private void errorOnUpdate(int propertyNumber)
	{
		propertyContainers[propertyNumber].setBackground(Color.RED);
	}

	private void updateSuccessful(int propertyNumber)
	{
		propertyContainers[propertyNumber].setBackground(defaultPanelColor);
	}
	

	private class BeanApplyModification implements ActionListener
	{
		//private IProperties propBean;
		//private IProperties beanValidationInstance;
		//private BeanWrapper validationWrapper;

		public BeanApplyModification() 
				{
			//this.propBean = propBean;
			//beanValidationInstance = createValidationInstance(propBean);
			//validationWrapper = new BeanWrapperImpl(beanValidationInstance);

		}

		/*private IProperties createValidationInstance(IProperties originalBean) throws NoSuchMethodException, IllegalAccessException, 
		InstantiationException, IllegalArgumentException
		{
			Class<? extends IProperties> beanClass = originalBean.getClass();
			Constructor<? extends IProperties> ctor = beanClass.getConstructor(new Class<?>[]{});
			if(ctor == null)
				throw new IllegalArgumentException("Properties bean should have a constructor with no arguments");

			return beanClass.newInstance();

		}*/

		@Override
		public void actionPerformed(ActionEvent e) {
			setValues(beanWrapper);

		}

		private void setValues(BeanWrapper wrapper) 
		{

			for(int i=0; i<propertySize; i++)
			{
				String name = propertyValues[i].getName();
				Object value = propertyValues[i].getValue();
				Object convValue = propertyValues[i].getConvertedValue();
				
				if(convValue != null && !convValue.equals(value))
					value = convValue;

				try {
					wrapper.setPropertyValue(name, value);

					Object newValue = wrapper.getPropertyValue(name);
					editors[i].setValue(newValue);
					updateSuccessful(i);		
				}catch(PropertyAccessException pae){
					PropertyChangeEvent pce =pae.getPropertyChangeEvent();
					Object oldValue = pce.getOldValue();
					if(oldValue != null)
					{
						editors[i].setValue(oldValue);
						propertyValues[i].setConvertedValue(oldValue);
					}
					errorOnUpdate(i);
				} catch (BeansException e) {
					errorOnUpdate(i);
				}

			}
		}


	}

	private class BeanToDefault implements ActionListener{


		public BeanToDefault() {
		
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setToDefault();
		}

		public void setToDefault()
		{
			bean.toDefaultValues();

			for(int i=0; i<propertySize; i++)
			{
				Object value = beanWrapper.getPropertyValue(propertyValues[i].getName());
				editors[i].setValue(value);
				propertyValues[i].setConvertedValue(value);
				updateSuccessful(i);
			}

		}
		
		
	}

}
