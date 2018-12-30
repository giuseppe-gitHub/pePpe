package it.gius.pePpe.simulator;

import it.gius.pePpe.engine.PhysicEngine;

/**
 * 
 * @author giuseppe
 * @opt all
 * @depend - - - it.gius.pePpe.simulator.IDrawContext
 * @depend - - - it.gius.pePpe.simulator.SimulationInfo
 * @depend - - - it.gius.pePpe.PhysicEngine
 * @depend - - - it.gius.pePpe.simulator.IEndListener
 * 
 * 
 */
public interface ISimulator {

	public void init(PhysicEngine engine, SimulationInfo info) throws SimulatorException;
	
	public IDrawContext getDrawContext() throws SimulatorException;
	
	public void start() throws SimulatorException;
	
	public void addEndListener(IEndListener listener) throws SimulatorException;
}
