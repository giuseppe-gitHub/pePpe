package it.gius.pePpe.configuration;

import it.gius.pePpe.aabb.AABBUpdater;
import it.gius.pePpe.aabb.IAABBManager;
//import it.gius.pePpe.distance.IDistance;
import it.gius.pePpe.collision.ICollision;
import it.gius.pePpe.contact.ContactManager;
import it.gius.pePpe.contact.ContactManagerInit;
import it.gius.pePpe.distance.IDistance2;
import it.gius.pePpe.integrator.IDoubleStepIntegrator;
import it.gius.pePpe.integrator.ISingleStepIntegrator;
import it.gius.pePpe.simulator.ISimulator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigurationFactory {


	private XmlBeanFactory springBeanFactory;
	//private Properties properties = new Properties();

	private ClassPathResource xmlResource;
	private ClassPathResource defaultPropertiesResource;

	private ClassPathResource userPropertiesResource = null;

	private IAABBManager aabbManager;
	private AABBUpdater aabbUpdater;

	//private IDistance distance;
	
	private IDistance2 distance2;
	
	private ICollision collision;
	
	private ContactManager contactManager;

	private ISimulator simulator;

	private ISingleStepIntegrator singleStepIntegrator;
	private IDoubleStepIntegrator doubleStepIntegrator;
	
	private ContactManagerInit contactManagerInit;
	
	private static final String xmlBeanFileDefaultName = "beans-pePpe.xml"; 
	private static final String propertiesFileDefaultName = "defaultContext.properties";


	private boolean configured = false;


	private static Logger logger = Logger.getLogger(ConfigurationFactory.class);


	public ConfigurationFactory() {
	}


	public boolean init() throws Exception
	{
		if(configured)
			return false;

		xmlResource = new ClassPathResource(xmlBeanFileDefaultName);
		realInit();

		configured = true;
		return true;
	}


	public boolean init(String propertiesSourceFile) throws Exception
	{
		if(configured)
			return false;

		xmlResource = new ClassPathResource(xmlBeanFileDefaultName);
		userPropertiesResource = new ClassPathResource(propertiesSourceFile);
		realInit();

		configured = true;
		return true;

	}

	public boolean init(String springSourceFile, String propertiesSourceFile) throws Exception
	{
		if(configured)
			return false;

		xmlResource = new ClassPathResource(springSourceFile);
		userPropertiesResource = new ClassPathResource(propertiesSourceFile);
		realInit();

		configured  = true;
		return true;
	}

	private void realInit() throws Exception
	{
		defaultPropertiesResource = new ClassPathResource(propertiesFileDefaultName);
		String xmlFile = xmlResource.getFilename();

		String defPropFile = defaultPropertiesResource.getFilename();
		logger.info("xml file: " + xmlFile);
		logger.info("default properties file: " + defPropFile);

		if(userPropertiesResource != null)
		{
			String userPropertiesFile = userPropertiesResource.getFilename();
			logger.info("user properties file: " + userPropertiesFile);
		}

		springBeanFactory = new XmlBeanFactory(xmlResource);
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		
		Resource[] resources = null;
		
		if(userPropertiesResource != null)
		{
			resources = new Resource[2];
			resources[0] = defaultPropertiesResource;
			resources[1] = userPropertiesResource;
		}
		else
		{
			resources = new Resource[1];
			resources[0] = defaultPropertiesResource;
		}
		
		configurer.setLocations(resources);
		configurer.postProcessBeanFactory(springBeanFactory);

		/*properties.load(defaultPropertiesResource.getInputStream());
		
		if(userPropertiesResource != null)
			properties.load(userPropertiesResource.getInputStream());*/
		

		aabbManager = (IAABBManager)springBeanFactory.getBean("aabbManager");
		logger.info("aabbManager: " + aabbManager.getClass().getCanonicalName());
		
		aabbUpdater = (AABBUpdater)springBeanFactory.getBean("aabbUpdater");
		logger.info("aabbUpdater: " + aabbUpdater.getClass().getCanonicalName());

		//distance = (IDistance)springBeanFactory.getBean("distance");
		//logger.info("distance: " + distance.getClass().getCanonicalName());
		
		distance2 = (IDistance2)springBeanFactory.getBean("distance2");
		logger.info("distance2: " + distance2.getClass().getCanonicalName());
		
		collision = (ICollision)springBeanFactory.getBean("collision");
		logger.info("collision: " + collision.getClass().getCanonicalName());

		simulator= (ISimulator)springBeanFactory.getBean("simulator");
		logger.info("simulator: " + simulator.getClass().getCanonicalName());

		singleStepIntegrator = (ISingleStepIntegrator)springBeanFactory.getBean("singleStepIntegrator");
		logger.info("singleStepIntegrator: " + singleStepIntegrator.getClass().getCanonicalName());

		doubleStepIntegrator = (IDoubleStepIntegrator)springBeanFactory.getBean("doubleStepIntegrator");
		logger.info("doubleStepIntegrator: " + doubleStepIntegrator.getClass().getCanonicalName());
		
		contactManager = (ContactManager)springBeanFactory.getBean("contactManager");
		logger.info("contactManager: " + contactManager.getClass().getCanonicalName());
		
		
	}


	public ISimulator getSimulator()
	{
		return simulator;
	}


	public IAABBManager getAABBManager()
	{
		return aabbManager;
	}


	/*public IDistance getDistance()
	{
		return distance; 
	}*/
	
	public IDistance2 getDistance2()
	{
		return distance2;
	}


	public ISingleStepIntegrator getSingleStepIntegrator()
	{
		return singleStepIntegrator;
	}

	public IDoubleStepIntegrator getDoubleStepIntegrator()
	{
		return doubleStepIntegrator;
	}

	public ContactManagerInit getContactManagerInit() {
		return contactManagerInit;
	}
	
	
	public ICollision getCollision() {
		return collision;
	}
	
	public ContactManager getContactManager() {
		return contactManager;
	}
	
	public AABBUpdater getAAABBUpdater() {
		return aabbUpdater;
	}
}
