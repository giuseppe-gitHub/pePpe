package it.gius.pePpe;

public class SimulationExcpetion extends Exception {

	
	private static final long serialVersionUID = -5655957262321220837L;
	
	public SimulationExcpetion() {
	}
	

	public SimulationExcpetion(String message)
	{
		super(message);
	}
	
	public SimulationExcpetion(Throwable t)
	{
		super(t);
	}


}
