package it.gius.pePpe.collision.manifoldBuild;

import java.util.Random;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.MathUtils;
import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.collision.manifoldBuild.ShapeCircleManifoldFiller;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.ShapeUtils;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactManifold.ContactType;
import junit.framework.TestCase;

public class ShapeCircleManifoldBuilderTest extends TestCase {

	private Polygon polyA,polyB;
	private Circle circleC,circleD, circleCOverlap;
	
	private ContactManifold manifoldSol1, manifoldSol2,manifoldSolEdge;
	
	private ManifoldTestUtils manifoldTestUtils = new ManifoldTestUtils();
	
	private ShapeCircleManifoldFiller builder = new ShapeCircleManifoldFiller();
	
	public ShapeCircleManifoldBuilderTest() {
		polyA = new Polygon();
		polyA.addVertex(new Vec2(13,3));
		polyA.addVertex(new Vec2(14,7));
		polyA.addVertex(new Vec2(10,8));
		polyA.addVertex(new Vec2(9,4));
		polyA.endPolygon();
		
		polyB = new Polygon();
		polyB.addVertex(new Vec2(-10,-10));
		polyB.addVertex(new Vec2(-10,10));
		polyB.addVertex(new Vec2(10,10));
		polyB.addVertex(new Vec2(10,-10));
		polyB.endPolygon();
		
		circleC = new Circle(new Vec2(5,4), 5);
		circleCOverlap = new Circle(new Vec2(14,4), 5); //overlap of 1 with circeC
		circleD = new Circle(new Vec2(1,14), 5);
		
		manifoldSol1 = new ContactManifold();
		manifoldSol1.size = 1;
		manifoldSol1.type = ContactType.POLYCIRCLE;
		manifoldSol1.points[0].localPoint.set(10,4);
		manifoldSol1.points[0].otherLocalPoint.set(9,4);
		manifoldSol1.points[0].distance = -1;
		manifoldSol1.points[0].pointOnShapeB = true;
		manifoldSol1.points[0].normalGlobal.set(1,0);
		manifoldSol1.points[0].pointID.featureB = 0;
		manifoldSol1.points[0].pointID.vertexB = true;
		manifoldSol1.points[0].pointID.featureA = 3;
		manifoldSol1.points[0].pointID.vertexA = true;
		
		
		manifoldSol2 = new ContactManifold();
		manifoldSol2.size = 1;
		manifoldSol2.type = ContactType.POLYCIRCLE;
		manifoldSol2.points[0].localPoint.set(10,4);
		manifoldSol2.points[0].otherLocalPoint.set(9,4);
		manifoldSol2.points[0].distance = -1;
		manifoldSol2.points[0].pointOnShapeB = false;
		manifoldSol2.points[0].normalGlobal.set(1,0);
		manifoldSol2.points[0].pointID.featureB = 3;
		manifoldSol2.points[0].pointID.vertexB = true;
		manifoldSol2.points[0].pointID.featureA = 0;
		manifoldSol2.points[0].pointID.vertexA = true;
		
		
		manifoldSolEdge = new ContactManifold();
		manifoldSolEdge.size = 1;
		manifoldSolEdge.type = ContactType.POLYCIRCLE;
		manifoldSolEdge.points[0].localPoint.set(1,9);
		manifoldSolEdge.points[0].otherLocalPoint.set(1,10);
		manifoldSolEdge.points[0].distance = -1;
		manifoldSolEdge.points[0].pointOnShapeB = true;
		manifoldSolEdge.points[0].normalGlobal.set(0,-1);
		manifoldSolEdge.points[0].pointID.featureB = 0;
		manifoldSolEdge.points[0].pointID.vertexB = true;
		manifoldSolEdge.points[0].pointID.featureA = 1;
		manifoldSolEdge.points[0].pointID.vertexA = false;
		
	}
	
		
	public void testBuildManifold1() {
		
		
		Vec2 normal = new Vec2(-1,0);
		float depth = 1;
		
		Transform transform = new Transform();
		transform.setIdentity();


		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYCIRCLE;

		builder.fillManifold(polyA, transform, circleC, transform, false, 
				normal, depth, manifold);


		System.out.println("localPoint: " + manifold.points[0].localPoint);
		System.out.println("otherLocalPoint: " + manifold.points[0].otherLocalPoint);
		System.out.println("normal: " + manifold.points[0].normalGlobal);
		System.out.println("distance: " + manifold.points[0].distance);
		System.out.println("pointOnShapeB: " + manifold.points[0].pointOnShapeB);
		
		/*if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
			fail();
		
		if(!manifold.points[0].otherLocalPoint.equals(new Vec2(9,4)))
			fail();**/
		
		assertEquals(manifold, manifoldSol1);

		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();

	}
	
	public void testBuildManifoldFlipped() {
		
		Vec2 normal = new Vec2(1,0);
		float depth = 1;
		
		Transform transform = new Transform();
		transform.setIdentity();

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYCIRCLE;

		builder.fillManifold(polyA, transform, circleC, transform, true, 
				normal, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].otherLocalPoint);
		System.out.println(manifold.points[0].normalGlobal);
		System.out.println(manifold.points[0].distance);
		System.out.println(manifold.points[0].pointOnShapeB);
		
