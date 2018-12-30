package it.gius.pePpe.testSuit;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.testSuit.property.IProperties;

public interface ISimulation extends IEndListener{
	
	public String getName();
	public String getDescription();
	
	public IProperties getProperties();
	
	public void reinit(ConfigurationFactory conFactory) throws SimulatorException;
	
	public void clear();
	
	public PhysicEngine getEngine();
	
	public SimulationInfo defaultSimulationInfo();
}
