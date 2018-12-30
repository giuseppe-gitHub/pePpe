package it.gius.pePpe.aabb.sap;


//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;

//import it.gius.pePpe.ShapesListNode;
import it.gius.data.structures.HashSetShort;
import it.gius.data.structures.IdList;
//import it.gius.pePpe.data.structures.HashSetInt;
import it.gius.pePpe.aabb.IAABBManager;
import it.gius.pePpe.aabb.IAABBPairChange;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.aabb.EndPoint;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.physic.BindAABBNode;
//import it.gius.pePpe.data.structures.IdArrayListA;


/**
 * 
 * @author giuseppe
 *
 * @opt operations
 * @composed 1 - 1 it.gius.pePpe.aabb.PairManager
 * @has * ShapesList 1 it.gius.pePpe.data.structures.IdArrayListA<ShapeListNode>
 * @has ? xEnds 1..* it.gius.pePpe.aabb.EndPoint
 * @has ? yEnds 1..* it.gius.pePpe.aabb.EndPoint
 * @depend - - - it.gius.pePpe.aabb.AABoundaryBox
 * @depend - - - it.gius.pePpe.aabb.Pair
 */
public class AABBSweepAndPrune implements IAABBManager {

	public static final int MIN_ARRAYS_SIZE = 32;

	private static final int X_AXIS = AABoundaryBox.X_AXIS;
	private static final int Y_AXIS = AABoundaryBox.Y_AXIS;

	private EndPoint[] xEnds;
	private EndPoint[] yEnds;
	//private Set<Short> activeAabb;
	//private List<Short> activeAabb;
	private HashSetShort activeAabb;

	private int epNumberXAxis = 0;

	//private IdArrayListA<ShapesListNode> shapesList;
	
	//private GlobalData globalData;

	private PairManager pairManager;


	private PairChangeArrayListener listener;


	public AABBSweepAndPrune() {
		pairManager = new PairManager();
		//activeAabb = new HashSet<Short>();
		activeAabb = new HashSetShort();
		//activeAabb = new ArrayList<Short>();
		listener = new PairChangeArrayListener();
		
		pairManager.setListener(listener);

	}

	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#init(it.gius.pePpe.data.structures.IdArrayListA)
	 */
	public void init(IdList<BindAABBNode> shapesList)
	{
		//this.shapesList = list;
		//this.globalData = globalData;
		
		//IdList<BindAABBNode> shapesList = globalData.globalShapes;

		pairManager.clear();

		int numEndPointXaxis = 2*shapesList.size();//globalData.globalShapes.size();
		int dim = Math.max(2*numEndPointXaxis, MIN_ARRAYS_SIZE); //larger array then needed to avoid frequent resizes

		xEnds = new EndPoint[dim];
		yEnds = new EndPoint[dim];

		//int actAabbDim = Math.max(dim/2, MIN_ARRAYS_SIZE);

		//activeAabb = new int[actAabbDim];


		//ShapesListNode[] nodes = globalSata.globalShapes.a_list;
		BindAABBNode[] nodes = shapesList.toLongerArray();
		int size = shapesList.size();

		for(int i=0; i<size; i++)
		{
			int iMin = 2*i;
			int iMax = iMin+1;
			BindAABBNode node = nodes[i];
			AABoundaryBox box = node.box;
			xEnds[iMin] = new EndPoint(); //min
			xEnds[iMin].idShape = node.id; xEnds[iMin].box = node.box;
			xEnds[iMin].min = true;
			xEnds[iMin].value = box.lowerBound.x;

			xEnds[iMax] = new EndPoint(); //max
			xEnds[iMax].idShape = node.id; xEnds[iMax].box = node.box;
			xEnds[iMax].min = false;
			xEnds[iMax].value = box.upperBound.x;

			yEnds[iMin] = new EndPoint(); //min
			yEnds[iMin].idShape = node.id; yEnds[iMin].box = node.box;
			yEnds[iMin].min = true;
			yEnds[iMin].value = box.lowerBound.y;

			yEnds[iMax] = new EndPoint(); //max
			yEnds[iMax].idShape = node.id; yEnds[iMax].box = node.box;
			yEnds[iMax].min = false;
			yEnds[iMax].value = box.upperBound.y;

		}

		epNumberXAxis = numEndPointXaxis;

		//long start = System.currentTimeMillis();

		Arrays.sort(xEnds,0,numEndPointXaxis);
		Arrays.sort(yEnds,0,numEndPointXaxis);

		//long sortTime = System.currentTimeMillis() -start;

		//System.out.println("Sort endPoints time in sap: " + sortTime + " ms");

		for(int i=0; i< epNumberXAxis; i++)
		{
			EndPoint epX = xEnds[i];
			int idX = epX.idShape;
			if(epX.min)
				nodes[idX].box.epMMins[X_AXIS] = i;
			else
				nodes[idX].box.epMMaxs[X_AXIS] = i;		

			EndPoint epY = yEnds[i];
			int idY = epY.idShape;
			if(epY.min)
				nodes[idY].box.epMMins[Y_AXIS] = i;
			else
				nodes[idY].box.epMMaxs[Y_AXIS] = i;
		}


		//long sweepStart = System.currentTimeMillis();
		sweep(shapesList);

		//long timeSweep = System.currentTimeMillis() - sweepStart;

		//System.out.println("Sweep time in sap: " + timeSweep + " ms");	

	}


