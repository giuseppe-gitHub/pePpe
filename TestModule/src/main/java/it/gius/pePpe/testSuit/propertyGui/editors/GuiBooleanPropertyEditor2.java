package it.gius.pePpe.testSuit.propertyGui.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;

public class GuiBooleanPropertyEditor2 extends AbstractGuiPropertyEditor implements ActionListener{

	public GuiBooleanPropertyEditor2() {
		super.containedEditor = new CustomBooleanEditor(false);
	}
	
	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	protected JCheckBox checkBox = null;

	@Override
	public Component getCustomEditor() {
		if(checkBox == null)
		{
			checkBox = new JCheckBox();
			Object value = getValue();
			if(value instanceof Boolean)
			{
				boolean selected = (Boolean)value;
				checkBox.setSelected(selected);
				checkBox.setText(Boolean.toString(selected));
			}
			checkBox.addActionListener(this);
			checkBox.setHorizontalAlignment(SwingConstants.CENTER);

		}

		return checkBox;
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
		Boolean selected = checkBox.isSelected();
		checkBox.setText(Boolean.toString(selected));
		
		super.updateValue(selected);
		
	}

	
	@Override
	protected void updateView() {
		Object value = getValue();
		if(value instanceof Boolean)
		{
			Boolean selected = (Boolean)value;
			checkBox.setSelected(selected);
			checkBox.setText(Boolean.toString(selected));
		}

	}
}
