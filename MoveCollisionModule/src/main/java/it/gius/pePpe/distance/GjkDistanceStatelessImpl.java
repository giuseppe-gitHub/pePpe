package it.gius.pePpe.distance;

import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.distance.strategy2.DistanceStrategy2;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class GjkDistanceStatelessImpl implements IDistance2{

	
	private DistanceStrategy2 strategy = new DistanceStrategy2();
	
	//private DistanceSolution oldSol = new DistanceSolution();
	
	
	public GjkDistanceStatelessImpl() {
		
	}
	
	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceSolution sol) {
		
		strategy.get(shapeA, shapeB).distance(shapeA, transformA, shapeB, transformB, sol);
		
		/*if(Math.abs(oldSol.distance - sol.distance) > 5)
			System.out.println("Distances too much diferrents");
		
		oldSol.distance = sol.distance;*/
	}
	
	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceWitnessCache witnessCacheInOut,
			DistanceSolution sol) {
		
		strategy.get(shapeA, shapeB).distance(shapeA, transformA, shapeB, transformB, witnessCacheInOut, sol);
		
	}
	
	@Override
	public void distance(Shape shapeA, Transform transformA, Vec2 q,
			DistanceSolution sol) {
		
		strategy.get(shapeA).distance(shapeA, transformA, q, sol);
		
	}
	
	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, OverlapSolution sol) {
		
		return strategy.get(shapeA, shapeB).overlap(shapeA, transformA, shapeB, transformB, sol);
	}
	
	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceWitnessCache witnessCacheInOut,
			OverlapSolution sol) {
		
		return strategy.get(shapeA, shapeB).overlap(shapeA, transformA, shapeB, transformB, witnessCacheInOut, sol);
	}
}
