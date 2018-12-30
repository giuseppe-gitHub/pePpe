package it.gius.data.structures;

import junit.framework.TestCase;

public class LinkedListTest extends TestCase {

	public void testPeek() {
		LinkedList<Short> list = new LinkedList<Short>();

		list.addLast((short)3);
		list.addLast((short)2);

		short result = list.peek();

		if(result != 3)
			fail();

		if(list.size() != 2)
			fail();


		list.poll();

		if(list.size() != 1)
			fail();

	}


	public void testPoll() {
		LinkedList<Short> list = new LinkedList<Short>();

		list.addLast((short)3);
		list.addLast((short)2);
		list.addLast((short)334);
		list.addLast((short)1);

		if(list.size() != 4)
			fail();

		short result = list.poll();

		if(result != 3)
			fail();

		result = list.poll();

		if(result != 2)
			fail();
	}


}