	private void sweep(IdList<BindAABBNode> shapesList)
	{
		activeAabb.clear();
		
		//IdList<BindAABBNode> shapesList = globalData.globalShapes;
		//int numCurrentActives = 0;
		//BindAABBNode[] nodes =  shapesList.toLongerArray();
		int i;
		int idMinA,idMaxA,idMinB,idMaxB;
		AABoundaryBox boxA,boxB;
		for(i=0; i<epNumberXAxis; i++)
		{
			EndPoint xEnd = xEnds[i];
			//BindAABBNode nodeA = shapesList.get(xEnd.idShape);//nodes[xEnd.idShape];
			boxA = xEnd.box;//nodeA.box;
			if(xEnd.min)
			{
				//activeAabb[numCurrentActives] = xEnd.idShape;
				//for(short id : activeAabb)
				for(int j = 0; j < activeAabb.size(); j++)
				{
					short id = activeAabb.elements[j];
					//checking overlap on y axis: the test is done with the indices of the position
					//of the end points in the array
					boxB = shapesList.get(id).box;//nodes[id].box;


					idMinA = boxA.epMMins[Y_AXIS];
					idMaxA = boxA.epMMaxs[Y_AXIS];

					idMinB = boxB.epMMins[Y_AXIS];
					idMaxB = boxB.epMMaxs[Y_AXIS];

					/*if(boxB.upperBound.y > boxA.lowerBound.y &&
							boxA.upperBound.y > boxB.lowerBound.y)*/

					//if true they overlap in y axis
					if(idMaxA  > idMinB && idMaxB  > idMinA)
						//if(AABoundaryBox.overlap(boxA, boxB))
						pairManager.addPair(xEnd.idShape, id);
				}

				activeAabb.add(xEnd.idShape);
				//numCurrentActives++;
			}
			else
			{
				//remove xEnd.idShape from active AABBs
				activeAabb.remove(xEnd.idShape);
			}
		}
	}

	private void resizeEndArrays()
	{
		EndPoint[] newXEnds = new EndPoint[xEnds.length*2];
		EndPoint[] newYEnds = new EndPoint[xEnds.length*2];

		for(int i=0; i<epNumberXAxis; i++ )
		{
			newXEnds[i] = xEnds[i];
			newYEnds[i] = yEnds[i];
		}

		xEnds = newXEnds;
		yEnds = newYEnds;
	}

	
	//@Override
	public void addListener(IAABBPairChange listener) {
		this.listener.addListener(listener);
		
	}
	
