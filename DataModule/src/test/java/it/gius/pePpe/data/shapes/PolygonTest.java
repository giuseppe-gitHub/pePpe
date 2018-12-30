package it.gius.pePpe.data.shapes;

import it.gius.pePpe.MathUtils;
import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.Vec2;

import junit.framework.TestCase;

public class PolygonTest extends TestCase {
	
	
	public void testSupportPoint1()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(10,20));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,10));
		polygon.endPolygon();
		
		Vec2 d = new Vec2(1,0.5f);
		
		Vec2 actual = new Vec2();
		polygon.supportPoint(d, actual, new VertexIndexWitness());
		
		Vec2 expected = new Vec2(20,20);
		
		assertEquals(expected, actual);

	}
	
	public void testSupportPoint2()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(-10,-10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,-10));
		polygon.endPolygon();
		
		Vec2 d = new Vec2(-0.001f,-1);
		
		Vec2 actual = new Vec2();
		polygon.supportPoint(d, actual, new VertexIndexWitness());
		
		Vec2 expected = new Vec2(-10,-10);
		
		assertEquals(expected, actual);

	}
	
	public void testSupportPointLastPoint()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(-10,-10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,-10));
		polygon.endPolygon();
		
		Vec2 d = new Vec2(+0.001f,-1);
		
		Vec2 actual = new Vec2();
		polygon.supportPoint(d, actual, new VertexIndexWitness());
		
		Vec2 expected = new Vec2(20,-10);
		
		assertEquals(expected, actual);

	}
	
	public void testSupportPointBreak()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(-10,-10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,-10));
		polygon.endPolygon();
		
		Vec2 d = new Vec2(0,-1);
		
		Vec2 actual = new Vec2();
		polygon.supportPoint(d, actual, new VertexIndexWitness());
		
		Vec2 expected = new Vec2(-10,-10);
		
		assertEquals(expected, actual);

	}

	public void testIsConvex() {

		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,10));
		boolean convex = polygon.isConvex();

		if(!convex)
			fail();


		polygon = new Polygon();
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,10));
		polygon.addVertex(new Vec2(15,15));
		 convex = polygon.isConvex();

		if(convex)
			fail();

	}
	
	public void testSetAsBox()
	{
		Polygon polygon = new Polygon();
		
		try {
			polygon.setAsBox(2, 2, 4, 4);
		} catch (BadShapeException e) {
			e.printStackTrace();
			fail();
		}
		
		if(!polygon.centroid.equals(new Vec2(3,3)))
			fail("Wrong centroid!");
		
		if(polygon.centroid.equals(new Vec2(3,2)))
			fail("Wrong centroid!");
		
		polygon = new Polygon();
		
		try {
			polygon.setAsBox(3, 3, 2, 4);
			fail();
		} catch (BadShapeException e) {
		}
		
	}
	
	
	public void testContains()
	{

		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(20,10));
		polygon.endPolygon();
		
		
		boolean contains = polygon.contains(new Vec2(15,12));
		
		if(!contains)
			fail();
		
		contains = polygon.contains(new Vec2(10,10));
		
		if(contains)
			fail();
		
		
		contains = polygon.contains(new Vec2(22,10));
		

		if(contains)
			fail();
	}
	
	public void testEquals()
	{
		Polygon polygonA = new Polygon();
		polygonA.addVertex(new Vec2(10,10));
		polygonA.addVertex(new Vec2(20,20));
		polygonA.addVertex(new Vec2(20,10));
		polygonA.endPolygon();
		
		Polygon polygonB = new Polygon();
		polygonB.addVertex(new Vec2(10,10));
		polygonB.addVertex(new Vec2(20,20));
		polygonB.addVertex(new Vec2(20,10));
		polygonB.endPolygon();
		
		Polygon polygonC = new Polygon();
		polygonC.addVertex(new Vec2(10,10));
		polygonC.addVertex(new Vec2(20,10)); //<-- different
		polygonC.addVertex(new Vec2(20,10));
		polygonC.endPolygon();
		
		Polygon polygonD = new Polygon();
		polygonD.addVertex(new Vec2(10,10));
		polygonD.addVertex(new Vec2(20,20));
		polygonD.addVertex(new Vec2(20,10));
		polygonD.addVertex(new Vec2(15,5));
		polygonD.endPolygon();
		
		if(!polygonA.equals(polygonA))
			fail();
		
		if(!polygonA.equals(polygonB))
			fail();
		
		if(polygonA.equals(polygonC))
			fail();
		
		if(polygonA.equals(polygonD))
			fail();
	}
	
	
	
	public void testContainsCC()
	{

		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(20,10));
		polygon.addVertex(new Vec2(20,20));
		polygon.addVertex(new Vec2(10,10));
		polygon.endPolygon();
		
		
		boolean contains = polygon.contains(new Vec2(15,12));
		
		if(!contains)
			fail();
		
		contains = polygon.contains(new Vec2(10,10));
		
		if(contains)
			fail();
		
		
		contains = polygon.contains(new Vec2(22,10));
		

		if(contains)
			fail();
	}
	
	
	public void testNormal1()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(0,10));
		polygon.endPolygon();
		
		Vec2 expected = new Vec2(1,-1);
		expected.normalize();
		
		Vec2 actual = polygon.getNormal(0);
		
		float distance = MathUtils.manhattanDistance(expected, actual);
		if(distance > SystemCostants.EPSILON)
			fail();
		
	
	}
	
	public void testNormal2()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(10,0));
		polygon.endPolygon();
		
		Vec2 expected = new Vec2(-1,1);
		expected.normalize();
		
		Vec2 actual = polygon.getNormal(0);
		
		float distance = MathUtils.manhattanDistance(expected, actual);
		if(distance > SystemCostants.EPSILON)
			fail();
		
	
	}
	
	
	public void testTangent1()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(10,0));
		polygon.endPolygon();
		
		Vec2 expected = new Vec2(1,1);
		expected.normalize();
		
		Vec2 actual = polygon.getTangent(0);
		
		float distance = MathUtils.manhattanDistance(expected, actual);
		if(distance > SystemCostants.EPSILON)
			fail();
	}
	
	public void testTangent2()
	{
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vec2(0,0));
		polygon.addVertex(new Vec2(-10,-10));
		polygon.addVertex(new Vec2(-10,0));
		polygon.endPolygon();
		
		Vec2 expected = new Vec2(-1,-1);
		expected.normalize();
		
		Vec2 actual = polygon.getTangent(0);
		
		float distance = MathUtils.manhattanDistance(expected, actual);
		if(distance > SystemCostants.EPSILON)
			fail();
	}

}
