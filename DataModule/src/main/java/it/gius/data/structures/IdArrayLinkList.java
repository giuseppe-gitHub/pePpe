package it.gius.data.structures;

import java.lang.reflect.Array;
import java.util.Iterator;

import java.util.ConcurrentModificationException;


public class IdArrayLinkList<T extends IdGetSet> implements IdList<T> {

	private static final short DEFAULT_SIZE = 16;

	public static final int DEFAULT_SUPPORTED_NESTED_LOOP = 4;

	private RealElement<T>[] elementArray;

	private final Class<T> elementType;

	private short firstNode = NULL_NODE;
	private short lastNode = NULL_NODE;

	private short size = 0;

	private IdArrayListIterator iterators[];
	
	private T[] toArray = null;

	private static class RealElement<T>
	{

		public T element;
		//public short id;
		public short prev;
		public short next;
		
		public short toArrayPos;
	}

	public IdArrayLinkList(Class<T> elementType, int startSize) {
		this(elementType,startSize,DEFAULT_SUPPORTED_NESTED_LOOP);
	}

	@SuppressWarnings("unchecked")
	public IdArrayLinkList(Class<T> elementType, int startSize, int supportedNestedLoop) {

		this.elementType = elementType;

		int dim  = Math.max(startSize, DEFAULT_SIZE);
		try {
			elementArray = (RealElement<T>[])Array.newInstance(RealElement.class, dim);
			toArray = (T[])Array.newInstance(elementType, dim);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		for(int i=0; i<elementArray.length; i++)
		{
			elementArray[i] = new RealElement<T>();
			elementArray[i].element = null;
		}

		dim = Math.max(supportedNestedLoop, DEFAULT_SUPPORTED_NESTED_LOOP);
		
		try {
			iterators = (IdArrayListIterator[])Array.newInstance(IdArrayListIterator.class, dim);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		for(int i=0; i< iterators.length; i++)
		{
			iterators[i] = new IdArrayListIterator();
			iterators[i].myIndex = i;
		}
		

	}


	@SuppressWarnings("unchecked")
	private void resizeArrays()
	{
		RealElement<T>[] newShapes = null;

		try {
			newShapes = (RealElement<T>[])Array.newInstance(RealElement.class, elementArray.length*2);
			toArray = (T[])Array.newInstance(elementType, elementArray.length*2);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		

		for(int i = 0; i < elementArray.length; i++)
		{
			newShapes[i] = elementArray[i];
			toArray[i] = elementArray[i].element;
			elementArray[i].toArrayPos = (short)i;
		}


		for(int i = elementArray.length; i < newShapes.length; i++)
		{
			newShapes[i] = new RealElement<T>();
			newShapes[i].element = null;
		}

		elementArray = newShapes;
	}


	private short nextFree()
	{


		if(lastNode != NULL_NODE && lastNode +1 < elementArray.length && elementArray[lastNode+1].element == null)
			return (short)(lastNode+1);

		short nextFree;

		for(nextFree = 0;nextFree<elementArray.length && elementArray[nextFree].element!= null;nextFree++);

		if(elementArray[nextFree].element!= null)
			return NULL_NODE;

		return nextFree;
	}


	public short add(T element) {

		if(size == elementArray.length)
			resizeArrays();

		short freeSlot = nextFree();

		for(int i=0; i< iterators.length; i++)
			iterators[i].added = true;

		if(firstNode == NULL_NODE)
			firstNode = freeSlot;
		else
			elementArray[lastNode].next = freeSlot;


		elementArray[freeSlot].element = element;

		//elementArray[freeSlot].id = freeSlot;
		elementArray[freeSlot].next = NULL_NODE;
		elementArray[freeSlot].prev = lastNode;
		//lastNode = NULL_NODE if rootNode was null

		lastNode = freeSlot;

		element.setId(freeSlot);
		
		
		toArray[size] = element;
		elementArray[freeSlot].toArrayPos = size;

		size++;

		return freeSlot;
	};


	public boolean cotains(T element) {

		if(element == null)
			return false;

		short id = element.getId();

		if(id <0 || id >= elementArray.length)
			return false;

		return element.equals(elementArray[id].element);
	}

	@Override
	public T get(short id) {
		/*if(id <0)
			throw new IllegalArgumentException("Id must be positive");

		if(id >= elementArray.length)
			throw new IllegalArgumentException("Id too big"); */

		return elementArray[id].element;
	}

	public short getFirst() {
		return firstNode;
	}


	public short getNext(short id) {
		return elementArray[id].next;
	}


	private int currentIterator = 0;


	public Iterator<T> iterator() {
		IdArrayListIterator iterator = iterators[currentIterator];
		iterator.reset();

		currentIterator = (currentIterator +1) < iterators.length ? currentIterator+1 : 0;

		return iterator;
	}
	

	public Iterator<T> iterator(short startId) {
		if(startId <0 || startId >= elementArray.length || elementArray[startId].element == null)
			throw new IllegalArgumentException();
		
		IdArrayListIterator iterator = iterators[currentIterator];
		iterator.reset(startId);

		currentIterator = (currentIterator +1) < iterators.length ? currentIterator+1 : 0;

		return iterator;
	}



	private class IdArrayListIterator implements Iterator<T>
	{
		private short currentId = NULL_NODE;
		//private boolean active = false;
		public boolean added = false;

		public int myIndex = 0;

		public void reset()
		{
			currentId = firstNode;
			added = false;
		}
		
		public void reset(short id)
		{
			currentId = id;
			added = false;
		}

		@Override
		public boolean hasNext() {

			boolean active = (currentId != NULL_NODE);

			if(!active)
				currentIterator = myIndex;
			else
				currentIterator = (myIndex +1) < iterators.length ? myIndex+1 : 0;
			//if(!active)
				//currentIterator = (currentIterator + (iterators.length-1)) % iterators.length;

			return active;
		}

		@Override
		public T next() {

			if(added)
				throw new ConcurrentModificationException("Trying to add inside iteration");

			RealElement<T> result = elementArray[currentId];

			//active = true;

			currentId = result.next;

			return result.element;
		}

		@Override
		public void remove() {

			bridgeRemove(currentId);

		}

		/*public void adding(short id)
		{
			if(currentId == NULL_NODE)
				currentId = id;
		}*/


		public void removing(short id)
		{
			if(currentId == id)
				currentId = elementArray[currentId].next; 
		}
	}


	private T bridgeRemove(short idItem)
	{
		return remove(idItem);
	}


	@Override
	public /*boolean*/T remove(short idItem) {
		if(idItem <0 || idItem >= elementArray.length || elementArray[idItem].element == null)
			return null;

		T result = elementArray[idItem].element;

		short prev = elementArray[idItem].prev;
		short next = elementArray[idItem].next;

		short toArrayPos = elementArray[idItem].toArrayPos;
		T last = toArray[size -1];
		toArray[toArrayPos] = last;
		toArray[size-1] = null;
		
		elementArray[last.getId()].toArrayPos = toArrayPos;
		
		//called before resetting the current node
		for(int i=0; i< iterators.length; i++)
			iterators[i].removing(idItem);

		

		elementArray[idItem].element = null;
		elementArray[idItem].prev = NULL_NODE;
		elementArray[idItem].next = NULL_NODE;
		
		size--;

		if(idItem == firstNode && idItem == lastNode)
		{
			firstNode = lastNode = NULL_NODE;
			size = 0;

			return result;
		}

		if(idItem == firstNode)
		{
			firstNode = next;
			elementArray[firstNode].prev = NULL_NODE;

			return result;
		}

		if(idItem == lastNode)
		{
			lastNode = prev;
			elementArray[lastNode].next = NULL_NODE;

			return result;
		}

		elementArray[prev].next = next;
		elementArray[next].prev = prev;


		return result;
	}


	@Override
	public int size() {
		return size;
	}
	
	@Override
	public T[] toLongerArray() {
		return toArray;

	}

}
