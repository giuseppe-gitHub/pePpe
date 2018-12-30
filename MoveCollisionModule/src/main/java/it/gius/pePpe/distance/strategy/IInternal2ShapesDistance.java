package it.gius.pePpe.distance.strategy;

import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.Bind;

public interface IInternal2ShapesDistance {

	/**
	 * 
	 * @param bindA
	 * @param bindB
	 * @param witnesses input/output parameter
	 * @param sol
	 */
	public abstract void distance(Bind bindA, Bind bindB, DistanceWitnessCache witnesses,
			DistanceSolution sol);
	
	/**
	 * 
	 * @param bindA
	 * @param bindB
	 * @param witnesses input/output paramter
	 * @param sol
	 * @return
	 */
	public abstract boolean overlap(Bind bindA, Bind bindB, DistanceWitnessCache witnesses,
			OverlapSolution sol);

}
