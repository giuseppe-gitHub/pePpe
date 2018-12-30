package it.gius.pePpe;

import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;
import it.gius.pePpe.aabb.sap.AABBSweepAndPrune;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.IGetAccesData;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import junit.framework.TestCase;

public class AABBSweepAndPruneTest extends TestCase implements IGetAccesData {

	private IdList<BindAABBNode> listShapes;
	private BindAABBNode nodeA,nodeB,nodeC;
	private AABBSweepAndPrune sap;
	
	private PhysicClassAcces access = null;
	
	@Override
	public void setAccess(PhysicClassAcces access) {
		this.access = access;	
	}

	public void testInit() {
		init();
		
		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;
		
		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail("Num pairs wrong");

		if(pairs[0].idShapeA != nodeA.id)
			fail();

		if(pairs[0].idShapeB != nodeB.id)
			fail();
	}

	public void testAddAABBAABoundaryBoxShort1() {
		init();

		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;
		
		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();


		AABBPair resultA = new AABBPair();
		resultA.idShapeA = nodeA.id;
		resultA.idShapeB = nodeB.id;

		if(numPairs != 1)
			fail();

		if(!pairs[0].equals(resultA))
			fail();

		BindAABBNode nodeD = new BindAABBNode();
		nodeD.box = new AABoundaryBox();
		nodeD.box.lowerBound.x = 4;
		nodeD.box.upperBound.x = 15;

		nodeD.box.lowerBound.y = 4;
		nodeD.box.upperBound.y = 18;
		
		nodeD.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());


