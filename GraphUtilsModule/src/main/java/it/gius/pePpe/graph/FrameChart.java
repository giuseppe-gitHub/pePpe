package it.gius.pePpe.graph;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class FrameChart extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3084957229428784014L;

	public FrameChart(String title,int xDim, int yDim,JFreeChart chart) {
		 super(title);
	        final JFreeChart thisChart = chart;
	        final ChartPanel chartPanel = new ChartPanel(thisChart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(xDim, yDim));
	        chartPanel.setMouseZoomable(true, false);
	        
	        this.setContentPane(chartPanel);
	        
	        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	

}
