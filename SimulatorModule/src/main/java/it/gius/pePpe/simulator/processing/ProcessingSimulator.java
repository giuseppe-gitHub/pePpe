package it.gius.pePpe.simulator.processing;


import org.apache.log4j.Logger;

import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.IDrawContext;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.processing.util.PAppletManager;

public class ProcessingSimulator implements ISimulator {

	private ProcessingDrawContext processingDrawContext = null;
	
	private static Logger logger = Logger.getLogger("simulator.ProcessingSimulator");

	private boolean started = false;
	private boolean inited = false;
	
	private SimulationInfo info;
	
	private boolean enableMonitor = true;
	
	public boolean isEnableMonitor() {
		return enableMonitor;
	}
	
	public void setEnableMonitor(boolean enableMonitor) {
		this.enableMonitor = enableMonitor;
	}
	
	private ProcessingDrawingApplet applet = null;
	
	private boolean manualStart = true;
	
	public boolean isManualStart() {
		return manualStart;
	}
	
	public void setManualStart(boolean manualStart) {
		this.manualStart = manualStart;
	}
	
	
	public ProcessingSimulator() {
		
	}
	
	
	public ProcessingDrawContext getProcessingDrawContext() {
		return processingDrawContext;
	}
	
	public void setProcessingDrawContext(
			ProcessingDrawContext processingDrawContext) {
		this.processingDrawContext = processingDrawContext;
	}
	
	
	
	@Override
	public IDrawContext getDrawContext() throws SimulatorException {
		
		if(processingDrawContext == null)
			throw new SimulatorException("Draw Context not initialized!");
		
		if(!inited)
			throw new SimulatorException("Simulator not initialized!");
		
		return processingDrawContext;
	}
	

	@Override
	public synchronized void addEndListener(IEndListener listener) throws SimulatorException{
		if(started)
			throw new SimulatorException("Simulation already started!");
		
		if(processingDrawContext == null)
			throw new SimulatorException("Draw Context not initialized!");
		
		processingDrawContext.addEndListener(listener);

	}
	
	@Override
	public synchronized void init(PhysicEngine engine, SimulationInfo info) throws SimulatorException{
		
		if(started || inited)
			throw new SimulatorException("Simulation already stared or inited!");
		
		if(processingDrawContext == null)
			throw new SimulatorException("Draw Context not initialized!");
		
		this.info = info;
		
		applet = new ProcessingDrawingApplet();
		applet.init(engine,info);
		
		PAppletManager manager = new PAppletManager(false,manualStart);
		
		processingDrawContext.init(manager,applet,engine, info);
		
		applet.setDrawContext(processingDrawContext);
		applet.setEnableMonitor(enableMonitor);

		inited = true;
	}

	@Override
	public synchronized void start() throws SimulatorException{

		if(started)
			throw new SimulatorException("Simulation already started!");
		
		if(processingDrawContext == null)
			throw new SimulatorException("Draw Context not initialized!");
		
		logger.info("starting simulation: " + info.simulationName );
		
		processingDrawContext.start();

		started = true;
	}

}
