package it.gius.pePpe.testSuit.propertyGui.editors;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class GuiFloatPropertyEditor2 extends GuiNumberPropertyEditor2 {
	

	public GuiFloatPropertyEditor2() {
		super(PropertyEditorConstants.NUM_COLUMNS);
		
		NumberFormat floatFormat = new DecimalFormat("0.0####", new DecimalFormatSymbols(Locale.US));
		
		super.initContainedEditor(Float.class, floatFormat, false);
	}
}
