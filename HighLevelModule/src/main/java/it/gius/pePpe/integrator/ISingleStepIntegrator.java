package it.gius.pePpe.integrator;

import it.gius.pePpe.engine.TimeStep;

public interface ISingleStepIntegrator extends IIntegratorInit {
	
	
	/**
	 * integrate velocity and position, to use after 
	 * applied all forces for the current step
	 * @param step
	 */
	public void step(TimeStep timeStep);

}
