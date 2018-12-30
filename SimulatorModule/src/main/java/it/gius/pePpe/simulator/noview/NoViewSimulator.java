package it.gius.pePpe.simulator.noview;

import org.apache.log4j.Logger;

import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.IDrawContext;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;

public class NoViewSimulator implements ISimulator{
	
	private static Logger logger = Logger.getLogger("simulator.ConsoleSimulator");
	
	private SimulationRunnable runnable = new SimulationRunnable();
	private boolean inited = false;
	private boolean started = false;
	
	private SimulationInfo info;
	
	private boolean enableMonitor =  true;
	
	public boolean isEnableMonitor() {
		return enableMonitor;
	}
	
	public void setEnableMonitor(boolean enableMonitor) {
		this.enableMonitor = enableMonitor;
	}
	
	
	public IDrawContext getDrawContext() throws SimulatorException {
		return null;
	}
	
	
	
	@Override
	public void addEndListener(IEndListener listener)
			throws SimulatorException {
		runnable.addListener(listener);
		
	}
	
	
	public void init(PhysicEngine engine, SimulationInfo info) throws SimulatorException {
		
		if(inited)
			throw new SimulatorException("Simulation already initialized");

		if(info.type == SimulationType.USER_DRIVEN)
			throw new SimulatorException("Simulation type not supported");

		
		this.info = info;
		
		runnable.setEnableMonitor(enableMonitor);
		
		runnable.init(engine, info);
		
		inited = true;
	};
	
	
	@Override
	public void start() throws SimulatorException {
		if(!inited)
			throw new SimulatorException("Simulation not yet initialized");
		
		if(started)
			throw new SimulatorException("Simulation already started");
		
		
		logger.info("starting simulation: " + info.simulationName );
		
		Thread thread = new Thread(runnable,"pePpe-thread");
		
		started = true;
		
		thread.start();
		
	}

}
