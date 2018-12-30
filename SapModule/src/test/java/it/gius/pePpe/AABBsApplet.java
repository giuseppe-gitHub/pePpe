package it.gius.pePpe;

//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jbox2d.common.Vec2;

import it.gius.data.structures.IdList;
import it.gius.pePpe.aabb.sap.AABBSweepAndPrune;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.processing.util.MyAbstractPApplet;



public class AABBsApplet extends MyAbstractPApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4315260381652628174L;
	
	
	//private GlobalData globalData;
	private IdList<BindAABBNode> list;
	private AABBPair[] pairs;
	private int pairsNumber;
	private AABBSweepAndPrune sap;
	
	private Map<Short,Boolean> overlapping;
	
	private boolean move = true;
	
	private Random random;
	
	
	public AABBsApplet(IdList<BindAABBNode> list) {
		this.list = list;
		overlapping = new HashMap<Short, Boolean>();
		sap = new AABBSweepAndPrune();
		
		int size = list.size();
		
		BindAABBNode[] arrayNode = list.toLongerArray();
		
		for(int i=0; i< size; i++)
		{
			BindAABBNode node =  arrayNode[i];
			
			overlapping.put(node.id, false);
			
		}
		
		random = new Random(System.currentTimeMillis());
		
	}
	
	
	
	public void setup(){
		super.setup();
		
		size(400,400);
		
		//globalData = new GlobalData();
		//globalData.globalShapes = list;
		
		sap.init(/*globalData.globalShapes*/list);
		
		pairs = sap.updateAndGetPairs();
		pairsNumber = sap.getPairsNumber();
		
		
		if(!goStop.isGo())
			noLoop();
	}
	
	

	@Override
	public void keyPressed() {
		super.keyPressed();
		
		move = true;
		
		if(key == 'm' && !goStop.isGo())
			redraw();
		
		if( key== 'c' && !goStop.isGo())
		{
			move = false;
			redraw();
		}
		
		if( key == 'a' ) //add
		{
			move = false;
			BindAABBNode node = new BindAABBNode();
			node.box = new AABoundaryBox();
			
			node.bind = PhysicClassAcces.getTestIstance().getNewBind(new BindInit());//new Bind(new BindInit());
			
			AABoundaryBox box = node.box;

			 box.lowerBound.x =  5+ 350*random.nextFloat();
			 box.upperBound.x = box.lowerBound.x +5+ 40*random.nextFloat();
			 
			 box.lowerBound.y =  5+ 350*random.nextFloat();
			 box.upperBound.y = box.lowerBound.y +5+ 40*random.nextFloat();
			
			list.add(node);
			
			sap.addAABB(node.box, node.id);
			
			redraw();
		}
		
		
		if( key == 'r' ) //remove
		{
			if(list.size() == 0)
				return;
			
			move = false;
						
			int n = random.nextInt(list.size());
						
			BindAABBNode[] array = list.toLongerArray();
			BindAABBNode node = array[n];
			
			sap.removeAABB(node.box);
			
			list.remove(node.id);
			
			System.out.println("Node " + node.id + " removed");
			
			redraw();
		}
		
	}
	
	
	public void draw()
	{
		//println("draw");
		
		if(move)
			move();
		
		sap.sortAndSweepAll();
		BindAABBNode node;
		AABoundaryBox box;
		/*for(int i=0; i< list.getNodeNumber(); i++)
		{
			node = list.a_list[index];
			box = node.box;
			
			sap.updateAABB(box);
			
			index = node.next;
		}*/
		
		pairs = sap.updateAndGetPairs();
		pairsNumber = sap.getPairsNumber();
		
		System.out.print("[ ");
		for(int i=0; i< pairsNumber; i++)
			System.out.print(pairs[i] +", ");
		
		System.out.println(" ]");
		
		background(255);
	
		System.out.println("Draw");
		
		noFill();
		strokeWeight(1);
		
			int size = list.size();
			BindAABBNode[] arrayNode = list.toLongerArray();
		for(int i=0; i< size; i++)
		{
			node =  arrayNode[i];
			
			overlapping.put(node.id, false);
			
		}
		
		for(int i=0; i<pairsNumber; i++)
		{
			overlapping.put(pairs[i].idShapeA, true);
			overlapping.put(pairs[i].idShapeB, true);
		}
				

		for(int i=0; i< size;i++)
		{
			node = arrayNode[i];
			box = node.box;
			
			//drawBBox(bboxs[i]);
			Vec2 l = box.lowerBound;
			Vec2 u = box.upperBound;

			
			
			stroke(0);
			fill(0);
			
			int averageX = (int)(u.x+l.x)/2;
			int averageY = (int)(u.y+l.y)/2;
			text(node.id,averageX, averageY);
			noFill();
			strokeWeight(1);
			if(overlapping.get(node.id))
				stroke(240,0,0);
			else
				stroke(0,0,240);
			
			rect(l.x,l.y, u.x - l.x, u.y - l.y);
			
			
		}
		

		
		
	}
	
	
	private void move()
	{
		 
		 Random random = new Random(System.currentTimeMillis());
		 
		 int size = list.size();
		 BindAABBNode[] arrayNode = list.toLongerArray();
		 for(int i=0; i<size; i++)
		 {
			 BindAABBNode node = arrayNode[i];
			 AABoundaryBox box = node.box;
			 
			 box.lowerBound.x = (box.lowerBound.x -5) +10*random.nextFloat();
			 box.upperBound.x = box.lowerBound.x +5 +40*random.nextFloat();
			 
			 box.lowerBound.y = (box.lowerBound.y -5) +10*random.nextFloat();
			 box.upperBound.y = box.lowerBound.y +5 +40*random.nextFloat();
			 
		 }
	}
	
	
	/*private void drawBBox(BoundaryBox bbox)
	{
		Vec2 l = bbox.getLowerBound();
		Vec2 u = bbox.getUpperBound();
		
		beginShape();
		vertex(l.x,l.y);
		vertex(l.x,u.y);
		vertex(u.x,u.y);
		vertex(u.x,l.y);
		endShape(PApplet.CLOSE);
	}*/

}
