package it.gius.pePpe.data.aabb;

import it.gius.pePpe.SystemCostants;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 *
 * @opt all
 */
public class AABoundaryBox {
	
	/**
	 * @hidden
	 */
	public static final int X_AXIS = SystemCostants.X_AXIS_INDEX;
	/**
	 * @hidden
	 */
	public static final int Y_AXIS = SystemCostants.y_AXIS_INDEX;

	public int epMMins[];
	public int epMMaxs[];	                     

	public Vec2 lowerBound;
	public Vec2 upperBound;


	/**
	 * @hidden
	 */
	public AABoundaryBox() {
		this.lowerBound = new Vec2();
		this.upperBound = new Vec2();

		epMMins = new int[2]; //0 -> x axis ; 1 -> y axis
		epMMaxs = new int[2]; //0 -> x axis ; 1 -> y axis
	}

	/**
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * 
	 * @hidden
	 */
	public AABoundaryBox(Vec2 lowerBound, Vec2 upperBound) {
		this.lowerBound = new Vec2();
		this.lowerBound.set(lowerBound);
		
		this.upperBound = new Vec2();
		this.upperBound.set(upperBound);

		epMMins = new int[2]; //0 -> x axis ; 1 -> y axis
		epMMaxs = new int[2]; //0 -> x axis ; 1 -> y axis
		
		
	}
	
	/*public void transform(Transform transform)
	{
		Transform.mulToOut(transform, lowerBound, lowerBound);
		Transform.mulToOut(transform, upperBound, upperBound);
	}*/
	
	
	/**
	 * 
	 * @param other
	 * @return true if this AABB contains the other
	 * 
	 */
	public boolean contains(AABoundaryBox other)
	{
		//TODO to test contains
		return (this.lowerBound.x < other.lowerBound.x && this.lowerBound.y < other.lowerBound.y &&
				this.upperBound.x > other.upperBound.x && this.upperBound.y > other.upperBound.y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AABoundaryBox other = (AABoundaryBox) obj;
		if (lowerBound == null) {
			if (other.lowerBound != null)
				return false;
		} else if (!lowerBound.equals(other.lowerBound))
			return false;
		if (upperBound == null) {
			if (other.upperBound != null)
				return false;
		} else if (!upperBound.equals(other.upperBound))
			return false;
		return true;
	}

	public boolean contains(Vec2 point)
	{
		//TODO test contains point
		if(point.x <= lowerBound.x || point.y <= lowerBound.y)
			return false;
		
		if(point.x >= upperBound.x || point.y >= upperBound.y)
			return false;
		
		return true;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return true if a & b overlap
	 */
	public static boolean overlap(AABoundaryBox a, AABoundaryBox b)
	{
		if(a.lowerBound.x > b.upperBound.x || b.lowerBound.x > a.upperBound.x)
			return false;

		if(a.lowerBound.y > b.upperBound.y || b.lowerBound.y > a.upperBound.y)
			return false;


		return true;
	}
	
	
	public void enlarge(float value)
	{
		this.lowerBound.x -= value;
		this.lowerBound.y -= value;
		
		this.upperBound.x += value;
		this.upperBound.y += value;
	}
	
	public void enlargeToOut(float value, AABoundaryBox aabbOut)
	{
		aabbOut.lowerBound.x = this.lowerBound.x - value;
		aabbOut.lowerBound.y = this.lowerBound.y - value;
		
		aabbOut.upperBound.x = this.upperBound.x + value;
		aabbOut.upperBound.y = this.upperBound.y + value;
	}
	
	@Override
	public String toString() {
		return "("  + this.lowerBound.x + ", "+ this.lowerBound.y + "; " 
				+ this.upperBound.x + ", " + this.upperBound.y + ")";
	}


}