		/*if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
			fail();
		
		if(!manifold.points[0].otherLocalPoint.equals(new Vec2(9,4)))
			fail();*/
		
		assertEquals(manifold, manifoldSol2);
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
	}
	
	
	public void testBuildManifoldTransform() {
		
		Vec2 normal = new Vec2(1,0);
		float depth = 1;
		
		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*org.jbox2d.common.MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		/*float f1 = 1.6225278f;
		float f2 = 3.6338496f;
		float angle = 2.410357f;*/
		transform.set(new Vec2(f1,f2), angle/*MathUtils.PI/4*/);

		
		ShapeUtils shapeUtils = new ShapeUtils();
		Polygon polyLocal = new Polygon();
		shapeUtils.mulTransToOutPolygon(polyA, transform, polyLocal);
		polyLocal.endPolygon();
		
		Vec2 center = new Vec2();
		//center.x = circleC.centroid.x - transform.position.x;
		//center.y = circleC.centroid.y - transform.position.y;
		Transform.mulTransToOut(transform, circleC.centroid, center);
		Circle circleLocal = new Circle(center, circleC.radius);

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYCIRCLE;

		builder.fillManifold(polyLocal, transform, circleLocal, transform, true, 
				normal, depth, manifold);


		Vec2 globalPoint = new Vec2();
		Transform.mulToOut(transform, manifold.points[0].localPoint, globalPoint);
		System.out.println(globalPoint);
		Vec2 otherGlobalPoint = new Vec2();
		Transform.mulToOut(transform, manifold.points[0].otherLocalPoint, otherGlobalPoint);
		System.out.println(otherGlobalPoint);
		System.out.println(manifold.points[0].normalGlobal);
		System.out.println(manifold.points[0].distance);
		System.out.println(manifold.points[0].pointOnShapeB);
		
		//if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
		float distance = MathUtils.manhattanDistance(globalPoint, new Vec2(10,4)); 
		if( distance > SystemCostants.EPSILON)
			fail("" +distance);
			
		distance = MathUtils.manhattanDistance(otherGlobalPoint, new Vec2(9,4));
		if( distance > SystemCostants.EPSILON)
			fail("" + distance);
		
		if(!manifoldTestUtils.checkPointIDsOnly(manifold, manifoldSol2))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
	}
	
	
	
	public void testBuildManifoldEdge() {
		
		Vec2 normal = new Vec2(0,1);
		float depth = 1;
		
		Transform transform = new Transform();
		transform.setIdentity();


		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYCIRCLE;

		builder.fillManifold(polyB, transform, circleD, transform, false, 
				normal, depth, manifold);


		System.out.println("localPoint: " + manifold.points[0].localPoint);
		System.out.println("otherLocalPoint: " + manifold.points[0].otherLocalPoint);
		System.out.println("normal: " + manifold.points[0].normalGlobal);
		System.out.println("distance: " + manifold.points[0].distance);
		System.out.println("pointOnShapeB: " + manifold.points[0].pointOnShapeB);
		
		/*if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
			fail();
		
		if(!manifold.points[0].otherLocalPoint.equals(new Vec2(9,4)))
			fail();**/
		
		assertEquals(manifold, manifoldSolEdge);

		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();

	}

	public void testBuildManifoldTransformEdge() {
		
		Vec2 normal = new Vec2(0,1);
		float depth = 1;
		
		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*org.jbox2d.common.MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		/*float f1 = 1.6225278f;
		float f2 = 3.6338496f;
		float angle = 2.410357f;*/
		transform.set(new Vec2(f1,f2), angle/*MathUtils.PI/4*/);

		
		ShapeUtils shapeUtils = new ShapeUtils();
		Polygon polyLocal = new Polygon();
		shapeUtils.mulTransToOutPolygon(polyB, transform, polyLocal);
		polyLocal.endPolygon();
		
		Vec2 center = new Vec2();
		//center.x = circleD.centroid.x - transform.position.x;
		//center.y = circleD.centroid.y - transform.position.y;
		Transform.mulTransToOut(transform, circleD.centroid, center);
		Circle circleLocal = new Circle(center, circleD.radius);

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYCIRCLE;

		builder.fillManifold(polyLocal, transform, circleLocal, transform, false, 
				normal, depth, manifold);


		Vec2 globalPoint = new Vec2();
		Transform.mulToOut(transform, manifold.points[0].localPoint, globalPoint);
		System.out.println(globalPoint);
		Vec2 otherGlobalPoint = new Vec2();
		Transform.mulToOut(transform, manifold.points[0].otherLocalPoint, otherGlobalPoint);
		System.out.println(otherGlobalPoint);
		System.out.println(manifold.points[0].normalGlobal);
		System.out.println(manifold.points[0].distance);
		System.out.println(manifold.points[0].pointOnShapeB);
		
		//if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
		float distance = MathUtils.manhattanDistance(globalPoint, manifoldSolEdge.points[0].localPoint); 
		if( distance > SystemCostants.SQRT_EPSILON)
			fail("" +distance);
			
		distance = MathUtils.manhattanDistance(otherGlobalPoint, manifoldSolEdge.points[0].otherLocalPoint);
		if( distance > SystemCostants.SQRT_EPSILON)
			fail("" + distance);
		
		if(!manifoldTestUtils.checkPointIDsOnly(manifold, manifoldSolEdge))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
	}
	
	
	public void testBuildManifoldCircleCircle()
	{
		Vec2 normal = new Vec2(-1,0);
		float depth = 1;
		
		Transform transform = new Transform();
		transform.setIdentity();


		ContactManifold actual = new ContactManifold();
		actual.type = ContactType.CIRCLECIRCLE;

		builder.fillManifold(circleCOverlap, transform, circleC, transform, false, 
				normal, depth, actual);


		System.out.println("localPoint: " + actual.points[0].localPoint);
		System.out.println("otherLocalPoint: " + actual.points[0].otherLocalPoint);
		System.out.println("normal: " + actual.points[0].normalGlobal);
		System.out.println("distance: " + actual.points[0].distance);
		System.out.println("pointOnShapeB: " + actual.points[0].pointOnShapeB);
		
		/*if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
			fail();
		
		if(!manifold.points[0].otherLocalPoint.equals(new Vec2(9,4)))
			fail();**/
		ContactManifold expected = new ContactManifold();
		expected.type = ContactType.CIRCLECIRCLE;
		expected.size = 1;
		expected.points[0].localPoint.set(10,4);
		expected.points[0].otherLocalPoint.set(9,4);
		expected.points[0].distance = -1;
		expected.points[0].normalGlobal.set(1,0);
		expected.points[0].pointID.featureA = 0;
		expected.points[0].pointID.vertexA = true;
		expected.points[0].pointID.featureB = 0;
		expected.points[0].pointID.vertexB = true;
		expected.points[0].pointOnShapeB = true;
		
		assertEquals(expected, actual);

		if(!manifoldTestUtils.checkOtherLocalPoint(actual.points[0], transform, transform))
			fail();
	}
	
	public void testBuildManifoldCircleCircleTransform()
	{
		Vec2 normal = new Vec2(-1,0);
		float depth = 1;
		
		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*org.jbox2d.common.MathUtils.PI* random.nextFloat();
		transform.set(new Vec2(f1,f2), angle);
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);


		ContactManifold actual = new ContactManifold();
		actual.type = ContactType.CIRCLECIRCLE;
		
		Vec2 centerC = new Vec2();
		Transform.mulTransToOut(transform, circleC.centroid, centerC);
		Circle circleCLocal = new Circle(centerC, circleC.radius);
		
		Vec2 centerCOverlap = new Vec2();
		Transform.mulTransToOut(transform, circleCOverlap.centroid, centerCOverlap);
		Circle circleCOverlapLocal = new Circle(centerCOverlap, circleCOverlap.radius);

		builder.fillManifold(circleCOverlapLocal, transform, circleCLocal, transform, false, 
				normal, depth, actual);

		/*if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
			fail();
		
		if(!manifold.points[0].otherLocalPoint.equals(new Vec2(9,4)))
			fail();**/
		ContactManifold expected = new ContactManifold();
		expected.type = ContactType.CIRCLECIRCLE;
		expected.size = 1;
		expected.points[0].localPoint.set(10,4);
		expected.points[0].otherLocalPoint.set(9,4);
		expected.points[0].distance = -1;
		expected.points[0].normalGlobal.set(1,0);
		expected.points[0].pointID.featureA = 0;
		expected.points[0].pointID.vertexA = true;
		expected.points[0].pointID.featureB = 0;
		expected.points[0].pointID.vertexB = true;
		expected.points[0].pointOnShapeB = true;
		
		Vec2 globalPoint = new Vec2();
		Transform.mulToOut(transform, actual.points[0].localPoint, globalPoint);
		System.out.println(globalPoint);
		Vec2 otherGlobalPoint = new Vec2();
		Transform.mulToOut(transform, actual.points[0].otherLocalPoint, otherGlobalPoint);
		System.out.println(otherGlobalPoint);
		System.out.println(actual.points[0].normalGlobal);
		System.out.println(actual.points[0].distance);
		System.out.println(actual.points[0].pointOnShapeB);
		
		//if(!manifold.points[0].localPoint.equals(new Vec2(10,4)))
		float distance = MathUtils.manhattanDistance(globalPoint, expected.points[0].localPoint); 
		if( distance > 10f*SystemCostants.EPSILON)
			fail("" +distance);
			
		distance = MathUtils.manhattanDistance(otherGlobalPoint, expected.points[0].otherLocalPoint);
		if( distance > 10f*SystemCostants.EPSILON)
			fail("" + distance);
		
		if(!manifoldTestUtils.checkPointIDsOnly(actual, expected))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(actual.points[0], transform, transform))
			fail();
	}
	
}
