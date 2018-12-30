package it.gius.data.structures;


public interface IdList<T extends IdGetSet> extends Iterable<T> {
	
	public static final short NULL_NODE = -1;
	
	public T get(short id);

	
	public short add(T element);
	public boolean cotains(T element);
	public T remove(short id);	
	
	public int size();
	
	public T[] toLongerArray();


}
