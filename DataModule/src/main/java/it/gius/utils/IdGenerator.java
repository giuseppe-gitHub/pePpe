package it.gius.utils;

public class IdGenerator {

	private int next = 0;
	
	public int nextId()
	{
		return next++;
	}
}
