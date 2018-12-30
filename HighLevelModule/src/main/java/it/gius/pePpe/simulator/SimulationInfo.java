package it.gius.pePpe.simulator;

public class SimulationInfo {

	
	public SimulationType type = SimulationType.USER_DRIVEN;
	
	public double simulationTime = -1;
	public float simulationStep = 0.1f;
	
	public Object otherData = null;
	
	public String simulationName = "pePpe";
	
	public enum SimulationType{
		
		TIME_DRIVEN,USER_DRIVEN;
	}
	
	
	public void set(SimulationInfo other)
	{
		this.simulationName = other.simulationName;
		this.simulationStep = other.simulationStep;
		this.simulationTime = other.simulationTime;
		this.type = other.type;
		this.otherData = other.otherData;
	}
}
