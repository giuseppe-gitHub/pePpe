package it.gius.pePpe.forces;

import it.gius.pePpe.Resources;
import it.gius.pePpe.data.physic.Body;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public abstract class AbstractDoubleBodyForce implements IForce {

	public short id;
	
	public Body bodyA;
	public Vec2 localBodyPointA;
	
	public Body bodyB;
	public Vec2 localBodyPointB;
	
	protected Resources resources;
	
	
	public AbstractDoubleBodyForce(Body bodyA, Vec2 localBodyPointA, Body bodyB, Vec2 localBodyPointB) {
		
		if(!bodyA.containsLocal(localBodyPointA))
			throw new IllegalArgumentException("localPoint not contained in bodyA");
		
		if(!bodyB.containsLocal(localBodyPointB))
			throw new IllegalArgumentException("localPoint not contained in bodyB");
		
		this.localBodyPointA = new Vec2();
		this.localBodyPointA.set(localBodyPointA);
		this.bodyA = bodyA;
		
		this.localBodyPointB = new Vec2();
		this.localBodyPointB.set(localBodyPointB);
		this.bodyB = bodyB;
		
		resources = new Resources();
		resources.bodiesIds = new short[2];
		resources.bodiesIds[0] = bodyA.globalId;
		resources.bodiesIds[1] = bodyB.globalId;
		resources.numBodies = 2;
	}
	
	@Override
	public boolean isConsistent() {
		
		return (bodyA.containsLocal(localBodyPointA) &&
			bodyB.containsLocal(localBodyPointB));
		
	}
	
	@Override
	public void setId(short id) {
		this.id = id;
	}

	@Override
	public short getId() {
		return id;
	}

	protected Vec2 globalPointA = new Vec2();
	protected Vec2 globalPointB = new Vec2();
	
	
	protected abstract Vec2 calcForce(float time);
	
	@Override
	public void apply(float time) {
		Transform.mulToOut(bodyA.transform,localBodyPointA,globalPointA);
		Transform.mulToOut(bodyB.transform,localBodyPointB,globalPointB);

		Vec2 force = calcForce(time);
		
		bodyA.applyForce(force, globalPointA);
		
		force.negateLocal();
		
		bodyB.applyForce(force,globalPointB);
	}
	
	
	@Override
	public Resources getResources() {
		
		return resources;
	}

	/*@Override
	public void added() {
		bodyA.addForceId(id);
		bodyB.addForceId(id);

	}

	@Override
	public void removing() {
		bodyA.removeForceId(id);
		bodyB.removeForceId(id);
	}*/

}
