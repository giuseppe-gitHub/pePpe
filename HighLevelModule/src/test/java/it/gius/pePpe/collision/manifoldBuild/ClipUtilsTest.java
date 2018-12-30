package it.gius.pePpe.collision.manifoldBuild;

import java.util.Random;

import it.gius.pePpe.collision.manifoldBuild.clipping.ClipPoint;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipUtils;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipEdge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.ShapeUtils;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import junit.framework.TestCase;

public class ClipUtilsTest extends TestCase {

	private ClipPoint[] result;
	
	public ClipUtilsTest() {
		
		result = new ClipPoint[2];
		result[0] = new ClipPoint();
		result[1] = new ClipPoint(); 
	}
	
	public void testMostPerpendicular1() {
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 normal = new Vec2(0,-1);
		
		Vec2 edge1 = new Vec2(10,0);
		Vec2 edge2 = new Vec2(2,2);
		
		int result = clipUtils.mostPerpendicular(edge1, edge2, normal);
		
		if(result != 1)
			fail("Wrong result");
		
	}

	
	public void testMostPerpendicular2() {
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 normal = new Vec2(1,-1);
		
		Vec2 edge1 = new Vec2(-5,1);
		Vec2 edge2 = new Vec2(-1,4);
		
		int result = clipUtils.mostPerpendicular(edge1, edge2, normal);
		
		if(result != 1)
			fail("Wrong result");
		
	}

	/*
	 * Test with and edge parallel to the normal
	 */
	public void testMostPerpendicular3() {
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 normal = new Vec2(2,-2);
		
		Vec2 edge1 = new Vec2(0.5f,-0.5f);
		Vec2 edge2 = new Vec2(-1,4);
		
		int result = clipUtils.mostPerpendicular(edge1, edge2, normal);
		
		if(result != 2)
			fail("Wrong result");
		
	}
	
	/*
	 * Test with and edge perpendicular to the normal
	 */
	public void testMostPerpendicular4() {
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 normal = new Vec2(3,-2);
		
		Vec2 edge1 = new Vec2(-0.5f,-0.75f);
		Vec2 edge2 = new Vec2(3,-1);
		
		int result = clipUtils.mostPerpendicular(edge1, edge2, normal);
		
		if(result != 1)
			fail("Wrong result");
		
	}
	
	
	
	/*
	 * example on
	 * http://www.codezealot.org/archives/394
	 * 
	 */
	public void testClip1()
	{
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 v1 = new Vec2(12,5);
		ClipPoint p1 = new ClipPoint();
		p1.index = 0;
		p1.point.set(v1);
		Vec2 v2 = new Vec2(4,5);
		ClipPoint p2 = new ClipPoint();
		p2.index = 1;
		p2.point.set(v2);
		
		Vec2 normal = new Vec2(1,0);
		float o = 8;
		
		clipUtils.clip(p1, p2, normal, o,1, result);
		
		if(!result[0].point.equals(v1))
			fail("Wrong first result");
		
		if(!result[1].point.equals(new Vec2(8,5)))
			fail("Wrong second result");
		
		/*if(!result[1].internal)
			fail();*/
		
		if(result[1].refPointIndex != 1)
			fail();
		
	}
	
	

	/*
	 * example on
	 * http://www.codezealot.org/archives/394
	 * 
	 */
	public void testClip2()
	{
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 v1 = new Vec2(12,5);
		ClipPoint p1 = new ClipPoint();
		p1.point.set(v1);
		p1.index = 0;
		Vec2 v2 = new Vec2(8,5);
		ClipPoint p2 = new ClipPoint();
		p2.point.set(v2);
		p2.index = 1;
		
		Vec2 normal = new Vec2(-1,0);
		float o = -14;
		
		clipUtils.clip(p1, p2, normal, o, 2, result);
		
		if(!result[0].point.equals(v1))
			fail("Wrong first result");
		
		if(!result[1].point.equals(v2))
			fail("Wrong second result");
		
	}
	
	
	/*
	 * example on
	 * http://www.codezealot.org/archives/394
	 * 
	 */
	public void testClip3()
	{
		ClipUtils clipUtils = new ClipUtils();
		
		Vec2 v1 = new Vec2(2,8);
		ClipPoint p1 = new ClipPoint();
		p1.point.set(v1);
		p1.index = 0;
		Vec2 v2 = new Vec2(6,4);
		ClipPoint p2 = new ClipPoint();
		p2.point.set(v2);
		p2.index = 1;
		
		Vec2 normal = new Vec2(1,0);
		float o = 4;
		
		clipUtils.clip(p1, p2, normal, o, 3, result);
		
		if(!result[0].point.equals(new Vec2(4,6)))
			fail("Wrong first result");
		
		/*if(!result[0].internal)
			fail();*/
	
		if(result[0].refPointIndex != 3)
			fail();
		
		if(!result[1].point.equals(v2))
			fail("Wrong second result");
			
	}
	
	
	public void testBestEdge1()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(10,0));
		polygon.addVertex(new Vec2(10,20));
		polygon.addVertex(new Vec2(0,20));
		polygon.endPolygon();
		
		Vec2 normal = new Vec2(2,5);
		
		ClipUtils clipUtils = new ClipUtils();
		
		Transform transform = new Transform();
		transform.setIdentity();
		
		ClipEdge edgeResult = new ClipEdge();
		
		ClipEdge expected = new ClipEdge(new Vec2(10,20),new Vec2(0,20),new Vec2(10,20),2);
		
		clipUtils.bestEdge(polygon, transform, normal, edgeResult);
		
		assertEquals(expected, edgeResult);
		
		
	}
	
	public void testBestEdge2()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(10,0));
		polygon.addVertex(new Vec2(10,20));
		polygon.addVertex(new Vec2(0,20));
		polygon.endPolygon();
		
		Vec2 normal = new Vec2(1,-1);
		
		ClipUtils clipUtils = new ClipUtils();
		
		Transform transform = new Transform();
		transform.setIdentity();
		transform.set(new Vec2(0,0), MathUtils.PI/4);
		
		ClipEdge edgeResult = new ClipEdge();
		
		//ClipEdge expected = new ClipEdge(new Vec2(10,20),new Vec2(0,20),new Vec2(10,20),2);
		
		clipUtils.bestEdge(polygon, transform, normal, edgeResult);
		
		if(edgeResult.edgeIndex != 0)
			fail();
		//assertEquals(expected, edgeResult);
		
		
	}
	
	/*
	 * test using Transform
	 */
	public void testBestEdge3()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(10,0));
		polygon.addVertex(new Vec2(10,20));
		polygon.addVertex(new Vec2(0,20));
		polygon.endPolygon();
		
		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		/*float f1 = 6.987238f;
		float f2 = 9.574436f;
		float angle = 1.0364108f;*/
		transform.set(new Vec2(f1,f2), angle/*MathUtils.PI/4*/);
		
		Polygon transPoly = new Polygon();
		ShapeUtils shapeUtils = new ShapeUtils();
		shapeUtils.mulTransToOutPolygon(polygon, transform, transPoly);
		transPoly.endPolygon();
		
		Vec2 normal = new Vec2(1,0);
		
		ClipUtils clipUtils = new ClipUtils();
		
		
		ClipEdge edgeResult = new ClipEdge();
		
		//ClipEdge expected = new ClipEdge(new Vec2(10,20),new Vec2(0,20),new Vec2(10,20),2);
		
		clipUtils.bestEdge(transPoly, transform, normal, edgeResult);
		
		if(edgeResult.edgeIndex != 1)
			fail();
	}
}
