package it.gius.pePpe.constraints;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.manifold.ContactPoint;

public class CollisionResponseUtil {

	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();
	private Vec2 pool4 = new Vec2();
	//private Vec2 pool5 = new Vec2();

	public boolean colliding(Body bodyA, Body bodyB, ContactPoint contactPoint, float threshold)
	{

		float value = getRelativeVelocityScalar(bodyA, bodyB, contactPoint);

		if(value >= threshold) //moving away
			return false;

		if(value > -threshold) //resting contact
			return false;

		return true; //colliding
	}
	
	public boolean restingContact(Body bodyA, Body bodyB, ContactPoint contactPoint, float threshold)
	{
		float value = getRelativeVelocityScalar(bodyA, bodyB, contactPoint);

		if(value >= threshold) //moving away
			return false;

		if(value > -threshold) //resting contact
			return true;

		return false; //colliding
		
	}


	public float getRelativeVelocityScalar(Body bodyA, Body bodyB, ContactPoint contactPoint)
	{
		Vec2 velocityPoint = pool1;
		Vec2 velocityOtherPoint = pool2;
		if(!contactPoint.pointOnShapeB)
		{
			bodyA.getPointVelocity(contactPoint.globalPoint, velocityPoint);
			bodyB.getPointVelocity(contactPoint.otherGlobalPoint, velocityOtherPoint);
		}
		else
		{
			bodyB.getPointVelocity(contactPoint.globalPoint, velocityPoint);
			bodyA.getPointVelocity(contactPoint.otherGlobalPoint, velocityOtherPoint);
		}

		Vec2 normal = pool3;

		normal.set(contactPoint.normalGlobal);
		
		Vec2 relativeVelocityVec = pool4;
		relativeVelocityVec.x = velocityOtherPoint.x - velocityPoint.x;
		relativeVelocityVec.y = velocityOtherPoint.y - velocityPoint.y;

		return (relativeVelocityVec.x)*normal.x  + (relativeVelocityVec.y)* normal.y;	
	}

	public void getRelativeVelocityVector(Body bodyA, Body bodyB, ContactPoint contactPoint, Vec2 velocityOut)
	{
		Vec2 velocityPoint = pool1;
		Vec2 velocityOtherPoint = pool2;
		if(!contactPoint.pointOnShapeB)
		{
			bodyA.getPointVelocity(contactPoint.globalPoint, velocityPoint);
			bodyB.getPointVelocity(contactPoint.otherGlobalPoint, velocityOtherPoint);
		}
		else
		{
			bodyB.getPointVelocity(contactPoint.globalPoint, velocityPoint);
			bodyA.getPointVelocity(contactPoint.otherGlobalPoint, velocityOtherPoint);
		}
		
		velocityOut.x = velocityOtherPoint.x - velocityPoint.x;
		velocityOut.y = velocityOtherPoint.y - velocityPoint.y;
	}


	public float calcImpulse(Bind bindA, Bind bindB, ContactPoint contactPoint )
	{
		Body bodyA = bindA.body;
		Body bodyB = bindB.body;
		
		float relativeVelocity;
		
		relativeVelocity = getRelativeVelocityScalar(bodyA, bodyB, contactPoint);
		float restitution = (bindA.phShape.restitution + bindB.phShape.restitution) * 0.5f;

		Vec2 rA = pool2;
		Vec2 rB = pool3;
		
		if(!contactPoint.pointOnShapeB)
		{
			Vec2 xA = bodyA.getGlobalCenter();
			rA.x = contactPoint.globalPoint.x - xA.x;
			rA.y = contactPoint.globalPoint.y - xA.y;

			Vec2 xB = bodyB.getGlobalCenter();
			rB.x = contactPoint.otherGlobalPoint.x - xB.x;
			rB.y = contactPoint.otherGlobalPoint.y - xB.y;
		}
		else
		{
			Vec2 xA = bodyA.getGlobalCenter();
			rA.x = contactPoint.otherGlobalPoint.x - xA.x;
			rA.y = contactPoint.otherGlobalPoint.y - xA.y;

			Vec2 xB = bodyB.getGlobalCenter();
			rB.x = contactPoint.globalPoint.x - xB.x;
			rB.y = contactPoint.globalPoint.y - xB.y;
		}
		
		Vec2 normal = pool4;

		normal.set(contactPoint.normalGlobal);


		float rotationTermA, rotationTermB, numerator;

		numerator = -(1 + restitution)*relativeVelocity;
		
		
		float rnA = Vec2.cross(rA, normal);

		rotationTermA = bodyA.inv_Iz * rnA * rnA;

		float rnB = Vec2.cross(rB, normal);
		
		rotationTermB = bodyB.inv_Iz *rnB *rnB;

		float impulse = numerator / (bodyA.inv_mass + bodyB.inv_mass + rotationTermA + rotationTermB);
		
		return impulse;
	}
	
	public void updateVelocity(Body body, Vec2 globalBodyPoint, float impulse, Vec2 normal)
	{
		
		Vec2 impulseVec = pool1;
		Vec2 r = pool2;
		
		r.x = globalBodyPoint.x - body.getGlobalCenter().x;
		r.y = globalBodyPoint.y - body.getGlobalCenter().y;
		
		impulseVec.x = impulse * normal.x;
		impulseVec.y = impulse * normal.y;
		
		body.linearVelocity.x = body.linearVelocity.x + impulseVec.x*body.inv_mass;
		body.linearVelocity.y = body.linearVelocity.y + impulseVec.y*body.inv_mass;
		
		body.angularVelocity = body.angularVelocity + body.inv_Iz * Vec2.cross(r, impulseVec);
	}
}
