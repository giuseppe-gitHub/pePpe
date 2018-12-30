package it.gius.pePpe.testSuit.propertyGui.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings({"rawtypes","unchecked"})
public class GuiEnumPropertyEditor2 extends AbstractGuiPropertyEditor implements ActionListener {

	private Enum source;
	private Class propertyType;


	public GuiEnumPropertyEditor2(Object source) throws IllegalArgumentException {
		if(!(source instanceof Enum))
			throw new IllegalArgumentException();

		this.source = (Enum)source;
		this.propertyType = (Class)this.source.getClass();

		super.containedEditor = new PropertyEditorSupport();
	}


	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	protected JPanel panel = null;
	protected JRadioButton[] buttons = null;

	@Override
	public Component getCustomEditor() {
		if(panel == null)
		{

			Enum[] constants = (Enum[])propertyType.getEnumConstants();


			buttons = new JRadioButton[constants.length];
			ButtonGroup buttonGroup = new ButtonGroup();

			panel = new JPanel(new GridLayout(1,0));

			//Object currValue = beanWrapper.getPropertyValue(propertyName);
			for(int i=0; i<constants.length; i++)
			{

				Enum name = Enum.valueOf(propertyType, constants[i].toString());
				buttons[i] = new JRadioButton(name.toString());
				buttons[i].setActionCommand(name.toString());

				//if(constants.toString().compareTo(currValue.toString()) == 0)
				//buttons[i].setSelected(true);

				buttons[i].addActionListener(this);
				buttonGroup.add(buttons[i]);

				panel.add(buttons[i]);
			}



		}

		return panel;
	}

	@Override
	public boolean isPaintable() {
		return false;
	}

	@Override
	public void paintValue(Graphics gfx, Rectangle box) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Enum value = Enum.valueOf(propertyType, e.getActionCommand());

		super.updateValue(value);

	}

	@Override
	protected void updateView() {
		Object newValue = super.getValue();
		for(int i=0; i<buttons.length; i++)
			if(buttons[i].getText().compareTo(newValue.toString()) == 0)
				buttons[i].setSelected(true);
	}
}
