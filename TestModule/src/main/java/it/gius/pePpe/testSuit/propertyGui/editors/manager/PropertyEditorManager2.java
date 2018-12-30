package it.gius.pePpe.testSuit.propertyGui.editors.manager;

import it.gius.pePpe.testSuit.propertyGui.editors.GuiBooleanPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiEnumPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiFloatPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiIntPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiStringPropertyEditor2;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.BeanWrapper;


public class PropertyEditorManager2 {
	private Map<Class<?>, Class<?>> editorClassMap;


	public PropertyEditorManager2() {

		editorClassMap = new Hashtable<Class<?>, Class<?>>();

		editorClassMap.put(int.class, GuiIntPropertyEditor2.class);
		editorClassMap.put(float.class, GuiFloatPropertyEditor2.class);
		editorClassMap.put(String.class, GuiStringPropertyEditor2.class);
		editorClassMap.put(boolean.class, GuiBooleanPropertyEditor2.class);
	}


	public PropertyEditor createNewEditor(BeanWrapper beanWrapper, PropertyDescriptor descriptor)
	{
		
		Class<?> propertyType = descriptor.getPropertyType();
		
		try {
			if(propertyType.isEnum())
			{
				Object source = beanWrapper.getPropertyValue(descriptor.getDisplayName());
				PropertyEditor editor = new GuiEnumPropertyEditor2(source);
				/*IExtendedPropertyEditor editor = new GuiEnumPropertyEditor();
				editor.setBeanWrapper(beanWrapper);
				editor.setPropertyName(descriptor.getDisplayName());
				editor.getCustomEditor();
				editor.updateValue();*/
				//return editor;
				return editor;
			}
		} catch (Exception e1) {
			return null;
		}

		Class<?> editorClass = editorClassMap.get(propertyType);
		if(editorClass == null)		
			return null;

		
		try {
			PropertyEditor editor = (PropertyEditor)editorClass.newInstance();
		
			return editor;
		} catch (Throwable e) {
			return null;
		}
	}
}
