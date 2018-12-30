package it.gius.pePpe.data.structures;

import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;

import java.util.ConcurrentModificationException;
import java.util.Random;

import junit.framework.TestCase;



@SuppressWarnings("all")
public class IdDoubleArrayListTest extends TestCase {

	public void testAddRemove() {

		IdList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class, 4);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		short id1 = list.add(hello);
		list.add(world);
		short id3 = list.add(excl);

		if(list.size() != 3)
			fail("wrong size");

		list.remove(id3);

		if(list.size()!=2)
			fail("wrong size");

		for(MockListsElement element : list)
		{
			if(!element.equals(hello) && !element.equals(world))
				fail("Wrong contained");
		}

		list.remove(id1);

		if(list.size()!=1)
			fail("wrong size");


		for(MockListsElement element : list)
		{
			if(!element.equals(world))
				fail("Wrong contained");
		}

	}

	public void testAddRemoveWithLinkedList()
	{
		int ds = 20;

		IdDoubleArrayList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class,ds);

		for(int i=0; i<ds; i++)
		{
			Random random = new Random();
			String next = "" + random.nextInt();
			list.add(new MockListsElement(next));
		}

		if(list.remove((short)1) == null)//if(!list.remove((short)1))
			fail();

		if(list.remove((short)2) == null)
			fail();

		if(list.size() != ds-2)
			fail();

		short id = list.add( new MockListsElement(""));

		if(id != 1)
			fail("id should be 1 but it is: " + id);

		id = list.add( new MockListsElement(""));

		if(id != 2)
			fail("id should be 2 but it is: " + id);


		id = list.add(new MockListsElement(""));
		
		if(id != ds)
			fail();

	}


	public void testIterationsRemove()
	{
		IdList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class, 4);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		list.add(hello);
		list.add(world);
		list.add(excl);

		if(list.size() != 3)
			fail("wrong size");

		int size = 0;


		try {
			for(MockListsElement element : list)
			{
				list.remove(element.getId());
				size++;
			}
			fail("Exception not throwed");
		} catch (Throwable e1) {
		}


		list.add(hello);
		list.add(world);
		list.add(excl);


		try {
			for(@SuppressWarnings("unused") MockListsElement element: list)
			{
				list.add(new MockListsElement("error"));
			}
			fail("Added during iteration");
		} catch (ConcurrentModificationException e) {

		}


	}


	public void testDoublLoop()
	{
		IdList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class, 4);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement brave = new MockListsElement("brave");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		list.add(hello);
		list.add(brave);
		list.add(world);
		list.add(excl);


		int counter = 0;
		for(MockListsElement el1 : list)
		{
			for(MockListsElement el2 : list)
			{
				counter++;

			}

			if(counter >= 100)
				fail("looping");
		}

		if(counter != list.size()*list.size())
			fail("" + counter);

	}



	public void testBreak1()
	{
		IdList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class, 4);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement brave = new MockListsElement("brave");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		list.add(hello);
		list.add(brave);
		list.add(world);
		list.add(excl);


		for(MockListsElement el2 : list)
		{
			if(el2.equals(hello))
				break;
		}

		int counter = 0;
		for(MockListsElement el1 : list)
		{
			for(MockListsElement el2 : list)
			{
				counter++;
				if(el2.equals(hello))
					break;
			}

			if(counter >= 15)
				fail("looping");
		}

		if(counter != list.size())
			fail();
	}


	public void testBreak2()
	{
		IdList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class, 4,3);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement brave = new MockListsElement("brave");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		list.add(hello);
		list.add(brave);
		list.add(world);
		list.add(excl);


		for(MockListsElement el2 : list)
		{
			if(el2.equals(hello))
				break;
		}

		int counter = 0;
		for(MockListsElement el1 : list)
		{
			for(MockListsElement el2 : list)
			{
				counter++;
				if(el2.equals(hello))
					break;
			}

			for(MockListsElement el2 : list)
			{
				counter++;
				if(el2.equals(brave))
					break;
			}


			if(counter >= 100)
				fail("looping");
		}

		if(counter != 3*list.size())
			fail();
	}


	public void testIteratorStartId()
	{
		IdList<MockListsElement> list = new IdDoubleArrayList<MockListsElement>(MockListsElement.class, 4);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement brave = new MockListsElement("brave");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		list.add(hello);
		list.add(brave);
		list.add(world);
		list.add(excl);


		int counter = 0;
		for(MockListsElement elA : list)
		{

			//short nextA = list.getNext(elA.id);
			MockListsElement[] array = list.toLongerArray();

			int n= list.size();
			for(int i=0; i<n; i++)
			{
				MockListsElement elB = array[i];
				counter++;
				if(elB== null)
					fail("array[i]== null");
			}

			if(counter >= 40)
				fail("looping");
		}

		/*if(counter != list.size()*(list.size()-1)/2)
			fail();*/

	}

}
