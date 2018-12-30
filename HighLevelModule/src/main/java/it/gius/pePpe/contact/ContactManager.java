package it.gius.pePpe.contact;

import org.jbox2d.common.Transform;

import it.gius.data.structures.HashMap;
import it.gius.data.structures.IdList;
import it.gius.data.structures.PoolStack;
//import it.gius.pePpe.aabb.IAABBManager;
import it.gius.pePpe.collision.ICollision;
import it.gius.pePpe.data.BindPair;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactPoint;

public class ContactManager {

	private boolean warmStart = true;
	private boolean caching = true;
	private boolean removeOldPointsInUpdate = false;
	private HashMap<BindPair, Contact> contactMap;
	private float translationThreshold = 0.4f;

	//private static float defaultMaxDistance = 4;

	private static final int defaultStackSize = 10;

	//private IAABBManager aabbManager;
	private IdList<BindAABBNode> shapesList;

	private ICollision collision;

	private PoolStack<Contact> stackContacts;
	private PoolStack<BindPair> stackPairs;

	public ContactManager(/*IAABBManager aabbManager, IDistance2 distance,*/
			ICollision collision, /*IdList<BindAABBNode> shapesList,*/ ContactManagerInit init) {
		//this.aabbManager = aabbManager;
		//this.shapesList = shapesList;
		if(init.translationThreshold < 0)
			throw new IllegalArgumentException("translation threshold cant be negative");
		

		this.contactMap = new HashMap<BindPair, Contact>(BindPair.class, Contact.class);

		this.collision = collision;//new Collision(distance, init.maxContactDistance);

		this.caching = init.caching;
		this.translationThreshold = init.translationThreshold;
		this.warmStart = init.warmStart;
		this.removeOldPointsInUpdate = init.removeOldPointsInUpdate;


		int stackSize = Math.max(defaultStackSize,init.poolStackSize);
		Contact[] contacts = new Contact[stackSize];

		for(int i=0; i<stackSize; i++)
			contacts[i] = new Contact(this.caching);

		stackContacts = new PoolStack<Contact>(Contact.class, stackSize, contacts);

		BindPair[] pairs = new BindPair[stackSize];
		for(int i=0; i<stackSize; i++)
			pairs[i] = new BindPair();

		stackPairs = new PoolStack<BindPair>(BindPair.class, stackSize, pairs);

	}

	private boolean inited = false;
	public void init(IdList<BindAABBNode> shapesList)
	{
		if(inited)
			return;

		this.shapesList = shapesList;

		inited = true;
	}

	private BindPair bindPairLocal;

	private BindPair[] toDeleteContacts = new BindPair[10];
	private int toDeleteSize = 0;


	private void resizeToDelete()
	{
		BindPair[] newArray = new BindPair[toDeleteContacts.length*2];

		for(int i=0; i<toDeleteContacts.length; i++)
			newArray[i] = toDeleteContacts[i];

		toDeleteContacts = newArray;
	}


	public void updateContacts(AABBPair[] pairs, int aabbSize)
	{
		/*AABBPair[] pairs= aabbManager.getPairs();
		int aabbSize = aabbManager.getPairsNumber();*/

		for(int i=0; i<aabbSize; i++){
			BindAABBNode nodeA = shapesList.get(pairs[i].idShapeA);
			BindAABBNode nodeB = shapesList.get(pairs[i].idShapeB);

			if(nodeA.bind.body != nodeB.bind.body){
				if(bindPairLocal == null)
				{
					bindPairLocal = stackPairs.pop();//new BindPair(nodeA.bind, nodeB.bind);
					if(bindPairLocal == null)
						bindPairLocal = new BindPair(nodeA.bind,nodeB.bind);
					else
						bindPairLocal.set(nodeA.bind, nodeB.bind);
				}
				else
					bindPairLocal.set(nodeA.bind, nodeB.bind);

				Contact oldContact = contactMap.get(bindPairLocal);

				if(oldContact == null){
					Contact newContact = generateNewContact(bindPairLocal);

					if(newContact != null && newContact.manifold.size > 0){
						newContact.neww = true;
						contactMap.put(bindPairLocal, newContact);
						bindPairLocal = null;
					}
					else{
						if(newContact != null)
							stackContacts.tryPush(newContact);
					}
				}
				else{
					updateContact(bindPairLocal,oldContact); //no need to put it back in the map
					if(oldContact.manifold.size > 0)
						oldContact.neww = true;
					else{
						BindPair pairToStack = contactMap.find(bindPairLocal);
						if(pairToStack != null){
							Contact removedContact = contactMap.remove(bindPairLocal);
							stackContacts.tryPush(removedContact);
							if(pairToStack != bindPairLocal)
								stackPairs.tryPush(pairToStack);
						}
					}
				}
			}
		}

		BindPair[] pairArray = contactMap.keyLongerArray();
		Contact[] contactArray = contactMap.valuesLongerArray();
		int mapSize = contactMap.size();

		toDeleteSize = 0;

		for(int i=0; i<mapSize; i++){

			if(!contactArray[i].neww){
				boolean toKeep = mantain(contactArray[i]);
				if(!toKeep){
					if(toDeleteSize >= toDeleteContacts.length)
						resizeToDelete();
					toDeleteContacts[toDeleteSize] = pairArray[i];
					toDeleteSize++;
				}
				else
				{
					Transform transformA = contactArray[i].bindA.body.transform;
					Transform transformB = contactArray[i].bindB.body.transform;
					for(int j=0; j<contactArray[i].manifold.size; j++)					
						updateGlobalPoint(transformA, transformB, contactArray[i].manifold.points[j]);
				}
			}
			else
			{
				contactArray[i].neww = false;
				Transform transformA = contactArray[i].bindA.body.transform;
				Transform transformB = contactArray[i].bindB.body.transform;
				for(int j=0; j<contactArray[i].manifold.size; j++)					
					updateGlobalPoint(transformA, transformB, contactArray[i].manifold.points[j]);

			}
		}

		for(int i=0; i<toDeleteSize; i++){
			Contact removedContact =contactMap.remove(toDeleteContacts[i]);
			stackContacts.tryPush(removedContact);
			stackPairs.tryPush(toDeleteContacts[i]);
		}

	}



