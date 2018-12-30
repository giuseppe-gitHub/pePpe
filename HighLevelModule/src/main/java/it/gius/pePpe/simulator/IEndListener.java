package it.gius.pePpe.simulator;


/**
 * 
 * @author giuseppe
 * @opt all
 */
public interface IEndListener {
	
	public static enum EndType{
		TIME_ELAPSED,USER_CLOSED,EXCEPTION;
		
		public Throwable throwable = null;
	}
	
	public void notifyEnd(EndType endType, SimulationInfo simInfo);

}
