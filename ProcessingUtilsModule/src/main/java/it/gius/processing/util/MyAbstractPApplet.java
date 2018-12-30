package it.gius.processing.util;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

/**
 * @opt all
 * @opt !attributes
 */
public abstract class MyAbstractPApplet extends PApplet {
	
	
	private static final long serialVersionUID = 3303930072725983932L;

	protected GoStop goStop;
	
	private List<WindowAdapter> adapters = null;
	
	public static final char START_STOP_KEY = 'p';
	public static final char ESCAPE_SUB_CHAR = 0;
	
	public void setGoStop(GoStop goStop)
	{
		this.goStop = goStop;
		
	}
	
	public void addWindowAdapter(WindowAdapter adapter)
	{
		if(this.adapters == null)
			this.adapters = new ArrayList<WindowAdapter>();
		
		this.adapters.add(adapter);
	}
	
	
	public void setup()
	{
		
		WindowListener[] listeners= frame.getWindowListeners();
	
		if(listeners.length > 0)
		{
			frame.removeWindowListener(listeners[0]);
		}

		
		for(WindowAdapter adapter : adapters)
			frame.addWindowListener(adapter);
		

	}
	
	
	
	
	@Override
	public void keyPressed() {
		
		if(key == KeyEvent.VK_ESCAPE)
		{
			key = ESCAPE_SUB_CHAR;
		}
		
		if(key == START_STOP_KEY && goStop != null)
		{
			goStop.toggle();
		}
			
	}

}
