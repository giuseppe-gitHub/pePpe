package it.gius.pePpe.data.physic;

import java.util.Random;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.physic.BodyPosition;
import it.gius.pePpe.data.physic.PhysicShape;
import it.gius.pePpe.data.shapes.Polygon;
import junit.framework.TestCase;

public class BodyTest extends TestCase {

	private Body body1,body2;

	private Polygon poly1,poly2;
	private PhysicShape phShape1,phShape2;

	public BodyTest() {
		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = (float)MathUtils.PI/4;
		bodyInit.globalOrigin.set(20,20);
		bodyInit.startAngularVelocity = MathUtils.PI/2f;
		bodyInit.startLinearVelocity = new Vec2();
		bodyInit.startLinearVelocity.set(1,0);
		body1 = new Body(bodyInit);
		
		bodyInit.angle = 0;
		body2 = new Body(bodyInit);
		
		poly1 = new Polygon();
		poly1.setAsBox(-10, -10, 10, 10);
		poly1.endPolygon();
		
		poly2 = new Polygon();
		poly2.addVertex(new Vec2(0,0));      //     * * *
		poly2.addVertex(new Vec2(10,10));	//      * *
		poly2.addVertex(new Vec2(0,10));	//      *
		poly2.endPolygon();
		
		phShape1 = new PhysicShape();
		phShape1.density = 1;
		phShape1.shape = poly1;
		
		body1.addPhysicShape(phShape1);
		
		phShape2 = new PhysicShape();
		phShape2.density = 1;
		phShape2.shape = poly2;
		
		body2.addPhysicShape(phShape2);
	}

	private Vec2 v1B1 = new Vec2(0,0);
	private Vec2 v2B1 = new Vec2(9,9);
	private Vec2 v1B2 = new Vec2(1,2);
	private Vec2 v2B2 = new Vec2(5,9);
	private Vec2 external = new Vec2(11,11);
	
	public void testContainsLocal() {
		
		if(!body1.containsLocal(v1B1))
			fail();
		
		if(!body1.containsLocal(v2B1))
			fail();
		
		if(!body2.containsLocal(v1B2))
			fail();
		
		if(!body2.containsLocal(v2B2))
			fail();
		
		if(body1.containsLocal(external))
			fail();
		
		if(body2.containsLocal(external))
			fail();
	}

	public void testContainsGlobal() {
		
		Vec2 globalPoint = new Vec2();
		
		Transform.mulToOut(body1.transform, v1B1, globalPoint);
		if(!body1.containsGlobal(globalPoint))
			fail();
		
		Transform.mulToOut(body1.transform, v2B1, globalPoint);
		if(!body1.containsGlobal(globalPoint))
			fail();
		
		Transform.mulToOut(body2.transform, v1B2, globalPoint);
		if(!body2.containsGlobal(globalPoint))
			fail();
		
		Transform.mulToOut(body2.transform, v2B2, globalPoint);
		if(!body2.containsGlobal(globalPoint))
			fail();
		
		
		Transform.mulToOut(body1.transform, external, globalPoint);
		if(body1.containsGlobal(globalPoint))
			fail();
		
		Transform.mulToOut(body2.transform, external, globalPoint);
		if(body2.containsGlobal(globalPoint))
			fail();
		
	}
	
	public void testApplyForceVec2Vec2() {
		
		body1.clearForces();
		Vec2 force = new Vec2(1,0);
		
		Vec2 globalPoint = new Vec2();
		Transform.mulToOut(body1.transform, v1B1, globalPoint);
		
		body1.applyForce(force, globalPoint);
		
		if(body1.getTorque() != 0)
			fail();
		
		if(body1.getForce().length() == 0)
			fail();
		
		body1.clearForces();
		
		Transform.mulToOut(body1.transform, v2B1, globalPoint);
		body1.applyForce(force, globalPoint);
		
		if(body1.getTorque() == 0)
			fail();
		
		if(body1.getForce().length() == 0)
			fail();
		
		body1.clearForces();
		
		force.set(1,1);
		Transform.mulToOut(body1.transform, v2B1, globalPoint);
		Mat22.mulToOut(body1.transform.R, force, force);
		body1.applyForce(force, globalPoint);
		
		Transform.mulToOut(body1.transform, v2B1.negate(), globalPoint);
		body1.applyForce(force.negate(),globalPoint);
		
		float torque = MathUtils.abs(body1.getTorque());
		if(torque > SystemCostants.EPSILON*10 )
			fail("" + body1.getTorque());
		
		if(body1.getForce().length() != 0)
			fail();
		
		
		
		force.set(1,2);
		Transform.mulToOut(body1.transform, v2B1, globalPoint);
		body1.applyForce(force, globalPoint);
		
		Transform.mulToOut(body1.transform, v2B1.negate(), globalPoint);
		body1.applyForce(force.negate(),globalPoint);
		
		torque = MathUtils.abs(body1.getTorque());
		if(torque < SystemCostants.EPSILON*10 )
			fail("" + body1.getTorque());
		
		if(body1.getForce().length() != 0)
			fail();
		
		
	}
	

