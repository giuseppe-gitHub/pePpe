package it.gius.pePpe.testSuit.propertyGui.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

public abstract class AbstractGuiPropertyEditor extends PropertyEditorSupport {

	protected PropertyEditor containedEditor;
	private PropertyChangeSupport changeSupport;

	public AbstractGuiPropertyEditor() {
		changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(Object oldValue, Object newValue) {
		changeSupport.firePropertyChange(null, oldValue, newValue);

	}

	@Override
	public String getAsText() {
		return containedEditor.getAsText();
	}

	@Override
	public String getJavaInitializationString() {
		return containedEditor.getJavaInitializationString();
	}

	@Override
	public String[] getTags() {
		return containedEditor.getTags();
	}

	@Override
	public Object getValue() {
		return containedEditor.getValue();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		containedEditor.setAsText(text);
		updateView();
	}

	@Override
	public void setValue(Object value) {
		containedEditor.setValue(value);
		updateView();
	}
	
	protected abstract void updateView();

	protected void updateValueAsText(String textValue)
	{
		Object oldValue = containedEditor.getValue();
		try {
			containedEditor.setAsText(textValue);
		} catch(IllegalArgumentException e){
		}finally {
			Object newValue = getValue();
			if(!newValue.equals(oldValue))
			{
				firePropertyChange(oldValue, newValue);
			}
		}
	}


	protected void updateValue(Object value)
	{
		Object oldValue = containedEditor.getValue();
		try {
			containedEditor.setValue(value);
		} catch(IllegalArgumentException e){
		}finally {
			Object newValue = getValue();
			if(!newValue.equals(oldValue))
			{
				firePropertyChange(oldValue, newValue);
			}
		}
	}

	
	@Override
	public abstract boolean supportsCustomEditor();

	@Override
	public abstract Component getCustomEditor();

	@Override
	public abstract boolean isPaintable();

	@Override
	public abstract void paintValue(Graphics gfx, Rectangle box);



}
