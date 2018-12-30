package it.gius.pePpe.manifold;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactPointID;
import it.gius.pePpe.manifold.ContactManifold.ContactType;
import junit.framework.TestCase;

public class ContactManifoldTest extends TestCase {

	public void testGetPointPosition() {
		ContactManifold manifold = new ContactManifold();
		int result = manifold.getPointPosition(new ContactPointID());

		if(result >= 0)
			fail();

		manifold.size = 2;
		manifold.points[0].localPoint.set(4,6);
		manifold.points[0].otherLocalPoint.set(4,5);
		manifold.points[0].distance = 1;
		manifold.points[0].normalGlobal.set(0,-1);
		manifold.points[0].pointOnShapeB = false;
		manifold.points[0].pointID.featureA = 0;
		manifold.points[0].pointID.vertexA = false;
		manifold.points[0].pointID.featureB = 3;
		manifold.points[0].pointID.vertexB = true;

		manifold.points[1].localPoint.set(6,4);
		manifold.points[1].otherLocalPoint.set(6,5);
		manifold.points[1].distance = -1;
		manifold.points[1].normalGlobal.set(0,1);
		manifold.points[1].pointOnShapeB = false;
		manifold.points[1].pointID.featureA = 1;
		manifold.points[1].pointID.vertexA = true;
		manifold.points[1].pointID.featureB = 2;
		manifold.points[1].pointID.vertexB = false;

		result = manifold.getPointPosition(manifold.points[0].pointID);

		if(result != 0)
			fail();

		result = manifold.getPointPosition(manifold.points[1].pointID);

		if(result != 1)
			fail();
		
		ContactPointID pointID = new ContactPointID();
		pointID.featureA = 0;
		pointID.featureB = 0;

		result = manifold.getPointPosition(pointID);
		
		if(result >= 0)
			fail();

		manifold.size = 1;
		
		result = manifold.getPointPosition(manifold.points[1].pointID);

		if(result >= 0)
			fail();
	}
	
	
	/*public void testAddPoint()
	{
		ContactManifold manifoldIn = new ContactManifold();

		manifoldIn.type = ContactType.POLYPOLY;
		manifoldIn.size = 2;
		manifoldIn.points[0].localPoint.set(4,6);
		manifoldIn.points[0].otherLocalPoint.set(4,5);
		manifoldIn.points[0].distance = 1;
		manifoldIn.points[0].normalGlobal.set(0,-1);
		manifoldIn.points[0].pointOnShapeB = false;
		manifoldIn.points[0].pointID.featureA = 0;
		manifoldIn.points[0].pointID.vertexA = false;
		manifoldIn.points[0].pointID.featureB = 3;
		manifoldIn.points[0].pointID.vertexB = true;

		manifoldIn.points[1].localPoint.set(6,4);
		manifoldIn.points[1].otherLocalPoint.set(6,5);
		manifoldIn.points[1].distance = -1;
		manifoldIn.points[1].normalGlobal.set(0,-1);
		manifoldIn.points[1].pointOnShapeB = false;
		manifoldIn.points[1].pointID.featureA = 1;
		manifoldIn.points[1].pointID.vertexA = true;
		manifoldIn.points[1].pointID.featureB = 2;
		manifoldIn.points[1].pointID.vertexB = false;
		
		ContactManifold manifoldTest = new ContactManifold();
		manifoldTest.type = ContactType.CIRCLECIRCLE;
		
		manifoldTest.addPoint(manifoldIn.points[0]);
		
		if(manifoldTest.size != 1)
			fail();
		
		if(!manifoldTest.points[0].equals(manifoldIn.points[0]))
			fail();
		
		manifoldTest.addPoint(manifoldIn.points[1]);
		
		if(manifoldTest.size != 1)
			fail();
		
		if(!manifoldTest.points[0].equals(manifoldIn.points[1]))
			fail();

		manifoldTest.type = ContactType.POLYPOLY;
		
		manifoldTest.addPoint(manifoldIn.points[0]);
		
		if(manifoldTest.size != 2)
			fail();
		
		if(!manifoldTest.points[1].equals(manifoldIn.points[0]))
			fail();
		
		ContactPoint pointToAdd = new ContactPoint();
		
		pointToAdd.localPoint.set(6,4);
		pointToAdd.otherLocalPoint.set(6,6);
		pointToAdd.distance = -2;
		pointToAdd.normalGlobal.set(0,1);
		pointToAdd.pointOnShapeB = false;
		pointToAdd.pointID.featureA = 2;
		pointToAdd.pointID.vertexA = true;
		pointToAdd.pointID.featureB = 3;
		pointToAdd.pointID.vertexB = false;
		
		int insertIndex = manifoldTest.addPoint(pointToAdd);
		
		if(insertIndex != 1)
			fail();
		
		if(manifoldTest.size != 2)
			fail();
		
		if(!manifoldTest.points[1].equals(pointToAdd))
			fail();
	}*/
	
