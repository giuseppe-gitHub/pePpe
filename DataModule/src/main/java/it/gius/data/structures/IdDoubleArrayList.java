package it.gius.data.structures;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class IdDoubleArrayList<T extends IdGetSet> implements IdList<T> {


	private static final short DEFAULT_SIZE = 16;
	public static final int DEFAULT_SUPPORTED_NESTED_LOOP = 4;
		
	private T[] elementArray;
	private short[] positions;
	private LinkedList<Short> freeIdsList;
	private final Class<T> elementType;

	private short size = 0;
	
	private short maxId = 0;
	
	
	private IdDoubleArrayListIterator[] iterators;
	private int currentIterator = 0;
	

	public IdDoubleArrayList(Class<T> elementType) {
		this(elementType,DEFAULT_SIZE,DEFAULT_SUPPORTED_NESTED_LOOP);

	}
	
	public IdDoubleArrayList(Class<T> elementType, int startSize)
	{
		this(elementType,startSize,DEFAULT_SUPPORTED_NESTED_LOOP);
	}

	@SuppressWarnings("unchecked")
	public IdDoubleArrayList(Class<T> elementType, int startSize,int supportedNestedLoop) {
		this.elementType = elementType;
		
		this.freeIdsList = new LinkedList<Short>();

		int dim  = Math.max(startSize, DEFAULT_SIZE);
		try {
			elementArray = (T[])Array.newInstance(this.elementType, dim);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}


		positions = new short[dim];

		for(int i=0; i<elementArray.length; i++)
		{
			elementArray[i]= null;
			positions[i] = NULL_NODE;
		}
		
		dim = Math.max(DEFAULT_SUPPORTED_NESTED_LOOP, supportedNestedLoop);
		try {
			iterators = (IdDoubleArrayListIterator[])Array.newInstance(IdDoubleArrayListIterator.class, dim);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		for(int i=0; i< iterators.length; i++)
		{
			iterators[i] = new IdDoubleArrayListIterator();
			iterators[i].myIndex = i;
		}
	}


	@SuppressWarnings("unchecked")
	private void resizeArrays()
	{
		T[] newShapes = null;
		short[] newPositions; 

		try {
			newShapes = (T[])Array.newInstance(elementType, elementArray.length*2);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		newPositions = new short[elementArray.length*2];

		for(int i = 0; i < elementArray.length; i++)
		{
			newShapes[i] = elementArray[i];
			newPositions[i] = positions[i];
		}


		for(int i = elementArray.length; i < newShapes.length; i++)
		{
			newShapes[i] = null;
			newPositions[i] = NULL_NODE;
		}

		elementArray = newShapes;
		positions = newPositions;
	}


	protected short nextFree()
	{

		if(maxId < positions.length && positions[maxId] == NULL_NODE )
			return (short)(maxId++);

		/*short nextFree = 0;
		for(;nextFree<positions.length && positions[nextFree]!= NULL_NODE;nextFree++);*/
		if(freeIdsList.size() == 0)
			throw new IllegalAccessError("List of free Ids is empty");
		
		short nextFree = freeIdsList.poll();

		if(positions[nextFree]!= NULL_NODE)
			return NULL_NODE;

		return nextFree;
	}


	public short add(T element) {

		if(size == elementArray.length)
			resizeArrays();

		short id = nextFree();

		elementArray[size] = element;
		element.setId(id);
		positions[id] = size;

		size++;
		
		for(int i=0; i< iterators.length; i++)
			iterators[i].added = true;
		

		return id;
	};

	public boolean cotains(T element) {

		short id = element.getId();
		short pos;
		if(id >= 0 &&  id < positions.length && (pos = positions[id]) != NULL_NODE)
			return element.equals(elementArray[pos]);
		else
			return false;
	};


	@Override
	public T get(short id) {
		if( id < 0 || id >= positions.length)
			return null;
		
		short pos = positions[id];
		
		if(pos != NULL_NODE)
			return elementArray[pos];
		else
			return null;
	}


	@Override
	public Iterator<T> iterator() {
		IdDoubleArrayListIterator iterator = iterators[currentIterator];
		iterator.reset();
		
		currentIterator = (currentIterator +1) < iterators.length ? currentIterator+1 : 0;
		
		return iterator;
	}
	
	private class IdDoubleArrayListIterator implements Iterator<T>
	{
		
		private short position = 0;
		public boolean added = false;
		public boolean removed = false;
		public int myIndex = 0;
		
		public void reset()
		{
			position = 0;
			added = false;
			removed = false;
		}
		
		@Override
		public boolean hasNext() {
			
			boolean active = (position < size);
			
			if(!active)
				currentIterator = myIndex;
			else
				currentIterator = (myIndex +1) < iterators.length ? myIndex+1 : 0;
			
			return active;
		}
		
		@Override
		public T next() {
			if(added)
				throw new ConcurrentModificationException("Trying to add inside iteration");
			
			if(removed)
				throw new ConcurrentModificationException("Trying to remove inside iteration");
			
			return elementArray[position++];
		}
		
		
		@Override
		public void remove() {
			throw new ConcurrentModificationException("Trying to remove inside iteration");
		}
		
		
		/*public void removing(short id)
		{
			if(id == elementArray[position].getId())
				position++;
		}*/
		
	}


	@Override
	public /*boolean*/T remove(short idItem) {
		if(idItem <0 || idItem >= positions.length || positions[idItem] == NULL_NODE)
			return null;//return false;
		
		
		
		short pos = positions[idItem];
		T result = elementArray[pos];
		
		positions[idItem] = NULL_NODE;
		
		for(int i=0; i< iterators.length; i++)
			iterators[i].removed = true;

		
		if(idItem == maxId-1  && maxId != 0)
			maxId--;
		
		freeIdsList.addLast(idItem);
		
		if(pos == size-1)
		{
			elementArray[pos] = null;
			size--;
			return result;
		}
		
		//else
		T last = elementArray[size-1];
		elementArray[size-1] = null;
		
		elementArray[pos] = last;
		positions[last.getId()] = pos;
		
		size--;
		
		return result;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T[] toLongerArray() {
		return elementArray;
	}

}
