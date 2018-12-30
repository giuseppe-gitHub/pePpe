package it.gius.processing.gjkTest;


import org.jbox2d.common.Vec2;

import processing.core.PApplet;
import it.gius.pePpe.MovingDistanceTestMain;
import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkOverlapSolution;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.algorithm.gjk.NotConvergingException;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
import it.gius.pePpe.algorithm.gjk.shapes.GjkSingleShape;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.processing.util.MyAbstractPApplet;

@Deprecated
@SuppressWarnings("all")
public class GjkPolyPointMotionApplet extends MyAbstractPApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GjkStandardAlgorithm algorithm = new GjkStandardAlgorithm();
	private EpaAlgorithm epa = new EpaAlgorithm();
	
	private Polygon poly = null;
	private Vec2 q = null;

	public GjkPolyPointMotionApplet(Polygon poly,Vec2 q) {
		this.poly = poly;
		this.q = q;
	}


	public void setup(){
		super.setup();

		size(MovingDistanceTestMain.sizeX,MovingDistanceTestMain.sizeY);
		smooth();

	}


	public void draw()
	{

		if(!goStop.isGo())
			noLoop();

		background(255);


		//poly.move();
		moveQ(q);
		

		DistanceSolution sol = new DistanceSolution();
		try {
			algorithm.distance(new GjkSingleShape(poly), q, sol);
			//GjkAlgorithm.distance(poly, q, sol);
			OverlapSolution overSol = new OverlapSolution();
			SimplexEdge simplexEdge = new SimplexEdge();
			GjkOverlapSolution gjkOverlapSolution = new GjkOverlapSolution();
			//DistanceSolution distSol = new DistanceSolution();
			
			//boolean overlap = algorithm.overlap(new GjkSingleShape(poly),SystemCostants.ORIGIN2D,gjkOverlapSolution, distSol);
			
			/*if(poly.contains(SystemCostants.ORIGIN2D))
			{
				epa.penetrationNormalAndDepth(new GjkSingleShape(poly), gjkOverlapSolution.simplexSolution,
						SystemCostants.ORIGIN2D, simplexEdge);
				
				overSol.normal.set(simplexEdge.normal);
				//overSol.penetrationDepth = simplexEdge.distance;
				overSol.distanceDepth = -simplexEdge.distance;
				overSol.epaIterations = simplexEdge.iterations;
				
				System.out.println("penetrationDepth: " + overSol.distanceDepth);
				System.out.println("normalPenetration: "+overSol.normal);
				line(q.x,q.y ,q.x+30*overSol.normal.x,q.y+30*overSol.normal.y);
			}*/
			//println("distance: " + sol.distance);
		} catch (NotConvergingException e) {
			e.printStackTrace();
			println("gjk algorith is not converging");
			println("sol.distance: " + sol.distance);
			println("sol.p1: "+sol.p1);
			goStop.setGo(false);
		}


		fill(0,0,210);
		stroke(10);

		if(sol.distance == 0)
			fill(210,0,0);
		
		drawPoly(poly);



		fill(210,0,0);
		ellipse(q.x,q.y,3,3);
		
		
		stroke(210,210,0);
		line(sol.p1.x,sol.p1.y,q.x,q.y);


	}
	
	private void moveQ(Vec2 q)
	{
		q.x = q.x -0.5f;
		q.y = q.y - 0.1f;
	}

	private void drawPoly(Polygon poly)
	{
		beginShape();
		for(int i=0; i< poly.getDim(); i++)
		{
			Vec2 v = poly.getVertex(i);
			vertex(v.x,v.y);
		}
		endShape(PApplet.CLOSE);

	}

}
