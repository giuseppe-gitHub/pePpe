package it.gius.pePpe.distance.strategy2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkOverlapSolution;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.algorithm.gjk.shapes.GjkSingleShape;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;
import it.gius.pePpe.distance.pool.GjkPool;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class GJKStandardDistance2 implements IInternal2ShapesDistance2,IInternalPointShapesDistance2{

	private GjkPool gjkPool;
	private GjkStandardAlgorithm standard;
	private EpaAlgorithm epa;

	public GJKStandardDistance2(GjkPool pool, GjkStandardAlgorithm standard, EpaAlgorithm epa) {
		this.gjkPool = pool;
		this.standard = standard;
		this.epa = epa;
	}




	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceWitnessCache witnessesInOut, DistanceSolution sol) {

		if(witnessesInOut != null && witnessesInOut.witness != null)
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB, witnessesInOut);
		else
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB);

		GjkMinkDiffShape gjkShape = gjkPool.getDoubleShape();

		standard.distance(gjkShape, sol);

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

	}

	@Override
	public void distance(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, DistanceSolution sol) {

		gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB);

		GjkMinkDiffShape gjkShape = gjkPool.getDoubleShape();

		standard.distance(gjkShape, sol);

	}


	@Override
	public void distance(Shape shape, Transform transform, Vec2 q, DistanceSolution sol) {

		gjkPool.setSingleShape(shape,transform);
		GjkSingleShape gjkShape = gjkPool.getSingleShape();

		standard.distance(gjkShape, q, sol);

	}


	private GjkOverlapSolution gjkOverlapSolution = new GjkOverlapSolution();
	private SimplexEdge edgeSolution = new SimplexEdge();
	private DistanceSolution distSol = new DistanceSolution();

	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache witnessesInOut,
			OverlapSolution sol) {

		if(witnessesInOut != null && witnessesInOut.witness != null)
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB, witnessesInOut);
		else
			gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB);

		GjkMinkDiffShape gjkShape = gjkPool.getDoubleShape();

		boolean doOverlap = standard.overlap(gjkShape, gjkOverlapSolution, distSol);

		sol.iterationCount = gjkOverlapSolution.iterations;
		sol.distanceDepth = distSol.distance;
		sol.p1.set(distSol.p1);
		sol.p2.set(distSol.p2);

		if(doOverlap)
		{
			epa.penetrationNormalAndDepth(gjkShape, gjkOverlapSolution.simplexSolution, SystemCostants.ORIGIN2D, edgeSolution);
			//sol.penetrationDepth = edgeSolution.distance;
			sol.distanceDepth = -edgeSolution.distance;
			/*I want the returned normal to go from shapeA to shapeB, the returned vector go in the opposite direction, so I negate it
			 * */
			sol.normal.set(edgeSolution.normal.negate());
			sol.epaIterations = edgeSolution.iterations;
		}
		else
		{
			//sol.penetrationDepth =-1;
			sol.epaIterations = -1;
			
			Vec2 normal = sol.normal;
			normal.x = sol.p2.x - sol.p1.x;
			normal.y = sol.p2.y - sol.p1.y;
			normal.normalize();
		}

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

		return doOverlap;
	}


	@Override
	public boolean overlap(Shape shapeA, Transform transformA, Shape shapeB,
			Transform transformB, OverlapSolution sol) {


		return overlap(shapeA,transformA,shapeB,transformB,null,sol);
		/*gjkPool.setDoubleShape(shapeA, transformA, shapeB, transformB);

		IGjkAlgorithmShape gjkShape = gjkPool.getDoubleShape();

		boolean result = standard.overlap(gjkShape, SystemCostants.ORIGIN2D, gjkOverlapSolution);

		sol.iterationCount = gjkOverlapSolution.iterations;

		if(result)
		{
			epa.penetrationNormalAndDepth(gjkShape, gjkOverlapSolution.simplexSolution, SystemCostants.ORIGIN2D, edgeSolution);
			sol.penetrationDepth = edgeSolution.distance;				
			sol.normalPenetration.set(edgeSolution.normal);
			sol.epaIterations = edgeSolution.iterations;
		}
		else
		{
			sol.penetrationDepth =-1;
			sol.epaIterations = -1;
		}

		return result;*/
	}



}
