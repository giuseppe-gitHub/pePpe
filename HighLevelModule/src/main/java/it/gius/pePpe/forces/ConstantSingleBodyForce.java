package it.gius.pePpe.forces;

import it.gius.pePpe.data.physic.Body;

import org.jbox2d.common.Vec2;

public class ConstantSingleBodyForce extends AbstractSingleBodyForce {

	public Vec2 force;
	
	public ConstantSingleBodyForce(Body body, Vec2 localPoint, Vec2 force) {
		super(body,localPoint);
		
		this.force = new Vec2();
		this.force.set(force);
	}
	
	@Override
	protected Vec2 calcForce(float time) {
		return force;
	}

}
