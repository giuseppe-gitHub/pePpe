package it.gius.data.structures;

import java.lang.reflect.Array;
//import java.util.Arrays;



public class IdArrayListB<T extends IdNode> {

	public static final short DEFAULT_SIZE = 16;
	public static final short NULL_NODE = -1;

	private T[] a_list;
	private short[] positions;
	private final Class<T> tClass;

	/*public short rootNode = NULL_NODE;
	public short lastNode = NULL_NODE;*/

	private short nodeNumber = 0;


	@SuppressWarnings("unchecked")
	public IdArrayListB(Class<T> tClass, int startSize) {
		this.tClass = tClass;
		int dim  = Math.max(startSize, DEFAULT_SIZE);
		try {
			a_list = (T[])Array.newInstance(tClass, dim);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}


		positions = new short[dim];
		
		for(int i=0; i<a_list.length; i++)
		{
			a_list[i]= null;
			positions[i] = NULL_NODE;
		}

	}

	@SuppressWarnings("unchecked")
	private void resizeArrays()
	{
		T[] newShapes = null;
		short[] newPositions; 
		
		try {
			newShapes = (T[])Array.newInstance(tClass, a_list.length*2);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		newPositions = new short[a_list.length*2];

		for(int i = 0; i < a_list.length; i++)
		{
			newShapes[i] = a_list[i];
			newPositions[i] = positions[i];
			/*newShapes[i].id = a_list[i].id;
			newShapes[i].next = a_list[i].next;*/
		}
		

		for(int i = a_list.length; i < newShapes.length; i++)
		{
			newShapes[i] = null;
			newPositions[i] = NULL_NODE;
		}

		a_list = newShapes;
		positions = newPositions;
	}
	
	
	private short nextFree()
	{
		short nextFree = 0;
		for(;nextFree<positions.length && positions[nextFree]!= NULL_NODE;nextFree++);
		
		if(positions[nextFree]!= NULL_NODE)
			return NULL_NODE;
		
		return nextFree;
	}
	
	
	public T[] toLongerArray()
	{
		return a_list;
	}
	
	public T get(int id)
	{
		short pos = positions[id];
		
		if(pos == NULL_NODE)
			return null;
		
		return a_list[pos];
	}
	
	public short addItem(T node)
	{
		if(nodeNumber == a_list.length)
			resizeArrays();
		
		short id = nextFree();
		
		a_list[nodeNumber] = node;
		node.id = id;
		positions[id] = nodeNumber;
		
		nodeNumber++;
		
		return id;
		
		/*short freeSlot = nextFree();
		
		if(rootNode == NULL_NODE)
			rootNode = freeSlot;
		else
			a_list[lastNode].next = freeSlot;
		
		a_list[freeSlot] = node;
		
		a_list[freeSlot].id = freeSlot;
		a_list[freeSlot].next = NULL_NODE;
		a_list[freeSlot].prev = lastNode; //lastNode = NULL_NODE if rootNode was null
		
		lastNode = freeSlot;
		
		nodeNumber++;*/
		
		
	}

	public boolean removeItem(int idItem)
	{
		if(idItem <0 || idItem >= a_list.length || positions[idItem] == NULL_NODE)
			return false;
		
		short pos = positions[idItem];
		positions[idItem] = NULL_NODE;
		
		if(pos == nodeNumber-1)
		{
			a_list[pos] = null;
			nodeNumber--;
			return true;
		}
		
		//else
		T last = a_list[nodeNumber-1];
		a_list[nodeNumber-1] = null;
		
		a_list[pos] = last;
		positions[last.id] = pos;
		
		nodeNumber--;
		
		return true;
		/*short prev = a_list[idItem].prev;
		short next = a_list[idItem].next;*/
		
		/*a_list[idItem] = null;
		
		nodeNumber--;
		
		if(idItem == rootNode && idItem == lastNode)
		{
			rootNode = lastNode = NULL_NODE;
			nodeNumber = 0;
			return;
		}
		
		if(idItem == rootNode)
		{
			rootNode = next;
			a_list[rootNode].prev = NULL_NODE;
			return;
		}
		
		if(idItem == lastNode)
		{
			lastNode = prev;
			a_list[lastNode].next = NULL_NODE;
			return;
		}
		
		a_list[prev].next = next;
		a_list[next].prev = prev;*/
		
		
	}
	
	public int getNodeNumber() {
		return nodeNumber;
	}
}