	//@Override
	public void removeListener(IAABBPairChange listener) {
		this.listener.removeListener(listener);
		
	}
	

	@Override
	public void updateAllAABB() {
		sortAndSweepAll();
		
	}
	
	@Override
	public void updatePairs() {
		pairManager.update();
	}

	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#sortAndSweepAll()
	 */
	public void sortAndSweepAll()
	{
		updateEndPoints();
		insertionSortXAndUpdatePairs();

		insertionSortYAndUpdatePairs();

	}


	private void updateEndPoints()
	{
		AABoundaryBox box;
		//ShapesListNode[] nodes = globalData.globalShapes.a_list;
		
		//IdList<BindAABBNode> list = globalData.globalShapes;
		EndPoint currEp;


		//updateing X axis
		for(int i=0; i<epNumberXAxis; i++)
		{
			currEp = xEnds[i];
			box = currEp.box;//list.get(currEp.idShape).box;//nodes[currEp.idShape].box;

			if(currEp.min)
				currEp.value = box.lowerBound.x;
			else
				currEp.value = box.upperBound.x;

		}

		//updating Y axis
		for(int i=0; i<epNumberXAxis; i++)
		{
			currEp = yEnds[i];
			box = currEp.box;//list.get(currEp.idShape).box;//nodes[currEp.idShape].box;

			if(currEp.min)
				currEp.value = box.lowerBound.y;
			else
				currEp.value = box.upperBound.y;

		}
	}


	private void insertionSortXAndUpdatePairs()
	{
		EndPoint epA,epB;
		int indexA;
		AABoundaryBox boxA,boxB;
		//IdList<BindAABBNode> shapesList = globalData.globalShapes;

		for(int i=1; i< epNumberXAxis; i++)
		{
			indexA = i;
			epA =  xEnds[indexA];
			epB = xEnds[indexA-1];
			while(indexA-1 >=0 && epA.value < xEnds[indexA-1].value)
			{
				epB = xEnds[indexA-1];

				boxB =  epB.box;//shapesList.get(epB.idShape).box;

				if(epA.min && !epB.min) // possible start overlap
				{
					boxA = epA.box;//shapesList.get(epA.idShape).box;
					if(AABoundaryBox.overlap(boxA, boxB))
						pairManager.addPair(epA.idShape, epB.idShape);
				}

				if(!epA.min && epB.min) //stop overlapping
				{
					pairManager.removePair(epA.idShape, epB.idShape);
				}


				xEnds[indexA] = epB;
				xEnds[indexA-1] = epA;



				if(epB.min)
					boxB.epMMins[X_AXIS] = indexA;
				else
					boxB.epMMaxs[X_AXIS] = indexA;

				indexA--;

			}

			boxA =  epA.box;//shapesList.get(epA.idShape).box;

			if(epA.min)
				boxA.epMMins[X_AXIS] = indexA;
			else
				boxA.epMMaxs[X_AXIS] = indexA;

		}
	}

