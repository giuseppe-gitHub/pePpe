package it.gius.pePpe.forces;

import it.gius.pePpe.Resources;
import it.gius.pePpe.data.physic.Body;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class MouseForce implements IForce{


	public short id;
	
	public Body body;
	public Vec2 localBodyPoint;
	
	public Vec2 point;
	
	protected Resources resources;
	
	private static final float zeta = 0.7f;
	private static final float minKcoef = 0.28f;
	
	private float k;
	private float criticDamping;
	private float damping;


	public MouseForce() {
		this.localBodyPoint = new Vec2();
		this.point = new Vec2();

		resources = new Resources();
		resources.bodiesIds = new short[1];
		resources.numBodies = 1;
	}
	

	public MouseForce(Vec2 mousePoint, Body body, float gravityModule) {
		this();
		
		this.point.set(mousePoint);
		this.init( body, gravityModule);
	}

	public MouseForce(Vec2 mousePoint, Body body) {
		this(mousePoint,body,0f);
	}

	private boolean inited = false;
	
	@Override
	public boolean isConsistent() {
		
		if(!inited)
			throw new IllegalAccessError("Force already inited");
		
		return body.containsLocal(localBodyPoint);
	}
	
	public void clear()
	{
		inited = false;
		if(this.body != null)
		{
			//this.body.removeForceId(id);
			resources.bodiesIds[0] = -1;
			this.body = null;
		}
	}
	

	public void init(Body body, float gravityModule)
	{

		if(inited)
			throw new IllegalAccessError("Force already inited");
		
		this.body = body;
		
		resources.bodiesIds[0] = body.globalId;
		
		Transform.mulTransToOut(body.transform,point,this.localBodyPoint);

		if(!body.containsLocal(localBodyPoint))
			throw new IllegalArgumentException("localBodyPoint not contained in body");

		if(gravityModule > 0)
		{
			vecLenght.set(body.getGlobalCenter());
			vecLenght.subLocal(point);
			float distance = vecLenght.length();
			
			float multiplier = ((minKcoef - 1f) / body.radius)*distance +1f;
			
			this.k = multiplier*body.mass*gravityModule;
		}
		else
			this.k = minKcoef*body.mass;

		this.criticDamping = 2*MathUtils.sqrt(k*body.mass);

		this.damping = zeta*this.criticDamping;
		
		inited = true;
	}
	
	@Override
	public short getId() {
		
		return id;
	}
	
	@Override
	public void setId(short id) {
		this.id = id;
		
	}
	

	public Body getBody()
	{
		return body;
	}

	public Vec2 getLocalBodyPoint()
	{
		return localBodyPoint;
	}
	
	
	@Override
	public Resources getResources() {
		return resources;
	}

	private Vec2 force = new Vec2();

	private Vec2 vecLenght = new Vec2();

	private Vec2 velocity = new Vec2();

	private Vec2 globalBodyPoint = new Vec2();

	@Override
	public void apply(float time) {


		Transform.mulToOut(body.transform,localBodyPoint,globalBodyPoint);

		vecLenght.set(point);
		vecLenght.subLocal(globalBodyPoint);

		float currLenght = vecLenght.normalize();
		
		float x = currLenght -2f;

		body.getPointVelocity(globalBodyPoint, velocity);

		
		float forceMagnitude = k*x;


		force.set(vecLenght);
		force.mulLocal(forceMagnitude);

		velocity.mulLocal(-damping);

		force.addLocal(velocity);


		body.applyForce(force, globalBodyPoint);

	}
}