		listShapes.add(nodeD);
		sap.addAABB(nodeD.box, nodeD.id);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 3)
			fail();


		AABBPair resultB = new AABBPair();
		resultB.idShapeA = nodeB.id;
		resultB.idShapeB = nodeD.id;

		AABBPair resultC = new AABBPair();
		resultC.idShapeA = nodeA.id;
		resultC.idShapeB = nodeD.id;


		AABBPair[] attended = new AABBPair[3];

		attended[0] = resultA;
		attended[1] = resultB;
		attended[2] = resultC;

		if(!assertPairs(pairs, attended, 3))
			fail();



		BindAABBNode nodeE = new BindAABBNode();
		nodeE.box = new AABoundaryBox();
		nodeE.box.lowerBound.x = 28;
		nodeE.box.upperBound.x = 33;

		nodeE.box.lowerBound.y = 23;
		nodeE.box.upperBound.y = 40;

		nodeE.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());
		
		listShapes.add(nodeE);
		sap.addAABB(nodeE.box, nodeE.id);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 4)
		{
			System.out.println(numPairs);
			fail();
		}

		AABBPair resultD = new AABBPair();
		resultD.idShapeA = nodeC.id;
		resultD.idShapeB = nodeE.id;

		attended = new AABBPair[4];

		attended[0] = resultA;
		attended[1] = resultB;
		attended[2] = resultC;
		attended[3] = resultD;

		if(!assertPairs(pairs, attended, 4))
			fail();
	}


	private boolean assertPairs(AABBPair[] a, AABBPair[] b, int lenght)
	{
		for(int i=0; i< lenght; i++)
		{
			boolean present = false;
			for(int j=0; j< lenght; j++)
			{
				if(a[i].equals(b[j]))
					present = true;
			}

			if(!present)
				return false;
		}

		return true;
	}

	public void testUpdateAABB1() {
		init();

		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;
		
		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail("Num pairs wrong");

		if(pairs[0].idShapeA != nodeA.id)
			fail();

		if(pairs[0].idShapeB != nodeB.id)
			fail();

		nodeC.box.lowerBound.x = 4;
		nodeC.box.upperBound.x = 12;

		nodeC.box.lowerBound.y = 13;
		nodeC.box.upperBound.y = 20;

		sap.updateAABB(nodeC.box);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 2)
			fail();

		AABBPair resultA = new AABBPair();
		resultA.idShapeA = nodeA.id;
		resultA.idShapeB = nodeB.id;

		AABBPair resultB = new AABBPair();
		resultA.idShapeA = nodeB.id;
		resultA.idShapeB = nodeC.id;

		if(!(pairs[0].equals(resultA) && pairs[1].equals(resultB)))
			if((pairs[1].equals(resultA) && pairs[0].equals(resultB)))
				fail();
	}

	public void testUpdateAABB2() {
		init();
		
		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;

		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail("Num pairs wrong");

		if(pairs[0].idShapeA != nodeA.id)
			fail();

		if(pairs[0].idShapeB != nodeB.id)
			fail();

		nodeB.box.lowerBound.x = 27;
		nodeB.box.upperBound.x = 33;

		nodeB.box.lowerBound.y = 17;
		nodeB.box.upperBound.y = 22;

		sap.updateAABB(nodeB.box);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail();

		AABBPair resultA = new AABBPair();
		resultA.idShapeA = nodeB.id;
		resultA.idShapeB = nodeC.id;


		if(!pairs[0].equals(resultA))
			fail();
	}


	public void testUpdateAABB3() {
		init();
		
		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;

		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail("Num pairs wrong");

		if(pairs[0].idShapeA != nodeA.id)
			fail();

		if(pairs[0].idShapeB != nodeB.id)
			fail();

		nodeB.box.lowerBound.x = -100;
		nodeB.box.upperBound.x = -90;

		nodeB.box.lowerBound.y = -200;
		nodeB.box.upperBound.y = -160;

		sap.updateAABB(nodeB.box);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 0)
			fail();

	}




	public void testRemoveAABBAABoundaryBox1() {
		init();


		BindAABBNode nodeD = new BindAABBNode();
		nodeD.box = new AABoundaryBox();
		nodeD.box.lowerBound.x = 4;
		nodeD.box.upperBound.x = 15;

		nodeD.box.lowerBound.y = 4;
		nodeD.box.upperBound.y = 18;
		
		nodeD.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());

		listShapes.add(nodeD);

		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;
		
		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		@SuppressWarnings("all")
		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();

		if(numPairs != 3)
			fail();

		sap.removeAABB(nodeC.box);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 3)
			fail();


		sap.removeAABB(nodeB.box);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail();
	}

	public void testRemoveAABBAABoundaryBox2() {
		init();
		
		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;

		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail();

		BindAABBNode nodeD = new BindAABBNode();
		nodeD.box = new AABoundaryBox();
		nodeD.box.lowerBound.x = 4;
		nodeD.box.upperBound.x = 15;

		nodeD.box.lowerBound.y = 4;
		nodeD.box.upperBound.y = 18;
		
		nodeD.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());

		listShapes.add(nodeD);

		sap.addAABB(nodeD.box,nodeD.id);

		sap.removeAABB(nodeB.box);

		listShapes.remove(nodeB.id);

		pairs = sap.updateAndGetPairs();
		numPairs = sap.getPairsNumber();

		if(numPairs != 1)
			fail();


		AABBPair attended = new AABBPair();
		attended.idShapeA = nodeA.id;
		attended.idShapeB = nodeD.id;

		if(!pairs[0].equals(attended))
			fail();


	}


	public void testsortAndSweepAll()
	{
		init();
		
		//GlobalData globalData = new GlobalData();
		//globalData.globalShapes = listShapes;

		sap = new AABBSweepAndPrune();
		sap.init(listShapes);

		AABBPair[] pairs = sap.updateAndGetPairs();
		int nPairs = sap.getPairsNumber();

		if(nPairs !=1)
			fail();

		nodeC.box.lowerBound.x = 4;
		nodeC.box.upperBound.x = 15;

		nodeC.box.lowerBound.y = 4;
		nodeC.box.upperBound.y = 18;

		sap.sortAndSweepAll();

		pairs = sap.updateAndGetPairs();
		nPairs = sap.getPairsNumber();

		if(nPairs != 3)
			fail();


		nodeA.box.lowerBound.x = 50;
		nodeA.box.upperBound.x = 55;

		nodeA.box.lowerBound.y = 50;
		nodeA.box.upperBound.y = 55;

		sap.sortAndSweepAll();

		pairs = sap.updateAndGetPairs();
		nPairs = sap.getPairsNumber();

		if(nPairs != 1)
			fail();
		
		AABBPair attended = new AABBPair();
		attended.idShapeA = nodeB.id;
		attended.idShapeB = nodeC.id;
		
		if(!pairs[0].equals(attended))
			fail();

	}


	private void init()
	{
		
		if(access == null)
			PhysicClassAcces.getIstance(this);
		
		listShapes = new IdDoubleArrayList<BindAABBNode>(BindAABBNode.class,3);

		nodeA = new BindAABBNode();
		AABoundaryBox boxA = new AABoundaryBox();
		boxA.lowerBound.x = 0;
		boxA.upperBound.x = 10;

		boxA.lowerBound.y = 0;
		boxA.upperBound.y = 10;

		nodeA.box = boxA;
		nodeA.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());

		listShapes.add(nodeA);

		nodeB = new BindAABBNode();
		AABoundaryBox boxB = new AABoundaryBox();
		boxB.lowerBound.x = 6;
		boxB.upperBound.x = 20;

		boxB.lowerBound.y = 6;
		boxB.upperBound.y = 15;

		nodeB.box = boxB;
		nodeB.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());

		listShapes.add(nodeB);

		nodeC = new BindAABBNode();
		AABoundaryBox boxC = new AABoundaryBox();
		boxC.lowerBound.x = 25;
		boxC.upperBound.x = 30;

		boxC.lowerBound.y = 20;
		boxC.upperBound.y = 25;

		nodeC.box = boxC;
		nodeC.bind = access.getNewBind(new BindInit());//new Bind(new BindInit());
		
		listShapes.add(nodeC);

	}

}
