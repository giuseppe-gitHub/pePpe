package it.gius.pePpe.testSuit.propertyGui.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

public abstract class GuiNumberPropertyEditor2 extends AbstractGuiPropertyEditor implements DocumentListener {

	protected int columnsTextField;
	protected  NumberFormat numberFormat;

	public GuiNumberPropertyEditor2(int columnsTextField) {
		this.columnsTextField = columnsTextField;
	}
	
	public void initContainedEditor(Class<?> numberClass, NumberFormat numberFormat, boolean allowEmpty)
	{
		this.containedEditor = new CustomNumberEditor(numberClass, numberFormat, allowEmpty);
		this.numberFormat = numberFormat;
	}

	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	protected JFormattedTextField inputTextField = null;


	@Override
	public Component getCustomEditor() {
		if(inputTextField == null)
		{
			inputTextField = new JFormattedTextField(numberFormat);
			inputTextField.setColumns(this.columnsTextField);
			inputTextField.setValue(getValue());
			inputTextField.getDocument().addDocumentListener(this);

		}

		return inputTextField;
	}


	@Override
	public boolean isPaintable() {
		return false;
	}

	@Override
	public void paintValue(Graphics gfx, Rectangle box) {
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		//textChanged(e);

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);

	}

	private void textChanged(DocumentEvent ev) {
		String textValue = null;
		try {
			textValue = ev.getDocument().getText(0, ev.getDocument().getLength());
		} catch (BadLocationException e1) {
			return;
		} 
		
		super.updateValueAsText(textValue);
	}


	@Override
	protected void updateView() {
		String text = super.getAsText();
		inputTextField.setText(text);
	}

}
