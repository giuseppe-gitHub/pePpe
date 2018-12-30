package it.gius.pePpe.mains;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Polygon;

public class ShapeTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Polygon poly = new Polygon();
		
		//convex polygon
		poly.addVertex(new Vec2(2,2));
		poly.addVertex(new Vec2(1,4));
		poly.addVertex(new Vec2(7,5));
		poly.addVertex(new Vec2(10,3));
		
		System.out.println("poly is valid: " + poly.isConvex());
		try {
			poly.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
		}
		
		
		try {
			AABoundaryBox bbox = new AABoundaryBox(); 
				
				poly.computeBox(bbox);
			
			Vec2 l = bbox.lowerBound;
			Vec2 u = bbox.upperBound;
			System.out.println("lowerBound: " +l);
			System.out.println("upperBound: "+ u);
		} catch (BadShapeException e1) {

			e1.printStackTrace();
		}
		
		Polygon poly2 = new Polygon();
		
		//concav polygon
		poly2.addVertex(new Vec2(2,2));
		poly2.addVertex(new Vec2(1,4));
		poly2.addVertex(new Vec2(4,3));
		poly2.addVertex(new Vec2(6,5));
		
		
		try {
			poly2.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			poly2.reset();
		}

	}

}
