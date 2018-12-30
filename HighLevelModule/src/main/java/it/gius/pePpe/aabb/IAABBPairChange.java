package it.gius.pePpe.aabb;

import it.gius.pePpe.data.aabb.AABBPair;

public interface IAABBPairChange {
	
	public void added(AABBPair pair);
	public void removed(AABBPair pair);

}
