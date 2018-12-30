package it.gius.pePpe.testSuit;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.testSuit.configurationInit.AbstractConFactoryInit;
import it.gius.pePpe.testSuit.configurationInit.ConfigurationException;

public class SimulationStarter {
	
	
	public void startSimulation(ISimulation simulation, AbstractConFactoryInit facInit, SimulationInfo simInfo) throws ConfigurationException, SimulatorException
	{
		ConfigurationFactory factory = facInit.getConfigurationFactory(simulation);
		
		ISimulator simulator = factory.getSimulator();
		
		PhysicEngine engine;
		try {
			//simulation.clear();
			
			/*IProperties properties = simulation.getProperties();
			if(properties != null)
				properties.toDefaultValues();*/
			
			
			simulation.reinit(factory);
			
			engine = simulation.getEngine();
			
			simInfo.simulationName = simulation.getName();
		} catch (Throwable t) {
			throw new SimulatorException(t);
		}
		
		if(engine == null)
			throw new SimulatorException("Null engine");
		
		simulator.init(engine, simInfo);
		
		simulator.addEndListener(simulation);
		
		simulator.start();
	}

}
