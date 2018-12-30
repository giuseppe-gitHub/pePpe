package it.gius.processing.gjkTest;

//import java.util.concurrent.Semaphore;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.MovingDistanceTestMain;
import it.gius.pePpe.SystemCostants;
//import it.gius.pePpe.algorithm.gjk.EpaAlgorithm;
import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkOverlapSolution;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.algorithm.gjk.NotConvergingException;
//import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
//import it.gius.pePpe.algorithm.gjk.shapes.GjkEllipse;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.algorithm.gjk.shapes.GjkSingleShape;
//import it.gius.pePpe.algorithm.gjk.shapes.GjkPolygon;
import it.gius.pePpe.algorithm.gjk.shapes.GjkShapesFactory;
//import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
//import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.pePpe.data.physic.PhysicShape;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.processing.util.MyAbstractPApplet;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class GjkFinalApplet extends MyAbstractPApplet {


	private double mediumIterationPP = 0;

	private long numComparisonPP = 0;

	private double mediumIterationEP = 0;
	//private long numComparisonEP = 0;

	private double mediumIterationOverlapPP = 0;
	private long numComparisonOverlapPP = 0;


	private GjkStandardAlgorithm algorithm = new GjkStandardAlgorithm();
	private EpaAlgorithm epa = new EpaAlgorithm();


	//private Polygon[] arrayPoly = null;
	//private Ellipse[] ellipseArray = null;
	//private Semaphore mutex = null;
	private GjkMinkDiffShape[][] minkDiffShape;
	//private GjkMinkDiffShape[] minkDiffpolyFirstEllipse;
	private boolean colliding[] = null;

	private Body[] bodies;
	
	//private Bind bindCacheI,bindCacheJ;

	/*public GjkFinalApplet(Polygon[] arrayPoly) {
		this.arrayPoly = arrayPoly;
		colliding = new boolean[arrayPoly.length];
	}

	public GjkFinalApplet(Polygon[] arrayPoly,Ellipse[] ellipseArray,Semaphore mutex) {
		this.arrayPoly = arrayPoly;
		this.ellipseArray = ellipseArray;
		//this.mutex = mutex;
		colliding = new boolean[arrayPoly.length];
		minkDiffShape = new GjkMinkDiffShape[arrayPoly.length][arrayPoly.length];

		minkDiffpolyFirstEllipse = new GjkMinkDiffShape[arrayPoly.length];

	}*/

	public GjkFinalApplet(Body[] bodies)/*, Bind bindCacheI,Bind bindCacheJ)*/ {
		this.bodies = bodies;
		//this.bindCacheI = bindCacheI;
		//this.bindCacheJ = bindCacheJ;

		colliding = new boolean[bodies.length];
		minkDiffShape = new GjkMinkDiffShape[bodies.length][bodies.length];

		//minkDiffpolyFirstEllipse = new GjkMinkDiffShape[arrayPoly.length];

	}

	public void setup(){
		super.setup();

		cursor(PApplet.CROSS);

		//frameRate(6f);

		size(MovingDistanceTestMain.sizeX,MovingDistanceTestMain.sizeY);
		//frameRate(15);
		if(!goStop.isGo())
			noLoop();

	}



	public double getMediumIterationOverlapPP() {
		return mediumIterationOverlapPP;
	}

	public double getMediumIterationPP() {
		return mediumIterationPP;
	}

	public double getMediumIterationEP() {
		return mediumIterationEP;
	}


	private DistanceSolution sol = new DistanceSolution();



	@SuppressWarnings("unused")
	private Vec2 oldP2 = new Vec2();
	private Vec2 oldP1 = new Vec2();

	public void draw()
	{

		//println("draw");
		background(255);


		strokeWeight(2);
		stroke(0);
		text(""+mouseX + ", " + mouseY, width-60, height-20);
		strokeWeight(1);


		/*if(mutex !=null)
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		 */
		/*for(int i=0;i < arrayPoly.length; i++)
				arrayPoly[i].move();*/


		for(int i=0; i<colliding.length;i++)
			colliding[i] = false;


		for(int i= 0;i < bodies.length; i++)
		{

			for(int j=i+1; j<bodies.length;j++)
			{

				/*try {

						if(!BoundaryBox.overlap(arrayPoly[i].getBoundaryBox(),arrayPoly[j].getBoundaryBox()))
							continue;
					} catch (BadShapeException e1) {
						e1.printStackTrace();
					}	*/


				if(minkDiffShape[i][j] == null)
				{
					//Bind bindI = bodies[i].phShapeList.get((short)0);
					Bind bindI = PhysicClassAcces.getTestIstance().getNewBind(new BindInit());//BindGenerator.getNewBind();
					PhysicShape phShapeI = bodies[i].phShapeList.get((short)0);
					bindI.body = bodies[i];
					bindI.phShape = phShapeI;
					
					GjkSingleShape contShapeI = GjkShapesFactory.getNewGjkContainedShape(bindI);

					//Bind bindJ = bodies[j].phShapeList.get((short)0);
					Bind bindJ = PhysicClassAcces.getTestIstance().getNewBind(new BindInit()); //BindGenerator.getNewBind();
					PhysicShape phShapeJ = bodies[j].phShapeList.get((short)0);
					bindJ.body = bodies[j];
					bindJ.phShape = phShapeJ;
					
					GjkSingleShape contShapeJ = GjkShapesFactory.getNewGjkContainedShape(bindJ);

					minkDiffShape[i][j] = new GjkMinkDiffShape(contShapeI,contShapeJ);
				}

				try {
					//GjkAlgorithm.distance(arrayPoly[i], arrayPoly[j], sol,sol.indexPoly1PointInSolution,sol.indexPoly2PointInSolution);
					algorithm.distance(minkDiffShape[i][j],sol);
					OverlapSolution overSol = new OverlapSolution();
					SimplexEdge simplexEdge = new SimplexEdge();
					GjkOverlapSolution gjkOverlapSolution = new GjkOverlapSolution();
					DistanceSolution distSol = new DistanceSolution();
					boolean overlap = algorithm.overlap(minkDiffShape[i][j], gjkOverlapSolution, distSol);
					if(overlap)
					{
						epa.penetrationNormalAndDepth(minkDiffShape[i][j], gjkOverlapSolution.simplexSolution,
								SystemCostants.ORIGIN2D, simplexEdge);
						
						overSol.normal.set(simplexEdge.normal);
						//overSol.penetrationDepth = simplexEdge.distance;
						overSol.distanceDepth = -simplexEdge.distance;
						overSol.epaIterations = simplexEdge.iterations;
					}
					else
					{
						//overSol.penetrationDepth = -1;
						overSol.distanceDepth = distSol.distance;
						overSol.epaIterations = -1;
					}
					//println("sol.indexPoly1PointInSolution: " + sol.indexPoly1PointInSolution);
					//println("sol.indexPoly2PointInSolution: " + sol.indexPoly2PointInSolution);
					stroke(210,210,0);
					line(sol.p1.x,sol.p1.y,sol.p2.x,sol.p2.y);
					fill(0,0,0);
					ellipse(sol.p1.x,sol.p1.y,5,5);
					ellipse(sol.p2.x,sol.p2.y,5,5);

					if(sol.distance >= -SystemCostants.EPSILON  && sol.distance < SystemCostants.EPSILON)
					{
						//System.out.println(sol.distance);
						colliding[i] = true;
						colliding[j] = true;
					}

					if(overlap)
					{
						/*SimplexEdge edgeSolution = new SimplexEdge();
							EpaAlgorithm.penetrationNormalAndDepth(minkDiffShape[i][j], sol.lastSimplex,
									SystemCostants.ORIGIN2D, edgeSolution);*/

						if(overSol.epaIterations > 1)
						{
							System.out.println("interpenetration between: " +i+ " and " +j);
							System.out.println("penetration depth: " + overSol.distanceDepth);
							System.out.println("penetration normal: " + overSol.normal);
							System.out.println("iterations epa: " + overSol.epaIterations);
						}
						//System.out.println("penetration iterations: " + edgeSolution.iterations);
						stroke(0,0,220);
						line(sol.p1.x,sol.p1.y,sol.p1.x + 15*overSol.normal.x,sol.p1.y + 15*overSol.normal.y);
					}
					else
					{

						//Vec2 vNewDist = sol.p1.sub(sol.p2);
						//float newDist = vNewDist.length();
						//System.out.println("distance: " +i +" , " + j + ": " + sol.distance + ", " + newDist);
						if(i == 2 && j == 3)
						{
							Vec2 vDist = oldP1.sub( sol.p1);
							float dist = vDist.length();

							if(dist >= 18)
							{
								System.out.println("----------jump--------");
								System.out.println("sol.iterations: " + sol.iterationCount);
							}

							oldP1.set(sol.p1);
						}
					}

					mediumIterationPP = mediumIterationPP*numComparisonPP + sol.iterationCount;
					numComparisonPP++;
					mediumIterationPP = mediumIterationPP/numComparisonPP;


					mediumIterationOverlapPP = mediumIterationOverlapPP*numComparisonOverlapPP + overSol.iterationCount;
					numComparisonOverlapPP++;
					mediumIterationOverlapPP = mediumIterationOverlapPP/numComparisonOverlapPP;

				} catch (NotConvergingException e) {
					e.printStackTrace();
					//println("center firt poly: " + arrayPoly[0].getCentroid());
					//println("center second poly: " + arrayPoly[1].getCentroid());
					goStop.setGo(false);
				}


			}


			fill(0,0,210);
			stroke(10);
			/*println("sol.distance: " + sol.distance);
				println("center firt poly: " + arrayPoly[0].getCenter());
				println("center second poly: " + arrayPoly[1].getCenter());*/
			if(colliding[i])
			{
				//if(sol.distance ==0)
				fill(210,0,0);
				stroke(210,0,0);
			}


			noFill();
			//drawPoly(arrayPoly[i],i);
			drawShape(bodies[i].phShapeList.get((short)0), bodies[i], i);

		}

		//boolean ellipseColliding = false;

		/*for( int i=0; i<ellipseArray.length;i++)
			{
				for(int j=0; j<arrayPoly.length;j++)
				{
					if(minkDiffpolyFirstEllipse[j] == null)
						minkDiffpolyFirstEllipse[j] = new GjkMinkDiffShape(new GjkEllipse(ellipseArray[i]),new GjkPolygon(arrayPoly[j]));
					//GjkMinkDiffShape minkDiffEllipse = new GjkMinkDiffShape(new GjkEllipse(ellipseArray[i]),new GjkPolygon(arrayPoly[j]));
					try {
						algorithm.distance(minkDiffpolyFirstEllipse[j],SystemCostants.ORIGIN2D,sol);
						OverlapSolution overSol = new OverlapSolution();
						boolean overlap = algorithm.overlap(minkDiffpolyFirstEllipse[j],SystemCostants.ORIGIN2D,overSol);
						if(sol.distance == 0)
							ellipseColliding = true;

						stroke(210,210,0);
						line(sol.p1.x,sol.p1.y,sol.p2.x,sol.p2.y);
						fill(0,0,0);
						ellipse(sol.p1.x,sol.p1.y,5,5);
						ellipse(sol.p2.x,sol.p2.y,5,5);

						if(/*sol.penetration /overlap)
						{
							/*SimplexEdge edgeSolution = new SimplexEdge();
							EpaAlgorithm.penetrationNormalAndDepth(minkDiffpolyFirstEllipse[j], sol.lastSimplex,
									SystemCostants.ORIGIN2D, edgeSolution);*

							System.out.println("interpenetration between: " +i+ " and " +j);
							System.out.println("penetration depth: " + overSol.penetrationDepth);
							System.out.println("penetration normal: " + overSol.normalPenetration);
							//System.out.println("penetration iterations: " + edgeSolution.iterations);
							stroke(0,0,220);
							line(sol.p1.x,sol.p1.y,sol.p1.x + 15*overSol.normalPenetration.x,sol.p1.y + 15*overSol.normalPenetration.y);
						}

						mediumIterationEP = mediumIterationEP*numComparisonEP + sol.iterationCount;
						numComparisonEP++;
						mediumIterationEP = mediumIterationEP/numComparisonEP;

						//println("iteration for ellipse polygon"+j +": " + sol.iterationCount);
					} catch (NotConvergingException e) {
						e.printStackTrace();
						println("not convergin ellipse and poly " +j);
					}
				}
				AABoundaryBox ellBox = new AABoundaryBox();
				try {
					ellipseArray[i].getBoundaryBox(ellBox);
					noFill();
					stroke(0,0,230);
					rect(ellBox.lowerBound.x,ellBox.lowerBound.y,
							ellBox.upperBound.x-ellBox.lowerBound.x, ellBox.upperBound.y-ellBox.lowerBound.y);
				} catch (BadShapeException e) {
					e.printStackTrace();
				}
				noFill();
				stroke(0);
				if(ellipseColliding)
					stroke(220,0,0);

				drawEllipse(ellipseArray[i]);
			}*/


		/*		if(mutex !=null)
				mutex.release();*/




	}

	private int indexBindDrag = -1;
	private boolean dragging = false;
	//private boolean dragEllipse = false;

	@Override
	public void mousePressed() {


		DistanceSolution sol = new DistanceSolution();
		for(int i= 0; i <bodies.length;i++)
		{
			try {
				//Bind bindI = bodies[i].phShapeList.get((short)0);
				//Bind bindI = bodies[i].phShapeList.get((short)0);
				Bind bindI = PhysicClassAcces.getTestIstance().getNewBind(new BindInit());//BindGenerator.getNewBind();
				PhysicShape phShapeI = bodies[i].phShapeList.get((short)0);
				bindI.body = bodies[i];
				bindI.phShape = phShapeI;
				
				GjkSingleShape algShapeI = GjkShapesFactory.getNewGjkAlgorithmShape(bindI);

				algorithm.distance((GjkSingleShape)algShapeI, new Vec2(mouseX,mouseY), sol);

				if(sol.distance <= 0)
				{
					indexBindDrag = i;
					dragging = true;
					if(!goStop.isGo())
						goStop.setGo(true);

					return;
				}

			} catch (NotConvergingException e) {
				e.printStackTrace();
			}
		}

		/*try {
			algorithm.distance(new GjkEllipse(ellipseArray[0]), new Vec2(mouseX,mouseY), sol);

			if(sol.distance <= 0)
			{
				dragging = true;
				dragEllipse = true;
				if(!goStop.isGo())
					goStop.setGo(true);

				return;
			}

			//println("iteration for ellipse mouse: " + sol.iterationCount);
		} catch (NotConvergingException e) {
			e.printStackTrace();
			println("mouseX: " + mouseX);
			println("mouseY: " + mouseY);
		}*/


	}


	/*public void mouseMoved() {
		if(!goStop.isGo())
			redraw();
	}*/

	@Override
	public void mouseDragged() {
		if(dragging)
		{
			/*if(dragEllipse)
				ellipseArray[0].setCenterPosition(mouseX,mouseY);
			else
				arrayPoly[indexPolyDrag].setPosition(new Vec2(mouseX,mouseY));
			 */

			//bodies[indexBindDrag].setGlobalCenter(new Vec2(mouseX,mouseY));
			bodies[indexBindDrag].getBodyPosition().globalCenter.set(mouseX,mouseY);
			bodies[indexBindDrag].synchronizeTransform();
		}
	}

	@Override
	public void mouseReleased() {
		dragging = false;
		indexBindDrag = -1;
		//dragEllipse = false;
		goStop.setGo(false);
	}


	private void drawShape(PhysicShape phShape, Body body, int index)
	{
		if(phShape.shape instanceof Polygon)
			drawPoly((Polygon)phShape.shape, index, body);

		

	}

	
	private void drawPoly(Polygon poly,int index, Body body)
	{
		pushMatrix();

		translate(body.transform.position.x, body.transform.position.y);
		rotate(body.getAngle());

		fill(0);
		for(int i=0; i< poly.getDim(); i++)
		{
			Vec2 v = poly.getVertex(i);
			text(i,v.x-3,v.y);
		}

		noFill();
		beginShape();
		for(int i=0; i< poly.getDim(); i++)
		{
			Vec2 v = poly.getVertex(i);
			vertex(v.x,v.y);
		}
		endShape(PApplet.CLOSE);

		fill(0);
		text(index,poly.getCentroid().x,poly.getCentroid().y);


		popMatrix();
	}

}
