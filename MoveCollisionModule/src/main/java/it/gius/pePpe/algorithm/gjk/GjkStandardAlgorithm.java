package it.gius.pePpe.algorithm.gjk;

import org.apache.log4j.Logger;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.GjkEpaUtilsFunctions;
import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.algorithm.gjk.shapes.GjkSingleShape;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

/**
 * 
 * @author giuseppe
 * @opt all
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.GjkUtilsFunctions
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.EpaAlgorithm
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.Simplex
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.IGjkAlgorithmShape
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.SimplexSolution 
 * @depend - <return> - it.gius.pePpe.algorithm.gjk.DistanceSolution
 * @depend - <return> - it.gius.pePpe.algorithm.gjk.OverlapSolution 
 * 
 */
public class GjkStandardAlgorithm {

	//pooling
	private final GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();
	//private EpaAlgorithm epa = new EpaAlgorithm();
	private SimplexSolution simpSol = new SimplexSolution();
	private Vec2 currSol = new Vec2();
	private Vec2 supportPoint = new Vec2();
	private SimplexSolution oldSimpSol = new SimplexSolution();

	private Vec2 d = new Vec2();
	private int maxIterations = 0;
	
	private final int defaultMax = 10;
	
	private static Logger logger = Logger.getLogger(GjkStandardAlgorithm.class);

	/**
	 * @hidden
	 */
	public GjkStandardAlgorithm() {
	}

	public static final int iterationCoefficient = 1;

	
	
	private VertexIndexWitness mockVertexWitness = new VertexIndexWitness();
	