	private void insertionSortYAndUpdatePairs()
	{
		EndPoint epA,epB;
		int indexA;
		//IdList<BindAABBNode> shapesList = globalData.globalShapes;
		
		//ShapesListNode[] nodes = shapesList.a_list;
		for(int i=1; i< epNumberXAxis; i++)
		{
			indexA = i;
			epA =  yEnds[indexA];
			epB = yEnds[indexA-1];
			while(indexA-1 >=0 && epA.value < yEnds[indexA-1].value)
			{
				epB = yEnds[indexA-1];


				if(epA.min && !epB.min) // possible start overlap
				{
					//BindAABBNode nodeA = shapesList.get(epA.idShape);//nodes[epA.idShape];
					AABoundaryBox boxA = epA.box;//nodeA.box;

					int indexAXmin = boxA.epMMins[X_AXIS];
					int indexAXmax = boxA.epMMaxs[X_AXIS];

					int indexAYmax = boxA.epMMaxs[Y_AXIS];

					//BindAABBNode nodeB = shapesList.get(epB.idShape);
					AABoundaryBox boxB =  epB.box;//nodeB.box;

					int indexBXmin = boxB.epMMins[X_AXIS];
					int indexBXmax = boxB.epMMaxs[X_AXIS];

					int indexBYmin = boxB.epMMins[Y_AXIS];

					//full test overlap
					if(indexAXmax > indexBXmin && indexBXmax > indexAXmin && yEnds[indexAYmax].value > yEnds[indexBYmin].value)
						pairManager.addPair(epA.idShape, epB.idShape);

				}

				if(!epA.min && epB.min) //stop overlapping
				{
					pairManager.removePair(epA.idShape, epB.idShape);
				}

				yEnds[indexA] = epB;
				yEnds[indexA-1] = epA;

				AABoundaryBox boxB =  epB.box;//shapesList.get(epB.idShape).box;

				if(epB.min)
					boxB.epMMins[Y_AXIS] = indexA;
				else
					boxB.epMMaxs[Y_AXIS] = indexA;

				indexA--;

			}

			AABoundaryBox boxA =  epA.box;//shapesList.get(epA.idShape).box;

			if(epA.min)
				boxA.epMMins[Y_AXIS] = indexA;
			else
				boxA.epMMaxs[Y_AXIS] = indexA;

		}	
	}




	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#removeAABB(it.gius.pePpe.data.aabb.AABoundaryBox[], int)
	 */
	public void removeAABB(AABoundaryBox[] boxes, int num)
	{
		//TODO improve it
		for(int i=0; i< num; i++)
			removeAABB(boxes[i]);
	}


	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#removeAABB(it.gius.pePpe.data.aabb.AABoundaryBox)
	 */
	public void removeAABB(AABoundaryBox boxA)
	{
		int indexAXmin = boxA.epMMins[X_AXIS];
		EndPoint endAXmin = xEnds[indexAXmin];
		//endAXmin.value = boxA.lowerBound.x;
		endAXmin.value = xEnds[epNumberXAxis-1].value +1;

		int indexAXmax = boxA.epMMaxs[X_AXIS];
		EndPoint endAXmax = xEnds[indexAXmax];
		//endAXmax.value = boxA.upperBound.x;
		endAXmax.value = xEnds[epNumberXAxis-1].value +4;



		indexAXmax = updateXEndPoint(endAXmax, indexAXmax);

		indexAXmin = updateXEndPoint(endAXmin, indexAXmin);
		
		if(indexAXmin != epNumberXAxis-2 || indexAXmax != epNumberXAxis-1)
			throw new RuntimeException("Error on removing aabb");

		boxA.epMMins[X_AXIS] = indexAXmin;
		boxA.epMMaxs[X_AXIS] = indexAXmax;


		int indexAYmin = boxA.epMMins[Y_AXIS];
		EndPoint endAYmin = yEnds[indexAYmin];
		//endAXmin.value = boxA.lowerBound.x;
		endAYmin.value = yEnds[epNumberXAxis-1].value +1;

		int indexAYmax = boxA.epMMaxs[Y_AXIS];
		EndPoint endAYmax = yEnds[indexAYmax];
		//endAXmax.value = boxA.upperBound.x;
		endAYmax.value = yEnds[epNumberXAxis-1].value +4;


		indexAYmax = updateYEndPointAndPairs(endAYmax, boxA, indexAYmax);

		indexAYmin = updateYEndPointAndPairs(endAYmin, boxA, indexAYmin);

		if(indexAYmin != epNumberXAxis-2 || indexAYmax != epNumberXAxis-1)
			throw new RuntimeException("Error on removing aabb");
		
		xEnds[indexAXmin].box = null;
		xEnds[indexAXmax].box = null;
		yEnds[indexAYmin].box = null;
		yEnds[indexAYmax].box = null;

		epNumberXAxis = epNumberXAxis-2;


	}


	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#addAABB(it.gius.pePpe.data.aabb.AABoundaryBox[], short[], int)
	 */
	public void addAABB(AABoundaryBox[] boxes, short indices[], int num)
	{
		//TODO improve it
		for(int i=0; i< num; i++)
			addAABB(boxes[i], indices[i]);
	}
	

	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#addAABB(it.gius.pePpe.data.aabb.AABoundaryBox, short)
	 */
	public void addAABB(AABoundaryBox boxA, short indexBoxA)
	{
		if(epNumberXAxis >= xEnds.length)
			resizeEndArrays();

		int index = epNumberXAxis;

		if(xEnds[index] == null)
			xEnds[index] = new EndPoint();

		xEnds[index].idShape = indexBoxA; xEnds[index].box = boxA;
		xEnds[index].min = true;
		boxA.epMMins[X_AXIS] = index;
		xEnds[index].value = boxA.lowerBound.x;

		if(xEnds[index+1] == null)
			xEnds[index+1] = new EndPoint();

		xEnds[index+1].idShape = indexBoxA; xEnds[index+1].box = boxA;
		xEnds[index+1].min = false;
		boxA.epMMaxs[X_AXIS] = index+1;
		xEnds[index+1].value = boxA.upperBound.x;


		if(yEnds[index] == null)
			yEnds[index] = new EndPoint();

		yEnds[index].idShape = indexBoxA; yEnds[index].box = boxA;
		yEnds[index].min = true;
		boxA.epMMins[Y_AXIS] = index;
		yEnds[index].value = boxA.lowerBound.y;

		if(yEnds[index+1] == null)
			yEnds[index+1] = new EndPoint();

		yEnds[index+1].idShape = indexBoxA; yEnds[index+1].box = boxA;
		yEnds[index+1].min = false;
		boxA.epMMaxs[Y_AXIS] = index+1;
		yEnds[index+1].value = boxA.upperBound.y;


		epNumberXAxis = epNumberXAxis+2;

		//updateAABB(boxA);

		int indexAXmin = updateXEndPoint(xEnds[index], index);

		int indexAXmax = updateXEndPoint(xEnds[index+1], index+1);


		boxA.epMMins[X_AXIS] = indexAXmin;
		boxA.epMMaxs[X_AXIS] = indexAXmax;


		int indexAYmin = updateYEndPointAndPairs(yEnds[index], boxA, index);


		int indexAYmax = updateYEndPointAndPairs(yEnds[index+1], boxA, index+1);



		boxA.epMMins[Y_AXIS] = indexAYmin;
		boxA.epMMaxs[Y_AXIS] = indexAYmax;
	}


	
	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#updateAABB(it.gius.pePpe.data.aabb.AABoundaryBox)
	 */
	public void updateAABB(AABoundaryBox boxA)
	{
		int indexAXmin = boxA.epMMins[X_AXIS];
		EndPoint endAXmin = xEnds[indexAXmin];
		endAXmin.value = boxA.lowerBound.x;
		//float oldLowerX = endAXmin.value;

		int indexAXmax = boxA.epMMaxs[X_AXIS];
		EndPoint endAXmax = xEnds[indexAXmax];
		endAXmax.value = boxA.upperBound.x;
		//float oldUpperX = endAXmin.value;

		/*need to change pairs for both y and x axes, if I would check overlap only in y axis, change in position that
		 * happen only in x axis would be lost (why useing updateXEndPointAndPairs instead of just updateXEndPoint)
		 */ 
		//updating minX position
		indexAXmin = updateXEndPointAndPairs(endAXmin, indexAXmin);

		//updating maxX position
		indexAXmax = updateXEndPointAndPairs(endAXmax, indexAXmax);


		boxA.epMMins[X_AXIS] = indexAXmin;
		boxA.epMMaxs[X_AXIS] = indexAXmax;

		int indexAYmin = boxA.epMMins[Y_AXIS];
		EndPoint endAYmin = yEnds[indexAYmin];
		endAYmin.value = boxA.lowerBound.y;

		int indexAYmax = boxA.epMMaxs[Y_AXIS];
		EndPoint endAYmax = yEnds[indexAYmax];
		endAYmax.value = boxA.upperBound.y;


		//updating y axis and adding pairs

		indexAYmin = updateYEndPointAndPairs(endAYmin, boxA, indexAYmin);


		indexAYmax = updateYEndPointAndPairs(endAYmax, boxA, indexAYmax);


		boxA.epMMins[Y_AXIS] = indexAYmin;
		boxA.epMMaxs[Y_AXIS] = indexAYmax;

	}
	

