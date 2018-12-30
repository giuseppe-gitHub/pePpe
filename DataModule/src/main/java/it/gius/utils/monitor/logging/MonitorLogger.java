package it.gius.utils.monitor.logging;

import org.apache.log4j.Logger;

import it.gius.utils.monitor.SimpleMonitor;
import it.gius.utils.monitor.StatisticData;

public class MonitorLogger {

	private SimpleMonitor monitor;
	
	private Logger logger;
	
	public MonitorLogger(SimpleMonitor monitor) {
		this.monitor = monitor;
		
		logger = Logger.getLogger(SimpleMonitor.class.getCanonicalName() + "." + monitor.name);
	}
	
	
	public void log()
	{
		if(!monitor.isEnabled())
			return;
		
		String timeUnit = null;

		if(monitor.timeUnit == SimpleMonitor.MS)
			timeUnit ="ms";
		else
			timeUnit ="ns";
		
		StatisticData stat = monitor.stat;

		logger.debug("monitoring    : "+ monitor.monitoring);
		logger.debug("average time  : " + stat.averageTime       + " " + timeUnit);
		logger.debug("max time      : " + stat.maxTime           + " " + timeUnit + ", with the firsts " + monitor.dropFirstsMax+" max dropped");
		logger.debug("min time      : " + stat.minTime           + " " + timeUnit + ", with the firsts " + monitor.dropFirstsMin+" min dropped");
		logger.debug("deviation time: " + stat.standardDeviation + " " + timeUnit);
		logger.debug("iterations    : " + stat.iterations);
	}
}
