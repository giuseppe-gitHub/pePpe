package it.gius.pePpe.forces;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.Resources;
import it.gius.pePpe.data.physic.Body;

public class AttractionForce implements IForce{

	private Body bodyA;
	private Body bodyB;

	private float gamma;

	public short id;

	@Override
	public short getId() {
		return id;
	}

	@Override
	public void setId(short id) {
		this.id = id;
	}
	
	private Resources resources;

	public AttractionForce(Body bodyA, Body bodyB,float gamma) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
		this.gamma = gamma;

		resources = new Resources();
		resources.bodiesIds = new short[2];
		resources.bodiesIds[0] = bodyA.globalId;
		resources.bodiesIds[1] = bodyB.globalId;
		resources.numBodies = 2;
	}
	
	@Override
	public boolean isConsistent() {
		
		return true;
	}
	
	/*@Override
	public void added() {
		this.bodyA.addForceId(id);
		this.bodyB.addForceId(id);
		
	}
	
	
	@Override
	public void removing() {
		bodyA.removeForceId(id);
		bodyB.removeForceId(id);
		
	}*/
	
	@Override
	public Resources getResources() {
		return resources;
	}


	private Vec2 forceToApply = new Vec2();

	private Vec2 pool1 = new Vec2();

	@Override
	public void apply(float time) {

		Vec2 difference = pool1;

		difference.set(bodyA.getGlobalCenter());
		difference.subLocal(bodyB.getGlobalCenter());

		/*if(bodyA.radius2 + bodyB.radius2 + 2*bodyA.radius*bodyB.radius
				+SystemCostants.SQRT_EPSILON < difference.lengthSquared() )
		{*/


			float r2 = difference.lengthSquared();

			float forceMagnitude = gamma*bodyA.mass*bodyB.mass/r2;

			Vec2 normal = difference;

			float lenght = MathUtils.sqrt(r2);

			normal.x = normal.x/lenght;
			normal.y = normal.y/lenght;

			forceToApply.set(normal);

			forceToApply.mulLocal(forceMagnitude);

			bodyB.applyForce(forceToApply);

			forceToApply.negateLocal();

			bodyA.applyForce(forceToApply);
		/*}
		else
		{
			
		}*/

	}


}
