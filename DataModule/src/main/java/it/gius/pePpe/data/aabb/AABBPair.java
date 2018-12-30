package it.gius.pePpe.data.aabb;

/**
 * 
 * @author giuseppe
 * @opt all
 */
public class AABBPair  implements Comparable<AABBPair> {

	public short idShapeA;
	public short idShapeB;

	//contain GjkMinkDiffShape for shape A and B and maybe other
	//public ICache cache = null;
	
	public boolean neww = true;
	public boolean removed = false;
	public boolean inArray = false;
	
	/*public static int cantorPairing(short idA,short idB)
	{
		int sum = idA + idB;
		return ((sum)*(sum +1)/2 +idB);
	}

	public int cantorPairing()
	{
		int sum = idShapeA + idShapeB;
		return ((sum)*(sum +1)/2 +idShapeB);
	}*/

	/**
	 * @hidden
	 */
	public AABBPair() {
	
	}
	
	public AABBPair(short idA,short idB) {
		this.idShapeA =idA;
		this.idShapeB = idB;
		
	}
	
	@Override
	public int compareTo(AABBPair o) {
		if(idShapeA == o.idShapeA && idShapeB == o.idShapeB)
			return 0;

		if(this.idShapeA > o.idShapeA)
			return 1;

		if(this.idShapeA < o.idShapeA)
			return -1;

		if(this.idShapeB > o.idShapeB)
			return 1;

		//if(this.idB < o.idB)
		return -1;
	}
	
	/**
	 * @hidden
	 */
	@Override
	public String toString() {
		
		return "(" + idShapeA + ", " + idShapeB +")";
	}
	
	
	//Thomas Wang Hash (cuttable hash for little HashTable)
	@Override
	public int hashCode() {
		
		int key = ((int)idShapeB) | ( ((int)idShapeA) <<Short.SIZE); 
		
		key += ~(key << 15);
		key ^=  (key >> 10);
		key +=  (key << 3);
		key ^=  (key >> 6);
		key += ~(key << 11);
		key ^=  (key >> 16);
		return key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AABBPair other = (AABBPair) obj;
		if (idShapeA != other.idShapeA)
			return false;
		if (idShapeB != other.idShapeB)
			return false;
		return true;
	}


}