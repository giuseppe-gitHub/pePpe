package it.gius.pePpe.testSuit.configurationInit;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.testSuit.ISimulation;

public abstract class AbstractConFactoryInit {


	public abstract ConfigurationFactory getConfigurationFactory(ISimulation simulation)
			throws ConfigurationException;
}