package it.gius.pePpe.distance.gjk;

import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.data.aabb.AABBPair;

public class GjkPairNode {
	
	public AABBPair pair = new AABBPair();
	
	//cache
	public GjkMinkDiffShape minkShape = null;
	
	public GjkPairNode nextPairA = null;
	public GjkPairNode nextPairB = null;
	
	public GjkPairNode prevPairA = null;
	public GjkPairNode prevPairB = null;
	
	
	public GjkPairNode() {
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
		GjkPairNode other = (GjkPairNode) obj;
		if (pair == null) {
			if (other.pair != null)
				return false;
		} else if (!pair.equals(other.pair))
			return false;
		return true;
	}
	
	

}
