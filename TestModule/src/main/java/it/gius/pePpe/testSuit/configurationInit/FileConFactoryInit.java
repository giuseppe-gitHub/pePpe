package it.gius.pePpe.testSuit.configurationInit;

import java.net.URL;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.testSuit.ISimulation;

import org.apache.log4j.Logger;

public class FileConFactoryInit extends AbstractConFactoryInit {

	public static final String XML_SUFFIX = "-pePpe.xml";
	public static final String PROP_SUFFIX = "-pePpe.properties";

	static final Logger logger = Logger.getLogger(FileConFactoryInit.class);


	@Override
	public ConfigurationFactory getConfigurationFactory(ISimulation simulation)
			throws ConfigurationException {
		String name = simulation.getName();
		if(name == null)
			throw new IllegalArgumentException("Simulation must have a name");

		ConfigurationFactory conFactory = new ConfigurationFactory();

		String xmlSourceFile = name.concat(XML_SUFFIX);
		String propSourceFile = name.concat(PROP_SUFFIX);


		URL xmlFound = this.getClass().getClassLoader().getResource(xmlSourceFile);
		URL propFound = this.getClass().getClassLoader().getResource(propSourceFile);

		try {
			if(xmlFound != null && propFound != null)
			{
				conFactory.init(xmlSourceFile,propSourceFile);
				return conFactory;
			}
		} catch (Exception e) {
			logger.error("Error initializating the Configuration Factory with files: " + xmlSourceFile +", " + propSourceFile + ".", e);
		}

		try {
			if(xmlFound == null && propFound != null)
			{
				conFactory.init(propSourceFile);
				return conFactory;
			}
		} catch (Exception e) {
			logger.error("Error initializating the Configuration Factory with file: " + propSourceFile + ".", e);
		}


		try {
			conFactory.init();
			return conFactory;
		} catch (Exception e) {
			logger.error("Error initializating the Configuration Factory with default files.", e);
		}

		throw new ConfigurationException("Unable to initialize the ConfigurationFactory");
	}


}
