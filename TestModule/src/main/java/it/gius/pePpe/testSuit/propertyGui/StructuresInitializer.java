package it.gius.pePpe.testSuit.propertyGui;

import it.gius.data.structures.HashSet;
import it.gius.pePpe.testSuit.propertyGui.changeListener.PropertyChangeSetter;
import it.gius.pePpe.testSuit.propertyGui.editors.manager.PropertyEditorManager2;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyValue;

public class StructuresInitializer {

	private PropertyEditorManager2 manager = new PropertyEditorManager2();
	
	public static class PropertyStructures{
		public PropertyValue[] provertyValues;
		public PropertyEditor[] editors;
		
		public int size = 0;
	}
	
	public PropertyStructures initStructures(BeanWrapper beanWrapper)
	{
		
		PropertyDescriptor[] descriptorIn = beanWrapper.getPropertyDescriptors();
		
		
		HashSet<PropertyValue> propertyValuesOut = new HashSet<PropertyValue>(PropertyValue.class);
		HashSet<PropertyEditor> editorOut = new HashSet<PropertyEditor>(PropertyEditor.class);
		
		int sizeOut = 0;
		for(int i=0; i< descriptorIn.length; i++)
		{
			PropertyDescriptor descriptor = descriptorIn[i];
			boolean readable = beanWrapper.isReadableProperty(descriptor.getDisplayName());
			boolean writable = beanWrapper.isWritableProperty(descriptor.getDisplayName());
			
			if(readable && writable && descriptorIn[i].getDisplayName().compareTo("class") != 0)
			{
				//PropertyEditor editorTemp = beanWrapper.findCustomEditor(descriptor.getPropertyType(), descriptor.getDisplayName());
				PropertyEditor editorTemp = manager.createNewEditor(beanWrapper, descriptor);
				if(editorTemp != null && editorTemp.supportsCustomEditor())
				{
					String propertyName = descriptor.getDisplayName();
					PropertyValue pv = new PropertyValue(propertyName, beanWrapper.getPropertyValue(propertyName));
					propertyValuesOut.add(pv);
					
					PropertyChangeSetter setterListener = new PropertyChangeSetter(pv);
					editorTemp.addPropertyChangeListener(setterListener);
					
					editorOut.add(editorTemp);
					sizeOut++;
				}
			}
		}
		
		PropertyStructures result = new PropertyStructures();
		result.provertyValues = propertyValuesOut.elements;
		result.editors= editorOut.elements;
		result.size = sizeOut;
		
		return result;
	}
}
