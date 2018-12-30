package it.gius.processing.gjkTest;

import java.util.concurrent.Semaphore;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.MovingDistanceTestMain;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.shapes.BadShapeException;
//import it.gius.pePpe.data.shapes.Polygon;
import it.gius.processing.util.MyAbstractPApplet;
//import processing.core.PApplet;


public class AABBsApplet extends MyAbstractPApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Polygon[] arrayPoly;
	Semaphore mutex = null;
	AABoundaryBox bboxs[];
	
	private Body[] bodies;
	
	/*public AABBsApplet(Polygon[] arrayPoly) {
		this.arrayPoly = arrayPoly;
		bboxs = new AABoundaryBox[arrayPoly.length];
	}
	
	public AABBsApplet(Polygon[] arrayPoly, Semaphore mutex) {
		this.arrayPoly = arrayPoly;
		bboxs = new AABoundaryBox[arrayPoly.length];
		this.mutex = mutex;
	}*/
	
	public AABBsApplet(Body[] bodies) {
		this.bodies = bodies;
		
		bboxs = new AABoundaryBox[bodies.length];

	}
	
	
	public void setup(){
		super.setup();
		size(MovingDistanceTestMain.sizeX,MovingDistanceTestMain.sizeY);
		if(!goStop.isGo())
			noLoop();
	}

	
	
	public void draw()
	{
		//println("draw");
		
		background(255);
		
		/*if(mutex!= null)
			try {
				mutex.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}*/
		
		
		for(int i=0; i< bboxs.length; i++)
		{
			if(bboxs[i] == null)
				bboxs[i] = new AABoundaryBox();
		try {
			 //bodies[i].phShapeList.get((short)0).getAABoundaryBox(bboxs[i]);//.shape.getBoundaryBox(bboxs[i]);
			bodies[i].phShapeList.get((short)0).shape.computeBox(bboxs[i],bodies[i].transform);
		} catch (BadShapeException e) {
			e.printStackTrace();
			return;
		}
		}
		
		noFill();
		strokeWeight(3);
		for(int i=0; i< bboxs.length;i++)
		{
			stroke(0,0,200);
			for(int j=0; j< bboxs.length; j++)
			{
				if(i!=j && AABoundaryBox.overlap(bboxs[i], bboxs[j]))
				{
					stroke(200,0,0);
					
				}
			}
			
			//drawBBox(bboxs[i]);
			Vec2 l = bboxs[i].lowerBound;
			Vec2 u = bboxs[i].upperBound;
			rect(l.x,l.y, u.x - l.x, u.y - l.y);
		}
		
		/*if(mutex!=null)
			mutex.release();*/
		
	}
	
	
	/*private void drawBBox(BoundaryBox bbox)
	{
		Vec2 l = bbox.getLowerBound();
		Vec2 u = bbox.getUpperBound();
		
		beginShape();
		vertex(l.x,l.y);
		vertex(l.x,u.y);
		vertex(u.x,u.y);
		vertex(u.x,l.y);
		endShape(PApplet.CLOSE);
	}*/

}
