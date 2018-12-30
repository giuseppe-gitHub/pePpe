package it.gius.pePpe.aabb;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.BindAABBNode;

public class AABBUpdater {
	
	//TODO test it

	private IAABBManager manager;
	private boolean useEnlargeAABB = true;
	private float enlargeFactor = 2;
	private IdList<BindAABBNode> shapesList;

	public AABBUpdater(IAABBManager manager, AABBUpdaterInit init) {
		
		if(init.useEnlargeAABB && init.enlargeFactor < 0)
			throw new IllegalArgumentException("enlargeFactor cant be negative");
		
		this.manager = manager;
		this.useEnlargeAABB = init.useEnlargeAABB;
		this.enlargeFactor = init.enlargeFactor;
	}
	
	private boolean inited = false;
	
	public void init(IdList<BindAABBNode> shapesList)
	{
		if(inited)
			return;
		
		this.shapesList = shapesList;
		inited = true;
	}


	public boolean isUseEnlargeAABB() {
		return useEnlargeAABB;
	}

	public void setUseEnlargeAABB(boolean useEnlargeAABB) {
		this.useEnlargeAABB = useEnlargeAABB;
	}

	public float getEnlargeFactor() {
		return enlargeFactor;
	}

	public void setEnlargeFactor(float enlargeFactor) {
		this.enlargeFactor = enlargeFactor;
	}
	
	private AABoundaryBox boxPool = new AABoundaryBox();

	public void updateAllAABB()
	{

		BindAABBNode[] shapesArray = shapesList.toLongerArray();
		int size = shapesList.size();
		
		if(!useEnlargeAABB)
		{
			for(int i=0; i<size;i++)
			{
				BindAABBNode node = shapesArray[i];
				node.bind.getAABoundaryBox(node.box);
			}

			manager.updateAllAABB();
		}
		else
		{
			AABoundaryBox newContainedBox = boxPool;
			for(int i=0; i<size;i++)
			{
				BindAABBNode node = shapesArray[i];
				node.bind.getAABoundaryBox(newContainedBox);
				
				if(!node.box.contains(newContainedBox))
				{
					newContainedBox.enlargeToOut(enlargeFactor, node.box);
					manager.updateAABB(node.box);
				}
			}
		}
		//manager.updatePairs();
	}
	
	
	public void fillAABB(BindAABBNode node)
	{
		if(!useEnlargeAABB)
		{
			node.bind.getAABoundaryBox(node.box);
		}
		else
		{
			AABoundaryBox containedBox = boxPool;
			node.bind.getAABoundaryBox(containedBox);
			containedBox.enlargeToOut(enlargeFactor, node.box);
		}
		
	}

}
