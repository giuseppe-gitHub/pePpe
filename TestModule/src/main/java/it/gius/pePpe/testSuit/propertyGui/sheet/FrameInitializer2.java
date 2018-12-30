package it.gius.pePpe.testSuit.propertyGui.sheet;

import java.beans.PropertyEditor;

import javax.swing.JFrame;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyValue;

import it.gius.pePpe.testSuit.property.IProperties;

public class FrameInitializer2 {



	public JFrame getNewFrame(String simulationName, int x, int y, IProperties bean, BeanWrapper beanWrapper,
			PropertyValue[] propertyValues, PropertyEditor[] editors, int propertySize) throws FrameInitializationException
			{
		PropertyFrame frame = new PropertyFrame(bean, beanWrapper, propertyValues, editors, propertySize);

		frame.init(simulationName, x, y);

		return frame;
			}
}
