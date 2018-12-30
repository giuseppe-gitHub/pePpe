package it.gius.pePpe.data.structures;




import it.gius.data.structures.IdArrayLinkList;
import it.gius.data.structures.IdList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import junit.framework.TestCase;

@SuppressWarnings("all")
public class IdArrayListTest extends TestCase {

	public void testAddRemove() {

		IdList<MockListsElement> list = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 4);

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


	public void testIterationsRemove()
	{
		IdList<MockListsElement> list = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 4);

		MockListsElement hello = new MockListsElement("Hello");
		MockListsElement world = new MockListsElement("world");
		MockListsElement excl = new MockListsElement("!");

		list.add(hello);
		list.add(world);
		list.add(excl);

		if(list.size() != 3)
			fail("wrong size");

		int size = 0;

		for(MockListsElement element : list)
		{
			list.remove(element.getId());
			size++;
		}

		if(size != 3)
			fail("Wrong iteration count");

		if(list.size() != 0)
			fail("wrong size");


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
		IdList<MockListsElement> list = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 4);

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
		IdList<MockListsElement> list = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 4);

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
		IdList<MockListsElement> list = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 4,3);

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
		IdArrayLinkList<MockListsElement> list = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 4);

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

			short nextA = list.getNext(elA.id);

			if(nextA != IdList.NULL_NODE)
			{
				for(Iterator<MockListsElement> iterator = list.iterator(nextA);  iterator.hasNext();)
				{
					MockListsElement elB = iterator.next();
					counter++;
					if(elA.equals(elB))
						fail("equals unexpected");
				}
			}

			if(counter >= 40)
				fail("looping");
		}
		
		if(counter != list.size()*(list.size()-1)/2)
			fail();

	}
}