	private int updateXEndPoint(EndPoint ep, int currentIndex)
	{
		
		//IdList<BindAABBNode> shapesList = globalData.globalShapes;

		int indexB = currentIndex-1;
		while(indexB >=0 && ep.value < xEnds[indexB].value) //moving to left
		{
			EndPoint epB = xEnds[indexB];
			xEnds[currentIndex] = epB;
			xEnds[indexB] = ep;

			AABoundaryBox boxB =  epB.box;//shapesList.get(epB.idShape).box;

			if(epB.min)
				boxB.epMMins[X_AXIS] = currentIndex;
			else
				boxB.epMMaxs[X_AXIS] = currentIndex;


			currentIndex = indexB;

			indexB--;
		}


		indexB = currentIndex+1;
		while(indexB < epNumberXAxis && ep.value > xEnds[indexB].value) //moving to right
		{
			EndPoint epB = xEnds[indexB];
			xEnds[currentIndex] = epB;
			xEnds[indexB] = ep;

			AABoundaryBox boxB =  epB.box;//shapesList.get(epB.idShape).box;

			if(epB.min)
				boxB.epMMins[X_AXIS] = currentIndex;
			else
				boxB.epMMaxs[X_AXIS] = currentIndex;

			currentIndex = indexB;

			indexB++;
		}


		return currentIndex;
	}



