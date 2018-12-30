package it.gius.pePpe.mains;

import it.gius.pePpe.MathUtils;
import it.gius.utils.monitor.SimpleMonitor;
import it.gius.utils.monitor.SimpleMonitor.MonitorInit;



public class HashCodePerformanceMain {

	public static int referencedEdge = 0;
	public static int incidentEdge = 0;
	public static boolean point0 = true;
	public static boolean referenceOnShapeA = true;

	public static double ITERATIONS = 1E5;
	
	public static int CALLS = 300;

	public static void main(String[] args) {

		MonitorInit monitorInit = new MonitorInit();
		monitorInit.timeUnit = SimpleMonitor.NS;
		
		SimpleMonitor monitorCantor = new SimpleMonitor("HashCodeMonitor","hash.cantorPair",monitorInit);
		SimpleMonitor monitorOr = new SimpleMonitor("HashCodeMonitor","hash.Or",monitorInit);
		
		for(int i=0; i< ITERATIONS; i++)
		{
			monitorCantor.preCall();
			for(int j= 0;j<CALLS; j++)
				hashCodeCantorPair();
			monitorCantor.postCall();
			inc();
		}
		clear();
		for(int i=0; i< ITERATIONS; i++)
		{
			monitorOr.preCall();
			for(int j= 0;j<CALLS; j++)
				hashCodeOr();
			monitorOr.postCall();
			inc();
		}
		
		monitorCantor.collect();
		monitorOr.collect();
		
		System.out.println(monitorCantor);
		System.out.println(monitorOr);
	}

	public static void clear()
	{
		referencedEdge = 0;
		incidentEdge = 0;
		point0 = true;
		referenceOnShapeA = true;
	}

	public static void inc()
	{
		referencedEdge++;
		incidentEdge = referencedEdge+1;

		if(referencedEdge % 2 == 0)
			point0 = !point0;
		else
			referenceOnShapeA = !referenceOnShapeA;
	}


	public static int hashCodeCantorPair() {
		int result = MathUtils.cantorPairing(referencedEdge, incidentEdge);
		result = MathUtils.cuttableHashCode(result);
		int temp = (point0 ? 0x000000FF : 0x0000FF00)^
				(referenceOnShapeA ? 0x00FF0000 : 0xFF000000);
		result = MathUtils.cuttableHashCode(result ^ temp);
		return result;
	}
	
	public static int hashHashCode() {
		int result = MathUtils.cuttableHashCode(referencedEdge);
		result = MathUtils.cuttableHashCode(incidentEdge | result);
		int temp = (point0 ? 0x000000FF : 0x0000FF00)^
				(referenceOnShapeA ? 0x00FF0000 : 0xFF000000);
		result = MathUtils.cuttableHashCode(result ^ temp);
		return result;
	}


	public static int hashCodeOr() {
		int result = (referencedEdge | (incidentEdge <<Short.SIZE));
		result = MathUtils.cuttableHashCode(result);
		int temp = (point0 ? 0x000000FF : 0x0000FF00)^
				(referenceOnShapeA ? 0x00FF0000 : 0xFF000000);
		result = MathUtils.cuttableHashCode(result ^ temp);
		return result;
	}
}



