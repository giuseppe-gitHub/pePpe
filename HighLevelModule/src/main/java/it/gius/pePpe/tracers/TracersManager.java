package it.gius.pePpe.tracers;

import it.gius.pePpe.data.tracers.ITracer;

public class TracersManager implements ITracer {
	
	private ITracer[] tracers = null;
	private int tracersSize = 0;
	
	public TracersManager() {
		tracers = new ITracer[100];
	}
	
	private void resize()
	{
		ITracer[] newTracers = new ITracer[tracers.length*2];
		
		for(int i=0; i< tracers.length; i++)
			newTracers[i] = tracers[i];
		
		tracers = newTracers;
	}
	
	public void addTracer(ITracer tracer)
	{
		if(tracersSize >= tracers.length)
			resize();
		
		tracers[tracersSize] = tracer;
		tracersSize++;
	}
	
	public void removeTracer(ITracer tracer)
	{
		int i = 0;
		boolean found = false;
		for(i=0; i< tracersSize;i++)
		{
			if(tracers[i].equals(tracer))
			{
				found = true;
				break;
			}
		}
		
		if(found)
		{
			for(; i< tracersSize-1;i++)
			{
				tracers[i] = tracers[i+1];
			}
			
			tracers[tracersSize-1] = null;
			
			tracersSize--;
		}
	}
	
	@Override
	public void trace(float time) {
		for(int i=0; i< tracersSize; i++)
			tracers[i].trace(time);
		
	}

}
