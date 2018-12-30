package it.gius.pePpe.collision.manifoldBuild.sat;

import java.util.Random;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.collision.manifoldBuild.sat.SATAlgorithm.MaxSeparationEdgeSolution;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import junit.framework.TestCase;

public class SATTest extends TestCase {

	private Polygon polyA,polyB;
	private float halfEdgeSize = 5;
	private float edgeSize = halfEdgeSize*2;

	public SATTest() {
		polyA = new Polygon();
		polyA.addVertex(new Vec2(0,0));
		polyA.addVertex(new Vec2(edgeSize,0));
		polyA.addVertex(new Vec2(edgeSize,edgeSize));
		polyA.addVertex(new Vec2(0,edgeSize));
		polyA.endPolygon();

		polyB = new Polygon();
		Vec2 firstPoint = new Vec2(edgeSize + halfEdgeSize, halfEdgeSize);				// 
		polyB.addVertex(firstPoint);												    // 
		polyB.addVertex(new Vec2(firstPoint.x + edgeSize, firstPoint.y + edgeSize));	//  	
		polyB.addVertex(new Vec2(firstPoint.x + 2*edgeSize, firstPoint.y));				//  
		polyB.addVertex(new Vec2(firstPoint.x + edgeSize, firstPoint.y - edgeSize));	//
		polyB.endPolygon();

	}


	public void testSeparation1() {

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float xTranslation = 20;
		transformB.position.set(edgeSize + xTranslation,0);


		int edgeOnA = 1;
		float actual = sat.separation(polyA, transformA, polyA, transformB, edgeOnA);

		float expected = xTranslation;

		assertEquals(expected, actual);
	}

	public void testSeparationWithEdgeShape()
	{
		Edge edge = new Edge(polyA.getVertex(1), polyA.getVertex(2));

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float xTranslation = 20;
		transformB.position.set(edgeSize + xTranslation,0);


		int edgeOnA = 0;
		float actual = sat.separation(edge, transformA, polyA, transformB, edgeOnA);

		float expected = xTranslation;

		assertEquals(expected, actual);

	}

	public void testSeparationOverlap()
	{
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float xTranslation = 5;
		transformB.position.set(xTranslation,0);


		int edgeOnA = 1;
		float actual = sat.separation(polyA, transformA, polyA, transformB, edgeOnA);

		float expected = -edgeSize + xTranslation;
		System.out.println(expected);

		assertEquals(expected, actual);
	}

	public void testSeparation2() {

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float xTranslation = 20;
		transformB.position.set(edgeSize + xTranslation,0);


		int edgeOnA = 3;
		float actual = sat.separation(polyA, transformB, polyA, transformA, edgeOnA);

		float expected = xTranslation;

		assertEquals(expected, actual);
	}

	public void testSeparation3() {

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		int edgeOnA = 1;
		float actual = sat.separation(polyA, transformB, polyB, transformA, edgeOnA);

		float expected = halfEdgeSize;

		assertEquals(expected, actual);
	}

	public void testSeparation4() {

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		int edgeOnA = 0;
		float actual = sat.separation(polyB, transformB, polyA, transformA, edgeOnA);

		float expected = 0;

		assertEquals(expected, actual);
	}


	public void testMaxSeparationEdgeA1() {

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float xTranslation = 20;
		transformB.position.set(edgeSize + xTranslation,0);

		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyA, transformA, polyA, transformB, 0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();
		expected.edgeIndex = 1;
		expected.globalNormal.set(1, 0);
		expected.separation = xTranslation;

		assertEquals(expected, actual);
	}

	public void testMaxSeparationEdgeA2() {
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float translation = 20;
		transformB.position.set(edgeSize + translation,edgeSize + translation);

		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyA, transformA, polyA, transformB, 0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();
		expected.edgeIndex = 2;
		expected.globalNormal.set(0,1);
		expected.separation = translation;

		assertEquals(expected, actual);
	}

	public void testMaxSeparationEdgeA3() {
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();


		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyA, transformA, polyB, transformB, 0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();
		expected.edgeIndex = 1;
		expected.globalNormal.set(1,0);
		expected.separation = halfEdgeSize;

		assertEquals(expected, actual);
	}

