package it.gius.pePpe.data.shapes;

import org.jbox2d.common.Vec2;

import junit.framework.TestCase;

public class CircleTest extends TestCase {

	public void testContains() {
		Circle circle = new Circle(new Vec2(5,5), 10);
		
		if(!circle.contains(new Vec2(13,1)))
			fail();
		
		if(!circle.contains(new Vec2(10,10)))
			fail();
		
		if(circle.contains(new Vec2(15,0)))
			fail();
	}
	
	public void testEquals()
	{
		Circle circleA = new Circle(new Vec2(4,4), 10);
		Circle circleB = new Circle(new Vec2(4,4), 10);
		Circle circleC = new Circle(new Vec2(4,2), 10);
		Circle circleD = new Circle(new Vec2(4,4), 13);
		
		if(circleA.equals(null))
			fail();
		
		if(!circleA.equals(circleA))
			fail();
		
		if(!circleA.equals(circleB))
			fail();
		
		if(circleA.equals(circleC))
			fail();
		
		if(circleA.equals(circleD))
			fail();
	}

}
