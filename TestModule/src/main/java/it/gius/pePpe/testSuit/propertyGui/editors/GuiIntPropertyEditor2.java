package it.gius.pePpe.testSuit.propertyGui.editors;

import java.text.NumberFormat;
import java.util.Locale;

public class GuiIntPropertyEditor2 extends GuiNumberPropertyEditor2{
	
	
	public GuiIntPropertyEditor2() {
		super(PropertyEditorConstants.NUM_COLUMNS);
		
		NumberFormat integerFormat = NumberFormat.getIntegerInstance(Locale.US);
		
		super.initContainedEditor(Integer.class, integerFormat, false);
	}

}
