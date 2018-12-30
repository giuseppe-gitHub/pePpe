package it.gius.pePpe.testSuit.propertyGui.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.Document;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;

public class GuiStringPropertyEditor2 extends AbstractGuiPropertyEditor implements DocumentListener{

	private int columnsTextField = PropertyEditorConstants.STRING_COLUMNS;

	private String fieldText = null;

	public GuiStringPropertyEditor2() {
		this.containedEditor = new StringTrimmerEditor(false);
	}

	protected JFormattedTextField inputTextField = null;


	@Override
	public Component getCustomEditor() {
		if(inputTextField == null)
		{
			DefaultFormatter formatter = new DefaultFormatter();
			formatter.setOverwriteMode(false);
			inputTextField = new JFormattedTextField(formatter);
			inputTextField.setColumns(this.columnsTextField);
			
			fieldText = new String(getAsText());
			inputTextField.setValue(fieldText);
			inputTextField.getDocument().addDocumentListener(this);

		}

		return inputTextField;
	}



	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public boolean isPaintable() {
		return false;
	}

	@Override
	public void paintValue(Graphics gfx, Rectangle box) {

	}


	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);

	}


	@Override
	public void changedUpdate(DocumentEvent e) {
		//textChanged(e);

	}

	private void textChanged(DocumentEvent ev) {
		String textValue = null;
		try {
			 Document document = ev.getDocument();
			textValue = document.getText(0, document.getLength());
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
