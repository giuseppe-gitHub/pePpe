package it.gius.pePpe.distance;

import java.util.Random;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.ShapeUtils;
import junit.framework.TestCase;

/*
 * Some test data from:
 * http://www.codezealot.org/archives/394
 */
public class GjkDistanceStatelessImplTest extends TestCase {

	//TODO test point circle, test cache

	private GjkDistanceStatelessImpl distance;
	private Polygon polyC, polyD;

	private Circle circle;

	public GjkDistanceStatelessImplTest() {
		distance = new GjkDistanceStatelessImpl();

		polyC = new Polygon();
		polyC.addVertex(new Vec2(2,8));
		polyC.addVertex(new Vec2(6,4));
		polyC.addVertex(new Vec2(9,7));
		polyC.addVertex(new Vec2(5,11));
		polyC.endPolygon();

		polyD = new Polygon();
		polyD.addVertex(new Vec2(4,2)); //low lx 0
		polyD.addVertex(new Vec2(12,2)); //low rx 1
		polyD.addVertex(new Vec2(12,5)); //up rx 2
		polyD.addVertex(new Vec2(4,5)); //up lx 3
		polyD.endPolygon();

		circle = new Circle(new Vec2(8,9), 5);
	}

	private void assertVec2(Vec2 expected, Vec2 actual)
	{
		if(expected.x != actual.x)
			fail();

		if(expected.y != actual.y)
			fail();
	}

	public void testOverlapPolyPoly()
	{

		Transform transform = new Transform();
		transform.setIdentity();

		OverlapSolution actual = new OverlapSolution();
		distance.overlap(polyC, transform, polyD, transform, actual);

		OverlapSolution expected = new OverlapSolution();

		expected.distanceDepth = -1;
		expected.normal.set(0,-1);

		assertEquals(expected.distanceDepth, actual.distanceDepth);

		//assertEquals(expected.normal, actual.normal);
		assertVec2(expected.normal, actual.normal);

	}


	public void testOverlapPolyPolyTransform()
	{

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*org.jbox2d.common.MathUtils.PI* random.nextFloat();
		transform.set(new Vec2(f1,f2), angle);
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);

		Polygon polyCLocal = new Polygon();
		ShapeUtils shapeUtils = new ShapeUtils();
		shapeUtils.mulTransToOutPolygon(polyC, transform, polyCLocal);

		Polygon polyDLocal = new Polygon();
		shapeUtils.mulTransToOutPolygon(polyD, transform, polyDLocal);

		OverlapSolution actual = new OverlapSolution();
		distance.overlap(polyCLocal, transform, polyDLocal, transform, actual);

		OverlapSolution expected = new OverlapSolution();

		expected.distanceDepth = -1;
		expected.normal.set(0,-1);

		float d = MathUtils.abs(expected.distanceDepth - actual.distanceDepth);
		System.out.println(d);
		if(d > SystemCostants.EPSILON)
			fail("" +d);
		//assertEquals(expected.distanceDepth, actual.distanceDepth);

