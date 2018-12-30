package it.gius.data.structures;

import java.lang.reflect.Array;


/**
 * Code from www.codercorner.com/PairManager.rar
 *
 * @opt all
 */
public class HashSet<T> {

	public static final int NULL_NODE = -1;

	private int hashTable[];

	private static class ListElement
	{
		public int prev;
		public int next;
		
		@Override
		public String toString() {
		return "("+prev +", "+ next + ")";
		}
	}

	private ListElement[] lists;

	protected int mask;

	public  T[] elements;
	private Class<T> tClass;
	private int elementsNumber;




	public HashSet(Class<T> tClass) {
		this.tClass = tClass;
		mask = 0;
		elementsNumber = 0;

		hashTable = null;
		lists = null;
		elements = null;
	}
	
	public void clear()
	{
		elementsNumber = 0;
		mask = 0;
		hashTable = null;
		lists = null;
		elements = null;
	}

	@SuppressWarnings("unchecked")
	private void resize()
	{
		int newHashSize;
		
		if(elementsNumber < 32)
			 newHashSize = nextPowerOfTwo(2*elementsNumber+1);
		else
			newHashSize = nextPowerOfTwo(elementsNumber+1);
		
		int[] newHashTable = new int[newHashSize];

		mask = newHashSize-1; //0...000111...1

		ListElement[] newLists = new ListElement[newHashSize];

		for(int i=0; i< newHashTable.length;i++)
		{
			newHashTable[i]= NULL_NODE;
			newLists[i] = new ListElement();
			newLists[i].prev =NULL_NODE;
			newLists[i].next =NULL_NODE;
		}


		T[] newElments = null;
		try {
			newElments = (T[])Array.newInstance(tClass, newHashSize);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		for(int i=0; i< elementsNumber; i++)
		{
			newElments[i] = elements[i];
			//int hash = elements[i].hashCode() & mask;
			int hash = getHash(elements[i]);
			int j = newHashTable[hash];
			newLists[i].next = j;

			if(j != NULL_NODE)
				newLists[j].prev = i;

			newHashTable[hash] = i;
		}

		elements = newElments;
		hashTable = newHashTable;
		lists = newLists;

	}
	
	

	private int nextPowerOfTwo(int x)
	{
		x |= (x >> 1);
		x |= (x >> 2);
		x |= (x >> 4);
		x |= (x >> 8);
		x |= (x >> 16);
		return x+1;
	}

	
	protected int getHash(T t)
	{
		return t.hashCode() & mask;
	}
	
	
	public T find(T t)
	{
		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		return find(t, hash);
	}

	private T find(T t,int hash)
	{
		if(hashTable == null)
			return null;

		int index = hashTable[hash];
		for(;index!= NULL_NODE && !elements[index].equals(t); index = lists[index].next);

		if(index == NULL_NODE)
			return null;

		return elements[index];
	}
	
	

	public T add(T t)
	{

		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		T found = find(t, hash);

		if(found != null)
			return found;

		if(hashTable == null || elementsNumber>=hashTable.length)
		{
			resize();
			//recompute hash with new mask
			//hash = t.hashCode() & mask;
			hash = getHash(t);
		}


		elements[elementsNumber] = t;

		int j = hashTable[hash];
		lists[elementsNumber].next = j;

		if(j != NULL_NODE)
			lists[j].prev = elementsNumber;

		hashTable[hash] = elementsNumber;

		elementsNumber++;

		return t;
	}


	public boolean remove(T t)
	{
		if(hashTable == null)
			return false;

		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		int index = getIndex(t, hash);

		if(index == NULL_NODE)
			return false;

		int prev = lists[index].prev;
		int next = lists[index].next;
		
		//cutting element from the structures
		if(prev != NULL_NODE)
			lists[prev].next = next;
		else
			hashTable[hash] = next;
		
		if(next != NULL_NODE)
			lists[next].prev = prev;
		
		//sub lastElement with the removed one, also changing all the structures
		int lastIndex = elementsNumber-1;
		
		
		if(lastIndex == index)
		{
			lists[index].next = NULL_NODE;
			lists[index].prev = NULL_NODE;
			elementsNumber--;
			return true;
		}
		
		T last = elements[lastIndex];
		//int lastHash = last.hashCode() & mask;
		int lastHash = getHash(last);
		
		elements[index] = last;
		elements[lastIndex] = null;

		
		int lastPrev = lists[lastIndex].prev;
		int lastNext = lists[lastIndex].next;
		
		lists[lastIndex].prev = NULL_NODE;
		lists[lastIndex].next = NULL_NODE;
		
		//cut last from his list to re-add it to the start of the list
		//cut
		if(lastPrev != NULL_NODE)
			lists[lastPrev].next = lastNext;
		else
			hashTable[lastHash] = lastNext;
		
		if(lastNext != NULL_NODE)
			lists[lastNext].prev = lastPrev;
		
		//re-adding to the start
		lists[index].prev = NULL_NODE;
		int j = hashTable[lastHash];
		lists[index].next = j;
		
		if(j != NULL_NODE)
			lists[j].prev =index;
		
		hashTable[lastHash] = index;

		
		elementsNumber--;
		
		return true;
	}

	
	public int getIndex(T t)
	{
		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		return getIndex(t, hash);
	}

	private int getIndex(T t, int hash)
	{
		if(hashTable == null)
			return NULL_NODE;

		int index = hashTable[hash];
		for(;index!= NULL_NODE && !elements[index].equals(t); index = lists[index].next);


		return index;
	}

	
	public int size()
	{
		return elementsNumber;
	}
	
	@Override
	public String toString() {
		String result = new String("[");
		for(int i=0; i<elementsNumber-1; i++)
			result = result.concat(elements[i].toString() + ", ");
		
		result = result.concat(elements[elementsNumber-1].toString() + "]");
		return result;
	}

}
