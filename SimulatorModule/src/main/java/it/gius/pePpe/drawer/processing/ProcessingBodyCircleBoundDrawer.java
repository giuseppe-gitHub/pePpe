package it.gius.pePpe.drawer.processing;


import it.gius.data.structures.IdList;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.drawer.DrawerProperties;

public class ProcessingBodyCircleBoundDrawer extends AbstractProcessingDrawer {

	public ProcessingBodyCircleBoundDrawer() {
		name = "Processing Body Circles Bound drawer";
	}
	
	private int boundCircles = 0xFFFF8C00;
	
	@Override
	public void setDrawerProperties(DrawerProperties drawerProperties) {
		this.boundCircles = defaultHexAlpha + drawerProperties.colorBoundCircles;
	}

	
	@Override
	public void draw() {
		if(!enable)
			return;

		IdList<BindAABBNode> list = engine.getGlobalShapes();

		int size = list.size();

		BindAABBNode arrayNode[] = list.toLongerArray();

		for(int i=0; i< size;i++)
		{
			BindAABBNode node = arrayNode[i];

			Bind bind = node.bind;

			drawCircle(bind.body);


		}

	}
	private void drawCircle(Body body)
	{
		applet.pushMatrix();


		applet.noFill();
		applet.stroke(boundCircles);
		applet.ellipse(body.getGlobalCenter().x, body.getGlobalCenter().y, 2*body.radius, 2*body.radius);

		applet.fill(255,0,0);
		applet.stroke(255,0,0);
		applet.ellipse(body.getGlobalCenter().x, body.getGlobalCenter().y, 3, 3);

		applet.popMatrix();
	}

}
