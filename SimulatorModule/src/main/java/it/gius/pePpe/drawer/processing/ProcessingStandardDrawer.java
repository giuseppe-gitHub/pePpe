package it.gius.pePpe.drawer.processing;


import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import processing.core.PApplet;
import it.gius.data.structures.IdList;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;

import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.drawer.DrawerProperties;
import it.gius.pePpe.forces.IForce;
import it.gius.pePpe.forces.MouseForce;
import it.gius.pePpe.forces.SpringBodyForce;
import it.gius.pePpe.forces.SpringPointForce;

public class ProcessingStandardDrawer extends AbstractProcessingDrawer{


	public ProcessingStandardDrawer() {
		name = "Processing standard drawer";
	}

	private int colorFillShapes = 0x784682B4;
	private int colorStrokeShapes = 0xFFAFEEEE;
	private int colorSprings = 0xFF00F100;

	private int fillShapesHexAlpha = 0x78000000; 

	@Override
	public void setDrawerProperties(DrawerProperties drawerProperties) {
		this.colorFillShapes =  fillShapesHexAlpha+ drawerProperties.colorFillShapes;
		this.colorStrokeShapes = defaultHexAlpha + drawerProperties.colorStrokeShapes;
		this.colorSprings = defaultHexAlpha + drawerProperties.colorSprings;
	}

	@Override
	public void draw() {

		if(!enable)
			return;

		IdList<BindAABBNode> list = engine.getGlobalShapes();

		int size = list.size();

		BindAABBNode arrayNode[] = list.toLongerArray();


		applet.fill(colorFillShapes);
		applet.stroke(colorStrokeShapes);

		for(int i=0; i< size;i++)
		{
			BindAABBNode node = arrayNode[i];

			Bind bind = node.bind;


			if(bind.phShape.shape instanceof Polygon)
			{
				drawPolygon((Polygon)bind.phShape.shape, bind.body);
				continue;
			}
			

			if(bind.phShape.shape instanceof Circle)
			{
				drawCircle((Circle)bind.phShape.shape, bind.body);
				continue;
			}

			if(bind.phShape.shape instanceof Edge)
			{
				drawEdge((Edge)bind.phShape.shape,bind.body);
			}

		}

		IdList<IForce> forceList = engine.getForcesList();

		size = forceList.size();

		IForce forces[] = forceList.toLongerArray();

		applet.pushMatrix();


		applet.fill(colorSprings);
		applet.stroke(colorSprings);

		for(int i=0; i< size;i++)
		{
			if(forces[i] instanceof SpringPointForce)
			{
				drawSpring((SpringPointForce)forces[i]);
				continue;
			}
			
			if(forces[i] instanceof MouseForce)
			{
				drawSpring((MouseForce)forces[i]);
				continue;
			}


			if(forces[i] instanceof SpringBodyForce)
				drawSpring((SpringBodyForce)forces[i]);
		}

		applet.popMatrix();

	}

	private Vec2 globalBodyPoint = new Vec2();


	protected void drawSpring(SpringPointForce spring)
	{

		Transform.mulToOut(spring.getBody().transform,spring.getLocalBodyPoint(),globalBodyPoint);

		applet.line(spring.point.x, spring.point.y, globalBodyPoint.x, globalBodyPoint.y);

	}

	protected void drawSpring(MouseForce spring)
	{

		Transform.mulToOut(spring.getBody().transform,spring.getLocalBodyPoint(),globalBodyPoint);

		applet.line(spring.point.x, spring.point.y, globalBodyPoint.x, globalBodyPoint.y);

	}

	
	protected Vec2 globalPointA = new Vec2();
	protected Vec2 globalPointB = new Vec2();

	protected void drawSpring(SpringBodyForce spring)
	{

		Transform.mulToOut(spring.bodyA.transform,spring.localBodyPointA,globalPointA);
		Transform.mulToOut(spring.bodyB.transform,spring.localBodyPointB,globalPointB);


		applet.line(globalPointA.x, globalPointA.y, globalPointB.x, globalPointB.y);

	}


	protected void drawEdge(Edge edge,Body body)
	{
		applet.pushMatrix();

		applet.translate(body.transform.position.x, body.transform.position.y);
		applet.rotate(body.getAngle());


		applet.line(edge.point1.x, edge.point1.y, edge.point2.x, edge.point2.y);

		applet.popMatrix();
	}


	protected void drawPolygon(Polygon polygon,Body body)
	{
		applet.pushMatrix();

		applet.translate(body.transform.position.x, body.transform.position.y);
		applet.rotate(body.getAngle());


		applet.beginShape();
		for(int i=0; i< polygon.getDim(); i++)
		{
			Vec2 v = polygon.getVertex(i);
			applet.vertex(v.x,v.y);
		}

		applet.endShape(PApplet.CLOSE);

		applet.popMatrix();
	}


	protected void drawCircle(Circle circle, Body body)
	{
		applet.pushMatrix();
		Transform bodyTransform = body.transform;
		applet.translate(bodyTransform.position.x, bodyTransform.position.y);
		applet.rotate(body.getAngle());

		applet.translate(circle.centroid.x, circle.centroid.y);

		applet.ellipse(0, 0, 2*circle.radius, 2*circle.radius);
		applet.line(0, 0, circle.radius, 0);

		applet.popMatrix();

	}

}