	public void testMaxSeparationEdgeA4() {
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();


		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyB, transformA, polyA, transformB, 0, actual);

		MaxSeparationEdgeSolution expected1 = new MaxSeparationEdgeSolution();
		expected1.edgeIndex = 0;
		expected1.globalNormal.set(polyB.getNormal(0));
		expected1.separation = 0;
		
		MaxSeparationEdgeSolution expected2 = new MaxSeparationEdgeSolution();
		expected2.edgeIndex = 3;
		expected2.globalNormal.set(polyB.getNormal(3));
		expected2.separation = 0;

		if(!actual.equals(expected1) && !actual.equals(expected2))
			fail();
	}

	public void testMaxSeparationEdgeWithShapeEdge1()
	{
		Edge edge = new Edge(polyA.getVertex(1), polyA.getVertex(2));

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		float xTranslation = 20;
		transformB.position.set(edgeSize + xTranslation,0);


		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(edge, transformA, polyA, transformB,0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();
		expected.edgeIndex = 0;
		expected.globalNormal.set(1,0);
		expected.separation = xTranslation;

		assertEquals(expected, actual);

	}
	
	public void testMaxSeparationEdgeWithShapeEdge2()
	{
		Edge edge = new Edge(polyA.getVertex(1), polyA.getVertex(2));

		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();
		
		float xTranslation = 20;
		transformA.position.set(xTranslation,0);

		Transform transformB = new Transform();
		transformB.setIdentity();

		

		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(edge, transformA, polyA, transformB,0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();
		expected.edgeIndex = 0;
		expected.globalNormal.set(-1,0);
		expected.separation = xTranslation;

		assertEquals(expected, actual);

	}


	public void testMaxSeparationEdgeOVerlap1()
	{
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();

		Random random = new Random();


		float xTranslation = random.nextInt((int)edgeSize-2) +1f;
		float yTranslation = random.nextInt((int)edgeSize-2) +1f;
		transformB.position.set(xTranslation,yTranslation);

		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyA, transformA, polyA, transformB, 0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();
		if(xTranslation > yTranslation)
		{
			expected.edgeIndex = 1;
			expected.globalNormal.set(polyA.getNormal(1));
			expected.separation = -edgeSize + xTranslation;
		}
		else
		{
			expected.edgeIndex = 2;
			expected.globalNormal.set(polyA.getNormal(2));
			expected.separation = -edgeSize + yTranslation;
		}

		System.out.println("expected index: " + expected.edgeIndex);
		System.out.println("expected separation: " + expected.separation);
		assertEquals(expected, actual);
	}

	public void testMaxSeparationEdgeOVerlap2()
	{
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();


		float translation = 2;
		transformB.position.set(translation,translation);

		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyA, transformA, polyA, transformB, 0, actual);

		MaxSeparationEdgeSolution expected = new MaxSeparationEdgeSolution();

		expected.edgeIndex = 2;
		expected.globalNormal.set(polyA.getNormal(2));
		expected.separation = -edgeSize + translation;


		System.out.println("expected index: " + expected.edgeIndex);
		System.out.println("expected separation: " + expected.separation);
		assertEquals(expected, actual);
	}

	public void testMaxSeparationEdgeOVerlap3()
	{
		SATAlgorithm sat = new SATAlgorithm();

		Transform  transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();


		float translation = 2;
		transformB.position.set(translation,translation);

		MaxSeparationEdgeSolution actual = new MaxSeparationEdgeSolution();
		sat.maxSeparationEdgeA(polyA, transformA, polyA, transformB, 3, actual);

		MaxSeparationEdgeSolution expected1 = new MaxSeparationEdgeSolution();

		expected1.edgeIndex = 1;
		expected1.globalNormal.set(polyA.getNormal(1));
		expected1.separation = -edgeSize + translation;

		MaxSeparationEdgeSolution expected2 = new MaxSeparationEdgeSolution();

		expected2.edgeIndex = 2;
		expected2.globalNormal.set(polyA.getNormal(2));
		expected2.separation = -edgeSize + translation;


		System.out.println("expected separation: " + expected1.separation);
		if(!actual.equals(expected1) && !actual.equals(expected2))
			fail();
	}

}