	public void distance(GjkSingleShape gjkSingleShape, Vec2 q, DistanceSolution sol)
	{
		Simplex simplex = gjkSingleShape.getStartingSimplex();


		float dim = (float)gjkSingleShape.getDim();

		//int max = (int)(dim*dim*dim/6 + dim*dim/2 + dim);
		int max;
		/*if(dim <= 0)
			max = defaultMax;
		else	
			max = (int)(iterationCoefficient*dim);*/
		
		max = Math.max(defaultMax, (int)(iterationCoefficient*dim));

		
		float epsilon = 0;
		
		/*if(shape.containsComplexCurve())
			epsilon = SystemCostants.SQRT_EPSILON*10;
		else */
			epsilon = SystemCostants.SQRT_EPSILON;
		
		float g = 10;
		//float oldG = Float.MAX_VALUE;
		for(int iteration=0; iteration<max; iteration++)
		{

			//int oldSimplexDim = simplex.currentDim;
			//Simplex oldSimplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
			//oldSimplex.set(simplex);

			utils.nearestPointSimplex2D(simplex, q, simpSol);

			simpSol.getSolutionPoint(currSol);


			d.set(q);
			d.subLocal(currSol);
			//d = q.sub(currSol); 


			/*Witness lastWitness = */gjkSingleShape.supportPoint(d, supportPoint,mockVertexWitness);

			//simplex = shape.updateSimplex(simpSol, supportPoint/*d*/,lastWitness);


			if(simpSol.numVerticesUsed == Simplex.LARGER_2D_SIMPLEX)
			{
				//System.out.println("g: " +g);
				sol.iterationCount = iteration+1;
				sol.distance = 0;
				sol.penetration = true;

				//gjkSingleShape.storeWitness(simpSol);
				gjkSingleShape.fillSolution(currSol,q, simpSol, sol);


				/*SimplexEdge edgeSolution = new SimplexEdge();
				epa.penetrationNormalAndDepth(shape, simplex, q, edgeSolution);
				sol.penetrationDepth = edgeSolution.distance;
				sol.normalPenetration = edgeSolution.normal;
				sol.epaIterations = edgeSolution.iterations;*/
				if(iteration > maxIterations)
				{
					maxIterations = iteration;
					logger.debug("max iterations: " + maxIterations);
				}
				

				return;
			}


			 g = utils.g(currSol,q,supportPoint/*shape*/);


			 /* g > oldG because of ellipse. Ellipse can start fluctuate between two points and never reach g = 0.
			  * but this condition brings a bug. So Ellipse was flagged as deprecated and the condition removed
			  * */
			 /*(iteration!=0 && oldSimpSol.equals(simpSol))
				 * sometimes the algorithm (for numeric errors) finds always the same supportPoint and it rejects it in the solution.
				 * if g is not little enough, the algorithm would fail. So we check if we found the same solution as before to avoid
				 * this case.
				 */
			if( /*g>oldG ||*/ (g >= -epsilon && g<= epsilon) ||  (iteration!= 0 && oldSimpSol.equals(simpSol) ))
			{
				//System.out.println("g: " + g);
				//sol.p1 = currSol;
				sol.iterationCount = iteration+1;
				sol.distance = d.length();
				sol.penetration = false;

				gjkSingleShape.fillSolution(currSol,q, simpSol, sol);

				//if(Math.abs(sol.p1.sub(sol.p2).length() - sol.distance) <= SystemCostants.SQRT_EPSILON)
				if(iteration > maxIterations)
				{
					maxIterations = iteration;
					//System.out.println("[GJKEPA]: max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}
				
				//gjkSingleShape.storeWitness(simpSol);
				
				return;
			}
			
			//oldG = g;

			simplex = gjkSingleShape.updateSimplex(simpSol, supportPoint/*d*/);

			if(simplex == null)
			{


				//System.out.println("simplex == null");
				sol.iterationCount = iteration+1;
				sol.distance = d.length();
				sol.penetration = false;
				//gjkSingleShape.storeWitness(simpSol);
				gjkSingleShape.fillSolution(currSol,q, simpSol, sol);

				//if(Math.abs(sol.p1.sub(sol.p2).length() - d.length()) >= 1)
				//System.out.println("|p1-p2| != d.lenght, "+ oldSimplexDim + oldSimplex);

				if(iteration > maxIterations)
				{
					maxIterations = iteration;
					//System.out.println("[GJKEPA]: max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}
				
				return;				
			}

			oldSimpSol.set(simpSol);

		}

		throw new NotConvergingException();

	}
	
	
	private TwoPointWitness tpWitness = new TwoPointWitness();

	public void distance(GjkMinkDiffShape shape, DistanceSolution sol)
	{
		Vec2 q = SystemCostants.ORIGIN2D;

		Simplex simplex = shape.getStartingSimplex();


		float dim = (float)shape.getDim();

		//int max = (int)(dim*dim*dim/6 + dim*dim/2 + dim);
		int max;
		/*if(dim <= 0)
			max = defaultMax;
		else	
			max = (int)(iterationCoefficient*dim);*/
		
		max = Math.max(defaultMax, (int)(iterationCoefficient*dim));

		
		float epsilon = 0;
		
		/*if(shape.containsComplexCurve())
			epsilon = SystemCostants.SQRT_EPSILON*10;
		else */
			epsilon = SystemCostants.SQRT_EPSILON;
		
		float g = 10;
		//float oldG = Float.MAX_VALUE;
		for(int iteration=0; iteration<max; iteration++)
		{

			//int oldSimplexDim = simplex.currentDim;
			//Simplex oldSimplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
			//oldSimplex.set(simplex);

			utils.nearestPointSimplex2D(simplex, q, simpSol);

			simpSol.getSolutionPoint(currSol);


			d.set(q);
			d.subLocal(currSol);
			//d = q.sub(currSol); 


			shape.supportPoint(d, supportPoint, tpWitness);

			//simplex = shape.updateSimplex(simpSol, supportPoint/*d*/,lastWitness);


			if(simpSol.numVerticesUsed == Simplex.LARGER_2D_SIMPLEX)
			{
				//System.out.println("g: " +g);
				sol.iterationCount = iteration+1;
				sol.distance = 0;
				sol.penetration = true;

				shape.storeWitness(simpSol);
				shape.fillSolution(currSol,q, simpSol, sol);


				/*SimplexEdge edgeSolution = new SimplexEdge();
				epa.penetrationNormalAndDepth(shape, simplex, q, edgeSolution);
				sol.penetrationDepth = edgeSolution.distance;
				sol.normalPenetration = edgeSolution.normal;
				sol.epaIterations = edgeSolution.iterations;*/
				if(iteration > maxIterations)
				{
					maxIterations = iteration;
					logger.debug("max iterations: " + maxIterations);
				}
				

				return;
			}


			 g = utils.g(currSol,q,supportPoint/*shape*/);


			 /* g > oldG because of ellipse. Ellipse can start fluctuate between two points and never reach g = 0.
			  * but this condition brings a bug. So Ellipse was flagged as deprecated and the condition removed
			  * */
			 /*(iteration!=0 && oldSimpSol.equals(simpSol))
				 * sometimes the algorithm (for numeric errors) finds always the same supportPoint and it rejects it in the solution.
				 * if g is not little enough, the algorithm would fail. So we check if we found the same solution as before to avoid
				 * this case.
				 */
			if( /*g>oldG ||*/ (g >= -epsilon && g<= epsilon) ||  (iteration!= 0 && oldSimpSol.equals(simpSol) ))
			{
				//System.out.println("g: " + g);
				//sol.p1 = currSol;
				sol.iterationCount = iteration+1;
				sol.distance = d.length();
				sol.penetration = false;

				shape.fillSolution(currSol,q, simpSol, sol);

				//if(Math.abs(sol.p1.sub(sol.p2).length() - sol.distance) <= SystemCostants.SQRT_EPSILON)
				if(iteration > maxIterations)
				{
					maxIterations = iteration;
					//System.out.println("[GJKEPA]: max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}
				
				shape.storeWitness(simpSol);
				
				return;
			}
			
			//oldG = g;

			simplex = shape.updateSimplex(simpSol, supportPoint/*d*/,tpWitness);

			if(simplex == null)
			{


				//System.out.println("simplex == null");
				sol.iterationCount = iteration+1;
				sol.distance = d.length();
				sol.penetration = false;
				shape.storeWitness(simpSol);
				shape.fillSolution(currSol,q, simpSol, sol);

				//if(Math.abs(sol.p1.sub(sol.p2).length() - d.length()) >= 1)
				//System.out.println("|p1-p2| != d.lenght, "+ oldSimplexDim + oldSimplex);

				if(iteration > maxIterations)
				{
					maxIterations = iteration;
					//System.out.println("[GJKEPA]: max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}
				
				return;				
			}

			oldSimpSol.set(simpSol);

		}

		throw new NotConvergingException();
	}

	
	//private SimplexEdge edgeSolution = new SimplexEdge();



	public boolean overlap(GjkMinkDiffShape shape, GjkOverlapSolution overlapSol, DistanceSolution distSol)
	{
		Vec2 q = SystemCostants.ORIGIN2D;

		Simplex simplex = shape.getStartingSimplex();

		float maxInnerDistanceSquared = shape.getMaxInnerDistance();
		maxInnerDistanceSquared = maxInnerDistanceSquared*maxInnerDistanceSquared;

		float dim = (float)shape.getDim();

		//int max = (int)(dim*dim*dim/6 + dim*dim/2 + dim);
		int max = Math.max(defaultMax,(int)(iterationCoefficient*dim));

		float g = 10;
		for(int iteration=0; iteration<max; iteration++)
		{

			utils.nearestPointSimplex2D(simplex, q, simpSol);

			simpSol.getSolutionPoint(currSol);


			//d.set(q);
			//d.subLocal(currSol);
			//d = q.sub(currSol);
			d.x = q.x -currSol.x;
			d.y = q.y -currSol.y;


			shape.supportPoint(d, supportPoint, tpWitness);

			//simplex = shape.updateSimplex(simpSol, supportPoint/*d*/,lastWitness);

			/*//No overlap possible
			if(d.lengthSquared() > maxInnerDistanceSquared + SystemCostants.POSITIVE_SLOPE)
			{
				sol.iterations = iteration+1;
				//sol.penetrationDepth = -1;
				shape.storeWitness(simpSol);

				return false;
			}*/


			if(simpSol.numVerticesUsed == Simplex.LARGER_2D_SIMPLEX)
			{
				//System.out.println("g: " +g);
				overlapSol.iterations = iteration+1;
				distSol.distance =0;
				distSol.penetration = true;
				shape.fillSolution(currSol,q, simpSol, distSol);
				
				shape.storeWitness(simpSol);


				//SimplexEdge edgeSolution = new SimplexEdge();
				//epa.penetrationNormalAndDepth(shape, simplex, q, edgeSolution);
				//sol.penetrationDepth = edgeSolution.distance;				
				//sol.normalPenetration.set(edgeSolution.normal);
				//sol.epaIterations = edgeSolution.iterations;
				overlapSol.simplexSolution.set(simplex);

				return true;
			}

			g = utils.g(currSol,q,supportPoint/*shape*/);

			/*(iteration!=0 && oldSimpSol.equals(simpSol))
			 * sometimes the algorithm (for numeric errors) finds always the same supportPoint and it rejects it in the solution.
			 * if g is not little enough, the algorithm would fail. So we check if we found the same solution as before to avoid
			 * this case.
			 */
			if((g >= -SystemCostants.SQRT_EPSILON && g<= SystemCostants.SQRT_EPSILON) ||  (iteration!=0 && oldSimpSol.equals(simpSol)))
			{

				//sol.p1 = currSol;
				overlapSol.iterations = iteration+1;
				
				distSol.distance = d.length();
				distSol.penetration = false;
				shape.fillSolution(currSol,q, simpSol, distSol);
				//sol.penetrationDepth = -1;
				shape.storeWitness(simpSol);
				

				return false;
			}

			simplex = shape.updateSimplex(simpSol, supportPoint/*d*/,tpWitness);

			if(simplex == null)
			{
				overlapSol.iterations = iteration+1;
				
				distSol.distance = d.length();
				distSol.penetration = false;
				shape.fillSolution(currSol,q, simpSol, distSol);
				shape.storeWitness(simpSol);
				//sol.penetrationDepth = -1;
				return false;				
			}

			oldSimpSol.set(simpSol);

		}

		throw new NotConvergingException();
	}

}
