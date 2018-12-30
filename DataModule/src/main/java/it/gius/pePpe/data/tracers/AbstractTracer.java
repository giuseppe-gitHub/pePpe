package it.gius.pePpe.data.tracers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public abstract class AbstractTracer<T> implements IResultReflectTracer {
	
	public String name = null;
	protected Class<T> classData = null;
	
	@Override
	public String getName() {
		return name;
	}
	
	public DataTrace<T>[] d_trace;
	public int traceSize = 0;
	
	@Override
	public int size() {
		return traceSize;
	}
	
	//protected abstract DataTrace[] newArray(int dim);
	
	@SuppressWarnings("unchecked")
	public AbstractTracer() {
		d_trace = (DataTrace<T>[])Array.newInstance(DataTrace.class, 500);
	}
	
	
	private void resize()
	{
		//DataTrace[] newTrace = /*new DataTrace[d_trace.length*2];*/newArray(d_trace.length*2);
		@SuppressWarnings("unchecked")
		DataTrace<T>[] newTrace = (DataTrace<T>[])Array.newInstance(DataTrace.class, d_trace.length*2);
		
		for(int i=0; i< d_trace.length; i++)
		{
			newTrace[i] = d_trace[i];
		}
		
		d_trace = newTrace;
	}
	
	
	protected abstract T newT();
	protected abstract void fillTrace(DataTrace<T> currTrace);
	
	@Override
	public void trace(float time)
	{
		if(traceSize >= d_trace.length)
			resize();
		
			
		if(d_trace[traceSize] == null)
		{
			d_trace[traceSize] = new DataTrace<T>();
			d_trace[traceSize].data = newT();
		}
		
		DataTrace<T> currTrace = d_trace[traceSize];
		
		currTrace.time = time;
		/*currTrace.x = currGlobalBodyPoint.x;
		currTrace.y = currGlobalBodyPoint.y;
		
		currTrace.vx = currPointVelocity.x;
		currTrace.vy = currPointVelocity.y;*/
		fillTrace(currTrace);
		
		traceSize++;
		
	}
	
	@Override
	public float getTime(int i) {
		if(i > traceSize)
			throw new IllegalArgumentException();
		
			return d_trace[i].time;
	}
	
	@Override
	public float getDataField(int i, String stringField) throws NoSuchFieldException,IllegalAccessException {
		
		if(i > traceSize)
			throw new IllegalArgumentException();
		
		Field field = classData.getField(stringField);
		float result = (Float)field.get(d_trace[i].data);
		
		return result;
	}

}