	private int updateXEndPointAndPairs(EndPoint epA, int currentIndex)
	{

		//IdList<BindAABBNode> shapesList = globalData.globalShapes;
		AABoundaryBox boxA;
		int indexB = currentIndex-1;
		while(indexB >=0 && epA.value < xEnds[indexB].value) //moving to left
		{
			EndPoint epB = xEnds[indexB];
			AABoundaryBox boxB =  epB.box;//shapesList.get(epB.idShape).box;

			if(epA.min && !epB.min) // possible start overlap
			{
				boxA = epA.box;//shapesList.get(epA.idShape).box;
				if(AABoundaryBox.overlap(boxA, boxB))
					pairManager.addPair(epA.idShape, epB.idShape);
			}

			if(!epA.min && epB.min) //stop overlapping
			{
				pairManager.removePair(epA.idShape, epB.idShape);
			}


			xEnds[currentIndex] = epB;
			xEnds[indexB] = epA;


			if(epB.min)
				boxB.epMMins[X_AXIS] = currentIndex;
			else
				boxB.epMMaxs[X_AXIS] = currentIndex;


			currentIndex = indexB;

			indexB--;
		}


		indexB = currentIndex+1;
		while(indexB < epNumberXAxis && epA.value > xEnds[indexB].value) //moving to right
		{
			EndPoint epB = xEnds[indexB];
			AABoundaryBox boxB =  epB.box;//shapesList.get(epB.idShape).box;

			if(!epA.min && epB.min) // possible start overlap
			{
				boxA = epA.box;//shapesList.get(epA.idShape).box;
				if(AABoundaryBox.overlap(boxA, boxB))
					pairManager.addPair(epA.idShape, epB.idShape);
			}

			if(epA.min && !epB.min) //stop overlapping
			{
				pairManager.removePair(epA.idShape, epB.idShape);
			}

			xEnds[currentIndex] = epB;
			xEnds[indexB] = epA;


			if(epB.min)
				boxB.epMMins[X_AXIS] = currentIndex;
			else
				boxB.epMMaxs[X_AXIS] = currentIndex;

			currentIndex = indexB;

			indexB++;
		}


		return currentIndex;
	}




