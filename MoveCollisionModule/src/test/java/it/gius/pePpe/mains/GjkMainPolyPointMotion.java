package it.gius.pePpe.mains;

import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.processing.gjkTest.GjkPolyPointMotionApplet;
//import it.gius.processing.gjkTest.GjkPolyPointStepToStepApplet;
import it.gius.processing.util.PAppletManager;
import it.gius.processing.util.ProcessingGraphicException;

import org.jbox2d.common.Vec2;

@Deprecated
public class GjkMainPolyPointMotion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Polygon poly1 = new Polygon();

		//convex polygon
		poly1.addVertex(new Vec2(40,110));
		poly1.addVertex(new Vec2(22,170));
		poly1.addVertex(new Vec2(100,190));
		poly1.addVertex(new Vec2(70,120));

		
		try {
			poly1.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 1");
			poly1 = null;
		}
		
		//poly1.setPosition(new Vec2(100,200));
		//poly1.velocity.set(0.7f,0);
		
		//Vec2 q = new Vec2 (110,210); //punto interno
		Vec2 q = new Vec2(190,190);
		
		GjkPolyPointMotionApplet applet = new GjkPolyPointMotionApplet(poly1,q);
		
		PAppletManager manager = new PAppletManager();
		try {
			manager.startAndAddApplet(new String[]{""}, applet, PAppletManager.DISPOSE_ALL);
		} catch (ProcessingGraphicException e) {
			e.printStackTrace();
			System.exit(1);
		}


	}

}
