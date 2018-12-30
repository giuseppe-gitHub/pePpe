package it.gius.pePpe.testSuit.propertyGui.registrar;



import it.gius.pePpe.testSuit.propertyGui.editors.GuiBooleanPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiFloatPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiIntPropertyEditor2;
import it.gius.pePpe.testSuit.propertyGui.editors.GuiStringPropertyEditor2;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public class GuiPropertyEditorRegistrar2  implements PropertyEditorRegistrar {

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {

		registry.registerCustomEditor(int.class, new GuiIntPropertyEditor2());
		registry.registerCustomEditor(float.class, new GuiFloatPropertyEditor2());
		registry.registerCustomEditor(boolean.class, new GuiBooleanPropertyEditor2());
		registry.registerCustomEditor(String.class, new GuiStringPropertyEditor2());

	}
}