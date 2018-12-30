package it.gius.pePpe.aabb;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.BindAABBNode;

public class AABBManagerMock implements IAABBManager {

	@Override
	public void init(IdList<BindAABBNode> shapesList) {
		

	}

	@Override
	public void updateAllAABB() {
		

	}

	@Override
	public void updatePairs() {
		

	}

	@Override
	public void removeAABB(AABoundaryBox[] boxes, int num) {
		

	}

	@Override
	public void removeAABB(AABoundaryBox boxA) {
		

	}

	@Override
	public void addAABB(AABoundaryBox[] boxes, short[] indices, int num) {
		
	}

	@Override
	public void addAABB(AABoundaryBox boxA, short indexBoxA) {
		

	}

	@Override
	public void updateAABB(AABoundaryBox boxA) {
		

	}

	@Override
	public AABBPair[] getPairs() {
		
		return null;
	}

	@Override
	public int getPairsNumber() {
		
		return 0;
	}

}
