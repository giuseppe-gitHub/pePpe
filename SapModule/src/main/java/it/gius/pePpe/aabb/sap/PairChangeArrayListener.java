package it.gius.pePpe.aabb.sap;

import it.gius.data.structures.HashSet;
import it.gius.pePpe.aabb.IAABBPairChange;
import it.gius.pePpe.data.aabb.AABBPair;

public class PairChangeArrayListener implements IAABBPairChange {

	private HashSet<IAABBPairChange> set;
	
	private IAABBPairChange[] array;
	
	public PairChangeArrayListener() {
		set = new HashSet<IAABBPairChange>(IAABBPairChange.class);
	}
	
	public void addListener(IAABBPairChange listener)
	{
		set.add(listener);
	}
	
	public void removeListener(IAABBPairChange listener)
	{
		set.remove(listener);
	}
	
	@Override
	public void added(AABBPair pair) {
		array = set.elements;
		int size = set.size();
		for(int i=0; i< size; i++)
			array[i].added(pair);

	}

	@Override
	public void removed(AABBPair pair) {
		array = set.elements;

		int size = set.size();
		for(int i=0; i< size; i++)
			array[i].removed(pair);
	}

}
