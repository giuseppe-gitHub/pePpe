package it.gius.pePpe.forces;

import it.gius.pePpe.data.physic.Body;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

public class SpringPointForce extends AbstractSingleBodyForce{

	
	public Object userData = null;
	
	public Vec2 point;

	protected float criticDamping;


	/**
	 * spring coefficient
	 */
	protected float k;

	/**
	 * rest lenght
	 */
	private float l;
	private float zeta;
	protected float damping;


	public SpringPointForce(Vec2 point, Body body, Vec2 localBodyPoint,SpringInit init) {
		super(body,localBodyPoint);

		
		this.point = new Vec2();
		this.point.set(point);
		
		this.k = init.k;
		this.l = init.l;
		this.zeta = init.zeta;


		this.criticDamping = 2*MathUtils.sqrt(k * body.mass);

		this.damping = this.zeta*this.criticDamping;
	}


	/*public SpringPointForce(Vec2 point, SpringInit init) {

		this.point = new Vec2();
		this.point.set(point);
		this.localBodyPoint = new Vec2();
		this.k = init.k;
		this.l = init.l;
		this.zeta = init.zeta;

		this.criticDamping = 0;
		this.damping = 0;
	}*/

	public Body getBody()
	{
		return body;
	}

	public Vec2 getLocalBodyPoint()
	{
		return localBodyPoint;
	}


	

	private Vec2 force = new Vec2();

	private Vec2 vecLenght = new Vec2();

	private Vec2 velocity = new Vec2();


	@Override
	public Vec2 calcForce(float time) {


		//vecLenght.set(point);
		//vecLenght.subLocal(globalBodyPoint);
		vecLenght.x = point.x - globalPoint.x;
		vecLenght.y = point.y - globalPoint.y;

		//float currLenght = vecLenght.normalize();

		float x = vecLenght.normalize() -l;

		body.getPointVelocity(globalPoint, velocity);

		//float velMagnitude = Vec2.dot(vecLenght, velocity);
		float velMagnitude = vecLenght.x * velocity.x + vecLenght.y * velocity.y;


		float forceMagnitude = k*x -damping*velMagnitude;

		//force.set(vecLenght);
		//force.mulLocal(forceMagnitude);
		force.x = vecLenght.x * forceMagnitude;
		force.y = vecLenght.y * forceMagnitude;

		return force;

	}

}
