package it.gius.pePpe.data.tracers;

public interface IResultReflectTracer extends ITracer{
	
	public String getName();
	public int size();
	
	public float getDataField(int i,String field) throws NoSuchFieldException,IllegalAccessException;
	public float getTime(int i);

}
