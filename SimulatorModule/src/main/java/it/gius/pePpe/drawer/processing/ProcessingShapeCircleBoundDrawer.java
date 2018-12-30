package it.gius.pePpe.drawer.processing;

import org.jbox2d.common.Vec2;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.drawer.DrawerProperties;

public class ProcessingShapeCircleBoundDrawer extends AbstractProcessingDrawer {


	public ProcessingShapeCircleBoundDrawer() {
		name = "Processing Shapes Circles Bound drawer";
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

		applet.noFill();
		applet.stroke(boundCircles);

		for(int i=0; i< size;i++)
		{
			BindAABBNode node = arrayNode[i];

			Bind bind = node.bind;

			drawCircle(bind.phShape.shape, bind.body);


		}

	}

	private void drawCircle(Shape shape,Body body)
	{
		applet.pushMatrix();

		Vec2 position = body.transform.position;
		float angle = body.getAngle();

		applet.translate(position.x, position.y);
		applet.rotate(angle);

		applet.noFill();
		applet.stroke(boundCircles);
		applet.ellipse(shape.centroid.x, shape.centroid.y, 2*shape.radius, 2*shape.radius);

		applet.fill(255,0,0);
		applet.stroke(255,0,0);
		applet.ellipse(shape.centroid.x, shape.centroid.y, 3, 3);

		applet.popMatrix();
	}

}
