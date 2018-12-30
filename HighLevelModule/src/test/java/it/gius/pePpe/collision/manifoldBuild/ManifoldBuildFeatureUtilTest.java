package it.gius.pePpe.collision.manifoldBuild;

import java.util.Random;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.collision.manifoldBuild.ManifoldBuildFeatureUtil;
import it.gius.pePpe.collision.manifoldBuild.PointIDFeature;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipPoint;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipEdge;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.manifold.ContactPoint;
import junit.framework.TestCase;

public class ManifoldBuildFeatureUtilTest extends TestCase {

	private Polygon polygonA,polygonB;
	
	private ManifoldTestUtils testUtil = new ManifoldTestUtils();


	public ManifoldBuildFeatureUtilTest() {
		polygonA = new Polygon();
		polygonB = new Polygon();

		//polygonA.setAsBox(-10, -10, 10, 10);
		polygonA.addVertex(new Vec2(-10,-10));
		polygonA.addVertex(new Vec2(-10,10));
		polygonA.addVertex(new Vec2(10,10));
		polygonA.addVertex(new Vec2(10,-10));
		polygonA.endPolygon();

		polygonB.addVertex(new Vec2(10,10));
		polygonB.addVertex(new Vec2(20,30));
		polygonB.addVertex(new Vec2(50,30));
		polygonB.addVertex(new Vec2(40,15));
		polygonB.endPolygon();


	}

	public void testBuilFeature1() {
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();
		expected.feature = 2;
		expected.vertex = true;

		util.buildFeature(polygonA, new Vec2(10,10), actual);

		assertEquals(expected, actual);
	}


	public void testBuilFeature2() {
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();
		expected.feature = 1;
		expected.vertex = false;

		util.buildFeature(polygonA, new Vec2(0,10), actual);

		assertEquals(expected, actual);
	}

	public void testBuilFeature3() {
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();
		expected.feature = 1;
		expected.vertex = false;

		util.buildFeature(polygonA, new Vec2(9.9f,10), actual);

		assertEquals(expected, actual);
	}

	public void testBuilFeature4() {
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();
		expected.feature = 0;
		expected.vertex = false;

		Vec2 point = new Vec2(15,20);

		util.buildFeature(polygonB, point, actual);

		assertEquals(expected, actual);
	}


	public void testBuilFeature5()
	{
		for(int i=0; i<10; i++)
			privateTestBuilFeature5();
	}

	private void privateTestBuilFeature5() {
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();
		Random random = new Random();

		expected.feature = random.nextInt(polygonB.getDim());
		System.out.println("Edge: " + expected.feature);
		expected.vertex = false;

		int nextFeature = expected.feature+1 != polygonB.getDim() ? expected.feature +1 : 0;

		float multiplier = random.nextFloat() +0.1f;
		if(multiplier > 0.9f)
			multiplier = 0.9f;


		System.out.println("multiplier: " + multiplier);

		Vec2 point = new Vec2();
		//polygonB.getVertex(expected.feature).mul(multiplier).add(polygonB.getVertex(nextFeature).mul(multiplier));
		Vec2 vertex1 = polygonB.getVertex(expected.feature);
		Vec2 vertex2 = polygonB.getVertex(nextFeature);
		point.x = vertex1.x * multiplier + vertex2.x * (1.0f - multiplier);
		point.y = vertex1.y * multiplier + vertex2.y * (1.0f - multiplier);

		util.buildFeature(polygonB, point, actual);

		assertEquals(expected, actual);
	}

	public void testBuildFeatureEdge1()
	{
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();

		expected.feature = 0;
		expected.vertex = true;

		Vec2 point = new Vec2(15,20);

		Edge edge = new Edge(point, new Vec2(35,20));

		util.buildFeature(edge, point, actual);

		assertEquals(expected, actual);
	}

	public void testBuildFeatureEdge2()
	{
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();

		expected.feature = 1;
		expected.vertex = true;

		Vec2 point1 = new Vec2(15,20);
		Vec2 point2 = new Vec2(35,20);

		Edge edge = new Edge(point1, point2);

		util.buildFeature(edge, point2, actual);

		assertEquals(expected, actual);
	}

	public void testBuildFeatureEdge3()
	{
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();
		PointIDFeature actual = new PointIDFeature();
		PointIDFeature expected = new PointIDFeature();

		expected.feature = 0;
		expected.vertex = false;

		Vec2 point1 = new Vec2(15,20);
		Vec2 point2 = new Vec2(35,20);

		Random random = new Random();
		float alpha = random.nextFloat()+ 0.1f;

		if(alpha > 0.9f)
			alpha = 0.9f;

		System.out.println("alpha:" + alpha);

		Vec2 pointInt = new Vec2();
		pointInt.x = alpha*point1.x + (1-alpha)*point2.x;
		pointInt.y = alpha*point1.y + (1-alpha)*point2.y;

		Edge edge = new Edge(point1, point2);

		util.buildFeature(edge, pointInt, actual);

		assertEquals(expected, actual);
	}

	//test data from VerticesShapesManifoldBuilderTest.testClip3

