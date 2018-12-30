package it.gius.pePpe.forces;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.physic.Body;

public class SpringBodyForce extends AbstractDoubleBodyForce{

	public Object userData = null;
	
		private float k;
	private float l;
	private float zeta;
	
	
	private float damping;
	private float criticDamping;
	
	public SpringBodyForce(Body bodyA, Vec2 localBodyPointA,
			Body bodyB, Vec2 localBodyPointB, SpringInit springInit) {
		super(bodyA,localBodyPointA,bodyB,localBodyPointB);
				
		this.k = springInit.k;
		this.l = springInit.l;
		this.zeta = springInit.zeta;


		this.criticDamping = 2*MathUtils.sqrt(k * (bodyA.mass + bodyB.mass));

		this.damping = this.zeta*this.criticDamping;
	}
	
	
	
	private Vec2 vecLenght = new Vec2();
	private Vec2 velocityA = new Vec2();
	private Vec2 velocityB = new Vec2();
	
	private Vec2 force = new Vec2();
	
	@Override
	public Vec2 calcForce(float time) {
		
		
		//vecLenght.set(globalPointA);
		//vecLenght.subLocal(globalPointB);
		vecLenght.x = globalPointA.x - globalPointB.x;
		vecLenght.y = globalPointA.y - globalPointB.y;

		//float currLenght = vecLenght.normalize();

		float x = vecLenght.normalize() -l;

		bodyA.getPointVelocity(globalPointA, velocityA);
		bodyB.getPointVelocity(globalPointB, velocityB);

		//velocityA.subLocal(velocityB);
		velocityA.x -= velocityB.x;
		velocityA.y -= velocityB.y;
		
		//float velMagnitude = Vec2.dot(vecLenght, velocityA);
		float velMagnitude = vecLenght.x * velocityA.x + vecLenght.y * velocityA.y;


		float forceMagnitude = -k*x -damping*velMagnitude;

		//force.set(vecLenght);
		//force.mulLocal(forceMagnitude);
		force.x = vecLenght.x *forceMagnitude;
		force.y = vecLenght.y * forceMagnitude;

		return force;

	}


}
