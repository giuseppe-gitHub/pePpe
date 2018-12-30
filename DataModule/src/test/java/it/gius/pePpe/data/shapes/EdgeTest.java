package it.gius.pePpe.data.shapes;

import org.jbox2d.common.Vec2;

import junit.framework.TestCase;

public class EdgeTest extends TestCase {

	public void testContains() {
		Edge edge = new Edge(new Vec2(2,2), new Vec2(4,4));
		
		if(edge.contains(new Vec2(1,2)))
			fail();
		
		if(!edge.contains(new Vec2(3,3)))
			fail();
		
		
		if(edge.contains(new Vec2(5,5)))
			fail();
		
		if(edge.contains(new Vec2(1,1)))
			fail();
	}
	
	public void testEquals()
	{
		Edge edgeA = new Edge(new Vec2(2,2), new Vec2(4,4));
		Edge edgeB = new Edge(new Vec2(2,2), new Vec2(4,4));
		Edge edgeC = new Edge(new Vec2(2,2), new Vec2(4,2));
		Edge edgeD = new Edge(new Vec2(3,2), new Vec2(4,2));
		
		if(!edgeA.equals(edgeA))
			fail();
		
		if(!edgeA.equals(edgeB))
			fail();
		
		if(edgeA.equals(edgeC))
			fail();
		
		if(edgeA.equals(edgeD))
			fail();
	}

}
