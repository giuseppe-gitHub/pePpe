package it.gius.pePpe;

import org.jbox2d.common.Vec2;

public abstract class MathUtils {
	
	public static float manhattanDistance(float x1,float y1, float x2, float y2)
	{
		return (Math.abs(x1-x2) + Math.abs(y1-y2));
	}

	
	public static float manhattanDistance(Vec2 v1, Vec2 v2)
	{
		return manhattanDistance(v1.x, v1.y, v2.x, v2.y);
	}
	
	//Thomas Wang Hash (cuttable hash for little HashTable)
	public static int cuttableHashCode(int key)
	{
		key += ~(key << 15);
		key ^=  (key >> 10);
		key +=  (key << 3);
		key ^=  (key >> 6);
		key += ~(key << 11);
		key ^=  (key >> 16);
		return key;
	}
	
	public static int cantorPairing(int k1,int k2)
	{
		int sum = k1 + k2;
		return ((sum)*(sum +1)/2 +k2);
	}
}
