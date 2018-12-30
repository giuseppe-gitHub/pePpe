package it.gius.pePpe.data.physic;

import org.jbox2d.common.Vec2;

public class BodyInit {

	public float linearDamping;
	public float angularDamping;
	
	public boolean fixed = false;
	
	public Vec2 startLinearVelocity = null;
	public float startAngularVelocity = 0;
	
	public Vec2 globalOrigin = new Vec2();
	public float angle = 0;
	
	
	public BodyInit clonePrototype()
	{
		BodyInit result = new BodyInit();
		
		result.angularDamping = this.angularDamping;
		result.linearDamping = this.linearDamping;
		
		result.startAngularVelocity = this.startAngularVelocity;
		result.startLinearVelocity = new Vec2();
		result.startLinearVelocity.set(this.startLinearVelocity);
		result.fixed = this.fixed;
		
		return result;
	}
}
