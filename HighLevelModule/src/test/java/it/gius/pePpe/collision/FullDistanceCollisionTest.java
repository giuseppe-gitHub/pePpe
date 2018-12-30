package it.gius.pePpe.collision;

//import java.io.IOException;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;




//import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.collision.CollisionInit;
import it.gius.pePpe.collision.FullDistanceCollision;
import it.gius.pePpe.collision.manifoldBuild.ManifoldTestUtils;
import it.gius.pePpe.data.BindPair;
import it.gius.pePpe.data.cache.CollisionCache;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.physic.IGetAccesData;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.distance.IDistance2;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactManifold.ContactType;
import junit.framework.TestCase;

public class FullDistanceCollisionTest extends TestCase implements IGetAccesData {

	private PhysicClassAcces access;
	private ManifoldTestUtils manifoldTestUtils;

	//private IDistance2 distance = null;
	//private Collision collision;

	private Polygon polyC,polyD;

	private Polygon polyForCircle;
	private Polygon polyForCircleEdgeFeature;
	private Circle circle, otherCircle;

	//private Polygon polyANOv,polyBNOv;

	public FullDistanceCollisionTest() {
		PhysicClassAcces.getIstance(this);

		manifoldTestUtils = new ManifoldTestUtils();

		/*ConfigurationFactory conf = new ConfigurationFactory();
		try {
			conf.init();

			distance = conf.getDistance2();
		} catch (IOException e) {
			e.printStackTrace();
		}

		collision = new Collision(distance, 10);*/


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

		/*polyANOv = new Polygon();
		Transform translateTransform = new Transform();
		translateTransform.position.set(0,2);

		ShapeUtils shapeUtils = new ShapeUtils();

		shapeUtils.mulToOutPolygon(polyC, translateTransform, polyANOv);

		polyBNOv = polyD.clone();*/

		polyForCircle = new Polygon();
		polyForCircle.addVertex(new Vec2(13,3));
		polyForCircle.addVertex(new Vec2(14,7));
		polyForCircle.addVertex(new Vec2(10,8));
		polyForCircle.addVertex(new Vec2(9,4));
		polyForCircle.endPolygon();

		circle = new Circle(new Vec2(5,4), 5);
		otherCircle = new Circle(new Vec2(13,4), 4);

		polyForCircleEdgeFeature = new Polygon();
		polyForCircleEdgeFeature.addVertex(new Vec2(-7,1));
		polyForCircleEdgeFeature.addVertex(new Vec2(-7,7));
		polyForCircleEdgeFeature.addVertex(new Vec2(-1,7));
		polyForCircleEdgeFeature.addVertex(new Vec2(-1,1));
		polyForCircleEdgeFeature.endPolygon();


	}

	@Override
	public void setAccess(PhysicClassAcces access) {
		this.access = access;

	}

	private static class DistanceMock implements IDistance2
	{

		private OverlapSolution ovSol;
		private boolean overlap;

		public DistanceMock(OverlapSolution ovSol, boolean overlap) {
			this.ovSol = ovSol;
			this.overlap = overlap;
		}

		@Override
		public void distance(Shape shapeA, Transform transformA, Shape shapeB,
				Transform transformB, DistanceSolution sol) {
		}

		@Override
		public void distance(Shape shapeA, Transform transformA, Shape shapeB,
				Transform transformB, DistanceWitnessCache witnessCacheInOut,
				DistanceSolution sol) {
		}

		@Override
		public void distance(Shape shapeA, Transform transformA, Vec2 q,
				DistanceSolution sol) {
		}

		@Override
		public boolean overlap(Shape shapeA, Transform transformA,
				Shape shapeB, Transform transformB,
				DistanceWitnessCache witnessCacheInOut, OverlapSolution sol) {


			sol.distanceDepth = ovSol.distanceDepth;
			sol.normal.set(ovSol.normal);
			sol.p1.set(ovSol.p1);
			sol.p2.set(ovSol.p2);

			return overlap;
		}

		@Override
		public boolean overlap(Shape shapeA, Transform transformA,
				Shape shapeB, Transform transformB, OverlapSolution sol) {
			return false;
		}
	}

