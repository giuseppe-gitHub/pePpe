package it.gius.pePpe.data.distance;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 * @opt all
 * @opt !operations
 */
public class DistanceSolution {
	
	public Vec2 p1 = new Vec2();
	public Vec2 p2 = new Vec2();
	
	public float distance;
	
	//debug data
	//public Object lastSimplex;
	public Object otherData;

	
	public boolean penetration = false;
	//public float penetrationDepth;
	//public Vec2 normalPenetration = new Vec2();
	
	//debug data
	public int iterationCount = -1;
	//debug data
	//public int epaIterations = -1;
	
	/**
	 * @hidden
	 */
	public DistanceSolution() {
		
	}
}
