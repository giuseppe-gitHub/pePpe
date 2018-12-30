package it.gius.pePpe.testSuit.propertyGui.changeListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.beans.PropertyValue;

public class PropertyChangeSetter implements PropertyChangeListener {

	private PropertyValue pv;
	
	public PropertyChangeSetter(PropertyValue pv) {
		this.pv = pv;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object oldValue  =evt.getOldValue();
		Object newValue = evt.getNewValue();
		
		String propertyName = evt.getPropertyName();
		
		if(propertyName == null || propertyName.compareTo(pv.getName()) == 0)
		{
			if(oldValue == null || !oldValue.equals(newValue))
			{
				//insert validation here and say to the frame to disable apply
				//if validation fail
				pv.setConvertedValue(newValue);
			}
		}
	}

}
