package it.gius.pePpe.distance.strategy2;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.algorithm.gjk.shapes.GjkSingleShape;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;
import it.gius.pePpe.distance.pool.GjkPool;



public class GJKCircleOtherDistance2 implements IInternal2ShapesDistance2,IInternalPointShapesDistance2{

	private GjkPool gjkPool;

	private GjkStandardAlgorithm gjkStandardAlgorithm;
	private EpaAlgorithm epa;


	public GJKCircleOtherDistance2(GjkPool pool, GjkStandardAlgorithm standard, EpaAlgorithm epa){
		this.gjkPool = pool;
		this.gjkStandardAlgorithm = standard;
		this.epa = epa;
	}



	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();

	@Override
	public void distance(Shape shape, Transform transform, Vec2 q,
			DistanceSolution sol) {

		//wrapped.distance(bind, q, sol);
		gjkPool.setSingleShape(shape,transform);
		GjkSingleShape gjkShape = gjkPool.getSingleShape();

		gjkStandardAlgorithm.distance(gjkShape, q, sol);


		float radius = shape.radius;

		if(!sol.penetration)
		{
			sol.distance -= radius;

			if(sol.distance <0)
			{
				sol.distance =0;
				sol.penetration = true;

				return;
			}

			Vec2 pCircle,pOtherShape;

			pCircle = sol.p1;
			pOtherShape = sol.p2;

			Vec2 newPSol = pool1;
			newPSol.set(pOtherShape);
			newPSol.subLocal(pCircle);

			newPSol.normalize();
			newPSol.mulLocal(radius);

			pCircle.addLocal(newPSol);
		}


	}


	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceSolution sol) {

		distance(shapeA, transformA, shapeB, transformB,null, sol);

	}


	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessesInOut,
			DistanceSolution sol) {

		//wrapped.distance(bindA, bindB, witnesses, sol);
		if(witnessesInOut != null && witnessesInOut.witness != null)
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB, witnessesInOut);
		else
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB);

		GjkMinkDiffShape gjkShape = gjkPool.getDoubleShape();

		gjkStandardAlgorithm.distance(gjkShape, sol);

		if(witnessesInOut != null)
		{
			/*Witness witnessA = gjkPool.getWitnessA();
			Witness witnessB = gjkPool.getWitnessB();

			witnessesInOut.witnessA = witnessA.clone();
			witnessesInOut.witnessB = witnessB.clone();*/
			if(witnessesInOut.witness == null)
				witnessesInOut.witness = new TwoPointWitness();
			
			TwoPointWitness witnessOut = gjkPool.getWitness();
			
			witnessesInOut.witness.set(witnessOut);
		}


		Vec2 pCircle = pool1 ,pOtherShape = pool2;
		float radius;
		boolean firstShapeCircle;

		if(shapeA instanceof Circle)
		{
			radius = shapeA.radius;
			pCircle.set(sol.p1);
			pOtherShape.set(sol.p2);
			firstShapeCircle = true;
		}
		else
		{
			radius = shapeB.radius;
			pCircle.set(sol.p2);
			pOtherShape.set(sol.p1);
			firstShapeCircle = false;
		}
		
		sol.p1.set(pOtherShape);
		sol.p2.set(pOtherShape);

		if(!sol.penetration)
		{

			sol.distance -= radius;

			if(sol.distance <0)
			{
				sol.distance =0;
				sol.penetration = true;

				return;
			}

			Vec2 newPSol = pool3;
			newPSol.set(pOtherShape);
			newPSol.subLocal(pCircle);

			newPSol.normalize();
			newPSol.mulLocal(radius);

			pCircle.addLocal(newPSol);
			
			if(firstShapeCircle)
			{
				sol.p1.set(pCircle);
				sol.p2.set(pOtherShape);
			}
			else
			{
				sol.p1.set(pOtherShape);
				sol.p2.set(pCircle);
			}

		}

	}


	private DistanceSolution distSolution = new DistanceSolution();
	private SimplexEdge edgeSolution = new SimplexEdge();


	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, OverlapSolution sol) {

		return overlap(shapeA, transformA, shapeB, transformB, null, sol);
	}


	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceWitnessCache witnessesInOut,
			OverlapSolution sol) {

		if(witnessesInOut != null && witnessesInOut.witness != null)
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB, witnessesInOut);
		else
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB);

		GjkMinkDiffShape gjkShape = gjkPool.getDoubleShape();

		gjkStandardAlgorithm.distance(gjkShape, distSolution);

		sol.iterationCount = distSolution.iterationCount;

		if(witnessesInOut != null)
		{
			//Witness witnessA = gjkPool.getWitnessA();
			//Witness witnessB = gjkPool.getWitnessB();

			//witnessesInOut.witnessA = witnessA.clone();
			//witnessesInOut.witnessB = witnessB.clone();
			if(witnessesInOut.witness == null)
				witnessesInOut.witness = new TwoPointWitness();
			
			TwoPointWitness witnessOut = gjkPool.getWitness();
			
			witnessesInOut.witness.set(witnessOut);
		}

		float radius;
		Vec2 pCircle;
		Vec2 pOther;

		boolean firstShapeCircle = false;

		if(shapeA instanceof Circle)
		{
			radius = shapeA.radius;
			pCircle = distSolution.p1;
			pOther = distSolution.p2;
			firstShapeCircle = true;
		}
		else
		{
			radius = shapeB.radius;
			pCircle = distSolution.p2;
			pOther = distSolution.p1;
			firstShapeCircle = false;
		}
		

		sol.p1.set(pOther);
		sol.p2.set(pOther);


		if(distSolution.penetration)
		{

			Simplex simplex = gjkShape.getCurrentSimplex();

			epa.penetrationNormalAndDepth(gjkShape, simplex, SystemCostants.ORIGIN2D, edgeSolution);

			sol.epaIterations = edgeSolution.iterations;
			//sol.penetrationDepth = edgeSolution.distance + radius;
			sol.distanceDepth = -(edgeSolution.distance + radius);
			/*I want the returned normal to go from shapeA to shapeB, the returned vector go in the opposite direction, so I negate it
			 * */
			sol.normal.set(edgeSolution.normal.negate());

			return true;

		}

		distSolution.distance -= radius;

		if(distSolution.distance <0)
		{
			Vec2 newPSol = pool1;
			newPSol.set(pOther);
			newPSol.subLocal(pCircle);

			float penetration = radius - newPSol.normalize();

			sol.epaIterations = 0;
			//sol.penetrationDepth = penetration;
			sol.distanceDepth = -penetration;
			
			/*
			 * newPSol go from the circle to the other shape.
			 * I want the returned normal to go from shapeA to shapeB,
			 * so if the first shape is the circle is OK, if not I negate the normal 
			 */
			if(firstShapeCircle)
				sol.normal.set(newPSol); 
			else
				sol.normal.set(newPSol.negate());

			return true;

		}

		
		sol.distanceDepth = distSolution.distance;
		
		Vec2 newPSol = pool1;
		newPSol.set(pOther);
		newPSol.subLocal(pCircle);

		newPSol.normalize();
		newPSol.mulLocal(radius);

		pCircle.addLocal(newPSol);
		sol.epaIterations = -1;

		if(firstShapeCircle)
		{
			sol.p1.set(pCircle);
			sol.p2.set(pOther);
		}
		else
		{
			sol.p1.set(pOther);
			sol.p2.set(pCircle);
		}
		
		Vec2 normal = sol.normal;
		
		normal.x = sol.p2.x - sol.p1.x;
		normal.y = sol.p2.y - sol.p1.y;
		
		normal.normalize();
		

		return false;
	}

}


