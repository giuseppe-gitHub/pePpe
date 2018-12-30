package it.gius.processing.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author giuseppe
 * @has 1 - 1..* it.gius.processing.util.MyAbstractPApplet
 * @opt all
 * @opt !attributes
 */
public class GoStop {
	
	private boolean go = false;
	private List<MyAbstractPApplet> applets = null;
	
	
	
	public GoStop(boolean go) {

		this.go = go;
		
		applets = new ArrayList<MyAbstractPApplet>();
	}
	
	public synchronized void addApplet(MyAbstractPApplet applet)
	{
		applets.add(applet);
	}
	
	public synchronized void removeApplet(MyAbstractPApplet applet)
	{
		applets.remove(applet);
	}
	
	public synchronized boolean isGo() {
		return go;
	}
	
	public synchronized void toggle()
	{
		setGo(!go);
	}
	
	public synchronized void setGo(boolean go) {
		
		if(this.go == go)
			return;
		
		this.go = go;
		for(MyAbstractPApplet applet: applets)
		{
			if(go)
				applet.loop();
			else
				applet.noLoop();
			
		}
		
	} 

}