	public void testRefreshContactPoints0()
	{
		ContactManifold manifoldTest = new ContactManifold();

		manifoldTest.type = ContactType.POLYPOLY;
		manifoldTest.size = 2;
		manifoldTest.points[0].localPoint.set(4,6);
		manifoldTest.points[0].otherLocalPoint.set(4,5);
		manifoldTest.points[0].distance = 1;
		manifoldTest.points[0].normalGlobal.set(0,-1);
		manifoldTest.points[0].pointOnShapeB = false;
		manifoldTest.points[0].pointID.featureA = 0;
		manifoldTest.points[0].pointID.vertexA = false;
		manifoldTest.points[0].pointID.featureB = 3;
		manifoldTest.points[0].pointID.vertexB = true;

		manifoldTest.points[1].localPoint.set(6,4);
		manifoldTest.points[1].otherLocalPoint.set(6,5);
		manifoldTest.points[1].distance = -1;
		manifoldTest.points[1].normalGlobal.set(0,-1);
		manifoldTest.points[1].pointOnShapeB = false;
		manifoldTest.points[1].pointID.featureA = 1;
		manifoldTest.points[1].pointID.vertexA = true;
		manifoldTest.points[1].pointID.featureB = 2;
		manifoldTest.points[1].pointID.vertexB = false;
		
		Transform transform = new Transform();
		transform.setIdentity();
		
		manifoldTest.refreshAllContactPoints(10, 0.1f, transform, transform);
		
		if(manifoldTest.size != 2)
			fail();
		
	}
	
	
	public void testRefreshContactPoints1()
	{
		ContactManifold manifoldTest = new ContactManifold();

		manifoldTest.type = ContactType.POLYPOLY;
		manifoldTest.size = 2;
		manifoldTest.points[0].localPoint.set(4,6);
		manifoldTest.points[0].otherLocalPoint.set(4,5);
		manifoldTest.points[0].distance = 1;
		manifoldTest.points[0].normalGlobal.set(0,-1);
		manifoldTest.points[0].pointOnShapeB = false;
		manifoldTest.points[0].pointID.featureA = 0;
		manifoldTest.points[0].pointID.vertexA = false;
		manifoldTest.points[0].pointID.featureB = 3;
		manifoldTest.points[0].pointID.vertexB = true;

		manifoldTest.points[1].localPoint.set(6,4);
		manifoldTest.points[1].otherLocalPoint.set(6,5);
		manifoldTest.points[1].distance = -1;
		manifoldTest.points[1].normalGlobal.set(0,-1);
		manifoldTest.points[1].pointOnShapeB = false;
		manifoldTest.points[1].pointID.featureA = 1;
		manifoldTest.points[1].pointID.vertexA = true;
		manifoldTest.points[1].pointID.featureB = 2;
		manifoldTest.points[1].pointID.vertexB = false;
		
		Transform transform = new Transform();
		transform.setIdentity();
		
		manifoldTest.refreshAllContactPoints(0, 0.1f, transform, transform);
		
		if(manifoldTest.size != 1)
			fail();
		
		if(!manifoldTest.points[0].localPoint.equals(new Vec2(6,4)))
			fail();
	}
	