	private int updateYEndPointAndPairs(EndPoint epA, AABoundaryBox boxA, int currentIndex)
	{

		//IdList<BindAABBNode> shapesList = globalData.globalShapes;
		int indexB = currentIndex-1;//indexAYmin-1;
		while(indexB >=0 && epA.value < yEnds[indexB].value) //moving to left
		{

			EndPoint endB = yEnds[indexB];

			if(epA.min && !endB.min) //possible starting overlap
			{
				//BindAABBNode nodeB = shapesList.get(endB.idShape);
				AABoundaryBox boxB =  endB.box;//nodeB.box;

				int indexBXmin = boxB.epMMins[X_AXIS];
				int indexBXmax = boxB.epMMaxs[X_AXIS];

				int indexBYmin = boxB.epMMins[Y_AXIS];


				//full test overlap
				if(boxA.epMMaxs[X_AXIS] > indexBXmin && indexBXmax > boxA.epMMins[X_AXIS] && boxA.upperBound.y/*endAYmax.value*/ > yEnds[indexBYmin].value)
					pairManager.addPair(epA.idShape, endB.idShape/*nodeB.id*/);

			}

			if(!epA.min && endB.min)//stop overlapping
			{
				pairManager.removePair(epA.idShape, endB.idShape);
			}

			yEnds[currentIndex] = endB;
			yEnds[indexB] = epA;//endAYmin;

			AABoundaryBox boxB =  endB.box;//shapesList.get(endB.idShape).box;

			if(endB.min)
				boxB.epMMins[Y_AXIS] = currentIndex;
			else
				boxB.epMMaxs[Y_AXIS] = currentIndex;

			currentIndex = indexB;
			indexB--;
		}



		indexB = currentIndex+1;
		while( indexB < epNumberXAxis && epA.value > yEnds[indexB].value ) //moving to right
		{
			EndPoint endB = yEnds[indexB];

			if(!epA.min && endB.min) //possible start overlapping
			{
				//BindAABBNode nodeB = shapesList.get(endB.idShape);
				AABoundaryBox boxB =  endB.box;//nodeB.box;

				int indexBXmin = boxB.epMMins[X_AXIS];
				int indexBXmax = boxB.epMMaxs[X_AXIS];

				int indexBYmax = boxB.epMMaxs[Y_AXIS];


				//full test overlap
				if(boxA.epMMaxs[X_AXIS] > indexBXmin && indexBXmax > boxA.epMMins[X_AXIS] && yEnds[indexBYmax].value > boxA.lowerBound.y)
					pairManager.addPair(epA.idShape, endB.idShape /*nodeB.id*/);

			}

			if(epA.min && !endB.min)//stop overlapping
			{
				pairManager.removePair(epA.idShape, endB.idShape);
			}


			yEnds[currentIndex] = endB;
			yEnds[indexB] = epA;


			AABoundaryBox boxB =  endB.box; //shapesList.get(endB.idShape).box;

			if(endB.min)
				boxB.epMMins[Y_AXIS] = currentIndex;
			else
				boxB.epMMaxs[Y_AXIS] = currentIndex;

			currentIndex = indexB;
			indexB++;
		}


		return currentIndex;
	}




	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#updateAndGetPairs()
	 */
	public AABBPair[] updateAndGetPairs()
	{
		pairManager.update();
		return pairManager.getActivePairs();
	}
	
	public AABBPair[] getPairs()
	{
		return pairManager.getActivePairs();
	}

	/* (non-Javadoc)
	 * @see it.gius.pePpe.aabb.AABBManager#getPairsNumber()
	 */
	public int getPairsNumber()
	{
		return pairManager.getCurrentPairsNumber();
	}

}
