package it.gius.data.structures;

import junit.framework.TestCase;

public class PoolStackTest extends TestCase {

	public void testEmpty() {
		Integer[] startingElements = new Integer[5];
		for(int i=0; i< startingElements.length; i++)
			startingElements[i] = new Integer(i);
			
		PoolStack<Integer> stack = new PoolStack<Integer>(Integer.class, 10, startingElements);
		
		for(int i=0; i<startingElements.length; i++)
		{
			if(stack.empty())
				fail();
			
			Integer element = stack.pop();
			
			assertEquals(new Integer(startingElements.length-i-1), element);
		}
		
		if(!stack.empty())
			fail();
		
		Integer el = stack.pop();
		
		if(el!= null)
			fail();
	}

	public void testPop() {
		Integer[] startingElements = new Integer[5];
		for(int i=0; i< startingElements.length; i++)
			startingElements[i] = new Integer(i);
			
		PoolStack<Integer> stack = new PoolStack<Integer>(Integer.class, 10, startingElements);
		
		Integer el = stack.pop();
		
		if(!el.equals(4))
			fail();
	}

	public void testTryPush() {
		Integer[] startingElements = new Integer[10];
		for(int i=0; i< startingElements.length; i++)
			startingElements[i] = new Integer(i);
			
		PoolStack<Integer> stack = new PoolStack<Integer>(Integer.class, 5, startingElements);
		
		if(stack.tryPush(44))
			fail();
		
		stack.pop();
		
		if(!stack.tryPush(5))
			fail();
		
		
		if(stack.tryPush(null))
			fail();
		
	}

}
