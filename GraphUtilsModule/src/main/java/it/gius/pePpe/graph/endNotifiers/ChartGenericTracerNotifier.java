package it.gius.pePpe.graph.endNotifiers;

import org.jfree.ui.RefineryUtilities;

import it.gius.pePpe.data.tracers.IResultReflectTracer;
import it.gius.pePpe.graph.FrameChart;
import it.gius.pePpe.graph.tracer.ChartTracerReflection;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulationInfo;

public class ChartGenericTracerNotifier implements IEndListener{
	
	private ChartTracerReflection cpt;
	private String chartName;
	
	private int xDim,yDim;
	
	public static final int MAX_SERIES = 10;

	public ChartGenericTracerNotifier(String chartName,int xDim, int yDim) {
		this.chartName = chartName;
		this.xDim = xDim;
		this.yDim = yDim;
		
		cpt = new ChartTracerReflection();
	}

	public void addTracer(IResultReflectTracer tracer, String[] fields)
	{
		cpt.addTracer(tracer, fields);
	}
	
	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {
		
		if(endType != EndType.EXCEPTION)
		{

			cpt.name = chartName;
			cpt.createChart();

			FrameChart frame = new FrameChart(chartName, xDim, yDim, cpt.getChart());

			frame.pack();
			RefineryUtilities.centerFrameOnScreen(frame);
			frame.setVisible(true);
		}

		
	}

}