	public void testCollideVertexVertex()
	{
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
		transformB.position.set(10.1f,0);
		
		OverlapSolution overlapSolution = new OverlapSolution();
		overlapSolution.distanceDepth = 0.1f;
		overlapSolution.normal.set(1,0);
		overlapSolution.p1.set(10,5);
		overlapSolution.p2.set(10.1f,5);
		
		DistanceMock distanceMock = new DistanceMock(overlapSolution, false);
		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision = new FullDistanceCollision(distanceMock, collisionInit);
		
		ContactManifold manifold = new ContactManifold();
		localCollision.collide(poly1, transformA, poly1, transformB, new CollisionCache(), manifold);
		
		if(manifold.size != 1)
			fail("actual size: " + manifold.size);
		
		if(!manifold.points[0].localPoint.equals(new Vec2(10,5)))
			fail();
		
		if(!manifold.points[0].otherLocalPoint.equals(new Vec2(0,5))) //global is (10.1, 5)
			fail();
	}
	
	

	public void testCollideVerticesShapeOverlapping() {

		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = -1f;
		overlapSol.normal.set(0,-1);

		DistanceMock distanceMock = new DistanceMock(overlapSol, true);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision1 = new FullDistanceCollision(distanceMock, collisionInit);

		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyC = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyC;
		bi.body = bodyC;
		Bind bindC = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyD = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = polyD;
		bi.body = bodyD;
		Bind bindD = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindC, bindD);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision1.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYPOLY;
		expectedManifold.size = 2;

		expectedManifold.points[0].localPoint.set(4,6);
		expectedManifold.points[0].otherLocalPoint.set(4,5);
		expectedManifold.points[0].distance = 1;
		expectedManifold.points[0].normalGlobal.set(0,-1);
		expectedManifold.points[0].pointOnShapeB = false;
		expectedManifold.points[0].pointID.featureA = 0;
		expectedManifold.points[0].pointID.vertexA = false;
		expectedManifold.points[0].pointID.featureB = 3;
		expectedManifold.points[0].pointID.vertexB = true;

