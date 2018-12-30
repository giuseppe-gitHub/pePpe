package it.gius.pePpe.integrator;

import it.gius.pePpe.engine.TimeStep;

public interface IDoubleStepIntegrator extends IIntegratorInit{

	
	public void integrateVelocity(TimeStep timeStep);
	public void integratePosition(TimeStep timeStep);
}
