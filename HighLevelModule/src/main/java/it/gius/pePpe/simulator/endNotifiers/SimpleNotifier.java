package it.gius.pePpe.simulator.endNotifiers;

import org.apache.log4j.Logger;

import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;

public class SimpleNotifier implements IEndListener
{

	private static Logger logger = Logger.getLogger("endNotifier.SimpleNotifier");
	
	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {

		if(endType == EndType.USER_CLOSED && simInfo.type == SimulationType.TIME_DRIVEN)
		{
			//System.out.println("[Notifier]: Simulation Ended before time elapsed");
			logger.info("Simulation " + simInfo.simulationName + " ended before time elapsed");
			return;
		}
		
		if(endType == EndType.TIME_ELAPSED && simInfo.type == SimulationType.TIME_DRIVEN)
		{
			//System.out.println("[Notifier]: Simulation Ended for time elapsed");
			logger.info("Simulation " + simInfo.simulationName + " ended for time elapsed");
			return;
		}

		if(endType == EndType.EXCEPTION)
		{
			//System.out.println("[Notifier]: Simulation ended by exception");
			if(endType.throwable != null)
				logger.error("Simulation " + simInfo.simulationName + " ended by exception", endType.throwable);
			else
				logger.error("Simulation " + simInfo.simulationName + " ended by exception");
			//if(endType.throwable != null)
				//endType.throwable.printStackTrace();
			return;
		}

		//System.out.println("[Notifier]: Simulation Ended!");
		logger.info("Simulation " + simInfo.simulationName + " ended!");

	}
}
