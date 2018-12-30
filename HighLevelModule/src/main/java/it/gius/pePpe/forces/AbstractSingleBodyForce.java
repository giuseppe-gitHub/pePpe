package it.gius.pePpe.forces;

import it.gius.pePpe.Resources;
import it.gius.pePpe.data.physic.Body;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public abstract class AbstractSingleBodyForce implements IForce {

	public short id;
	
	public Body body;
	public Vec2 localBodyPoint;
	
	protected Resources resources;
	
	
	public AbstractSingleBodyForce(Body body, Vec2 localPoint) {
		
		if(!body.containsLocal(localPoint))
			throw new IllegalArgumentException("localPoint not contained in body");
		
		this.localBodyPoint = new Vec2();
		this.localBodyPoint.set(localPoint);
		this.body = body;
		
		resources = new Resources();
		resources.bodiesIds = new short[1];
		resources.bodiesIds[0] = body.globalId;
		resources.numBodies = 1;
	}

	@Override
	public boolean isConsistent() {
		return (body.containsLocal(localBodyPoint));
	}
	
	protected abstract Vec2 calcForce(float time);
	
	
	protected Vec2 globalPoint = new Vec2();
	
	@Override
	public void apply(float time) {
		
		Transform.mulToOut(body.transform,localBodyPoint,globalPoint);
		
		Vec2 force = calcForce(time);
		
		body.applyForce(force, globalPoint);
	}
	
	
	
	@Override
	public void setId(short id) {
		this.id = id;

	}

	@Override
	public short getId() {
		return id;
	}
	
	
	@Override
	public Resources getResources() {
		return resources;
	}

	/*@Override
	public void added() {
		body.addForceId(id);
	}

	@Override
	public void removing() {
		body.removeForceId(id);
	}*/

}
