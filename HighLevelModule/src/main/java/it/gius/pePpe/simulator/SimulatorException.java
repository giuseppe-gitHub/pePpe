package it.gius.pePpe.simulator;

public class SimulatorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 149190746524413571L;
	
	public SimulatorException() {
	}
	

	public SimulatorException(String message)
	{
		super(message);
	}
	
	public SimulatorException(Throwable t)
	{
		super(t);
	}
}
