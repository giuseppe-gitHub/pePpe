package it.gius.pePpe.testSuit.simulations;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class VoidSimulation implements ISimulation {

	private PhysicEngine engine;
	private SimulationInfo simInfo;
	
	public VoidSimulation() {
		simInfo = new SimulationInfo();
		simInfo.simulationStep = 0.1f;
		simInfo.simulationTime = 200;
		simInfo.type = SimulationType.TIME_DRIVEN;
	}
	
	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {

	}

	@Override
	public String getName() {
		return "Void";
	}

	@Override
	public String getDescription() {
		return "Void Simulation";
	}
	
	@Override
	public IProperties getProperties() {
		return null;
	}

	@Override
	public void reinit(ConfigurationFactory conFactory)
			throws SimulatorException {
		engine = new PhysicEngine();
		engine.init(conFactory);
		
		
	}

	@Override
	public void clear() {

		engine = null;
	}

	@Override
	public PhysicEngine getEngine() {
		return engine;
	}
	
	@Override
	public SimulationInfo defaultSimulationInfo() {
		return simInfo;
	}

}
