package it.gius.data.structures;

import java.lang.reflect.Array;

public class PoolStack<T> {
	
	private T elements[];
	private int lastElementPos = 0;
	private Class<T> tClass;
	
	@SuppressWarnings("unchecked")
	public PoolStack(Class<T> tClass, int maxSize, T[] startingElements) {
		this.tClass = tClass;
		elements = (T[])Array.newInstance(this.tClass, maxSize);
		
		int fillSize = Math.min(maxSize, startingElements.length);
		
		for(int i=0; i<fillSize;i++)
			elements[i] = startingElements[i];
		
		lastElementPos = fillSize-1;
	}
	
	public boolean empty()
	{
		return lastElementPos < 0;
	}
	
	public T pop()
	{
		if(lastElementPos < 0)
			return null;
		
		T result = elements[lastElementPos];
		lastElementPos--;
		return result;
	}
	
	public boolean tryPush(T element)
	{
		if(lastElementPos == elements.length-1  || element == null)
			return false;
		
		lastElementPos++;
		
		elements[lastElementPos] = element;
		
		return true;
	}
	
	

}