	public void testGetPointVelocity()
	{
		Vec2 velocity1 = new Vec2();
		
		Vec2 globalPoint = new Vec2();
		Transform.mulToOut(body1.transform, new Vec2(9,9), globalPoint);
		body1.getPointVelocity(globalPoint, velocity1);
	
		Vec2 velocity2 = new Vec2();
		Transform.mulToOut(body1.transform, new Vec2(4,4), globalPoint);
		body1.getPointVelocity(globalPoint, velocity2);
		
		if(velocity2.length() > velocity1.length())
			fail();
	}

	public void testRemovePhysicShape() {
		
		if(body1.removePhysicShape(phShape2))
			fail();
	}

	public void testSynchronizeTransform() {
		
		BodyPosition bs = body1.getBodyPosition();
		bs.globalCenter.set(0,0);
		body1.synchronizeTransform();
		if(!body1.transform.position.equals(new Vec2(0,0)))
			fail();
		
		bs.angle = MathUtils.PI;
		
		if(!body1.transform.position.equals(new Vec2(0,0)))
			fail();
		
		
		BodyPosition bs2 = body2.getBodyPosition();
		bs2.globalCenter.set(0,0);
		body2.synchronizeTransform();
		
		if(body2.transform.position.equals(new Vec2(0,0)))
			fail();
		
	}
	
	public void testSynchronizeTransform2() {
	
		BodyInit bodyInit = new BodyInit();
		bodyInit.angle = 0;
		bodyInit.globalOrigin.set(20,20);
		Body rotateBody = new Body(bodyInit);
		
		Polygon poly = new Polygon();
		poly.addVertex(new Vec2(0,0));
		poly.addVertex(new Vec2(6,6));
		poly.addVertex(new Vec2(0,6));
		poly.endPolygon();
		
		PhysicShape phShape = new PhysicShape();
		phShape.shape = poly;
		phShape.density = 1;
		
		rotateBody.addPhysicShape(phShape);
		
		BodyPosition actual = rotateBody.getBodyPosition();
		
		BodyPosition expected1 = new BodyPosition();
		expected1.angle = 0;
		expected1.globalCenter.set(22,24);
		
		assertEquals(expected1, actual);
		
		actual.angle -= MathUtils.PI/2f;
		rotateBody.synchronizeTransform();
		
		Transform expected2 = new Transform();
		expected2.position.set(18,26);
		expected2.R.set(-MathUtils.PI/2f);
		
		//assertEquals(expected2, rotateBody.transform);
		float distance =it.gius.pePpe.MathUtils.manhattanDistance(expected2.position,rotateBody.transform.position);
		if(distance > SystemCostants.SQRT_EPSILON)
			fail("" + distance);
		
	}

	public void testRecomputePhysicData(){
		BodyInit bodyInit = new BodyInit();
		
		float density = 2;
		
		Random random = new Random();
		
		bodyInit.angle = random.nextFloat()*MathUtils.PI *2f;
		bodyInit.globalOrigin.set(0,0);
		Body papillonBody = new Body(bodyInit);
		
		float side = random.nextFloat()*10;
		
		System.out.println("side: " + side);
		
		Polygon rightTriangle = new Polygon();
		rightTriangle.addVertex(new Vec2(0,0));
		rightTriangle.addVertex(new Vec2(side,side));
		rightTriangle.addVertex(new Vec2(side,-side));
		rightTriangle.endPolygon();
		
		float massTriangle = density*side*2f*side / 2f;
		System.out.println("massTriangle: " + massTriangle);
		
		PhysicShape phRT = new PhysicShape();
		phRT.shape = rightTriangle;
		
		phRT.density = density;
		
		papillonBody.addPhysicShape(phRT);
		
		Vec2 globalCenter = papillonBody.getBodyPosition().globalCenter;
		
		Vec2 localCenter = new Vec2();
		
		Transform.mulTransToOut(papillonBody.transform, globalCenter, localCenter);
		
		if(!rightTriangle.contains(localCenter))
			fail();
		
		if(papillonBody.mass != massTriangle)
			fail();
		
		//http://www.efunda.com/math/areas/triangle.cfm
		float b = side*2;
		float h = side;
		float expectedIz = density * (b*b*b*h)/36;
		System.out.println(papillonBody.Iz);
		
		//assertEquals(expectedIz, papillonBody.Iz);
		if(MathUtils.abs(expectedIz-papillonBody.Iz) > 0.1f)
			fail();
		
		Polygon leftTriangle = new Polygon();
		leftTriangle.addVertex(new Vec2(0,0));
		leftTriangle.addVertex(new Vec2(-side,side));
		leftTriangle.addVertex(new Vec2(-side,-side));
		leftTriangle.endPolygon();
		
		PhysicShape phLT = new PhysicShape();
		phLT.shape = leftTriangle;
		phLT.density = density;
		
		papillonBody.addPhysicShape(phLT);
		
		globalCenter = papillonBody.getBodyPosition().globalCenter;
		
		if(!globalCenter.equals(new Vec2(0,0)))
			fail();
		
		if(papillonBody.mass != massTriangle*2)
			fail();
		
			
	}

}
