package it.gius.pePpe.data.distance;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 * @opt all
 * @opt !operations
 */
public class OverlapSolution {

	//public float penetrationDepth;
	/**
	 * The penetration direction.
	 * It points from the first shape to the second shape.
	 */
	public Vec2 normal = new Vec2();
	
	public float distanceDepth; //>0 & <0
	public Vec2 p1 = new Vec2();
	public Vec2 p2 = new Vec2();
	
	//debug data
	public int iterationCount = -1;
	//debug data
	public int epaIterations = -1;
	
	/**
	 * @hidden
	 */
	public OverlapSolution() {
		
	}
}
