package it.gius.data.structures;


public class LinkedList<T> {


	public class ListElement {
		public T element;
		public ListElement next;
	}

	
	private ListElement first = null;
	private ListElement last = null;
	
	private int size = 0;
	
	
	public T peek()
	{
		if(first == null)
			return null;
		return first.element;
	}
	
	public int size() {
		return size;
	}

	
	
	public T poll()
	{
		if(first == null)
			return null;
		T result = first.element;
		
		if(first == last)
			first = last = null;
		else
			first = first.next;
		
		size--;
		
		return result;
	}
	
	
	public void addLast(T element)
	{
		if(first == null)
		{
			first = new ListElement();
			first.element = element;
			first.next = null;
			last = first;
		}
		else
		{
			ListElement newListElement = new ListElement();
			newListElement.element = element;
			newListElement.next = null;
			
			last.next = newListElement;
			last = newListElement;
		}
		
		size++;
	}


}
