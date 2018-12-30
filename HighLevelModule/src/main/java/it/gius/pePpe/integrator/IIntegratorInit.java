package it.gius.pePpe.integrator;

import it.gius.pePpe.data.physic.BodyList;
import it.gius.pePpe.forces.ForceManager;
import it.gius.pePpe.simulator.SimulatorException;

public interface IIntegratorInit {

	public void init(BodyList bodyList,ForceManager forceManager) 
			throws SimulatorException;
}
