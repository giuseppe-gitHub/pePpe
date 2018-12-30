package it.gius.pePpe.testSuit.propertyGui;

import javax.swing.JFrame;

import org.springframework.beans.BeanWrapper;

import it.gius.pePpe.testSuit.property.IProperties;
import it.gius.pePpe.testSuit.propertyGui.StructuresInitializer.PropertyStructures;
import it.gius.pePpe.testSuit.propertyGui.sheet.FrameInitializationException;
import it.gius.pePpe.testSuit.propertyGui.sheet.FrameInitializer2;

public class GuiPropertySet {
	
	private BeanWrapperBuilder builder = new BeanWrapperBuilder();
	private StructuresInitializer structuresInitializer = new StructuresInitializer();
	private FrameInitializer2 frameInitializer = new FrameInitializer2();
	
	public GuiPropertySet() {
	}
	
	public JFrame setPropertyWithGui(String simulationName, int x, int y, IProperties propertiesBean, boolean resetBean) throws GuiPropertiesException
	{
		if(resetBean)
			propertiesBean.toDefaultValues();
		
		BeanWrapper beanWrapper;
		PropertyStructures structures;
		try {
			beanWrapper = builder.buildNewWrapper(propertiesBean);
			structures = structuresInitializer.initStructures(beanWrapper);
		} catch (Exception e1) {
			throw new GuiPropertiesException(e1.getMessage(),e1);
		}
		
		JFrame frame = null;
		try {
			frame = frameInitializer.getNewFrame(simulationName, x, y, propertiesBean, beanWrapper,
					structures.provertyValues, structures.editors, structures.size);
		} catch (FrameInitializationException e) {
			throw new GuiPropertiesException(e.getMessage(),e);
		}
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    frame.pack();
	    
		frame.setVisible(true);

		return frame;
		
	}

}
