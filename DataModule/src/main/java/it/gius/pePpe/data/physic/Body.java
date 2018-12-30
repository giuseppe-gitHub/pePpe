package it.gius.pePpe.data.physic;


import java.util.Iterator;

//import it.gius.data.structures.HashSetShort;
import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;
import it.gius.pePpe.MathUtils;
import it.gius.pePpe.SystemCostants;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;


public class Body implements Iterable<PhysicShape>{

	private static final int BINDS_SIZE = 10;
	
	
	public short globalId;
	
	public Transform transform;
	private BodyPosition bodyPosition;
	
	private Vec2 localCenterOfMass; 
	
	
	public float radius = 0;
	public float radius2 = 0;
	//public PhysicData phData;
	public float mass;
	public float inv_mass;
	
	public float Iz;
	public float inv_Iz;
	
	public Object userData;
	
	public boolean fixed = false;
	
	public float linearDamping;
	public float angularDamping;
	
	public Vec2 linearVelocity;
	public float angularVelocity;
	
	public IdList<PhysicShape> phShapeList;
	
	@Override
	public Iterator<PhysicShape> iterator() {
		return phShapeList.iterator();
	}
	
	//HashSetShort forcesId = new HashSetShort();
	//private GlobalData globalData;
	//private List<BodyUpdateListener> listners;
	

	private Vec2 force = new Vec2();
	private float torque = 0;
	
	public Body next = null;
	public Body prev = null;
	
	
	private Vec2 pool1 = new Vec2();
	//private Vec2 pool2 = new Vec2();
	
	 Body(BodyInit bi) {
		
		
		if(bi.linearDamping < 0 || bi.linearDamping > 1)
			throw new IllegalArgumentException("Linear damping must be:  0 <= c <= 1");
		
		if(bi.angularDamping <0 || bi.linearDamping > 1)
			throw new IllegalArgumentException("Angular damping must be: 0 <= c <= 1");
		
		transform  = new Transform();
		bodyPosition = new BodyPosition();
		bodyPosition.globalCenter.set(bi.globalOrigin);
		//check if start with an angle different from 0 create problems
		//bodyPosition.angle = 0;
		bodyPosition.angle = bi.angle;
		
		linearVelocity = new Vec2();
		if(bi.startLinearVelocity != null)
			linearVelocity.set(bi.startLinearVelocity);

		angularVelocity = bi.startAngularVelocity;
		
		transform.position.set(bi.globalOrigin);
		//transform.R.set(0);
		transform.R.set(bi.angle);
		
		localCenterOfMass = new Vec2();
		localCenterOfMass.setZero();
		
		mass = inv_mass = 0;
		Iz = inv_Iz = 0;
		
		this.fixed = bi.fixed;
		
		this.linearDamping = bi.linearDamping;
		this.angularDamping = bi.angularDamping;
		
		phShapeList = new IdDoubleArrayList<PhysicShape>(PhysicShape.class, BINDS_SIZE, 4);
		
		//listners = new ArrayList<BodyUpdateListener>();

	}
	
	/*public void addListener(BodyUpdateListener listener)
	{
		listners.add(listener);
	}
	
	public void removeListener(BodyUpdateListener listener)
	{
		listners.remove(listener);
	}*/
	 
	 @Override
	public int hashCode() {
		return MathUtils.cuttableHashCode(globalId);
	}
	
	
	public void cloneBodyInit(BodyInit bodyInit)
	{
		bodyInit.angularDamping = this.angularDamping;
		bodyInit.linearDamping = this.linearDamping;
		bodyInit.startAngularVelocity = this.angularVelocity;
		bodyInit.startLinearVelocity.set(this.linearVelocity);
	}
	
	
	public void clearForces()
	{
		force.setZero();
		torque = 0;
	}
	
	public void applyForce(Vec2 force)
	{
		this.force.addLocal(force);
	}
	
	public void applyTorque(float torque)
	{
		this.torque += torque;
	}
	
	public void applyForce(Vec2 force, Vec2 applyingGlobalPoint)
	{
		
		//this.force.addLocal(force);
		this.force.x += force.x;
		this.force.y += force.y;
		
		Vec2 r = pool1;
		
		//r.set(applyingGlobalPoint);
		//r.subLocal(bodyPosition.globalCenter);
		r.x = applyingGlobalPoint.x - bodyPosition.globalCenter.x;
		r.y = applyingGlobalPoint.y - bodyPosition.globalCenter.y;
		
		//this.torque += Vec2.cross(r, force);
		torque += r.x*force.y - r.y * force.x;
		
	}
	
	
	public void getPointVelocity(Vec2 globalPoint, Vec2 outVelocity)
	{
		
		Vec2 wr = pool1;
		//Vec2 r = pool2;
		
		//outVelocity.set(linearVelocity);
		
		//r.set(globalPoint);
		//r.subLocal(bodyPosition.globalCenter);
		//r.x = globalPoint.x - bodyPosition.globalCenter.x;
		//r.y = globalPoint.y - bodyPosition.globalCenter.y;
		
		//Vec2.crossToOut(angularVelocity, r, wr);
		wr.x = -angularVelocity* (globalPoint.y - bodyPosition.globalCenter.y);//r.y;
		wr.y = angularVelocity *(globalPoint.x - bodyPosition.globalCenter.x); //r.x;
		
		//outVelocity.addLocal(wr);
		outVelocity.x = linearVelocity.x + wr.x;
		outVelocity.y = linearVelocity.y + wr.y;
	}
	
