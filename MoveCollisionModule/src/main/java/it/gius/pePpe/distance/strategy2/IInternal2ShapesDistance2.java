package it.gius.pePpe.distance.strategy2;

import org.jbox2d.common.Transform;

import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Shape;

public interface IInternal2ShapesDistance2 {
	
	public abstract void distance(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB,  DistanceSolution sol);
	public abstract void distance(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessesInOut, DistanceSolution sol);
	
	public abstract boolean overlap(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, OverlapSolution sol);
	public abstract boolean overlap(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessesInOut, OverlapSolution sol);


}
