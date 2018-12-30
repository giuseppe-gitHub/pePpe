package it.gius.pePpe.distance.cache;


//import it.gius.pePpe.data.shapes.Shape;
import it.gius.data.structures.HashMap;
import it.gius.data.structures.HashSet;
import it.gius.pePpe.data.cache.DistanceWitnessCache;



/**
 * 
 * @author giuseppe
 *
 * @opt all
 */
public class WitnessCacheManager {

	//TODO test this class completely

	private HashMap<PairNode,DistanceWitnessCache>  pairCacheMap;
	private HashSet<SingleShapeNode> singleShapeSet;

	private PairNode pairNodePool = new PairNode();
	private SingleShapeNode singleShapePoolA = new SingleShapeNode();
	private SingleShapeNode singleShapePoolB = new SingleShapeNode();


	public WitnessCacheManager() {
		singleShapeSet = new HashSet<SingleShapeNode>(SingleShapeNode.class);
		pairCacheMap = new HashMap<PairNode, DistanceWitnessCache>(PairNode.class, DistanceWitnessCache.class);

	}

	public void put(short idA, short idB, DistanceWitnessCache cache)
	{

		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}

		pairNodePool.pair.idShapeA = idA;
		pairNodePool.pair.idShapeB = idB;
		boolean found =  pairCacheMap.containsKey(pairNodePool);


		if(found)
		{
			pairCacheMap.put(pairNodePool, cache);
			return;
		}
		//else
		//System.out.println("Cache Missed: " + idA +", " + idB);

		PairNode newNode = new PairNode();
		newNode.pair.idShapeA = idA;
		newNode.pair.idShapeB = idB;

		singleShapePoolA.idShape = idA;

		SingleShapeNode nodeA = singleShapeSet.find(singleShapePoolA);

		if(nodeA == null)
		{
			nodeA = new SingleShapeNode();
			nodeA.idShape = idA;
			nodeA.rootPair = newNode;
			
			singleShapeSet.add(nodeA);
		}
		else
		{
			PairNode oldRootPair = nodeA.rootPair;
			nodeA.rootPair = newNode;
			
			newNode.nextPairA = oldRootPair;
			
			if(oldRootPair != null)
				oldRootPair.prevPairA = newNode;
		}

		singleShapePoolB.idShape = idB;

		SingleShapeNode nodeB = singleShapeSet.find(singleShapePoolB);

		if(nodeB == null)
		{
			nodeB = new SingleShapeNode();
			nodeB.idShape = idB;
			nodeB.rootPair = newNode;
			
			singleShapeSet.add(nodeB);
		}
		else
		{
			PairNode oldRootPair = nodeB.rootPair;	
			nodeB.rootPair = newNode;
			
			newNode.nextPairB = oldRootPair;
			
			if(oldRootPair != null)
				oldRootPair.prevPairB = newNode;
		}
		
		
		pairCacheMap.put(newNode, cache);
		
	}


	public DistanceWitnessCache get(short idA, short idB)
	{
		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}

		pairNodePool.pair.idShapeA = idA;
		pairNodePool.pair.idShapeB = idB;
		DistanceWitnessCache cache =  pairCacheMap.get(pairNodePool);

		return cache;
	}


	public void remove(short id)
	{
		singleShapePoolA.idShape = id;

		SingleShapeNode singleShapeNode =  singleShapeSet.find(singleShapePoolA);

		if(singleShapeNode != null)
		{
			PairNode currentNode = singleShapeNode.rootPair;

			while(currentNode != null)
			{
				pairCacheMap.remove(currentNode);


				if(currentNode.pair.idShapeA == id)
				{
					if(currentNode.prevPairB != null)
						currentNode.prevPairB.nextPairB = currentNode.nextPairB;
					if(currentNode.nextPairB != null)
						currentNode.nextPairB.prevPairB = currentNode.prevPairB;

					currentNode = currentNode.nextPairA;
					continue;
				}

				if(currentNode.pair.idShapeB == id)
				{
					if(currentNode.prevPairA != null)
						currentNode.prevPairA.nextPairA = currentNode.nextPairA;
					if(currentNode.nextPairA != null)
						currentNode.nextPairA.prevPairA = currentNode.prevPairA;

					currentNode = currentNode.nextPairB;
				}
				else
					throw new RuntimeException();
			}

			singleShapeSet.remove(singleShapeNode);

		}


	}


}
