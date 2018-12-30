package it.gius.pePpe.simulator.processing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.gius.pePpe.drawer.DrawerProperties;
import it.gius.pePpe.drawer.IDrawer;
import it.gius.pePpe.drawer.DrawerSet;
import it.gius.pePpe.drawer.processing.IProcessingDrawer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.render.IRenderer;
import it.gius.pePpe.simulator.Brush;
import it.gius.pePpe.simulator.IDrawContext;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.IRenderable;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
//import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.processing.util.PAppletManager;
import it.gius.processing.util.ProcessingGraphicException;

public class ProcessingDrawContext implements IDrawContext {


	private Map<String,IProcessingDrawer> drawers = null;

	private ClosedListenerImpl closedListener = null;
	private List<IEndListener> endListeners = null;

	private ProcessingDrawingApplet applet = null;
	private PAppletManager appletManager = null;

	private DrawerSet drawerSet = null;
	
	private IRenderer renderer = null;

	private String[] drawerNames = null;

	private int xSize = 500;
	private int ySize = 500;

	private boolean inited = false;
	private boolean started = false;
	
	//private SimulationInfo info = null;
	private DrawerProperties drawerProperties = null;
	
	public void setDrawerProperties(DrawerProperties drawerProperties) {
		this.drawerProperties = drawerProperties;
	}
	
	public DrawerProperties getDrawerProperties() {
		return drawerProperties;
	}
	
	private float frameRate = 1;

	public ProcessingDrawContext() {
	}
	
	public float getFrameRate() {
		return frameRate;
	}
	
	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}
	

		
	public int getxSize() {
		return xSize;
	}



	public void setxSize(int xSize) {
		this.xSize = xSize;
	}


	public int getySize() {
		return ySize;
	}


	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	

	public void setDrawers(IProcessingDrawer[] drawers)
	{
		this.drawers = new HashMap<String, IProcessingDrawer>();
		for(int i=0; i< drawers.length; i++)
		{
			this.drawers.put(drawers[i].getName(), drawers[i]);
		}

		drawerNames = new String[this.drawers.size()];

		Set<String> setNames = this.drawers.keySet();

		drawerNames = setNames.toArray(drawerNames); 
	}


	public IProcessingDrawer[] getDrawers()
	{
		return (IProcessingDrawer[])drawers.values().toArray();
	}

	public void init(PAppletManager manager, ProcessingDrawingApplet applet, PhysicEngine engine, SimulationInfo info) throws SimulatorException
	{
		if(inited)
			throw new SimulatorException("Draw Context already initialized!");

		
		//this.info = info;
		
		if(closedListener == null)
			closedListener = new ClosedListenerImpl();
		
		closedListener.setSimInfo(info);
		
		applet.setClosedListener(closedListener);
		
		if(drawerSet == null)
			drawerSet = new DrawerSet();

		appletManager = manager;

		this.applet = applet;

		for(IProcessingDrawer drawer : drawers.values())
		{
			drawer.setApplet(applet);
			drawer.setEngine(engine);
			
			if(drawerProperties != null)
				drawer.setDrawerProperties(drawerProperties);

			if(drawer.isEnable())
				drawerSet.addDrawer(drawer);
		}
		
		if(renderer != null)
			drawerSet.addDrawer(renderer);

		applet.setDrawer(drawerSet);
		
		if(xSize >= 150 && ySize >= 150)
		{
			applet.setxSize(xSize);
			applet.setySize(ySize);
		}
		
		this.applet.setMyFrameRate(frameRate);
		
		if(drawerProperties != null)
			this.applet.setBackgroundColor(drawerProperties.colorBackground);


		inited = true;
	}

	@Override
	public Brush getBrush() {
		
		return applet;
	}
	
	
	@Override
	public void addRenderable(IRenderable renderable) {
		renderer.addRenderable(renderable);
		
	}
	
	@Override
	public void removeRenderable(IRenderable renderable) {
		renderer.removeRenderable(renderable);
		
	}


	public IRenderer getRenderer() {
		return renderer;
	}
	
	public void setRenderer(IRenderer renderer) {
		this.renderer = renderer;
	}


	public void addEndListener(IEndListener listener) throws SimulatorException{
		if(started)
			throw new SimulatorException("Simulation already started!");

		//if(info.type == SimulationType.USER_DRIVEN)
		//{
			if(closedListener == null)
				closedListener = new ClosedListenerImpl();
			
			closedListener.addEndListener(listener);
		//}
		
		//if(info.type == SimulationType.TIME_DRIVEN)
		//{
			if(endListeners == null)
				endListeners = new ArrayList<IEndListener>();
			
			endListeners.add(listener);
		//}
			

	}


	public void start() throws SimulatorException
	{
		if(!inited)
			throw new SimulatorException("Draw Context not yet initialized!");

		if(started)
			throw new SimulatorException("Simulation already started!");

		drawerSet.setEnable(true);
		
		//if(info.type == SimulationType.TIME_DRIVEN)
			applet.setEndListeners(endListeners);
		

		try {
			//if(info.type == SimulationType.USER_DRIVEN)
				appletManager.startAndAddApplet(new String[]{""}, applet, PAppletManager.DISPOSE_ONLY_ME,closedListener);
			//else
				//appletManager.startAndAddApplet(new String[]{""}, applet, PAppletManager.DISPOSE_ONLY_ME);

						
			
		} catch (ProcessingGraphicException e) {
			e.printStackTrace();
			throw new SimulatorException(e.getMessage());
		}

		started = true;
	}

	@Override
	public String info() {
		return "";
	}

	@Override
	public String[] drawerSet() {

		return drawerNames;

	}

	@Override
	public boolean exist(String drawer) {
		return drawers.containsKey(drawer);
	}

	@Override
	public boolean isActive(String stringDrawer) {
		IDrawer drawer = drawers.get(stringDrawer);

		return (drawer.isEnable() && drawerSet.contains(drawer));
	}

	@Override
	public void activate(String stringDrawer) throws SimulatorException{

		IDrawer drawer = drawers.get(stringDrawer);

		if(!drawer.isEnable())
		{
			drawer.setEnable(true);
			drawerSet.addDrawer(drawer);

			if(applet.getDrawer() != drawerSet)
				applet.setDrawer(drawerSet);
		}
	}

	@Override
	public void disactivate(String stringDrawer) {

		IDrawer drawer = drawers.get(stringDrawer);

		if(drawer.isEnable())
		{
			drawer.setEnable(false);
			drawerSet.removeDrawer(drawer);

			if(applet.getDrawer() != drawerSet)
				applet.setDrawer(drawerSet);
		}
	}

}
