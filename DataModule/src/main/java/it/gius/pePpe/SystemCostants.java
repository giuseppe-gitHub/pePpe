package it.gius.pePpe;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 * @opt all
 */
public abstract class SystemCostants {
	
	
	private SystemCostants() {
	}
	
	public static final boolean DEBUG = true;
	
	public static final float PI = (float)StrictMath.PI;
	
	public static final float EPSILON = 2E-6f;
	
	public static final float SQRT_EPSILON = (float)Math.sqrt(SystemCostants.EPSILON);
	
	public static final Vec2 ORIGIN2D = new Vec2(0,0);

	public static final Vec2 X_AXIS_UNIT_VECTOR = new Vec2(1,0);
	
	public static final Vec2 Y_AXIS_UNIT_VECTOR = new Vec2(0,1);
	
	
	public static final int X_AXIS_INDEX = 0;
	public static final int y_AXIS_INDEX = 1;
	
	
	public static final int CLOCKWISE_WINDING = 1;
	public static final int COUNTER_CLOCKWISE_WINDING = 2;
	
	
	public static final float POSITIVE_SLOPE = 0.46f;
	public static final float NEGATIVE_SLOPE = POSITIVE_SLOPE;
	
	public static final float DEFAULT_AABB_ENLARGE = 2;
}