	public boolean containsLocal(Vec2 localPoint)
	{
		
		PhysicShape[] phShapes = phShapeList.toLongerArray();
		
		int size = phShapeList.size();
		
		for(int i=0; i<size;i++)
		{
			if(phShapes[i].shape.contains(localPoint))
				return true;
		}
		
		return false;
	}
	
	
	public boolean containsGlobal(Vec2 globalPoint)
	{
		Vec2 localPoint = pool1;
		
		Transform.mulTransToOut(transform, globalPoint, localPoint);
		
		return containsLocal(localPoint);
	}
	
	
	public Vec2 getForce() {
		return force;
	}
	
	
	public float getTorque() {
		return torque;
	}

	
	/*public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}*/
	
	
	public float getAngle()
	{
		return bodyPosition.angle;
	}
	
	public Vec2 getGlobalCenter()
	{
		return bodyPosition.globalCenter;
	}
	
	public Vec2 getLocalCenter()
	{
		return localCenterOfMass;
	}
	
	
	
	/*public void setAngle(float angle)
	{
		bodyPosition.angle = angle;
		synchronizeTransform();
	}*/
	
	/*public void setGlobalCenter(Vec2 center)
	{
		bodyPosition.globalCenter.set(center);
		synchronizeTransform();
	}
	
	public void addToGlobalCenter(Vec2 toAdd)
	{
		bodyPosition.globalCenter.addLocal(toAdd);
		synchronizeTransform();
	}
	
	public void mulGlobalCenter(float multiplier)
	{
		bodyPosition.globalCenter.mulLocal(multiplier);
		synchronizeTransform();
	}*/
	
	/**
	 *  read-only field outside engine
	 * @return
	 */
	public BodyPosition getBodyPosition() {
		return bodyPosition;
	}
	
	
	
	
	
	/*public void addForceId(short id)
	{
		forcesId.add(id);
	}
	
	public void removeForceId(short id)
	{
		forcesId.remove(id);
	}*/

	
	short addPhysicShape(PhysicShape phShape)
	{
	
		/*if(bind.body != this)
			bind.body = this;*/
		
		short id = phShapeList.add(phShape);
				
		recomputePhysicData();
		recomputeRadius();
		
		/*for(BodyUpdateListener listner : listners)
			listner.added(node);*/
		
		return id;
	}

	
	boolean removePhysicShape(PhysicShape phShape)
	{
		
		boolean removed = false;
		PhysicShape pNode = phShapeList.get(phShape.localBodyId);
		if( pNode == phShape)
		{
			removed = true;
			
			phShapeList.remove(pNode.localBodyId);
			
			recomputePhysicData();
			recomputeRadius();
			
			/*for(BodyUpdateListener listener : listners)
				listener.removed(bNode);*/
			
		}
		
		return removed;
	}	
	

	
	public void synchronizeTransform()
	{
		Vec2 originToCenter = pool1;
		
		Mat22 R = transform.R;
		R.set(bodyPosition.angle);
		
		//transform.R.mulToOut(localCenterOfMass, originToCenter);
		originToCenter.x = R.col1.x * localCenterOfMass.x + R.col2.x * localCenterOfMass.y;
		originToCenter.y = R.col1.y * localCenterOfMass.x + R.col2.y * localCenterOfMass.y;
		
		//transform.position.set(bodyPosition.globalCenter);
		//transform.position.subLocal(originToCenter);
		transform.position.x = bodyPosition.globalCenter.x - originToCenter.x;
		transform.position.y = bodyPosition.globalCenter.y - originToCenter.y;
	}
	
	
	private void recomputeRadius()
	{
		PhysicShape[] phShapes = phShapeList.toLongerArray();
		int size = phShapeList.size();
		
		Vec2 difference = pool1;
		
		float max = 0;
		
		for(int i=0; i< size; i++)
		{
			difference.set(phShapes[i].shape.centroid);
			difference.subLocal(localCenterOfMass);
			
			float current = difference.length() + phShapes[i].shape.radius;
			
			if(current > max)
				max = current;
		}
		
		radius = max;
		
		radius2 =  radius*radius;
	}
	
	
	private void recomputePhysicData()
	{
		
		mass = inv_mass = 0;
		Iz = inv_Iz = 0;
		
		localCenterOfMass.setZero();
		
		
		PhysicData phData = new PhysicData();
		
		int size = phShapeList.size();
		
		PhysicShape[] phShapes = phShapeList.toLongerArray();
		
		for(int i=0; i< size; i++)
		{
			PhysicShape phShape = phShapes[i];
			
			phShape.getPhysicData(phData);
			
			mass += phData.mass;
			
			phData.massCenter.mulLocal(phData.mass);
			localCenterOfMass.addLocal(phData.massCenter);
			
			//all the Izs must be relative to the origin
			Iz += phData.Iz;
			
		}
		
		
		if(mass > SystemCostants.EPSILON)
		{
			inv_mass = 1.0f/mass;
			localCenterOfMass.mulLocal(inv_mass);
		}
		else
		{
			inv_mass = mass = 1.0f;
			localCenterOfMass.setZero();
		}
		
		
		if(Iz > 0)
		{
			//parallel axis theorem
			Iz = Iz - mass*localCenterOfMass.lengthSquared();
			inv_Iz = 1.0f/Iz;
		}
		else
		{
			Iz = inv_Iz = 0;
		}
		
		Transform.mulToOut(transform, localCenterOfMass, bodyPosition.globalCenter);
		
		if(this.fixed)
		{
			mass = inv_mass = 0;
			Iz = inv_Iz = 0;
		}
		
		//TODO add change linear velocity in recomputeMassData
	}
}
