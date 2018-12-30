package it.gius.pePpe.aabb;


import it.gius.pePpe.aabb.sap.PairManager;
import it.gius.pePpe.data.aabb.AABBPair;

import java.util.Random;

import junit.framework.TestCase;

public class PairManagerTest extends TestCase {

	private final static int size = 20;
	private AABBPair[] arrayPair;
	private Random random;

	public PairManagerTest() {

		arrayPair = new AABBPair[size];
		 random = new Random(System.currentTimeMillis());
		for(int i=0; i< arrayPair.length;i++)
		{
			arrayPair[i] = new AABBPair();
			int a = i*size + random.nextInt(size);
			arrayPair[i].idShapeA = (short)a;
			int b = i*size + (short)random.nextInt(size);
			arrayPair[i].idShapeB = (short)b; 

			if(arrayPair[i].idShapeA == arrayPair[i].idShapeB)
				arrayPair[i].idShapeB++;
			
			if(arrayPair[i].idShapeA > arrayPair[i].idShapeB)
			{
				short temp = arrayPair[i].idShapeA;
				arrayPair[i].idShapeA = arrayPair[i].idShapeB;
				arrayPair[i].idShapeB = temp;
			}
		}

	}

	public void testAddSome() {

		PairManager manager = new PairManager();

		int n = random.nextInt(size);
		
		for(int i=0; i< n; i++)
		{
			manager.addPair(arrayPair[i].idShapeA, arrayPair[i].idShapeB);
		}

		manager.update();
		if(manager.getCurrentPairsNumber() != n)
			fail();
		
		AABBPair[] result = manager.getActivePairs();
		
		for(int i=0; i<n;i++)
		{
			boolean present = false;
			for(int j=0; j<n; j++)
			{
				if(result[i].equals(arrayPair[j]))
					present = true;
			}
			
			if(!present)
				fail();
		}
	}
	
	public void testAddSomeAndDelete() {

		PairManager manager = new PairManager();

		int n = 2+ random.nextInt(size-2);
		
		for(int i=0; i< n; i++)
		{
			manager.addPair(arrayPair[i].idShapeA, arrayPair[i].idShapeB);
		}
		
		
		int nDelete = random.nextInt(n);
		while(nDelete >= n)
			nDelete = nDelete/2;
		
		if(nDelete == 0)
			nDelete = 2;
		
		int delete[] = new int[nDelete];
		
		int temp;
		if(n-nDelete <= 0)
			temp = 0;
		else
			temp = random.nextInt(n-nDelete);

		delete[0] = temp;
		for(int i=1; i<delete.length; i++)
		{
			delete[i] = delete[i-1] +1; 
		}
		
		for(int i=0; i< delete.length; i++)
			manager.removePair(arrayPair[delete[i]].idShapeA,
					arrayPair[delete[i]].idShapeB);

		
		manager.update();
		
		if(manager.getCurrentPairsNumber() != n-nDelete)
			fail();
		
		AABBPair[] result = manager.getActivePairs();
		
		for(int i=0; i<n-nDelete;i++)
		{
			boolean present = false;
			for(int j=0; j<n; j++)
			{
				if(result[i].equals(arrayPair[j]))
					present = true;
			}
			
			if(!present)
			{
				boolean deletionOk = false;
				for(int k = 0; k<n; k++)
				{
					if(result[i].equals(arrayPair[delete[k]]))
						deletionOk = true;
						
				}
				if(!deletionOk)
					fail();
			}
		}
	}

	public void testAddRemoveSameItemMultipleTime() {

		PairManager manager = new PairManager();

		for(int i=0; i< 3; i++)
		{
			manager.addPair(arrayPair[0].idShapeA, arrayPair[0].idShapeB);
			manager.removePair(arrayPair[0].idShapeA, arrayPair[0].idShapeB);
		}

		manager.update();
		if(manager.getCurrentPairsNumber() != 0)
			fail();
	}

}