		//assertEquals(expected.normal, actual.normal);
		d = it.gius.pePpe.MathUtils.manhattanDistance(expected.normal, actual.normal);
		System.out.println(d);
		if(d > SystemCostants.EPSILON)
			fail("" +d);

	}


	public void testDistancePolyPoly()
	{

		Transform transformA = new Transform();
		transformA.setIdentity();

		transformA.position.set(0,2);

		Transform transformB = new Transform();
		transformB.setIdentity();

		DistanceSolution actual = new DistanceSolution();
		distance.distance(polyC, transformA, polyD, transformB, actual);

		DistanceSolution expected = new DistanceSolution();

		expected.distance= 1;
		//expected.normal.set(0,-1);
		expected.p1.set(6,4+2);
		expected.p2.set(6,5);
		expected.penetration = false;

		assertEquals(expected.distance, actual.distance);

		assertEquals(expected.penetration, actual.penetration);
		//assertEquals(expected.normal, actual.normal);
		//assertVec2(expected.normal, actual.normal);
		assertVec2(expected.p1, actual.p1);
		assertVec2(expected.p2,actual.p2);

	}

	public void testOverlapCirclePoly()
	{

		Transform transform = new Transform();
		transform.setIdentity();

		OverlapSolution actual = new OverlapSolution();
		distance.overlap(circle, transform, polyD, transform, actual);

		OverlapSolution expected = new OverlapSolution();

		expected.distanceDepth = -1;
		expected.normal.set(0,-1);

		assertEquals(expected.distanceDepth, actual.distanceDepth);

		//assertEquals(expected.normal, actual.normal);
		assertVec2(expected.normal, actual.normal);

	}

	public void testOverlapCirclePolyTransform()
	{

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*org.jbox2d.common.MathUtils.PI* random.nextFloat();
		transform.set(new Vec2(f1,f2), angle);
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);

		Polygon polyDLocal = new Polygon();
		new ShapeUtils().mulTransToOutPolygon(polyD, transform, polyDLocal);

		Vec2 centerLocal = new Vec2();
		Transform.mulTransToOut(transform, circle.centroid, centerLocal);
		Circle circleLocal = new Circle(centerLocal, circle.radius);

		OverlapSolution actual = new OverlapSolution();
		distance.overlap(circleLocal, transform, polyDLocal, transform, actual);

		OverlapSolution expected = new OverlapSolution();

		expected.distanceDepth = -1;
		expected.normal.set(0,-1);

		float d = MathUtils.abs(expected.distanceDepth - actual.distanceDepth);
		System.out.println(d);
		if(d > SystemCostants.EPSILON)
			fail("" +d);
		//assertEquals(expected.distanceDepth, actual.distanceDepth);

		//assertEquals(expected.normal, actual.normal);
		d = it.gius.pePpe.MathUtils.manhattanDistance(expected.normal, actual.normal);
		System.out.println(d);
		if(d > SystemCostants.EPSILON)
			fail("" +d);


	}

	public void testDistanceCirclePoly()
	{

		Transform transformA = new Transform();
		transformA.setIdentity();

		transformA.position.set(0,2);

		Transform transformB = new Transform();
		transformB.setIdentity();

		DistanceSolution actual = new DistanceSolution();
		distance.distance(circle, transformA, polyD, transformB, actual);

		DistanceSolution expected = new DistanceSolution();

		expected.distance= 1;
		expected.p1.set(8,6);
		expected.p2.set(8,5);


		assertEquals(expected.distance, actual.distance);

		//assertEquals(expected.normal, actual.normal);
		assertVec2(expected.p1, actual.p1);
		assertVec2(expected.p2, actual.p2);

	}


	public void testOverlapCircleCircleTransform()
	{

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*org.jbox2d.common.MathUtils.PI* random.nextFloat();
		transform.set(new Vec2(f1,f2), angle);
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);

		float depthCircles = 2;
		Vec2 otherCenter = new Vec2();
		otherCenter.set(circle.centroid);
		otherCenter.addLocal(new Vec2(2*circle.radius-depthCircles, 0));
		//Circle otherCircle = new Circle(otherCenter, circle.radius);
		Vec2 otherCenterLocal = new Vec2();
		Transform.mulTransToOut(transform, otherCenter, otherCenterLocal);
		Circle otherCircleLocal = new Circle(otherCenterLocal, circle.radius);

		Vec2 centerLocal = new Vec2();
		Transform.mulTransToOut(transform, circle.centroid, centerLocal);
		Circle circleLocal = new Circle(centerLocal, circle.radius);

		OverlapSolution actual = new OverlapSolution();
		distance.overlap(circleLocal, transform, otherCircleLocal, transform, actual);

		OverlapSolution expected = new OverlapSolution();

		expected.distanceDepth = -depthCircles;
		expected.normal.set(1,0);

		float d = MathUtils.abs(expected.distanceDepth - actual.distanceDepth);
		System.out.println(d);
		if(d > SystemCostants.EPSILON)
			fail("" +d);
		//assertEquals(expected.distanceDepth, actual.distanceDepth);

		//assertEquals(expected.normal, actual.normal);
		d = it.gius.pePpe.MathUtils.manhattanDistance(expected.normal, actual.normal);
		System.out.println(d);
		if(d > SystemCostants.EPSILON)
			fail("" +d);


	}


	public void testDistancePolyPoint()
	{

		Transform transform = new Transform();
		transform.setIdentity();

		DistanceSolution actual = new DistanceSolution();
		float distancePP = 2;
		distance.distance(polyC, transform, new Vec2(9+distancePP,7), actual);

		DistanceSolution expected = new DistanceSolution();

		expected.distance = distancePP;
		expected.p1.set(9,7);
		expected.p2.set(9+distancePP,7);
		expected.penetration = false;

		assertEquals(actual.distance, expected.distance);
		assertEquals(expected.penetration, actual.penetration);

		assertVec2(expected.p1, actual.p1);
		assertVec2(expected.p2, actual.p2);
	}


	public void testCachePolyPoly1()
	{

		Transform transformA = new Transform();
		transformA.setIdentity();

		Transform transformB = new Transform();
		transformB.setIdentity();
		
		transformB.position.set(20,20);

		DistanceSolution solution = new DistanceSolution();
		DistanceWitnessCache actual = new DistanceWitnessCache();
		distance.distance(polyD, transformA, polyD, transformB, actual, solution);

		DistanceWitnessCache expected = new DistanceWitnessCache();
		expected.witness.firstWitness.index = 2; //up rx 2
		expected.witness.secondWitness.index = 0; //low lx 0
		
		assertEquals(expected, actual);
		
	}

	public void testCachePolyPoly2()
	{

		Transform transformA = new Transform();
		transformA.setIdentity();

		transformA.position.set(20,20);
		
		Transform transformB = new Transform();
		transformB.setIdentity();
		

		DistanceSolution solution = new DistanceSolution();
		DistanceWitnessCache actual = new DistanceWitnessCache();
		distance.distance(polyD, transformA, polyD, transformB, actual, solution);

		DistanceWitnessCache expected = new DistanceWitnessCache();
		expected.witness.firstWitness.index = 0; //low lx 0
		expected.witness.secondWitness.index = 2; //up rx 2
		
		assertEquals(expected, actual);
		
	}
}
