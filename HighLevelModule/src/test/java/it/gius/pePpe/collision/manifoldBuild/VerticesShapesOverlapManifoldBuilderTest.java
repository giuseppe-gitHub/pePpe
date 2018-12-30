package it.gius.pePpe.collision.manifoldBuild;

import java.util.Random;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.collision.manifoldBuild.VerticesShapesOverlapManifoldBuilder;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.ShapeUtils;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactPointID;
import it.gius.pePpe.manifold.ContactManifold.ContactType;
import junit.framework.TestCase;

/*
 * Tests data from:
 * http://www.codezealot.org/archives/394
 */
public class VerticesShapesOverlapManifoldBuilderTest extends TestCase {

	private ContactManifold sol1,sol2, sol3;
	private Polygon polyA,polyB;
	
	private Polygon polyC,polyD;
	private Polygon polyCWinding,polyDWinding;
	private Edge edgeC, edgeD;
	
	private Polygon polyE,polyF;
	
	private ManifoldTestUtils manifoldTestUtils = new ManifoldTestUtils();

	public VerticesShapesOverlapManifoldBuilderTest() {
		polyA = new Polygon();
		polyA.addVertex(new Vec2(8,4));
		//polyA.addVertex(new Vec2(14,4.2f));
		polyA.addVertex(new Vec2(14,4));
		polyA.addVertex(new Vec2(14,9));
		polyA.addVertex(new Vec2(8,9));
		polyA.endPolygon();

		polyB = new Polygon();
		polyB.addVertex(new Vec2(4,2));
		polyB.addVertex(new Vec2(12,2));
		polyB.addVertex(new Vec2(12,5));
		polyB.addVertex(new Vec2(4,5));
		polyB.endPolygon();
		
		polyC = new Polygon();
		polyC.addVertex(new Vec2(2,8));
		polyC.addVertex(new Vec2(6,4));
		polyC.addVertex(new Vec2(9,7));
		polyC.addVertex(new Vec2(5,11));
		polyC.endPolygon();
		
		polyCWinding = new Polygon();
		polyCWinding.addVertex(new Vec2(5,11));
		polyCWinding.addVertex(new Vec2(9,7));
		polyCWinding.addVertex(new Vec2(6,4));
		polyCWinding.addVertex(new Vec2(2,8));
		polyCWinding.endPolygon();
		
		
		polyD = new Polygon();
		polyD.addVertex(new Vec2(4,2));
		polyD.addVertex(new Vec2(12,2));
		polyD.addVertex(new Vec2(12,5));
		polyD.addVertex(new Vec2(4,5));
		polyD.endPolygon();
		
		polyDWinding = new Polygon();
		polyDWinding.addVertex(new Vec2(4,5));
		polyDWinding.addVertex(new Vec2(12,5));
		polyDWinding.addVertex(new Vec2(12,2));
		polyDWinding.addVertex(new Vec2(4,2));
		polyDWinding.endPolygon();
		
		edgeC = new Edge(new Vec2(2,8), new Vec2(6,4));
		edgeD = new Edge(new Vec2(12,5), new Vec2(4,5));
		
		polyE = new Polygon();
		polyE.addVertex(new Vec2(13,3));
		polyE.addVertex(new Vec2(14,7));
		polyE.addVertex(new Vec2(10,8));
		polyE.addVertex(new Vec2(9,4));
		polyE.endPolygon();
		
		polyF = new Polygon();
		polyF.addVertex(new Vec2(4,2));
		polyF.addVertex(new Vec2(12,2));
		polyF.addVertex(new Vec2(12,5));
		polyF.addVertex(new Vec2(4,5));
		polyF.endPolygon();

		sol1 = new ContactManifold();
		sol1.type = ContactType.POLYPOLY;
		sol1.size = 2;
		sol1.points[0].localPoint.set(12,5);
		sol1.points[0].otherLocalPoint.set(12,4);
		sol1.points[0].distance = -1;
		sol1.points[0].normalGlobal.set(0,1);
		sol1.points[0].pointOnShapeB = true;
		/*sol1.points[0].pointID.vertexIndex = 2;
		sol1.points[0].pointID.edgeIndex = 0;
		sol1.points[0].pointID.vertexOnShapeA = false;*/
		sol1.points[0].pointID.featureA = 0;
		sol1.points[0].pointID.featureB = 2;
		sol1.points[0].pointID.vertexB = true;
		sol1.points[0].pointID.vertexA = false;

		sol1.points[1].localPoint.set(8,5);
		sol1.points[1].otherLocalPoint.set(8,4);
		sol1.points[1].distance = -1;
		sol1.points[1].normalGlobal.set(0,1);
		sol1.points[1].pointOnShapeB = true;
		/*sol1.points[1].pointID.vertexIndex = 0;
		sol1.points[1].pointID.edgeIndex = 2;
		sol1.points[1].pointID.vertexOnShapeA = true;*/
		sol1.points[1].pointID.featureA = 0;
		sol1.points[1].pointID.featureB = 2;
		sol1.points[1].pointID.vertexA = true;
		sol1.points[1].pointID.vertexB = false;


		sol2 = new ContactManifold();
		sol2.type = ContactType.POLYPOLY;
		sol2.size = 2;
		sol2.points[0].localPoint.set(8,4);
		sol2.points[0].otherLocalPoint.set(8,5);
		sol2.points[0].distance = -1;
		sol2.points[0].normalGlobal.set(0,-1);
		sol2.points[0].pointOnShapeB = false;
		/*sol2.points[0].pointID.vertexIndex = 0;
		sol2.points[0].pointID.edgeIndex = 2;
		sol2.points[0].pointID.vertexOnShapeA = true;*/
		sol2.points[0].pointID.featureA = 0;
		sol2.points[0].pointID.featureB = 2;
		sol2.points[0].pointID.vertexA = true;
		sol2.points[0].pointID.vertexB = false;

		sol2.points[1].localPoint.set(12,4);
		sol2.points[1].otherLocalPoint.set(12,5);
		sol2.points[1].distance = -1;
		sol2.points[1].normalGlobal.set(0,-1);
		sol2.points[1].pointOnShapeB = false;
		/*sol2.points[1].pointID.vertexIndex = 2;
		sol2.points[1].pointID.edgeIndex = 0;
		sol2.points[1].pointID.vertexOnShapeA = false;*/
		sol2.points[1].pointID.featureB = 2;
		sol2.points[1].pointID.featureA = 0;
		sol2.points[1].pointID.vertexA = false;
		sol2.points[1].pointID.vertexB = true;
		
		sol3 = new ContactManifold();
		sol3.type = ContactType.POLYPOLY;
		sol3.size = 1;
		sol3.points[0].localPoint.set(6,4);
		sol3.points[0].otherLocalPoint.set(6,5);
		sol3.points[0].distance = -1;
		sol3.points[0].normalGlobal.set(0,-1);
		sol3.points[0].pointOnShapeB = false;
		/*sol3.points[0].pointID.vertexIndex = 1;
		sol3.points[0].pointID.edgeIndex = 2;
		sol3.points[0].pointID.vertexOnShapeA = true;*/
		sol3.points[0].pointID.featureA = 1;
		sol3.points[0].pointID.featureB = 2;
		sol3.points[0].pointID.vertexA = true;
		sol3.points[0].pointID.vertexB = false;

	}
	

	
	public void testClip1() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();


		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(polyA, transform, polyB, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		System.out.println(manifold.points[0].normalGlobal);
		System.out.println(manifold.points[0].otherLocalPoint);
		System.out.println(manifold.points[1].localPoint);
		System.out.println(manifold.points[1].pointID);
		System.out.println(manifold.points[1].normalGlobal);
		System.out.println(manifold.points[1].otherLocalPoint);


		if(!manifold.equals(sol1) && !manifold.equals(sol2))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0],transform,transform))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[1],transform,transform))
			fail();


	}

	/*public void testClip2()
	{
		for(int i=0; i<10; i++)
			privateTestClip2();
	}*/

	public void testClip2() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		/*float f1 = 1.6225278f;
		float f2 = 3.6338496f;
		float angle = 2.410357f;*/
		transform.set(new Vec2(f1,f2), angle/*MathUtils.PI/4*/);

		Polygon transA = new Polygon();
		ShapeUtils shapeUtils = new ShapeUtils();
		shapeUtils.mulTransToOutPolygon(polyA, transform, transA);
		transA.endPolygon();
		
		Polygon transB = new Polygon();
		shapeUtils.mulTransToOutPolygon(polyB, transform, transB);
		transB.endPolygon();

		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(transA, transform, transB, transform, 
				normalDepth, depth, manifold);

		Vec2 point = new Vec2();
		Transform.mulToOut(transform, manifold.points[0].localPoint, point);

		System.out.println(point);
		System.out.println(manifold.points[0].pointID);
		System.out.println(manifold.points[0].normalGlobal);

		Transform.mulToOut(transform, manifold.points[1].localPoint, point);
		System.out.println(point);
		System.out.println(manifold.points[1].pointID);
		System.out.println(manifold.points[1].normalGlobal);

		if(manifold.size != 2)
			fail("size: " + manifold.size);

				
		if(!manifoldTestUtils.checkPointIDsOnly(manifold, sol1) && !manifoldTestUtils.checkPointIDsOnly(manifold, sol2))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[1], transform, transform))
			fail();
	}

	
	
	
	public void testClip3() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(polyC, transform, polyD, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(6,4)))
				fail();


		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.x == sol3.points[0].normalGlobal.x && manifold.points[0].normalGlobal.y == sol3.points[0].normalGlobal.y))
			fail();
		
		
		if(manifold.size != 1)
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		

	}
	
	public void testClip3Winding() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(polyC, transform, polyDWinding, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		System.out.println("manifold.points[0].pointOnShapeB: " + manifold.points[0].pointOnShapeB);
		
		if(manifold.size != 1)
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		

	}
	
	public void testClipEdgeEdge1()
	{
		VerticesShapesOverlapManifoldBuilder clipEE = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipEE.buildManifold(edgeC, transform, edgeD, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(6,4)))
				fail();


		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.x == sol3.points[0].normalGlobal.x && manifold.points[0].normalGlobal.y == sol3.points[0].normalGlobal.y))
			fail();
		
		
		if(manifold.size != 1)
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
	}
	
	public void testClipEdgeEdge2()
	{
		VerticesShapesOverlapManifoldBuilder clipEE = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(1,1);
		normalDepth.normalize();
		float depth = normalDepth.x;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipEE.buildManifold(edgeC, transform, edgeD, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(4,5)))
				fail();


		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.equals(normalDepth.negate())))
			fail();
		
		
		if(manifold.size != 1)
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
	}

	

	public void testClipEdgePoly()
	{
		VerticesShapesOverlapManifoldBuilder clipEE = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipEE.buildManifold(edgeC, transform, polyD, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(6,4)))
				fail();


		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.x == sol3.points[0].normalGlobal.x && manifold.points[0].normalGlobal.y == sol3.points[0].normalGlobal.y))
			fail();
		
		
		if(manifold.size != 1)
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
	}
	
	public void testClipPolyEdge()
	{
		VerticesShapesOverlapManifoldBuilder clipEE = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipEE.buildManifold(polyC, transform, edgeD, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(6,4)))
				fail();


		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.x == sol3.points[0].normalGlobal.x && manifold.points[0].normalGlobal.y == sol3.points[0].normalGlobal.y))
			fail();
		
		
		if(manifold.size != 1)
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
	}
	
	public void testClipPolyEdgeReversed()
	{
		VerticesShapesOverlapManifoldBuilder clipEE = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;
		
		Edge edgeDReversed = new Edge(edgeD.point2, edgeD.point1);

		clipEE.buildManifold(polyC, transform, edgeDReversed, transform, 
				normalDepth, depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
	
		if(manifold.size != 1)
			fail();
		
		if(!manifold.points[0].localPoint.equals(new Vec2(6,4)))
				fail();


		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.x == sol3.points[0].normalGlobal.x && manifold.points[0].normalGlobal.y == sol3.points[0].normalGlobal.y))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
	}
	
	
	public void testClipThreshold() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(polyC, transform, polyD, transform, 
				normalDepth, depth,1.2f, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		

		if(manifold.size != 2)
			fail();
		
		if(!manifold.points[0].localPoint.equals(new Vec2(4,6)))
			fail();
		
		if(!manifold.points[1].localPoint.equals(new Vec2(6,4)))
				fail();

		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[1], transform, transform))
			fail();
		

	}
	
	public void testClipThresholdEdgEdge() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(edgeC, transform, edgeD, transform, 
				normalDepth, depth,1.2f, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		

		if(manifold.size != 2)
			fail();
		
		if(!manifold.points[0].localPoint.equals(new Vec2(4,6)))
			fail();
		
		if(!manifold.points[1].localPoint.equals(new Vec2(6,4)))
				fail();

		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[1], transform, transform))
			fail();
		

	}
	
	
	public void testClip4() {
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		/*float f1 = 1.6225278f;
		float f2 = 3.6338496f;
		float angle = 2.410357f;*/
		transform.set(new Vec2(f1,f2), angle/*MathUtils.PI/4*/);

		Polygon transA = new Polygon();
		ShapeUtils shapeUtils = new ShapeUtils();
		shapeUtils.mulTransToOutPolygon(polyC, transform, transA);
		transA.endPolygon();
		
		Polygon transB = new Polygon();
		shapeUtils.mulTransToOutPolygon(polyD, transform, transB);
		transB.endPolygon();

		Vec2 normalDepth = new Vec2(0,-1);
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(transA, transform, transB, transform, 
				normalDepth, depth, manifold);

		Vec2 point = new Vec2();
		Transform.mulToOut(transform, manifold.points[0].localPoint, point);

		System.out.println(point);
		System.out.println(manifold.points[0].pointID);
		System.out.println(manifold.points[0].normalGlobal);

		if(manifold.size != 1)
			fail("size: " + manifold.size);
		
		
		//assertEquals(manifold.points[0].normal,sol3.points[0].normal);
		if(!(manifold.points[0].normalGlobal.x == sol3.points[0].normalGlobal.x && manifold.points[0].normalGlobal.y == sol3.points[0].normalGlobal.y))
			fail();

		if(!manifoldTestUtils.checkPointIDsOnly(manifold, sol3))
			fail();
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();
		
	}


	public void testClip5()
	{
		VerticesShapesOverlapManifoldBuilder clipPP = new VerticesShapesOverlapManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		Vec2 normalDepth = new Vec2(-0.19f,-0.98f);
		float depth = 1.7f;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		clipPP.buildManifold(polyE, transform, polyF, transform, 
				normalDepth, depth, manifold);


		if(manifold.size != 2)
			fail();
		
		if(MathUtils.abs(manifold.points[0].distance)
				< MathUtils.abs(manifold.points[1].distance))
			fail();
		
		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		System.out.println(manifold.points[0].distance);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(12,5)))
				fail();

		System.out.println(manifold.points[1].localPoint);
		System.out.println(manifold.points[1].pointID);
		System.out.println(manifold.points[1].distance);
		
		ContactPointID pointID = new ContactPointID();
		/*pointID.edgeIndex = 2;
		pointID.vertexIndex = 3;
		pointID.vertexOnShapeA = true;*/
		pointID.featureA= 3;
		pointID.featureB = 2;
		pointID.vertexA = true;
		pointID.vertexB = false;
		
		if(!manifold.points[1].pointID.equals(pointID))
			fail();
		
		
		if(!manifoldTestUtils.checkOtherLocalPoint(manifold.points[0], transform, transform))
			fail();

	}
	
}

