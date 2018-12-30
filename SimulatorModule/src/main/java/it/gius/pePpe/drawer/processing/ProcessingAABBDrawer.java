package it.gius.pePpe.drawer.processing;

import org.jbox2d.common.Vec2;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.drawer.DrawerProperties;

public class ProcessingAABBDrawer extends AbstractProcessingDrawer {

	public ProcessingAABBDrawer() {
		name = "Processing AABB drawer";
	}

	private DrawerProperties drawerProperties;
	private int colorAABB;

	@Override
	public void setDrawerProperties(DrawerProperties drawerProperties) {
		this.drawerProperties = drawerProperties;
		this.colorAABB = defaultHexAlpha + drawerProperties.colorAABB;
	}

	@Override
	public void draw() {
		if(!enable)
			return;

		applet.pushMatrix();
		applet.noFill();

		if(drawerProperties != null)
			applet.stroke(colorAABB);
		else
			applet.stroke(255,140,0);

		IdList<BindAABBNode> list = engine.getGlobalShapes();

		int size = list.size();

		BindAABBNode[] arrayNode = list.toLongerArray();

		for(int i=0; i< size;i++)
		{
			BindAABBNode node = arrayNode[i];

			drawAABB(node);

		}

		applet.popMatrix();

	}



	//private AABoundaryBox boxPool = new AABoundaryBox();

	private void drawAABB(BindAABBNode node)
	{

		//node.bind.getAABoundaryBox(boxPool);
		//node.updateAABB();
		//node.updateEnlargeAABB();
		AABoundaryBox box = node.box;//boxPool;
		Vec2 l = box.lowerBound;
		Vec2 u = box.upperBound;
		applet.rect(l.x,l.y, u.x - l.x, u.y - l.y);	

	}

}