	public void testRefreshContactPoints2()
	{
		ContactManifold manifoldTest = new ContactManifold();

		manifoldTest.type = ContactType.POLYPOLY;
		manifoldTest.size = 2;
		
		manifoldTest.points[0].localPoint.set(6,4);
		manifoldTest.points[0].otherLocalPoint.set(6,5);
		manifoldTest.points[0].distance = -1;
		manifoldTest.points[0].normalGlobal.set(0,-1);
		manifoldTest.points[0].pointOnShapeB = false;
		manifoldTest.points[0].pointID.featureA = 1;
		manifoldTest.points[0].pointID.vertexA = true;
		manifoldTest.points[0].pointID.featureB = 2;
		manifoldTest.points[0].pointID.vertexB = false;
		
		manifoldTest.points[1].localPoint.set(4,6);
		manifoldTest.points[1].otherLocalPoint.set(4,5);
		manifoldTest.points[1].distance = 1;
		manifoldTest.points[1].normalGlobal.set(0,-1);
		manifoldTest.points[1].pointOnShapeB = false;
		manifoldTest.points[1].pointID.featureA = 0;
		manifoldTest.points[1].pointID.vertexA = false;
		manifoldTest.points[1].pointID.featureB = 3;
		manifoldTest.points[1].pointID.vertexB = true;

		
		Transform transform = new Transform();
		transform.setIdentity();
		
		manifoldTest.refreshAllContactPoints(0, 0.1f, transform, transform);
		
		if(manifoldTest.size != 1)
			fail();
		
		if(!manifoldTest.points[0].localPoint.equals(new Vec2(6,4)))
			fail();
	}


	public void testRefreshContactPoints3()
	{
		ContactManifold manifoldTest = new ContactManifold();

		manifoldTest.type = ContactType.POLYPOLY;
		manifoldTest.size = 2;
		
		manifoldTest.points[0].localPoint.set(6,4);
		manifoldTest.points[0].otherLocalPoint.set(6,5);
		manifoldTest.points[0].distance = -1;
		manifoldTest.points[0].normalGlobal.set(0,-1);
		manifoldTest.points[0].pointOnShapeB = false;
		manifoldTest.points[0].pointID.featureA = 1;
		manifoldTest.points[0].pointID.vertexA = true;
		manifoldTest.points[0].pointID.featureB = 2;
		manifoldTest.points[0].pointID.vertexB = false;
		
		manifoldTest.points[1].localPoint.set(4,6);
		manifoldTest.points[1].otherLocalPoint.set(4,5);
		manifoldTest.points[1].distance = 1;
		manifoldTest.points[1].normalGlobal.set(0,-1);
		manifoldTest.points[1].pointOnShapeB = false;
		manifoldTest.points[1].pointID.featureA = 0;
		manifoldTest.points[1].pointID.vertexA = false;
		manifoldTest.points[1].pointID.featureB = 3;
		manifoldTest.points[1].pointID.vertexB = true;

		
		Transform transform = new Transform();
		transform.setIdentity();
		
		Transform otherTransform = new Transform();
		otherTransform.setIdentity();
		otherTransform.position.set(1,0);
		
		manifoldTest.refreshAllContactPoints(10, 0.9f, transform, otherTransform);
		
		if(manifoldTest.size != 0)
			fail();
		

	}
	
	

	public void testRefreshContactPoints4()
	{
		ContactManifold manifoldTest = new ContactManifold();

		manifoldTest.type = ContactType.POLYPOLY;
		manifoldTest.size = 2;
		
		manifoldTest.points[0].localPoint.set(6,4);
		manifoldTest.points[0].otherLocalPoint.set(6,5);
		manifoldTest.points[0].distance = -1;
		manifoldTest.points[0].normalGlobal.set(0,-1);
		manifoldTest.points[0].pointOnShapeB = false;
		manifoldTest.points[0].pointID.featureA = 1;
		manifoldTest.points[0].pointID.vertexA = true;
		manifoldTest.points[0].pointID.featureB = 2;
		manifoldTest.points[0].pointID.vertexB = false;
		
		manifoldTest.points[1].localPoint.set(4,6);
		manifoldTest.points[1].otherLocalPoint.set(4,5);
		manifoldTest.points[1].distance = 1;
		manifoldTest.points[1].normalGlobal.set(0,-1);
		manifoldTest.points[1].pointOnShapeB = false;
		manifoldTest.points[1].pointID.featureA = 0;
		manifoldTest.points[1].pointID.vertexA = false;
		manifoldTest.points[1].pointID.featureB = 3;
		manifoldTest.points[1].pointID.vertexB = true;

		
		Transform transform = new Transform();
		transform.setIdentity();
		
		Transform otherTransform = new Transform();
		otherTransform.setIdentity();
		otherTransform.position.set(1,0);
		
		manifoldTest.refreshAllContactPoints(10, 1.1f, transform, otherTransform);
		
		if(manifoldTest.size != 2)
			fail();
		

	}
}
