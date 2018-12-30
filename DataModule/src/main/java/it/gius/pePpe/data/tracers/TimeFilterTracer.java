package it.gius.pePpe.data.tracers;

public class TimeFilterTracer implements ITracer {

	private ITracer wrapped;
	private float step;
	
	private float lastTimeTraced = 0;
	
	public TimeFilterTracer(ITracer wrapped,float step) {
		this.wrapped = wrapped;
		this.step = step;
	}
	
	@Override
	public void trace(float time) {

		if(time == 0)
		{
			wrapped.trace(time);
			lastTimeTraced = 0;
			return;
		}
		
		if(time > lastTimeTraced + step)
		{
			wrapped.trace(time);
			lastTimeTraced += step;
			return;
		}
		
	}

}
