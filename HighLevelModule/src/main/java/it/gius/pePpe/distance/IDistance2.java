package it.gius.pePpe.distance;

import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Shape;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public interface IDistance2 {
	
	//public void init(GlobalData globalData);

	//public void distance(short idA,short idB, DistanceSolution sol);
	public void distance(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceSolution sol);
	public void distance(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessCacheInOut, DistanceSolution sol);

	//public void distance(short id, Vec2 q, DistanceSolution sol);
	public void distance(Shape shapeA, Transform transformA, Vec2 q, DistanceSolution sol);

	//public boolean overlap(short idA,short idB, OverlapSolution sol);
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, OverlapSolution sol);
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessCacheInOut, OverlapSolution sol);

	//public void remove(short id);
}
