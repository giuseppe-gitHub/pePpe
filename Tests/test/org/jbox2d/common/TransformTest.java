package org.jbox2d.common;

import java.util.Random;

import junit.framework.TestCase;

public class TransformTest extends TestCase {

	public void testTransToOut() {
		Transform t = new Transform();
		t.position.set(10,20);
		t.R.set((float)Math.PI/3);
		
		Transform trans = new Transform();
		
		t.transToOut(trans);
		
		Random random = new Random(System.currentTimeMillis());
		
		Vec2 expected = new Vec2(30*random.nextFloat(),30*random.nextFloat());
		
		Vec2 result = new Vec2();
		
		Transform.mulToOut(t, expected,result);
		
		Transform.mulToOut(trans, result, result);
		
		Mat22 matResult = new Mat22();
		
		Mat22.mulToOut(t.R, trans.R, matResult);
		
		System.out.println(matResult);
		
		assertEquals(expected, result);
	}

}