	private Contact generateNewContact(BindPair bindPair)
	{	
		//Contact contact = new Contact(caching); 
		Contact contact = stackContacts.pop();
		
		if(contact == null)
			contact = new Contact(caching);
		else
			contact.cache.clear();

		collision.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, contact.cache, contact.manifold);

		contact.bindA = bindPair.bindA;
		contact.bindB = bindPair.bindB;
		return contact;
	}


	private ContactManifold manifoldPool = new ContactManifold();
	private boolean[] added = new boolean[2]; //manifoldPool point
	private boolean[] updated = new boolean[2]; //manifoldOut point

	private void updateContact(BindPair bindPair,Contact contact)
	{
		collision.collide(bindPair.bindA.phShape.shape, bindPair.bindA.body.transform,
				bindPair.bindB.phShape.shape, bindPair.bindB.body.transform, contact.cache, manifoldPool);

		if(manifoldPool.size == 0)
		{
			contact.manifold.size = 0;
			return;
		}

		updated[0] = false;
		updated[1] = false;
		ContactManifold manifoldOut = contact.manifold;
		//(1) check pointsID and subsitute corrisponent points (warm start here)
		for(int i=0; i<manifoldPool.size; i++)
		{
			added[i] = false;
			int pos = manifoldOut.getPointPosition(manifoldPool.points[i].pointID);
			if(pos >= 0)
			{
				manifoldOut.points[pos].set(manifoldPool.points[i]);
				//warm start here //TODO warmStart
				added[i] = true;
				updated[pos] = true;
			}
		}

		if(!removeOldPointsInUpdate)
		{
			//(2) manifoldOut.refreshContactPoints(); //to delete old points that doesn't match the criteria
			int initSize = manifoldOut.size;
			for(int i = initSize-1; i>=0; i--)
			{
				if(!updated[i])
					manifoldOut.refreshSingleContactPoint(i, collision.getMaxContactDistance(), translationThreshold,
							bindPair.bindA.body.transform, bindPair.bindB.body.transform);
			}
		}
		else
		{
			//(2 alternative) delete all contact points not added in this step
			int initSize = manifoldOut.size;
			for(int i= initSize-1; i >= 0; i--)
			{
				if(!updated[i])
					manifoldOut.removePoint(i);
			}
		}

		//(3) add all remaining points don't added on (1)
		for(int i=0; i<manifoldPool.size; i++)
		{
			if(!added[i])
			{
				int pos = addPoint(manifoldOut,manifoldPool.points[i],updated);
				updated[pos] = true;
			}
		}
	}

	private int addPoint(ContactManifold manifoldOut, ContactPoint pointToAdd, boolean[] updated)
	{
		int insertIndex;

		if(manifoldOut.size == manifoldOut.type.maxSize)
		{
			insertIndex = whereToInsertPoint(manifoldOut.size, updated);
		}
		else
		{
			insertIndex = manifoldOut.size;
			manifoldOut.size++;
		}

		manifoldOut.points[insertIndex].set(pointToAdd);

		return insertIndex;
	}

	private int whereToInsertPoint(int size, boolean[] updated)
	{
		for(int i=0;i<size; i++)
			if(!updated[i])
				return i;

		throw new RuntimeException("Wrong add point");
	}


	private boolean mantain(Contact contact)
	{
		//alternative policy: do not maintain point not suggested by the broadphase
		//return false;

		Bind bindA = contact.bindA;
		Bind bindB = contact.bindB;
		if(bindA.removed || bindB.removed)
			return false;

		// contact.manifold.refreshContactPoints(); //to delete old points that doesn't match the criteria
		contact.manifold.refreshAllContactPoints(collision.getMaxContactDistance(), translationThreshold,
				bindA.body.transform, bindB.body.transform);

		if(contact.manifold.size == 0)
			return false;

		return true;
	}
	
	private void updateGlobalPoint(Transform transformA, Transform transformB, ContactPoint contactPoint)
	{
		if(!contactPoint.pointOnShapeB)
		{
			Transform.mulToOut(transformA, contactPoint.localPoint, contactPoint.globalPoint);
			Transform.mulToOut(transformB, contactPoint.otherLocalPoint, contactPoint.otherGlobalPoint);
		}
		else
		{
			Transform.mulToOut(transformB, contactPoint.localPoint, contactPoint.globalPoint);
			Transform.mulToOut(transformA, contactPoint.otherLocalPoint, contactPoint.otherGlobalPoint);
		}
	}


	public boolean isWarmStart() {
		return warmStart;
	}

	public void setWarmStart(boolean warmStart) {
		this.warmStart = warmStart;
	}

	public boolean isCaching() {
		return caching;
	}


	/*public float getMaxContactDistance() {
		return collision.getMaxContactDistance();
	}

	public void setMaxContactDistance(float maxContactDistance) {
		collision.setMaxContactDistance(maxContactDistance);
	}*/

	public float getTranslationThreshold() {
		return translationThreshold;
	}

	public void setTranslationThreshold(float translationThreshold) {
		this.translationThreshold = translationThreshold;
	}

	public int size()
	{
		return contactMap.size();
	}

	public Contact[] toLongerArray()
	{
		return contactMap.valuesLongerArray();
	}


}
