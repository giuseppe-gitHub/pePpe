package it.gius.pePpe.testSuit.propertyGui;

import it.gius.pePpe.testSuit.propertyGui.StructuresInitializer.PropertyStructures;
import it.gius.pePpe.testSuit.propertyGui.sheet.FrameInitializationException;
import it.gius.pePpe.testSuit.propertyGui.sheet.FrameInitializer2;
import it.gius.pePpe.testSuit.simulations.boxStack.BoxStackProperties;
import it.gius.pePpe.testSuit.simulations.distance.DistanceProperties;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.springframework.beans.BeanWrapper;



@SuppressWarnings("all")
public class SpringTestMain2 {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		} 
		
		//BoxStackProperties bean = new BoxStackProperties();
		DistanceProperties bean = new DistanceProperties();
		bean.toDefaultValues();
		
		
		BeanWrapperBuilder builder = new BeanWrapperBuilder();
		BeanWrapper beanWrapper = builder.buildNewWrapper(bean);

		StructuresInitializer structuresInitializer = new StructuresInitializer();
		
		PropertyStructures s = structuresInitializer.initStructures(beanWrapper);
	
		FrameInitializer2 frameInitializer = new FrameInitializer2();
		JFrame frame = null;
		try {
			frame = frameInitializer.getNewFrame("testGui", 100, 100, bean, beanWrapper, s.provertyValues, s.editors, s.size);
		} catch (FrameInitializationException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    frame.pack();
	    
		frame.setVisible(true);

	}

}
