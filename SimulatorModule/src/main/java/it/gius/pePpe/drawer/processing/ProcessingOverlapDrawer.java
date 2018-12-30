package it.gius.pePpe.drawer.processing;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;

import it.gius.data.structures.IdList;
//import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.distance.IDistance2;
import it.gius.pePpe.drawer.DrawerProperties;

public class ProcessingOverlapDrawer extends AbstractProcessingDrawer {

	public ProcessingOverlapDrawer() {
		name = "Processing overlap drawer";
	}

	@Override
	public void setDrawerProperties(DrawerProperties drawerProperties) {
		
	}

	private OverlapSolution overlapSolution = new OverlapSolution();
	//private DistanceSolution distanceSolution = new DistanceSolution();

	private static final int lenght = 15;
	private static final int lenght3 = lenght/3;

	@Override
	public void draw() {
		if(!enable)
			return;


		applet.pushMatrix();

		applet.fill(255,0,0);
		applet.stroke(255,0,0);

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

					//engine.distance(nodeA.id, nodeB.id, distanceSolution);
					IDistance2 distance2 = engine.getDistanceModule();
					Shape shapeA = nodeA.bind.phShape.shape;
					Transform transformA = nodeA.bind.body.transform;
					Shape shapeB = nodeB.bind.phShape.shape;
					Transform transformB = nodeB.bind.body.transform;
					
					//distance2.distance(shapeA, transformA, shapeB, transformB, distanceSolution);
					//if(engine.overlap(nodeA.id, nodeB.id, overlapSolution))
					if(distance2.overlap(shapeA, transformA, shapeB, transformB, overlapSolution))
						drawOverlap(/*distanceSolution,*/ overlapSolution);
				}

			}

		}

		applet.popMatrix();

	}


	private void drawOverlap(/*DistanceSolution distanceSolution,*/ OverlapSolution overlapSolution)
	{
		/*applet.line(overlapSolution.p1.x,overlapSolution.p1.y,
				overlapSolution.p1.x + lenght*overlapSolution.normalPenetration.x,
				overlapSolution.p1.y + lenght*overlapSolution.normalPenetration.y);*/
		drawArrow((int)overlapSolution.p1.x,(int)overlapSolution.p1.y, 
				(int)(overlapSolution.p1.x + lenght*overlapSolution.normal.x),
				(int)(overlapSolution.p1.y + lenght*overlapSolution.normal.y));
		
		applet.text((-overlapSolution.distanceDepth),overlapSolution.p1.x,overlapSolution.p1.y);
	}
	
	private void drawArrow(int x1, int y1, int x2, int y2) {
		  applet.line(x1, y1, x2, y2);
		  applet.pushMatrix();
		  applet.translate(x2, y2);
		  float a = MathUtils.atan2(x1-x2, y2-y1);
		  applet.rotate(a);
		  applet.line(0, 0, -lenght3, -lenght3);
		  applet.line(0, 0, lenght3, -lenght3);
		  applet.popMatrix();
		} 



}
