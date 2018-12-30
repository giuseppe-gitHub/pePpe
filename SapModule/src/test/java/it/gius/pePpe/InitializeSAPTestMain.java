package it.gius.pePpe;

import java.util.Random;

import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;
import it.gius.pePpe.aabb.sap.AABBSweepAndPrune;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.pePpe.data.physic.ShapesListNode;
//import it.gius.pePpe.data.structures.IdArrayListA;
import it.gius.pePpe.data.physic.BindInit;

@SuppressWarnings("all")
public class InitializeSAPTestMain {

	/**
	 * @param args
	 */
	private static Random random = new Random(System.currentTimeMillis());
	
	private  static long timeSap;
	
	private static long timeBf;
	
	
	
	public static void main(String[] args) {
		int testTimes = 1;
		int size = 5000;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Testing " + testTimes + " times");
		System.out.println("Testing " + size + " shapes");
		
		long averageTimeSap = 0;
		long averageBF = 0;
		
		
		for(int i=0; i< testTimes; i++)
		{
			bruteForceDim = 0;
			test(size);
			
			Thread.yield();
			averageTimeSap = averageTimeSap*(i) + timeSap;
			averageTimeSap = averageTimeSap/(i+1);
			
			averageBF = averageBF*(i) + timeBf;
			averageBF = averageBF/(i+1);
			
			
			System.gc();
		}
		
		System.out.println();
		System.out.println();
		System.out.println("Average time sap: " + averageTimeSap + " ms");
		System.out.println("Average time bf: " + averageBF + " ms");
	}
	
	public static void test(int size) {
		
		int maxOverlaps = (size*size) -size;
		
		//System.out.println("Testing " + size + " shapes");
		//System.out.println(maxOverlaps  + " possible overlaps");
		
		IdList<BindAABBNode> list = new IdDoubleArrayList<BindAABBNode>(BindAABBNode.class);
		
		long startInit = System.currentTimeMillis();
		for(int i=0; i< size; i++)
		{
			BindAABBNode node = new BindAABBNode();
			node.box = new AABoundaryBox();
			
			BindInit bindInit = new BindInit();
			Bind bind = PhysicClassAcces.getTestIstance().getNewBind(new BindInit());//new Bind(bindInit); 
			node.bind = bind;
			
			node.box.lowerBound.x = random.nextFloat()*200;
			node.box.upperBound.x = node.box.lowerBound.x + random.nextFloat()*15;
			
			node.box.lowerBound.y = random.nextFloat() * 200;
			node.box.upperBound.y = node.box.lowerBound.y + random.nextFloat()*15;
			list.add(node);
		}
		long stopInit = System.currentTimeMillis();
		
		long timeForInit = stopInit - startInit;
		
		//System.out.println("Init in: " + timeForInit + " ms");
		
		AABBSweepAndPrune sap = new AABBSweepAndPrune();
		
		long timeStartSap = System.currentTimeMillis();
		//GlobalData globalData = new GlobalData();
		
		//globalData.globalShapes = list;
		
		sap.init(/*globalData.globalShapes*/list);
		
		AABBPair[] sapPairs = sap.updateAndGetPairs();
		int sapNPairs = sap.getPairsNumber();
		
		System.out.println("sapNPairs: " + sapNPairs);
		
		long timeEndSap = System.currentTimeMillis();
		
		timeSap = timeEndSap - timeStartSap;
		
		if(sapPairs[sapNPairs] != null)
			System.out.println("sapPairs[sapNPairs]: "+ sapPairs[sapNPairs]);
		
		long timeStartBf = System.currentTimeMillis();
		AABBPair[] bruteForcePairs = bruteForceTest(list);
		long timeEndBf = System.currentTimeMillis();
		
		timeBf = timeEndBf - timeStartBf;
		
		if(bruteForcePairs[sapNPairs] != null)
			System.out.println("bruteForcePairs[sapNPairs]: " + bruteForcePairs[sapNPairs]);
		
		System.out.println("bruteForceDim: " + bruteForceDim);
		
		float perCentOverlap = (float)sapNPairs*100/(float)maxOverlaps;
		
		if(sapNPairs == bruteForceDim)
			System.out.println("PerCent overlap found on total possible: " + perCentOverlap + "%");
		
		 
		
		long compareStart = System.currentTimeMillis();
		//boolean result = compare(sapPairs, sapNPairs, bruteForcePairs, bruteForceDim);
		long compareEnd = System.currentTimeMillis();
		
		long compareTime = compareEnd - compareStart;
		
		//System.out.println("time for comparing the two results: " + compareTime + " ms");
		
		
		if(false /*!result*/)
		{
			//System.out.println("Correct result");
			//System.out.println("AABB overlapping: " + sapNPairs);
			//System.out.println("Time Sap: " + timeSap + " ms");
			
			//System.out.println("Time brute force: " + timeBf + " ms");
			
			throw new IllegalStateException();
			
		}
		
		//assert(result);
		
	}
	
	
	private static int bruteForceDim = 0;
	
	private static AABBPair[] bruteForceTest(IdList<BindAABBNode> list)
	{
		AABBPair[] bruteForcePairs = new AABBPair[list.size()];
		
		BindAABBNode arrayNode[] = list.toLongerArray();
		
		for(int i=0; i<list.size(); i++)
		{
			BindAABBNode nodeA = arrayNode[i];
			AABoundaryBox boxA = nodeA.box;

			for(int j=i+1; j< list.size();j++)
			{
				BindAABBNode nodeB = arrayNode[j];
				AABoundaryBox boxB = nodeB.box;
				
				if(AABoundaryBox.overlap(boxA, boxB))
				{
					
					if(bruteForceDim >= bruteForcePairs.length)
					{
						AABBPair[] newArray = new AABBPair[bruteForceDim*2];
						
						for(int k=0; k< bruteForcePairs.length;k++)
							newArray[k] = bruteForcePairs[k];
						
						bruteForcePairs = newArray;
					}
					
					bruteForcePairs[bruteForceDim] = new AABBPair();
					bruteForcePairs[bruteForceDim].idShapeA = nodeA.id;
					bruteForcePairs[bruteForceDim].idShapeB = nodeB.id;
					
					bruteForceDim++;
				}
				
			}
			
		}
		
		return bruteForcePairs;
	}
	
	
	private static boolean compare(AABBPair[] a, int sizeA, AABBPair[] b, int sizeB)
	{
		if(sizeA != sizeB)
			return false;
		
		for(int i=0; i< sizeA; i++)
		{
			boolean present = false;
			for(int j=0;!present && j<sizeB; j++)
			{
				if(a[i].equals(b[j]))
					present = true;
			}
			
			if(!present)
				return false;
		}
		
		return true;
	}

}
