package it.gius.pePpe.algorithm.epa;

import org.apache.log4j.Logger;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.GjkEpaUtilsFunctions;
import it.gius.pePpe.algorithm.gjk.NotConvergingException;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;


/**
 * 
 * @author giuseppe
 * @depend - <use&return> - it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.GjkUtilsFunctions
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.Simplex
 * @depend - <use> - it.gius.pePpe.algorithm.gjk.IGjkAlgorithmShape
 * @opt all
 * @opt !attributes
 */
public class EpaAlgorithm {

	private static GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();

	private Vec2 supportPoint = new Vec2();
	private Vec2 pq = new Vec2();
	private Simplex unboundSimplex = new Simplex(Simplex.UNBOUNDED_SIMPLEX);
	//private SimplexEdge oldSimplexEdge = new SimplexEdge();

	private final int defaultMax = 10;
	private final int iterationCoefficient = 1;
	
	private int maxIterations = 0;

	private static Logger logger = Logger.getLogger(EpaAlgorithm.class);

	/**
	 * @hidden
	 */
	public EpaAlgorithm() {

	}
	
	private TwoPointWitness tpWitness = new TwoPointWitness();

	public  void penetrationNormalAndDepth(GjkMinkDiffShape shape, Simplex simplex, Vec2 q, SimplexEdge edgeSolution)
	{
		//Simplex unboundSimplex = new Simplex(Simplex.UNBOUNDED_SIMPLEX);

		//SimplexEdge oldEdge = new SimplexEdge();
		/*for(int i=0; i<simplex.currentDim; i++)
		{*/
		/*boolean sub = subPonintI(shape, simplex, q,0);
		if(sub && SystemCostants.DEBUG)
		{
			System.out.println("Simplex point"+ 0 +" changed: " + simplex);
			Polygon polygonTemp = new Polygon();
			for(int i=0; i< simplex.currentDim;i++)
				polygonTemp.addVertex(simplex.vs[i]);
			
			if(!polygonTemp.isConvex())
			{
				System.out.println("Simplex not more convex: " + polygonTemp);//throw new RuntimeException("Simplex not more convex");
			}
			else
			{

				polygonTemp.endPolygon();

				if(!polygonTemp.contains(q))
				{
					System.out.println("Simplex doesnt contains origin: " + polygonTemp);
				}
			}
		}*/
		//}

		unboundSimplex.set(simplex);

		unboundSimplex.computeWinding();

		/*Vec2 supportPoint = new Vec2();
		Vec2 pq = null;*/

		int dim = shape.getDim();

		int max;
		/*if(shape.containsComplexCurve())
			max = 14;
		else
			max = 9*dim;

		if(max <0)
			max = 14;*/
		max = Math.max(defaultMax, (int)(iterationCoefficient*dim));

		int i;
		float d = 0;
		float g = 0;
		//float oldG = Float.MAX_VALUE;

		float epsilon = SystemCostants.EPSILON;

		/*if(shape.containsComplexCurve())
			epsilon*=10;*/

		for(i=0; i<max; i++)
		{
			utils.nearestEdgeInsidePointSimplex2D(unboundSimplex, q, edgeSolution);

			shape.supportPoint(edgeSolution.normal, supportPoint, tpWitness);

			//pq = supportPoint.sub(q);
			//pq.set(supportPoint);
			//pq.subLocal(q);
			pq.x = supportPoint.x -q.x;
			pq.y = supportPoint.y -q.y;

			//d = Vec2.dot(pq, edgeSolution.normal);
			d = pq.x*edgeSolution.normal.x + pq.y*edgeSolution.normal.y;

			g = d-edgeSolution.distance;
			

			/*if(oldG < 1 && g > oldG*10)
			{

				edgeSolution.set(oldSimplexEdge);

				if(i > maxIterations)
				{
					maxIterations = i;
					//System.out.println("[EPA] max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}

				edgeSolution.iterations = i;
				return;	
			}*/

			/*if(shape.containsComplexCurve() && MathUtils.manhattanDistace(unboundSimplex.vs[edgeSolution.indexB],supportPoint)<0.5f)//check(unboundSimplex, supportPoint))
			{
				if(i > maxIterations)
				{
					maxIterations = i;
					//System.out.println("[EPA] max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}

				edgeSolution.iterations = i;
				return;
			}*/

			if( g < epsilon || supportPoint.equals(unboundSimplex.vs[edgeSolution.indexB]))
			{
				/*if(d-edgeSolution.distance != 0)
					System.out.println("d-edgeSolution.distance: " + (d-edgeSolution.distance));*/

				if(i > maxIterations)
				{
					maxIterations = i;
					//System.out.println("[EPA] max iterations: " + maxIterations);
					logger.debug("max iterations: " + maxIterations);
				}

				edgeSolution.iterations = i;
				return;
			}
			else
			{
				unboundSimplex.insert(supportPoint, tpWitness, edgeSolution.indexB);

				if(SystemCostants.DEBUG)
				{
					Polygon polygonTemp = new Polygon();
					for(i=0; i<unboundSimplex.currentDim; i++)
						polygonTemp.addVertex(unboundSimplex.vs[i]);

					if(!polygonTemp.isConvex())
					{
						/*System.out.println("Simplex not more convex: " + polygonTemp);//throw new RuntimeException("Simplex not more convex");
						System.out.println("Simplex not more convex, insertion index: " + edgeSolution.indexB);
						System.out.println("Simplex not more convex, insertion point: " + supportPoint);
						System.out.println("Simplex not more convex, g: " + g);
						System.out.println("Simplex not more convex, epsilon: " + epsilon);*/
						throw new RuntimeException("Simplex not more convex");
					}
					else
					{

						polygonTemp.endPolygon();

						if(!polygonTemp.contains(q))
						{
							/*System.out.println("Simplex doesnt contains origin: " + polygonTemp);
							System.out.println("Simplex doesnt contains origin, insertion index: " + edgeSolution.indexB);
							System.out.println("Simplex doesnt contains origin, insertion point: " + supportPoint);
							System.out.println("Simplex doesnt contains origin, g: " + g);
							System.out.println("Simplex doesnt contains origin, epsilon: " + epsilon);*/
							throw new RuntimeException("Simplex doesnt contains origin");
						}
					}
				}
			}

			/*oldEdge.indexB = edgeSolution.indexB;
			oldEdge.distance = edgeSolution.distance;
			oldEdge.normal.set(edgeSolution.normal);*/
			//oldSupport.subLocal(supportPoint);
			//float temp = oldSupport.length();
			//if(temp < 2.0 && d -edgeSolution.distance < 0.6f)
			//System.out.println("oldSupport and supportPoint distance: " + temp);
			//oldG = g;

			//oldSimplexEdge.set(edgeSolution);

		}


		edgeSolution.iterations = i;

		/*System.out.println("Not Converging");
		System.out.println("unboundSimplex dim: " + unboundSimplex.currentDim);*/
		throw new NotConvergingException("iterations: " + i +", d- edgeSolution.distance: " + (d -edgeSolution.distance));


	}
	
	
	//private Vec2 pool1 = new Vec2();
	//private Vec2 pool2 = new Vec2();
	
	/*private boolean subPonintI(IGjkAlgorithmShape gjkShape, Simplex simplex, Vec2 q, int index)
	{
		Vec2 point0 = simplex.vs[index];
		
		Vec2 d = pool1;
		d.x = point0.x - q.x;
		d.y = point0.y - q.y;
		
		Vec2 supportPoint = pool2;
		
		Witness witness = gjkShape.supportPoint(d, supportPoint);
		
		if(!supportPoint.equals(point0))
		{
			simplex.vs[index].set(supportPoint);
			simplex.witnesses[index] = witness;
			return true;
		}
		
		return false;
	}*/
	

	/*private boolean check(Simplex simplex, Vec2 newSupport)
	{
		int n = simplex.currentDim;

		for(int i=0; i< n; i++)
			if(MathUtils.manhattanDistace(simplex.vs[i],newSupport)<0.8f)
				return true;


		return false;
	}*/

}
