package it.gius.pePpe;

import java.util.Random;

import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.processing.util.PAppletManager;
import it.gius.processing.util.ProcessingGraphicException;

public class SAPAppletMain {

	/**
	 * @param args
	 */
	private static IdList<BindAABBNode> list;
	
	private static final int NUM_SHAPES = 15;
	
	public static void main(String[] args) {
		
		init();
		
		PAppletManager manager = new PAppletManager(true,true);
		
		AABBsApplet applet = new AABBsApplet(list);
		
		try {
			manager.startAndAddApplet(new String[]{""}, applet, 
					PAppletManager.EXIT_ON_CLOSE);
		} catch (ProcessingGraphicException e) {
			e.printStackTrace();
		}

	}
	
	
	private static void init()
	{
		list = new IdDoubleArrayList<BindAABBNode>(BindAABBNode.class,(short)30);
		 
		 
		 Random random = new Random(System.currentTimeMillis());
		 
		 for(int i=0; i<NUM_SHAPES; i++)
		 {
			 BindAABBNode node = new BindAABBNode();
			 node.box = new AABoundaryBox();
			 AABoundaryBox box = node.box;
			 
			 node.bind = PhysicClassAcces.getTestIstance().getNewBind(new BindInit());//new Bind(new BindInit());
			 
			 box.lowerBound.x =  5+ 350*random.nextFloat();
			 box.upperBound.x = box.lowerBound.x +5+ 40*random.nextFloat();
			 
			 box.lowerBound.y =  5+ 350*random.nextFloat();
			 box.upperBound.y = box.lowerBound.y +5+ 40*random.nextFloat();
			 
			 list.add(node);
		 }
		
	}
	
	

}
