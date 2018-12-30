package it.gius.pePpe.forces;

import it.gius.data.structures.IdGetSet;
import it.gius.pePpe.Resources;

public interface IForce extends IdGetSet{

	public void apply(float time);
	
	//public void added();
	//public void removing();
	
	public Resources getResources();
	/**
	 * When some engine data change (example: a bind get removed) is possible that the force wouldn't
	 * be linked to is resources, in this case the force will be removed by the force manager
	 * @return
	 */
	public boolean isConsistent();
	
}
