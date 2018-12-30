package it.gius.pePpe.data.structures;

import it.gius.data.structures.HashMap;
import it.gius.pePpe.data.aabb.AABBPair;
import junit.framework.TestCase;

public class HashMapTest extends TestCase {


	public void testPut() {
		HashMap<AABBPair, Integer> map = new HashMap<AABBPair, Integer>(AABBPair.class, Integer.class);

		AABBPair pair = new AABBPair((short)0,(short)1);
		map.put(pair, 8);

		if(map.size()!=1)
			fail();

		int x = map.get(pair);

		if(x != 8)
			fail();

		map.put(pair, 9);

		x = map.get(pair);

		if(x != 9)
			fail();
	}

	public void testRemove() {
		HashMap<AABBPair, Integer> map = new HashMap<AABBPair, Integer>(AABBPair.class, Integer.class);

		AABBPair pair = new AABBPair((short)0,(short)1);
		map.put(pair, 8);

		if(map.size()!=1)
			fail();

		int x = map.remove(pair);

		if(x != 8)
			fail();

		if(map.containsKey(pair))
			fail();
	}

	public void testValuesLongerArray() {
		HashMap<AABBPair, Integer> map = new HashMap<AABBPair, Integer>(AABBPair.class, Integer.class);

		AABBPair pair = new AABBPair((short)0,(short)1);
		map.put(pair, 1);

		pair = new AABBPair((short)2,(short)3);
		map.put(pair, 5);

		pair = new AABBPair((short)7,(short)3);
		map.put(pair, 10);

		pair = new AABBPair((short)4,(short)3);
		map.put(pair, 7);

		if(map.size() != 4)
			fail();

		int size = map.size();
		Integer[] actual = map.valuesLongerArray();
		AABBPair[] pairs = map.keyLongerArray();

		for(int i=0; i<size; i++)
		{
			int expected = pairs[i].idShapeA + pairs[i].idShapeB;
			if(expected != actual[i])
				fail();
		}

		pair = new AABBPair((short)0,(short)1);
		int temp = map.put(pair, -1);
		
		if(temp != 1)
			fail();

		pair = new AABBPair((short)2,(short)3);
		temp = map.put(pair, -1);
		
		if(temp != 5)
			fail();

		pair = new AABBPair((short)7,(short)3);
		temp =map.put(pair, 4);
		
		if(temp != 10)
			fail();

		pair = new AABBPair((short)4,(short)3);
		temp = map.put(pair, 1);
		
		if(temp != 7)
			fail();

		size = map.size();
		actual = map.valuesLongerArray();
		pairs = map.keyLongerArray();

		for(int i=0; i<size; i++)
		{
			int expected = pairs[i].idShapeA - pairs[i].idShapeB;
			if(expected != actual[i])
				fail();
		}
	}

}
