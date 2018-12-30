package it.gius.pePpe.forces;

import java.util.Iterator;

import org.jbox2d.common.Vec2;

import it.gius.data.structures.HashMap;
import it.gius.data.structures.HashSetShort;
import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;
import it.gius.pePpe.Resources;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyList;

public class ForceManager implements Iterable<IForce>{


	private IdList<IForce> forceList;
	private BodyList bodyList;

	private Vec2 gravity = new Vec2(0,1);
	private float gravityModule = 1;


	public HashMap<Short, HashSetShort> bodyForcesMap;

	public ForceManager() {
		forceList = new IdDoubleArrayList<IForce>(IForce.class,20,4);

		bodyForcesMap = new HashMap<Short, HashSetShort>(Short.class, HashSetShort.class);
	}

	public void init(BodyList bodyList)
	{
		this.bodyList = bodyList;
	}

	public Vec2 getGravity() {
		return gravity;
	}

	public float getGravityModule() {
		return gravityModule;
	}

	public void setGravity(Vec2 gravity) {
		this.gravity.set(gravity);
		this.gravityModule = gravity.length();
	}

	public void setGravity(float x, float y)
	{
		this.gravity.x = x;
		this.gravity.y = y;
		this.gravityModule = gravity.length();
	}


	public void addForce(IForce force)
	{
		forceList.add(force);
		//force.added();
		Resources resources = force.getResources();

		for(int i=0; i<resources.numBodies; i++)
		{
			HashSetShort forcesIds = bodyForcesMap.get(resources.bodiesIds[i]);
			if(forcesIds == null)
			{
				forcesIds = new HashSetShort();
				forcesIds.add(force.getId());
				bodyForcesMap.put(resources.bodiesIds[i], forcesIds);
			}
			else
			{
				forcesIds.add(force.getId());
			}
		}
	}

	public void removeForce(short id)
	{
		//IForce thisForce = forceList.get(id);

		//if(thisForce != null)
		//{
			//thisForce.removing();
		IForce force = forceList.remove(id);
		if(force != null)
			removeForceReferences(force,(short)-1);
		//}
	}

	public void removeForce(IForce force)
	{
		//IForce thisForce = forceList.get(force.getId());

		//if(thisForce == force)
		//{
			//thisForce.removing();
		IForce thisForce = forceList.remove(force.getId());
		if(thisForce == force)
			removeForceReferences(force,(short)-1);
		//}
	}

	private void removeForceReferences(IForce force, short bodyIdException)
	{
		
		if(force != null) //force was present
		{
			Resources resources = force.getResources();
			for(int i=0; i<resources.numBodies; i++)
			{
				if(resources.bodiesIds[i] != bodyIdException)
				{
					HashSetShort forcesIds = bodyForcesMap.get(resources.bodiesIds[i]);
					if(forcesIds == null)
						throw new IllegalAccessError();

					forcesIds.remove(force.getId());
				}
			}
		}
	}

	public void removingBody(short bodyId)
	{
		//TODO to test removingBody

		HashSetShort forcesIds = bodyForcesMap.get(bodyId);

		if(forcesIds != null)
		{

			Short[] arrayForcesIds = forcesIds.elements;
			for(int i=0; i<forcesIds.size();i++)
			{
				//IForce thisForce = forceList.get(arrayForcesIds[i]);
				//if(thisForce != null)
				IForce thisForce = forceList.remove(arrayForcesIds[i]);
				if(thisForce != null)
					removeForceReferences(thisForce, bodyId);
			}

			bodyForcesMap.remove(bodyId);
		}
	}

	private short[] poolArray = new short[10];

	public void updatingBody(short bodyId)
	{
		//TODO to test updatingBody

		HashSetShort forcesIds = bodyForcesMap.get(bodyId);

		if(forcesIds != null)
		{

			Short[] arrayForcesIds = forcesIds.elements;
			int size = forcesIds.size();

			if(size > poolArray.length)
				poolArray = new short[size];

			int toDeleteNum = 0;
			short[] toDelete = poolArray; 
			for(int i=0; i<size;i++)
			{
				IForce thisForce = forceList.get(arrayForcesIds[i]);
				if(thisForce != null && !thisForce.isConsistent())
				{
					forceList.remove(arrayForcesIds[i]);
					removeForceReferences(thisForce, bodyId);
					toDelete[toDeleteNum] = arrayForcesIds[i];
					toDeleteNum++;
				}
			}

			for(int i=0; i<toDeleteNum;i++)
				forcesIds.remove(toDelete[i]);


			//bodyForcesMap.remove(bodyId);
		}

	}

	@Override
	public Iterator<IForce> iterator() {
		return forceList.iterator();
	}

	public IdList<IForce> getForceList() {
		return forceList;
	}
	

	/**
	 * Set to zero the numeric result calculated in the previous time step
	 */
	public void clearForces()
	{
		Body currBody = bodyList.firstBody;

		while(currBody != null)
		{
			currBody.clearForces();

			currBody = currBody.next;
		}

	}

	private Vec2 pool1 = new Vec2();

	public void applyForces(float time)
	{
		int size = forceList.size();

		IForce[] forces = forceList.toLongerArray();

		for(int i=0; i<size; i++)
			forces[i].apply(time);

		Body currBody = bodyList.firstBody;

		Vec2 gravityForce = pool1;

		while(currBody != null)
		{
			gravityForce.x = gravity.x *currBody.mass;
			gravityForce.y = gravity.y *currBody.mass;
			currBody.applyForce(gravityForce);

			currBody = currBody.next;
		}
	}

}
