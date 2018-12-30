package it.gius.pePpe.distance.cache;


import it.gius.pePpe.data.aabb.AABBPair;

public class PairNode {
	
	public AABBPair pair = new AABBPair();
	
	
	public PairNode nextPairA = null;
	public PairNode nextPairB = null;
	
	public PairNode prevPairA = null;
	public PairNode prevPairB = null;
	
	
	public PairNode() {
	}
	
	
	@Override
	public int hashCode() {
		return pair.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PairNode other = (PairNode) obj;
		if (pair == null) {
			if (other.pair != null)
				return false;
		} else if (!pair.equals(other.pair))
			return false;
		return true;
	}
	
	

}
