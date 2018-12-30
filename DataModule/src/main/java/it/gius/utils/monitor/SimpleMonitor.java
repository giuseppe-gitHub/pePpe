package it.gius.utils.monitor;

import java.util.Arrays;


public class SimpleMonitor {

	public StatisticData stat;

	private double times[];

	public int dropFirstsMin = 0;
	public int dropFirstsMax = 0;

	private double dropLower = -1;
	private double dropUpper = -1;

	private double[] maxs;
	private double[] mins;

	public final int timeUnit;

	public static final int MS = 0;
	public static final int NS = 1;

	private boolean enabled = true;

	public static class MonitorInit{
		public int dropFirstsMax = 0;
		public int dropFirstsMin = 0;
		public double dropLower = -1;
		public double dropUpper = -1;

		public int timeUnit = MS;

	}


	public final String name;

	public String monitoring;


	public SimpleMonitor(String name,String monitoring) {
		this(name,monitoring,1000,new MonitorInit());
	}

	public SimpleMonitor(String name, String monitoring, MonitorInit init)
	{
		this(name,monitoring,1000,init);
	}

	public SimpleMonitor(String name, String monitoring, int expectedIterations,  MonitorInit init)
	{
		if(init.dropFirstsMin <0 || init.dropFirstsMax <0 )
			throw new IllegalArgumentException();

		this.name = name;
		this.monitoring = monitoring;

		this.stat = new StatisticData();
		this.times = new double[expectedIterations];

		this.dropFirstsMin = init.dropFirstsMin;
		this.dropFirstsMax = init.dropFirstsMax;

		this.mins = new double[this.dropFirstsMin +1];
		this.maxs = new double[this.dropFirstsMax +1];


		this.dropLower = init.dropLower;
		this.dropUpper = init.dropUpper;

		this.timeUnit = init.timeUnit;

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private double currStartTime = 0;


	public void preCall()
	{
		if(!enabled)
			return;

		if(timeUnit == SimpleMonitor.MS)
			currStartTime = System.currentTimeMillis();
		else
			currStartTime = System.nanoTime();

	}


	private void resizeArray()
	{
		double[] newTimes = new double[times.length*2];

		for(int i=0; i< times.length; i++)
		{
			newTimes[i] = times[i];
		}

		times = newTimes;
	}

	public void postCall()
	{

		if(!enabled)
			return;

		double currStopTime;

		if(timeUnit == SimpleMonitor.MS)
			currStopTime= System.currentTimeMillis();
		else
			currStopTime= System.nanoTime();

		if(times.length <= stat.iterations)
			resizeArray();

		double newValue = currStopTime-currStartTime;

		if(dropLower >0 && newValue < dropLower )
			return;

		if(dropUpper > 0 && newValue > dropUpper)
			return;

		times[(int)stat.iterations] = newValue;

		stat.iterations++;
	}


	@Override
	public String toString() {
		String timeUnit = null;

		if(this.timeUnit == MS)
			timeUnit ="ms";
		else
			timeUnit ="ns";

		String result = "["+name+"] monitoring    : " + monitoring +"\n"+
						"["+name+"] average time  : " + stat.averageTime       + " " + timeUnit + "\n" +
						"["+name+"] max time      : " + stat.maxTime           + " " + timeUnit + ", with the firsts " + dropFirstsMax+" max dropped\n" +
						"["+name+"] min time      : " + stat.minTime           + " " + timeUnit + ", with the firsts " + dropFirstsMin+" min dropped\n" +
						"["+name+"] deviation time: " + stat.standardDeviation + " " + timeUnit + "\n" +
						"["+name+"] iterations    : " + stat.iterations        ;

		return result;
	}


	public void collect()
	{

		if(!enabled)
			return;
		//stat.maxTime = times[0];
		//stat.minTime = times[0];
		for(int i=0; i< mins.length; i++)
			mins[i] = Double.MAX_VALUE;	

		for(int i=0; i<maxs.length; i++)
			maxs[i] = 0;

		double sum = 0;

		for(int i=0; i< stat.iterations; i++)
		{
			//stat.averageTime = stat.averageTime*i + (times[(int)i]);
			//stat.averageTime = stat.averageTime / (double)(i+1);
			sum += times[i]; 

			if(times[i] > maxs[0])
			{
				/*for(int j=0; j< maxs.length-1; j++)
					maxs[j+1] = maxs[j];*/

				maxs[0] = times[i];
				Arrays.sort(maxs);
			}

			if(times[i] < mins[mins.length-1])
			{
				/*for(int j=0; j< mins.length-1; j++)
					mins[j+1] = mins[j];*/

				mins[mins.length-1] = times[i];
				Arrays.sort(mins);
			}
		}

		stat.averageTime = (double)sum / (double)stat.iterations;

		stat.maxTime = maxs[0];
		stat.minTime = mins[mins.length-1];


		double x  = 0;
		for(int i=0; i< stat.iterations; i++)
		{
			x = (stat.averageTime - times[i]);
			sum += x*x;
		}

		stat.standardDeviation = Math.sqrt(sum/(double)stat.iterations);
	}
}
