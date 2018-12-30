package it.gius.pePpe.testSuit.propertyGui;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanWrapperBuilder {
	
	//private PropertyEditorRegistrar registrar = new GuiPropertyEditorRegistrar2();
	
	public BeanWrapper buildNewWrapper(Object bean)
	{
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);
		
		//registrar.registerCustomEditors(beanWrapper);
		
		beanWrapper.setExtractOldValueForEditor(false);
		
		return beanWrapper;
	}

}
