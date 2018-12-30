package it.gius.pePpe.collision.manifoldBuild;

import java.util.Random;

import it.gius.pePpe.data.cache.SATCache;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.ShapeUtils;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactManifold.ContactType;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import junit.framework.TestCase;

/*
 * Tests data from:
 * http://www.codezealot.org/archives/394
 */
public class VerticesShapeSATManifoldBuilderTest extends TestCase {

	private ContactManifold sol1,sol2, sol3;
	private Polygon polyA,polyB;
	private Polygon polyC,polyD;
	private Edge edgeC, edgeD;
	private ManifoldTestUtils manifoldTestUtils = new ManifoldTestUtils();


	public VerticesShapeSATManifoldBuilderTest() {
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
		
		
		polyD = new Polygon();
		polyD.addVertex(new Vec2(4,2));
		polyD.addVertex(new Vec2(12,2));
		polyD.addVertex(new Vec2(12,5));
		polyD.addVertex(new Vec2(4,5));
		polyD.endPolygon();
		
		edgeC = new Edge(new Vec2(2,8), new Vec2(6,4));
		edgeD = new Edge(new Vec2(12,5), new Vec2(4,5));
		
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

	public void testSATBuild1() {
		VerticesShapeSATManifoldBuilder satBuilder = new VerticesShapeSATManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();


		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		float threshold = 0.5f;
		satBuilder.buildManifold(polyA, transform, polyB, transform, 
				new SATCache(), threshold, manifold);


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

	public void testSATBuild2() {
		VerticesShapeSATManifoldBuilder satBuilder = new VerticesShapeSATManifoldBuilder();

		Transform transform = new Transform();
		Random random = new Random(System.currentTimeMillis());
		float f1 = random.nextFloat()*10;
		float f2 = random.nextFloat()*10;
		float angle = 2*MathUtils.PI* random.nextFloat();
		System.out.println("f1: " + f1 +",f2: " + f2 + ", angle: " + angle);
		//f1 = 6.590192f; f2 = 5.534292f; angle= 0.68537897f;
		transform.set(new Vec2(f1,f2), angle/*MathUtils.PI/4*/);

		Polygon transA = new Polygon();
		ShapeUtils shapeUtils = new ShapeUtils();
		shapeUtils.mulTransToOutPolygon(polyA, transform, transA);
		transA.endPolygon();
		
		Polygon transB = new Polygon();
		shapeUtils.mulTransToOutPolygon(polyB, transform, transB);
		transB.endPolygon();

		float threshold = 0.5f;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		satBuilder.buildManifold(transA, transform, transB, transform, 
				new SATCache(), threshold, manifold);

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

	
	
	public void testSATBuild3() {
		VerticesShapeSATManifoldBuilder satBuilder = new VerticesShapeSATManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		float threshold = 1f;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		satBuilder.buildManifold(polyC, transform, polyD, transform, 
				new SATCache(), threshold, manifold);


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

	/*
	 * Problem with sat collision algorithm is in the following test.
	 * Sometimes Vertext - Vertex contactPoint can't be found,
	 * cause the clipping algorithm cut it out.
	 * In the other collision implementation it can be used the IDistance2
	 * when shapes aren't overlapping
	 */
	public void testSATBuildFailing()
	{
		VerticesShapeSATManifoldBuilder satBuilder = new VerticesShapeSATManifoldBuilder();
		
		Polygon poly1 = new Polygon();
		poly1.addVertex(new Vec2(5,0));
		poly1.addVertex(new Vec2(10,5));
		poly1.addVertex(new Vec2(5,10));
		poly1.addVertex(new Vec2(0,5));
		poly1.endPolygon();
		
		Transform transformA = new Transform();
		transformA.setIdentity();
		
		Transform transformB = new Transform();
		transformB.setIdentity();
		transformB.position.set(10,0);
		
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		satBuilder.buildManifold(poly1, transformA, poly1, transformB, 
				new SATCache(), depth, manifold);
		
		if(manifold.size != 1)
			fail("actual size: " + manifold.size);

	}
	
	public void testClipEdgeEdge()
	{
		VerticesShapeSATManifoldBuilder satBuilder = new VerticesShapeSATManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		satBuilder.buildManifold(edgeC, transform, edgeD, transform, 
				new SATCache(), depth, manifold);


		System.out.println(manifold.points[0].localPoint);
		System.out.println(manifold.points[0].pointID);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(4,5)))
			fail();

		Vec2 normalDepth = new Vec2(1,1);
		normalDepth.normalize();

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
		VerticesShapeSATManifoldBuilder satBuilder = new VerticesShapeSATManifoldBuilder();

		Transform transform = new Transform();
		transform.setIdentity();

		
		float depth = 1;

		ContactManifold manifold = new ContactManifold();
		manifold.type = ContactType.POLYPOLY;

		satBuilder.buildManifold(edgeC, transform, polyD, transform, 
				new SATCache(), depth, manifold);


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

}
