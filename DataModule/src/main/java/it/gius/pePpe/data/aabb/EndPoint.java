package it.gius.pePpe.data.aabb;

/**
 * 
 * @author giuseppe
 * @opt all
 * @opt !operations
 */
public class EndPoint implements Comparable<EndPoint>{

	public boolean min;

	/**
	 * @hidden
	 */
	public EndPoint() {
	}

	public float value;

	public short idShape;
	public AABoundaryBox box;

	@Override
	public int compareTo(EndPoint other) {
		if(this.value == other.value)
			return 0;

		if(this.value > other.value)
			return 1;
		else
			return -1;
	}
	
	@Override
	public String toString() {
		
		if(min)
			return "(min: " + value + ", id: " +idShape+")";
		else
			return "(max: " + value + ", id: " +idShape +")";
		
	}


}
