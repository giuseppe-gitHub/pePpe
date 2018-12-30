package it.gius.pePpe.simulator.noview;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.IEndListener.EndType;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.utils.monitor.SimpleMonitor;
import it.gius.utils.monitor.SimpleMonitor.MonitorInit;
import it.gius.utils.monitor.logging.MonitorLogger;

public class SimulationRunnable implements Runnable{

	private Logger logger = Logger.getLogger("ConsoleSimulation");

	private List<IEndListener> endListeners = new ArrayList<IEndListener>();
	private PhysicEngine engine = null;
	private SimulationInfo simInfo = null;
	
	private double nextPerCent = perCentStep;
	
	private static final double perCentStep = 0.1; 

	private SimpleMonitor monitor = null;
	
	private boolean enableMonitor = true;
	
	public boolean isEnableMonitor() {
		return enableMonitor;
	}
	
	public void setEnableMonitor(boolean enableMonitor) {
		this.enableMonitor = enableMonitor;
	}
	

	public SimulationRunnable() {
	}

	public void init(PhysicEngine engine, SimulationInfo simInfo) {
		this.engine = engine;
		this.simInfo = simInfo;
		
		MonitorInit mInit = new MonitorInit();
		mInit.dropFirstsMax = 2;
		mInit.dropFirstsMin = 2;
		
		mInit.dropLower = 100;
		mInit.dropUpper = -1;
		
		mInit.timeUnit = SimpleMonitor.NS;
		
		int iter = (int)(simInfo.simulationTime / simInfo.simulationStep);
		
		monitor = new SimpleMonitor("ConsoleSimulation-Monitor","engine.step",iter,mInit);
		
		monitor.setEnabled(enableMonitor);
	}

	public void addListener(IEndListener listener)
	{
		endListeners.add(listener);
	}


	public void removeListener(IEndListener listener)
	{
		endListeners.remove(listener);
	}

	private double totalSimulated = 0;

	@Override
	public void run()
	{
		try {
			realRun();
		} catch (Throwable e) {
			
			EndType endType = EndType.EXCEPTION;
			endType.throwable = e;
			if(endListeners != null)
			{
				for(IEndListener listener : endListeners)
					listener.notifyEnd(endType,simInfo);
			}
		}
	}
	
	private int count = 1;
	
	
	private void realRun() {

		
		//String threadName = Thread.currentThread().getName();
		
		while(true){


			monitor.preCall();
			totalSimulated = engine.step(simInfo.simulationStep);

			monitor.postCall();
			
			double curPerCent = totalSimulated / simInfo.simulationTime;
			
			if(curPerCent >= nextPerCent)
			{
				//System.out.println("["+ threadName + "] " + ((int)(nextPerCent*100)) + "% elaborated");
				logger.info(((int)(nextPerCent*100.0)) + "% elaborated");
				count++;
				nextPerCent = count* perCentStep;
				
			}


			if(simInfo.type == SimulationType.TIME_DRIVEN && totalSimulated >= simInfo.simulationTime)
			{

				monitor.collect();
				MonitorLogger monitorLogger = new MonitorLogger(monitor);
				monitorLogger.log();
						
				EndType endType = EndType.TIME_ELAPSED;
				
				if(endListeners != null)
				{
					for(IEndListener listener : endListeners)
						listener.notifyEnd(endType,simInfo);
				}

				return;
			}

		}


	}

}
