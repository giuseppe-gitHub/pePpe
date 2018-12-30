package it.gius.pePpe.graph.tracer;

import it.gius.pePpe.data.tracers.IResultReflectTracer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartTracerReflection {
public static final int MAX_SERIES = 10;
	

	private static class TracerData {
		IResultReflectTracer tracer;
		String[] fields;
	}

	private TracerData[] tracersData;
	private int tracersSize=0;
	
	private JFreeChart chart = null;
	
	public String name = "default";
	
	public ChartTracerReflection() {
		tracersData  = new TracerData[MAX_SERIES];
	}
	
	
	public void addTracer(IResultReflectTracer tracer,String[] fields)
	{
		if(fields == null || fields.length <1)
			throw new IllegalArgumentException("need to specify at least one field");
		
		if(tracersData[tracersSize] == null)
			tracersData[tracersSize] = new TracerData();
		
		tracersData[tracersSize].tracer = tracer;
		tracersData[tracersSize].fields = fields;
		tracersSize++;
	}
	
	public int size()
	{
		return tracersSize;
	}
	
	
	public void createChart()
	{
		XYSeriesCollection collection = new XYSeriesCollection();
		for(int i=0; i<tracersSize; i++)
		{
			for(int j=0; j<tracersData[i].fields.length; j++)
			{
				try {
					XYSeries tempSeries = createXYSeries(tracersData[i].tracer,tracersData[i].fields[j]);
					collection.addSeries(tempSeries);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		chart = ChartFactory.createXYLineChart(
				name,                        //Title
				"time",                 //x-axis-Label
				"values",                       //y-axis-Label
				collection,                  //dataset
				PlotOrientation.VERTICAL,	 //orientation
				true,						 //Show legend
				true,						 //Use tooltips
				false						 //Configure chart to generate urls?
				);

	}
	
	private XYSeries createXYSeries(IResultReflectTracer tracer,String stringField) throws NoSuchFieldException,IllegalAccessException
	{
		XYSeries series = new XYSeries(tracer.getName() + "." + stringField);

		for(int i=0; i< tracer.size(); i++)
			series.add(tracer.getTime(i),tracer.getDataField(i, stringField));
		
		return series; 
	}
	
	
	public JFreeChart getChart()
	{
		return chart;
	}
}
