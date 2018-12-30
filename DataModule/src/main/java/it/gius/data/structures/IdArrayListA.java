package it.gius.data.structures;

import java.lang.reflect.Array;


/**
 * 
 * @author giuseppe
 *
 * @param <T> subclass of it.gius.pePpe.data.structures.IdNode 
 * 
 * @composed - - 1..* it.gius.pePpe.data.structures.IdNode
 * @opt all
 */
public class IdArrayListA<T extends IdNode> {

	public static final short DEFAULT_SIZE = 16;
	public static final short NULL_NODE = -1;

	public T[] a_list;

	private final Class<T> tClass;

	public short rootNode = NULL_NODE;
	public short lastNode = NULL_NODE;

	private short nodeNumber = 0;


	@SuppressWarnings("unchecked")
	public IdArrayListA(Class<T> tClass, int startSize) {
		this.tClass = tClass;
		int dim  = Math.max(startSize, DEFAULT_SIZE);
		try {
			a_list = (T[])Array.newInstance(tClass, dim);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

	}

	@SuppressWarnings("unchecked")
	private void resizeArrays()
	{
		T[] newShapes = null;
		
		try {
			newShapes = (T[])Array.newInstance(tClass, a_list.length*2);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		

		for(int i = 0; i < a_list.length; i++)
		{
			newShapes[i] = a_list[i];
			/*newShapes[i].id = a_list[i].id;
			newShapes[i].next = a_list[i].next;*/
		}
		

		for(int i = a_list.length; i < newShapes.length; i++)
		{
			newShapes[i] = null;
		}

		a_list = newShapes;
	}
	
	
	private short nextFree()
	{
		short nextFree = 0;
		
		if(lastNode != NULL_NODE && lastNode +1 < a_list.length && a_list[lastNode+1] == null)
			return (short)(lastNode+1);
		
		for(;nextFree<a_list.length && a_list[nextFree]!= null;nextFree++);
		
		if(a_list[nextFree]!= null)
			return NULL_NODE;
		
		return nextFree;
	}
	
	
	
	public short addItem(T node)
	{
		if(nodeNumber == a_list.length)
			resizeArrays();
		
		short freeSlot = nextFree();
		
		if(rootNode == NULL_NODE)
			rootNode = freeSlot;
		else
			a_list[lastNode].next = freeSlot;
		
		a_list[freeSlot] = node;
		
		a_list[freeSlot].id = freeSlot;
		a_list[freeSlot].next = NULL_NODE;
		a_list[freeSlot].prev = lastNode; //lastNode = NULL_NODE if rootNode was null
		
		lastNode = freeSlot;
		
		nodeNumber++;
		
		return freeSlot;
		
		
	}

	public boolean removeItem(int idItem)
	{
		if(idItem <0 || idItem >= a_list.length || a_list[idItem] == null)
			return false;
		
	
		short prev = a_list[idItem].prev;
		short next = a_list[idItem].next;
		
		a_list[idItem] = null;
		
		nodeNumber--;
		
		if(idItem == rootNode && idItem == lastNode)
		{
			rootNode = lastNode = NULL_NODE;
			nodeNumber = 0;
			return true;
		}
		
		if(idItem == rootNode)
		{
			rootNode = next;
			a_list[rootNode].prev = NULL_NODE;
			return true;
		}
		
		if(idItem == lastNode)
		{
			lastNode = prev;
			a_list[lastNode].next = NULL_NODE;
			return true;
		}
		
		a_list[prev].next = next;
		a_list[next].prev = prev;
		
		return true;
		
	}
	
	public int size() {
		return nodeNumber;
	}
}
