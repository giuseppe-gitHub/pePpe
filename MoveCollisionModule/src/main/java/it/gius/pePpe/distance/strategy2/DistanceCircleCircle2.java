package it.gius.pePpe.distance.strategy2;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Shape;

public class DistanceCircleCircle2 implements IInternal2ShapesDistance2{
	
		
	private Vec2 centerA = new Vec2();
	private Vec2 centerB = new Vec2();
	
	private Vec2 ab = new Vec2();
	
	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessesInOut,
			DistanceSolution sol) {

		distance(shapeA,transformA,shapeB,transformB,sol);
	}
	
	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceSolution sol) {
		
		Transform.mulToOut(transformA, shapeA.centroid, centerA);
		Transform.mulToOut(transformB, shapeB.centroid, centerB);
		
		float radiusA = shapeA.radius;
		float radiusB = shapeB.radius;
		
		float radiusSum = radiusA + radiusB;
		
		ab.set(centerB);
		ab.subLocal(centerA);
		
		sol.distance = ab.normalize() - radiusSum;
		
		sol.iterationCount = 0;
		
		sol.p1.set(ab);
		sol.p1.mulLocal(radiusA);
		sol.p1.addLocal(centerA);
		
		sol.p2.set(ab);
		sol.p2.mulLocal(-radiusB);
		sol.p2.addLocal(centerB);
		
		
		if(sol.distance <0)
		{
			sol.distance = 0;
			sol.penetration = true;
			//sol.p1.set(centerA);
			sol.p2.set(sol.p1);
			return;
		}
		
		sol.penetration = false;
		
		
	}
	
	
	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceWitnessCache witnessesInOut,
			OverlapSolution sol) {

		return overlap(shapeA,transformA,shapeB,transformB,sol);
	}

	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB,
			OverlapSolution sol) {

		Transform.mulToOut(transformA, shapeA.centroid, centerA);
		Transform.mulToOut(transformB, shapeB.centroid, centerB);
		
		float radiusA = shapeA.radius;
		float radiusB = shapeB.radius;
		
		float radiusSum = radiusA + radiusB;
		
		ab.set(centerB);
		ab.subLocal(centerA);
		
		float penetrationDepth = radiusSum - ab.normalize();
		
		sol.p1.set(ab);
		sol.p1.mulLocal(radiusA);
		sol.p1.addLocal(centerA);
		
		sol.p2.set(ab);
		sol.p2.mulLocal(-radiusB);
		sol.p2.addLocal(centerB);
		
		sol.iterationCount = 0;
		sol.epaIterations = 0;
		if(penetrationDepth >0)
		{
			//sol.penetrationDepth = penetrationDepth;
			sol.distanceDepth = -penetrationDepth;
			sol.normal.set(ab); 
			sol.p2.set(sol.p1);
			return true;
		}
		
		//sol.penetrationDepth = -1;
		sol.distanceDepth = -penetrationDepth;
		sol.normal.set(ab);
		
		
		return false;
	}

}
