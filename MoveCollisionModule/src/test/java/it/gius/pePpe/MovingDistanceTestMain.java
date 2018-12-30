package it.gius.pePpe;

import java.awt.event.WindowEvent;
//import java.util.concurrent.Semaphore;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.processing.gjkTest.GjkFinalApplet;
import it.gius.processing.gjkTest.AABBsApplet;
import it.gius.processing.util.PAppletManager;
import it.gius.processing.util.ProcessingGraphicException;
import it.gius.processing.util.WindowClosedListener;

public class MovingDistanceTestMain {

	
	public static final int sizeX = 400;
	public static final int sizeY = 400;
	
	static Polygon poly1,poly2,poly3 = null;
	
	static Circle Circle;

	public static void main(String args[])
	{
		initShapes();
		
		Polygon[] arrayPoly = new Polygon[3];
		arrayPoly[0] = poly1;
		arrayPoly[1] = poly2;
		arrayPoly[2] = poly3;
		//arrayPoly[0] = poly3;
		
		Circle[] CircleArray = new Circle[1];
		CircleArray[0] = Circle;
		
		BodyInit bodyInit = new BodyInit();
		bodyInit.angularDamping = 0;
		bodyInit.linearDamping = 0;
		bodyInit.startAngularVelocity = 0;
		bodyInit.startLinearVelocity = new Vec2();
		bodyInit.startLinearVelocity.setZero();
		
		bodyInit.globalOrigin.set(new Vec2(95,200));
		//poly1.setPosition(new Vec2(95,200));
		PhysicClassAcces access = PhysicClassAcces.getTestIstance();
		Body[] bodies = new Body[4];
		
		bodies[0] = access.getNewBody(bodyInit);//new Body(bodyInit);
		
		BindInit bindInit = new BindInit();
		bindInit.body = bodies[0];
		bindInit.shape = poly1;
		bindInit.density = 0.1f;
		bindInit.friction = 0;
		bindInit.restituion = 1;
		
		Bind  bind = access.getNewBind(bindInit);//new Bind(bindInit);
		
		//bodies[0].addBind(bind);
		access.addPhysicShapeToBody(bodies[0], bind.phShape);//bodies[0].addPhysicShape(bind.phShape);
		
		BodyInit bodyInit2 = bodyInit.clonePrototype();
		bodyInit2.globalOrigin.set(new Vec2(300,200));
		//poly2.setPosition(new Vec2(300,200));

		
		bodies[1] = access.getNewBody(bodyInit2);//new Body(bodyInit2);
		//bodies[1].setAngle((float)Math.PI/4f);
		bodies[1].getBodyPosition().angle = (float)Math.PI/4f;
		bodies[1].synchronizeTransform();
		
		BindInit bindInit2 = bindInit.clonePrototype();
		bindInit2.shape = poly2;
		
		Bind bind2 = access.getNewBind(bindInit2);//new Bind(bindInit2);
		
		//bodies[1].addBind(bind2);
		access.addPhysicShapeToBody(bodies[1], bind2.phShape);//bodies[1].addPhysicShape(bind2.phShape);
		
		BodyInit bodyInit3 = bodyInit.clonePrototype();
		bodyInit3.globalOrigin.set(new Vec2(250,100));
		//poly3.setPosition(new Vec2(250,100));
		
		bodies[2] = access.getNewBody(bodyInit3);//new Body(bodyInit3);
		//bodies[2].setAngle(3f*(float)Math.PI/2f - 3f*(float)Math.PI/10f);
		bodies[2].getBodyPosition().angle = 3f*(float)Math.PI/2f - 3f*(float)Math.PI/10f;
		bodies[2].synchronizeTransform();
		
		BindInit bindInit3 = bindInit.clonePrototype();
		bindInit3.shape = poly3;
		
		Bind bind3 = access.getNewBind(bindInit3);//new Bind(bindInit3);
		
		//bodies[2].addBind(bind3);
		access.addPhysicShapeToBody(bodies[2], bind3.phShape);	//bodies[2].addPhysicShape(bind3.phShape);
		
		BodyInit bodyInit4 = bodyInit.clonePrototype();
		bodyInit4.globalOrigin.set(new Vec2(100,100));
		
		bodies[3] = access.getNewBody(bodyInit4);//new Body(bodyInit4);
		
		BindInit bindInit4 = bindInit.clonePrototype();
		bindInit4.shape = Circle;
		
		Bind bind4 = access.getNewBind(bindInit4);//new Bind(bindInit4);
		
		//bodies[3].addBind(bind4);
		access.addPhysicShapeToBody(bodies[3], bind4.phShape);//bodies[3].addPhysicShape(bind4.phShape);
		//bodies[3].setAngle(2f*(float)Math.PI/3f);
		bodies[3].getBodyPosition().angle = 2f*(float)Math.PI/3f;
		bodies[3].synchronizeTransform();

		//Semaphore mutex = new Semaphore(1);
		
		//GjkFinalApplet gjkApplet = new GjkFinalApplet(arrayPoly,CircleArray,mutex);
		
		GjkFinalApplet gjkApplet = new GjkFinalApplet(bodies);//,bindCacheI,bindCacheJ);
		//AABBsApplet minkApplet = new AABBsApplet(arrayPoly,mutex);
		AABBsApplet aabbApplet = new AABBsApplet(bodies);
		boolean exitOnLastDisposed = false;
		boolean manualStart = true;
		PAppletManager manager = new PAppletManager(exitOnLastDisposed,manualStart);


		try {
			/*mutex = */manager.startAndAddApplet(new String[]{"--location=150,200"},
					gjkApplet,PAppletManager.DISPOSE_ALL, new WindowClosed(gjkApplet));
			
			manager.startAndAddApplet(new String[]{"--location=605,200"},
					aabbApplet,PAppletManager.DISPOSE_ONLY_ME);
			
		} catch (ProcessingGraphicException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/*try {
			if(mutex!= null)
				mutex.acquire();
			
			double mediumEP = gjkApplet.getMediumIterationEP();
			double mediumPP = gjkApplet.getMediumIterationPP();
			double mediumOverlapPP = gjkApplet.getMediumIterationOverlapPP();
			
			System.out.println("medium iteration distance Circle polygon: " + mediumEP);
			System.out.println("medium iteration distance polygon polygon: " + mediumPP);
			System.out.println("medium iteration overlap polygon polygon: " + mediumOverlapPP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/


	}
	
	private static class WindowClosed implements WindowClosedListener
	{
		
		private GjkFinalApplet gjkApplet;
		
		public WindowClosed(GjkFinalApplet applet) {
			this.gjkApplet = applet;
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			
			double mediumEP = gjkApplet.getMediumIterationEP();
			double mediumPP = gjkApplet.getMediumIterationPP();
			double mediumOverlapPP = gjkApplet.getMediumIterationOverlapPP();
			
			System.out.println("medium iteration distance Circle polygon: " + mediumEP);
			System.out.println("medium iteration distance polygon polygon: " + mediumPP);
			System.out.println("medium iteration overlap polygon polygon: " + mediumOverlapPP);
		}
	}
	
	
	private static void initShapes()
	{
		poly1 = new Polygon();

		//convex polygon
		poly1.addVertex(new Vec2(20,10));
		poly1.addVertex(new Vec2(2,30));
		poly1.addVertex(new Vec2(40,40));
		poly1.addVertex(new Vec2(50,20));

		
		try {
			poly1.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 1");
			poly1 = null;
		}
		
		
		//poly1.velocity.set(0.5f,0);	
		
		
		poly2 = new Polygon();

		//convex polygon
		poly2.addVertex(new Vec2(20,15));
		poly2.addVertex(new Vec2(12,50));
		poly2.addVertex(new Vec2(60,50));
		poly2.addVertex(new Vec2(50,20));

		
		try {
			poly2.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 2");
			poly2 = null;
		}
		
		
		//poly2.velocity.set(-0.25f,0);	
		
		
		
		poly3 = new Polygon();

		//convex polygon
		poly3.addVertex(new Vec2(0,0));
		poly3.addVertex(new Vec2(0,20));
		poly3.addVertex(new Vec2(30,50));
		poly3.addVertex(new Vec2(50,50));
		poly3.addVertex(new Vec2(90,40));
		poly3.addVertex(new Vec2(75,20));

		
		try {
			poly3.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 3");
			poly3 = null;
		}
		

		//poly2.velocity.set(-0.25f,0);	

		
		Circle = new Circle(new Vec2(100,100),30);
		//new Vec2(100,100)

	}

}
