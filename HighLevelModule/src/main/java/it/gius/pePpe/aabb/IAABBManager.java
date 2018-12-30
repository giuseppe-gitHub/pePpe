package it.gius.pePpe.aabb;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.physic.BindAABBNode;

public interface IAABBManager {

	//TODO think if remove init
	public abstract void init(IdList<BindAABBNode> shapesList);
	
	/*public abstract void addListener(IAABBPairChange listener);
	public abstract void removeListener(IAABBPairChange listener);*/

	public abstract void updateAllAABB();
	
	public abstract void updatePairs();

	public abstract void removeAABB(AABoundaryBox[] boxes, int num);

	public abstract void removeAABB(AABoundaryBox boxA);

	public abstract void addAABB(AABoundaryBox[] boxes, short indices[], int num);

	public abstract void addAABB(AABoundaryBox boxA, short indexBoxA);

	public abstract void updateAABB(AABoundaryBox boxA);

	public abstract AABBPair[] getPairs();

	public abstract int getPairsNumber();

}