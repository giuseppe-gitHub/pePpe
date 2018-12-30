package it.gius.pePpe.aabb.sap;


/**
 * @composed - - 1..* it.gius.pePpe.aabb.Pair
 */
import it.gius.data.structures.HashSet;
import it.gius.pePpe.aabb.IAABBPairChange;
import it.gius.pePpe.data.aabb.AABBPair;


/**
 * Contain code from Pierre Terdiman article "Sweep-and-prune"
 * @composed 1 PairHashSet 1 it.gius.pePpe.data.structures.HashSet
 * @depend - - - it.gius.pePpe.aabb.Pair
 * @opt all
 */
public class PairManager {


	//private ItemList<PairNode> oldPairs;
	private static final int DEFAULT_SIZE = 32; 

	private HashSet<AABBPair> hashSet;

	private AABBPair[] keys;
	private int currentKeysPosition;
	
	private  AABBPair pool1 = new AABBPair();
	
	private IAABBPairChange listener = null;
	
	public void setListener(IAABBPairChange listener) {
		this.listener = listener;
	}


	public PairManager() {
		hashSet = new HashSet<AABBPair>(AABBPair.class);

		keys = new AABBPair[DEFAULT_SIZE];
		currentKeysPosition = 0;
		//oldPairs = new ItemList<PairNode>(PairNode.class, DEFAULT_SIZE);
	}
	
	public void clear()
	{
		hashSet.clear();
		currentKeysPosition = 0;
	}

	private void resizeKeys()
	{
		AABBPair[] newKeys = new AABBPair[keys.length*2];

		for(int i=0; i< keys.length;i++)
			newKeys[i] = keys[i];

		keys = newKeys;
	}

	public void addPair(short idA, short idB)
	{
		if(idA == idB)
			throw new IllegalArgumentException();
		
		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}

		AABBPair pair = new AABBPair();
		pair.idShapeA = idA;
		pair.idShapeB = idB;

		AABBPair result = hashSet.add(pair);

		if(/*result.neww*/ result == pair)
		{
			result.neww = true;

			if(currentKeysPosition >= keys.length)
				resizeKeys();

			keys[currentKeysPosition] = result;
			currentKeysPosition++;

			result.inArray = true;
		}

		result.removed = false;
	}

	public void removePair(short idA,short idB)
	{
		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}

		AABBPair pair = pool1;
		pair.idShapeA = idA;
		pair.idShapeB = idB;

		AABBPair result = hashSet.find(pair);

		if(result != null)
		{
			if(!result.inArray)
			{

				if(currentKeysPosition >= keys.length)
					resizeKeys();

				keys[currentKeysPosition] = result;
				currentKeysPosition++;
				
				result.inArray = true;
			}

			result.removed = true;
		}

	}

	public void update()
	{
		
		
		for(int index=0; index< currentKeysPosition; index++)
		{
			AABBPair pair = keys[index];
			keys[index] = null;
			
			assert(pair.inArray);
			
			if(pair.removed)
			{
				if(!pair.neww)
				{
					//callback remove
					if(listener != null)
						listener.removed(pair);
				}
				
				boolean  removed= hashSet.remove(pair);
				assert(removed);
			}
			else
			{
				pair.inArray = false;
				
				if(pair.neww)
				{
					//callback add
					if(listener != null)
						listener.added(pair);
				}
				
				pair.neww = false;
				
			}
		}
		
		currentKeysPosition = 0;
		
	}

	public AABBPair[] getActivePairs()
	{
		return hashSet.elements;
	}
	
	public int getCurrentPairsNumber()
	{
		return hashSet.size();
	}
	/*public void addPair(short idA, short idB)
	{

		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}

		int cantorPair = Pair.cantorPairing(idA, idB);

		Integer id = cantorPairToId.get(cantorPair);

		if(id == null || total.a_list[id] == null)
		{
			Pair pair = new Pair();
			pair.cache = null;
			pair.idShapeA = idA;
			pair.idShapeB = idB;

			PairNode node = new PairNode();
			node.pair = pair;
			node.removed = false;
			node.newlyChanged = true;

			id = total.addItem(node);

			cantorPairToId.put(cantorPair, id);

		}
		else
		{
			PairNode node =  total.a_list[id];
			node.removed = false;
			node.newlyChanged = true;
		}


		if(pairNumber == current.length)
			resizeArrays();

		int freeSlot = nextFree();

		current[freeSlot] = new Pair();
		current[freeSlot].idShapeA = idA;
		current[freeSlot].idShapeB = idB;
		current[freeSlot].cache = null;
	}

	public void removePair(short idA,short idB)
	{
		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}

		int cantorPair = Pair.cantorPairing(idA, idB);
		Integer id = cantorPairToId.get(cantorPair);

		if(id == null || total.a_list[id] == null)
			return;

		total.a_list[id].removed = true;
		total.a_list[id].newlyChanged = true;
	}

	public Pair[] getCurrentActivePairs()
	{

		return null;
	}*/

}