	public void testFromClipPointToContactPoint1()
	{
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();

		ClipPoint clipPointIn = new ClipPoint();
		clipPointIn.depth = 1f;
		clipPointIn.point.set(6,4); //globalPoint
		clipPointIn.index = 1;
		clipPointIn.refPointIndex = -1;

		Vec2 normalDepth = new Vec2(0,-1);
		Vec2 normalNegated = normalDepth.negate();

		Vec2 localPoint = new Vec2(6,4);
		Vec2 otherLocalPoint = new Vec2(6,5);

		boolean refEdgeOnB = true;

		Transform transform = new Transform();
		transform.setIdentity();

		Transform incTransform = transform;
		Transform refTransform = transform;

		ClipEdge incEdge = new ClipEdge(new Vec2(2,8),new Vec2(6,4), new Vec2(6,4),0);
		ClipEdge refEdge = new ClipEdge(new Vec2(12,5), new Vec2(4,5), new Vec2(12,5),2);

		ContactPoint contactPointOut = new ContactPoint();


		util.fromClipPointToContactPoint(clipPointIn, normalDepth, normalNegated, localPoint,
				otherLocalPoint, refEdgeOnB, incTransform, refTransform, incEdge, refEdge, contactPointOut);

		ContactPoint expected = new ContactPoint();
		expected.distance = -1;
		expected.localPoint.set(6,4);
		expected.otherLocalPoint.set(6,5);
		expected.normalGlobal.set(0,-1);
		expected.pointOnShapeB = false;
		expected.pointID.featureA = 1;
		expected.pointID.vertexA = true;
		expected.pointID.featureB = 2;
		expected.pointID.vertexB = false;

		assertEquals(expected, contactPointOut);
		
		if(!testUtil.checkOtherLocalPoint(contactPointOut, transform, transform))
			fail();
		
		
	}

	public void testFromClipPointToContactPointTransform()
	{
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();

		ClipPoint clipPointIn = new ClipPoint();
		clipPointIn.depth = 1f;
		clipPointIn.point.set(6,4); //globalPoint
		clipPointIn.index = 1;
		clipPointIn.refPointIndex = -1;

		Vec2 normalDepth = new Vec2(0,-1);
		Vec2 normalNegated = normalDepth.negate();

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		transform.set(new Vec2(f1,f2), angle);
		
		Vec2 localPoint = new Vec2(6,4);
		Vec2 otherLocalPoint = new Vec2(6,5);

		boolean refEdgeOnB = true;

		Transform.mulTransToOut(transform, localPoint, localPoint);
		Transform.mulTransToOut(transform, otherLocalPoint, otherLocalPoint);

		Transform incTransform = transform;
		Transform refTransform = transform;
		

		//global points on edges
		ClipEdge incEdge = new ClipEdge(new Vec2(2,8),new Vec2(6,4), new Vec2(6,4),0);
		ClipEdge refEdge = new ClipEdge(new Vec2(12,5), new Vec2(4,5), new Vec2(12,5),2);

		ContactPoint contactPointOut = new ContactPoint();


		util.fromClipPointToContactPoint(clipPointIn, normalDepth, normalNegated, localPoint,
				otherLocalPoint, refEdgeOnB, incTransform, refTransform, incEdge, refEdge, contactPointOut);

		ContactPoint expected = new ContactPoint();
		expected.distance = -1;
		expected.localPoint.set(6,4);
		Transform.mulTransToOut(transform, expected.localPoint, expected.localPoint);
		expected.otherLocalPoint.set(6,5);
		Transform.mulTransToOut(transform, expected.otherLocalPoint, expected.otherLocalPoint);
		expected.normalGlobal.set(0,-1);
		expected.pointOnShapeB = false;
		expected.pointID.featureA = 1;
		expected.pointID.vertexA = true;
		expected.pointID.featureB = 2;
		expected.pointID.vertexB = false;

		assertEquals(expected, contactPointOut);
		
		if(!testUtil.checkOtherLocalPoint(contactPointOut, transform, transform))
			fail();
	}

	public void testFromClipPointToContactPoint2()
	{
		ManifoldBuildFeatureUtil util = new ManifoldBuildFeatureUtil();

		ClipPoint clipPointIn = new ClipPoint();
		clipPointIn.depth = -1f;
		clipPointIn.point.set(4,6); //globalPoint
		clipPointIn.index = -1;
		clipPointIn.refPointIndex = 3;

		Vec2 normalDepth = new Vec2(0,-1);
		Vec2 normalNegated = normalDepth.negate();

		Vec2 localPoint = new Vec2(4,6);
		Vec2 otherLocalPoint = new Vec2(4,5);

		boolean refEdgeOnB = true;

		Transform transform = new Transform();
		transform.setIdentity();

		Transform incTransform = transform;
		Transform refTransform = transform;

		ClipEdge incEdge = new ClipEdge(new Vec2(2,8),new Vec2(6,4), new Vec2(6,4),0);
		ClipEdge refEdge = new ClipEdge(new Vec2(12,5), new Vec2(4,5), new Vec2(12,5),2);

		ContactPoint contactPointOut = new ContactPoint();


		util.fromClipPointToContactPoint(clipPointIn, normalDepth, normalNegated, localPoint,
				otherLocalPoint, refEdgeOnB, incTransform, refTransform, incEdge, refEdge, contactPointOut);

		ContactPoint expected = new ContactPoint();
		expected.distance = 1;
		expected.localPoint.set(4,6);
		expected.otherLocalPoint.set(4,5);
		expected.normalGlobal.set(0,-1);
		expected.pointOnShapeB = false;
		expected.pointID.featureA = 0;
		expected.pointID.vertexA = false;
		expected.pointID.featureB = 3;
		expected.pointID.vertexB = true;

		assertEquals(expected, contactPointOut);
		
		if(!testUtil.checkOtherLocalPoint(contactPointOut, transform, transform))
			fail();
	}

}
