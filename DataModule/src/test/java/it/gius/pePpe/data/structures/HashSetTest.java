package it.gius.pePpe.data.structures;

import it.gius.data.structures.HashSet;

import java.util.Random;

import junit.framework.TestCase;

public class HashSetTest extends TestCase {



	private MockHashListItem[] array;

	private final int arraySize = 9;

	public HashSetTest() {


		array = new MockHashListItem[arraySize];

		for(int i=0;i<array.length;i++)
		{
			array[i] = new MockHashListItem();
			array[i].value = i;
		}
	}


	public void testAdd() {

		HashSet<MockHashListItem> hashList = new HashSet<MockHashListItem>(MockHashListItem.class);

		for(int i=0; i<array.length;i++)
			hashList.add(array[i]);


		if(hashList.size()!= array.length)
			fail();
	}

	public void testFind() {

		HashSet<MockHashListItem> hashList = new HashSet<MockHashListItem>(MockHashListItem.class);

		for(int i=0; i<array.length;i++)
			hashList.add(array[i]);


		if(hashList.size()!= array.length)
			fail();


		for(int i=0; i<array.length; i++)
		{
			MockHashListItem item= hashList.find(array[i]);
			if(item == null)
				fail();
		}
	}


	public void testRemoveFromStartToEnd() {
		HashSet<MockHashListItem> hashList = new HashSet<MockHashListItem>(MockHashListItem.class);

		for(int i=0; i<array.length;i++)
			hashList.add(array[i]);


		if(hashList.size()!= array.length)
			fail();
		
		for(int i=0; i<array.length;i++)
		{
			if(!hashList.remove(array[i]))
				fail();
		}
		
		if(hashList.size() != 0)
			fail();

	}

	public void testRemoveFromEndToStart() {
		HashSet<MockHashListItem> hashList = new HashSet<MockHashListItem>(MockHashListItem.class);

		for(int i=0; i<array.length;i++)
			hashList.add(array[i]);


		if(hashList.size()!= array.length)
			fail();
		
		for(int i=array.length-1; i>=0;i--)
		{
			if(!hashList.remove(array[i]))
				fail();
		}
		
		if(hashList.size() != 0)
			fail();

	}
	
	public void testRemoveRandom() {
		HashSet<MockHashListItem> hashList = new HashSet<MockHashListItem>(MockHashListItem.class);

		for(int i=0; i<array.length;i++)
			hashList.add(array[i]);


		if(hashList.size()!= array.length)
			fail();
		
		Random random = new Random(System.currentTimeMillis());
		
		int max = random.nextInt(array.length-1)+1;
		boolean removed[] = new boolean[array.length];
		
		for(int i=0; i< removed.length; i++)
			removed[i] = false;
		
		for(int i=0; i<max;i++)
		{
			int n = 0;
			for(; removed[n];n = Math.abs(random.nextInt()) % (array.length));
			
			if(!hashList.remove(array[n]))
				fail();
			else
				removed[n] = true;
		}
		
		if(hashList.size() != array.length - max)
			fail();

	}



	public void testGetIndex() {

		HashSet<MockHashListItem> hashList = new HashSet<MockHashListItem>(MockHashListItem.class);

		for(int i=0; i<array.length;i++)
			hashList.add(array[i]);


		if(hashList.size()!= array.length)
			fail();


		for(int i=0; i<array.length; i++)
		{
			int index = hashList.getIndex(array[i]);
			if(index == HashSet.NULL_NODE)
				fail();
		}
	}


}