		expectedManifold.points[1].localPoint.set(6,4);
		expectedManifold.points[1].otherLocalPoint.set(6,5);
		expectedManifold.points[1].distance = -1;
		expectedManifold.points[1].normalGlobal.set(0,-1);
		expectedManifold.points[1].pointOnShapeB = false;
		expectedManifold.points[1].pointID.featureA = 1;
		expectedManifold.points[1].pointID.vertexA = true;
		expectedManifold.points[1].pointID.featureB = 2;
		expectedManifold.points[1].pointID.vertexB = false;

		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyC.transform, bodyD.transform))
				fail();
		}

	}


	public void testCollideVerticesShapeNotOverlapping() {

		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = 1f;
		overlapSol.normal.set(0,-1);
		overlapSol.p1.set(6,6);
		overlapSol.p2.set(6,5);

		DistanceMock distanceMock = new DistanceMock(overlapSol, false);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision2 = new FullDistanceCollision(distanceMock, collisionInit);

		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(0,2);

		Body bodyC = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyC;
		bi.body = bodyC;
		Bind bindC = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyD = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = polyD;
		bi.body = bodyD;
		Bind bindD = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindC, bindD);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision2.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYPOLY;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(6,4);
		expectedManifold.points[0].otherLocalPoint.set(6,5);
		expectedManifold.points[0].distance = 1;
		expectedManifold.points[0].normalGlobal.set(0,-1);
		expectedManifold.points[0].pointOnShapeB = false;
		expectedManifold.points[0].pointID.featureA = 1;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 2;
		expectedManifold.points[0].pointID.vertexB = false;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyC.transform, bodyD.transform))
				fail();
		}

	}



	public void testCollideShapeCircleOverlapping() {

		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = -1f;
		overlapSol.normal.set(-1,0);

		DistanceMock distanceMock = new DistanceMock(overlapSol, true);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision3 = new FullDistanceCollision(distanceMock, collisionInit);


		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(0,0);

		Body bodyPoly = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyForCircle;
		bi.body = bodyPoly;
		Bind bindPoly = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindPoly, bindCircle);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision3.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYCIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(10,4);
		expectedManifold.points[0].otherLocalPoint.set(9,4);
		expectedManifold.points[0].distance = -1;
		expectedManifold.points[0].pointOnShapeB = true;
		expectedManifold.points[0].normalGlobal.set(1,0);
		expectedManifold.points[0].pointID.featureA = 3;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 0;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyCircle.transform, bodyPoly.transform))
				fail();
		}
	}


	public void testCollideShapeCircleOverlappingFlipped() {
		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = -1f;
		overlapSol.normal.set(1,0);

		DistanceMock distanceMock = new DistanceMock(overlapSol, true);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision4 = new FullDistanceCollision(distanceMock, collisionInit);


		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(0,0);

		Body bodyPoly = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyForCircle;
		bi.body = bodyPoly;
		Bind bindPoly = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindCircle, bindPoly);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;

		//collision.collide(bindPair, cache, manifoldOut);
		localCollision4.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYCIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(10,4);
		expectedManifold.points[0].otherLocalPoint.set(9,4);
		expectedManifold.points[0].distance = -1;
		expectedManifold.points[0].pointOnShapeB = false;
		expectedManifold.points[0].normalGlobal.set(1,0);
		expectedManifold.points[0].pointID.featureA = 0;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 3;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyCircle.transform, bodyPoly.transform))
				fail();
		}
	}


	public void testCollideShapeCircleNotOverlappingPolyVertexFeature() {
		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = 1f;
		overlapSol.normal.set(-1,0);
		overlapSol.p1.set(11,4);
		overlapSol.p2.set(10,4);

		DistanceMock distanceMock = new DistanceMock(overlapSol, false);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision5 = new FullDistanceCollision(distanceMock, collisionInit);


		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(2,0); //translate so that they will not overlap

		Body bodyPoly = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyForCircle;
		bi.body = bodyPoly;
		Bind bindPoly = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindPoly, bindCircle);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;

		localCollision5.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);
		//collision.collide(bindPair, cache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYCIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(10,4);
		expectedManifold.points[0].otherLocalPoint.set(9,4);
		expectedManifold.points[0].distance = 1;
		expectedManifold.points[0].pointOnShapeB = true;
		expectedManifold.points[0].normalGlobal.set(1,0);
		expectedManifold.points[0].pointID.featureA = 3;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 0;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyCircle.transform, bodyPoly.transform))
				fail();
		}
	}

	public void testCollideShapeCircleNotOverlappingPolyEdgeFeature() {

		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = 1f;
		overlapSol.normal.set(1,0);
		overlapSol.p1.set(-1,4);
		overlapSol.p2.set(0,4);

		DistanceMock distanceMock = new DistanceMock(overlapSol, false);


		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision6 = new FullDistanceCollision(distanceMock, collisionInit);

		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(0,0);

		Body bodyPoly = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyForCircleEdgeFeature;
		bi.body = bodyPoly;
		Bind bindPoly = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindPoly, bindCircle);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision6.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);
		//collision.collide(bindPair, cache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYCIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(0,4);
		expectedManifold.points[0].otherLocalPoint.set(-1,4);
		expectedManifold.points[0].distance = 1;
		expectedManifold.points[0].pointOnShapeB = true;
		expectedManifold.points[0].normalGlobal.set(-1,0);
		expectedManifold.points[0].pointID.featureA = 2;
		expectedManifold.points[0].pointID.vertexA = false;
		expectedManifold.points[0].pointID.featureB = 0;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyPoly.transform, bodyCircle.transform))
				fail();
		}
	}


	public void testCollideShapeCircleNotOverlappingFlipped() {

		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = 1f;
		overlapSol.normal.set(1,0);
		overlapSol.p1.set(10,4);
		overlapSol.p2.set(11,4);

		DistanceMock distanceMock = new DistanceMock(overlapSol, false);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision7 = new FullDistanceCollision(distanceMock, collisionInit);


		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(2,0); //translate so that they will not overlap

		Body bodyPoly = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = polyForCircle;
		bi.body = bodyPoly;
		Bind bindPoly = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindCircle, bindPoly);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision7.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);
		//collision.collide(bindPair, cache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.POLYCIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(10,4);
		expectedManifold.points[0].otherLocalPoint.set(9,4);
		expectedManifold.points[0].distance = 1;
		expectedManifold.points[0].pointOnShapeB = false;
		expectedManifold.points[0].normalGlobal.set(1,0);
		expectedManifold.points[0].pointID.featureA = 0;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 3;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyCircle.transform, bodyPoly.transform))
				fail();
		}
	}


	public void testCollideCircleCircleOverlap()
	{
		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = -1f;
		overlapSol.normal.set(-1,0);

		DistanceMock distanceMock = new DistanceMock(overlapSol, true);


		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision8 = new FullDistanceCollision(distanceMock, collisionInit);


		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(0,0);

		Body bodyOtherCircle = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = otherCircle;
		bi.body = bodyOtherCircle;
		Bind bindOtherCircle = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindOtherCircle, bindCircle);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision8.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);
		//collision.collide(bindPair, cache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.CIRCLECIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(10,4);
		expectedManifold.points[0].otherLocalPoint.set(9,4);
		expectedManifold.points[0].distance = -1;
		expectedManifold.points[0].pointOnShapeB = true;
		expectedManifold.points[0].normalGlobal.set(1,0);
		expectedManifold.points[0].pointID.featureA = 0;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 0;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyOtherCircle.transform, bodyCircle.transform))
				fail();
		}
	}

	public void testCollideCircleCircleNotOverlap()
	{
		OverlapSolution overlapSol = new OverlapSolution();
		overlapSol.distanceDepth = 1f;
		overlapSol.normal.set(-1,0);
		overlapSol.p1.set(11,4);
		overlapSol.p2.set(10,4);

		DistanceMock distanceMock = new DistanceMock(overlapSol, false);

		CollisionInit collisionInit = new CollisionInit();
		collisionInit.maxContactDistance = 10;
		FullDistanceCollision localCollision9 = new FullDistanceCollision(distanceMock, collisionInit);


		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(2,0); //translate so that they will not overlap

		Body bodyOtherCircle = access.getNewBody(bodyInit);

		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = otherCircle;
		bi.body = bodyOtherCircle;
		Bind bindOtherCircle = access.getNewBind(bi);


		bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.setZero();

		Body bodyCircle = access.getNewBody(bodyInit);

		bi = new BindInit();
		bi.density = 1;
		bi.shape = circle;
		bi.body = bodyCircle;
		Bind bindCircle = access.getNewBind(bi);

		BindPair bindPair = new BindPair(bindOtherCircle, bindCircle);

		DistanceWitnessCache cache = new DistanceWitnessCache();

		ContactManifold manifoldOut = new ContactManifold();

		CollisionCache collisionCache = new CollisionCache();
		collisionCache.distanceCache = cache;
		localCollision9.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, collisionCache, manifoldOut);
		//collision.collide(bindPair, cache, manifoldOut);

		ContactManifold expectedManifold = new ContactManifold();
		expectedManifold.type = ContactType.CIRCLECIRCLE;
		expectedManifold.size = 1;

		expectedManifold.points[0].localPoint.set(10,4);
		expectedManifold.points[0].otherLocalPoint.set(9,4); //global: 11,4
		expectedManifold.points[0].distance = 1;
		expectedManifold.points[0].pointOnShapeB = true;
		expectedManifold.points[0].normalGlobal.set(1,0);
		expectedManifold.points[0].pointID.featureA = 0;
		expectedManifold.points[0].pointID.vertexA = true;
		expectedManifold.points[0].pointID.featureB = 0;
		expectedManifold.points[0].pointID.vertexB = true;


		assertEquals(expectedManifold, manifoldOut);

		for(int i=0; i<manifoldOut.size; i++)
		{
			if(!manifoldTestUtils.checkOtherLocalPoint(manifoldOut.points[i], bodyCircle.transform, bodyOtherCircle.transform))
				fail();
		}
	}
}
