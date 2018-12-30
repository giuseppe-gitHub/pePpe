package it.gius.processing.util;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import processing.core.PApplet;

/**
 * 
 * @author giuseppe
 * @depend - <new> - it.gius.processing.util.MyAbstractPApplet
 * @depend - <new> - it.gius.processing.util.GoStop
 * @opt all
 */
public class PAppletManager {

	public static final int DISPOSE_ALL = 0;
	public static final int DISPOSE_ONLY_ME = 1;
	public static final int EXIT_ON_CLOSE = 2;

	public static final int STANDARD_CLOSE = DISPOSE_ONLY_ME;

	private Integer activeApplets = 0;
	private Object synch = new Object();

	private boolean exitOnLastDisposed = false;
	private List<ClosingWindowAdapter> adapterList;
	private GoStop goStop = null;

	public PAppletManager() {
		adapterList = new ArrayList<ClosingWindowAdapter>();
		goStop = new GoStop(false);
	}

	public PAppletManager(boolean exitOnLastDisposed, boolean manualStart) {
		this.exitOnLastDisposed = exitOnLastDisposed;
		adapterList = new ArrayList<ClosingWindowAdapter>();
		goStop = new GoStop(!manualStart);
	}

	private boolean correctType(int type)
	{
		return type == DISPOSE_ALL || type == DISPOSE_ONLY_ME || type == EXIT_ON_CLOSE;
	}

	public Semaphore startAndAddApplet(String[] args,MyAbstractPApplet applet,int closingType)  throws ProcessingGraphicException
	{
		return startAndAddApplet(args, applet, closingType,null);
	}

	public Semaphore startAndAddApplet(String[] args,MyAbstractPApplet applet,int closingType, WindowClosedListener listener)  throws ProcessingGraphicException
	{
		synchronized (synch) {

			applet.setGoStop(goStop);

			goStop.addApplet(applet);
			
			Semaphore s = new Semaphore(0);
			ClosingWindowAdapter adapter = null;
			if(correctType(closingType))
				adapter = new ClosingWindowAdapter(applet,s,closingType);
			else
				adapter = new ClosingWindowAdapter(applet,s);
			
			applet.addWindowAdapter(adapter);
			
			ClosedWindowAdapter closedAdapter = null;
			if(listener != null)
			{
				 closedAdapter = new ClosedWindowAdapter(listener);
				applet.addWindowAdapter(closedAdapter);
			}

			PApplet.runSketch(args, applet);
			

			Frame frame = applet.frame;
			

			if(frame == null)
				throw new ProcessingGraphicException();


			WindowListener[] listeners= frame.getWindowListeners();
			
			if(listeners.length <=0)
				throw new ProcessingGraphicException();
			
			for(WindowListener tempListener : listeners)
			{
				if(!tempListener.equals(adapter)  && !tempListener.equals(closedAdapter))
					frame.removeWindowListener(tempListener);
			}
			
			adapter.setFrame(frame);

			adapterList.add(adapter);

			activeApplets++;

			return s;

		}


	}

	
	private class ClosedWindowAdapter extends WindowAdapter
	{
		private WindowClosedListener listener;

		public ClosedWindowAdapter(WindowClosedListener listener) {
			this.listener = listener;

		}

		@Override
		public void windowClosed(WindowEvent e) {
			listener.windowClosed(e);
		}
	}


	private class ClosingWindowAdapter extends WindowAdapter
	{
		private Frame frame;
		private Semaphore s = null;
		private MyAbstractPApplet applet = null;

		private static final int CALLED_BY_SYSTEM = 0;
		private static final int CALLED_BY_MYSELF = 1;

		private int closingType = PAppletManager.STANDARD_CLOSE;


		/*public ClosingWindowAdapter(Frame frame,MyAbstractPApplet applet) {
			this.frame = frame;
			this.applet = applet;
		}
		 */
		
		public void setFrame(Frame frame) {
			this.frame = frame;
		}
		

		public ClosingWindowAdapter(MyAbstractPApplet applet,Semaphore s) {
			this.applet = applet;
			this.s = s;

		}

		public ClosingWindowAdapter(MyAbstractPApplet applet,Semaphore s,int closingType) {
			this.applet = applet;
			this.s = s;
			this.closingType = closingType;

		}


		@Override
		public void windowClosing(WindowEvent e) {

			disposeOrExit(CALLED_BY_SYSTEM);
		}


		private void disposeOrExit(int callType)
		{
			synchronized (synch) {
				frame.dispose();
				applet.dispose();
				goStop.removeApplet(applet);
				if(s!= null)
					s.release();

				adapterList.remove(this);
				activeApplets--;

				if(closingType == PAppletManager.DISPOSE_ALL && callType == CALLED_BY_SYSTEM)
				{
					int size = adapterList.size();
					ClosingWindowAdapter[] tempArray = new ClosingWindowAdapter[size];
					adapterList.toArray(tempArray);

					for(int i= 0; i < size; i++)
					{
						tempArray[i].disposeOrExit(CALLED_BY_MYSELF);
					}

				}

				if(closingType == PAppletManager.EXIT_ON_CLOSE && callType == CALLED_BY_SYSTEM)
					System.exit(0);


				if(exitOnLastDisposed && activeApplets == 0 && callType == CALLED_BY_SYSTEM)
					System.exit(0);

			}

		}

	}

}
