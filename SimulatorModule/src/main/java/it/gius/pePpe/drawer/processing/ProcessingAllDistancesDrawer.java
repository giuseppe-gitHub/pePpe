package it.gius.pePpe.drawer.processing;

import org.jbox2d.common.Transform;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.distance.IDistance2;
import it.gius.pePpe.drawer.DrawerProperties;

public class ProcessingAllDistancesDrawer extends AbstractProcessingDrawer {

	public ProcessingAllDistancesDrawer() {
		name = "Processing All Distances drawer";
	}

	private DistanceSolution distSol = new DistanceSolution();

	private int colorDistances = 0xFFF00000;

	@Override
	public void setDrawerProperties(DrawerProperties drawerProperties) {
		this.colorDistances = defaultHexAlpha + drawerProperties.colorDistances;
	}

	@Override
	public void draw() {
		if(!enable)
			return;


		applet.pushMatrix();

			applet.fill(colorDistances);
			applet.stroke(colorDistances);

		IdList<BindAABBNode> list = engine.getGlobalShapes();

		int size = list.size();

		BindAABBNode[] arrayNode = list.toLongerArray();

		for(int i=0; i< size;i++)
		{
			BindAABBNode nodeA = arrayNode[i];


			for(int j=i+1; j<size;j++)
			{
				BindAABBNode nodeB = arrayNode[j];

				if(nodeA.bind.body != nodeB.bind.body)
				{

					//engine.distance(nodeA.id, nodeB.id, distSol);
					IDistance2 distance2 = engine.getDistanceModule();
					Shape shapeA = nodeA.bind.phShape.shape;
					Transform transformA = nodeA.bind.body.transform;
					Shape shapeB = nodeB.bind.phShape.shape;
					Transform transformB = nodeB.bind.body.transform;
					
					distance2.distance(shapeA,transformA,shapeB,transformB,distSol);
					//OverlapSolution oSol = new OverlapSolution();
					//distance2.overlap(shapeA, transformA, shapeB, transformB, oSol);

					drawDistance(distSol);
					//drawDistance(oSol);
				}

			}

		}

		applet.popMatrix();

	}

	@SuppressWarnings("all")
	private void drawDistance(OverlapSolution overlapSol)
	{
		applet.line(overlapSol.p1.x, overlapSol.p1.y, overlapSol.p2.x, overlapSol.p2.y);
		applet.ellipse(overlapSol.p1.x,overlapSol.p1.y,5,5);
		applet.ellipse(overlapSol.p2.x,overlapSol.p2.y,5,5);
		
		applet.text(overlapSol.distanceDepth, overlapSol.p1.x, overlapSol.p1.y);
	}

	@SuppressWarnings("all")
	private void drawDistance(DistanceSolution distSol)
	{
		applet.line(distSol.p1.x, distSol.p1.y, distSol.p2.x, distSol.p2.y);
		applet.ellipse(distSol.p1.x,distSol.p1.y,5,5);
		applet.ellipse(distSol.p2.x,distSol.p2.y,5,5);
		
		//applet.text(distSol.distance,distSol.p1.x,distSol.p1.y);
		
	}




}
