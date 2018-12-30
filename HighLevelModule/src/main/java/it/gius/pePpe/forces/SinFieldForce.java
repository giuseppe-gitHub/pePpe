package it.gius.pePpe.forces;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.Resources;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyList;

public class SinFieldForce implements IForce {

	
	public short id;
	public float omega = 1;
	public float amplitude = 1;
	public float offset = 1;
	public Vec2 directionNormal = new Vec2(1,0);
	private BodyList bodyList;
	
	private Resources resources;
	
	public SinFieldForce(BodyList bodyList) {
		this.bodyList = bodyList;
		
		this.resources = new Resources();
		resources.numBodies = 0;
	}
	
	@Override
	public boolean isConsistent() {
		return true;
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
		
	}
	
	@Override
	public void removing() {
		
	}*/
	
	private Vec2 force = new Vec2();
	
	@Override
	public void apply(float time) {

		
		Body body = bodyList.firstBody;
		
		float forceMagnitude = amplitude*MathUtils.sin(omega*time) +offset;
		
		
		force.set(directionNormal);
		force.mulLocal(forceMagnitude);
		
		while(body != null)
		{
			body.applyForce(force);
			body = body.next;
		}
	}

}
