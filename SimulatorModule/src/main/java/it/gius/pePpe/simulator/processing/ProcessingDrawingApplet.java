package it.gius.pePpe.simulator.processing;

import java.util.List;

import org.apache.log4j.Logger;
import org.jbox2d.common.Vec2;

import processing.core.PApplet;

import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.drawer.IDrawer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.forces.MouseForce;
import it.gius.pePpe.forces.SpringInit;
import it.gius.pePpe.simulator.Brush;
import it.gius.pePpe.simulator.IDrawContext;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.IEndListener.EndType;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.processing.util.MyAbstractPApplet;
import it.gius.utils.monitor.SimpleMonitor;
import it.gius.utils.monitor.SimpleMonitor.MonitorInit;
import it.gius.utils.monitor.logging.MonitorLogger;

public class ProcessingDrawingApplet extends MyAbstractPApplet implements Brush {

	private static Logger logger = Logger.getLogger(ProcessingDrawingApplet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5063477566125704269L;

	private PhysicEngine engine;

	private SimulationInfo simulationInfo = null;

	private IDrawer drawer = null;

	private IDrawContext context = null;
	private ClosedListenerImpl closedListener = null;

	private float totalSimulated = 0;

	private int xSize = 500;
	private int ySize = 500;

	private int backgroundColor = 10;

	private float myFrameRate = 1f;

	private List<IEndListener> endListeners = null;

	private MouseForce mouseForce;
	//private Vec2 mousePoint = new Vec2();

	public ProcessingDrawingApplet() {

		/*SpringInit springInit = new SpringInit();
		springInit.k = 1000;
		springInit.l = 0;
		springInit.zeta = 0.6f;*/

		mouseForce = new MouseForce();
	}

	public void init(PhysicEngine engine, SimulationInfo info)
	{
		this.engine = engine;
		this.simulationInfo = info;
	}

	private String stringHelp = null;

	public void setDrawContext(IDrawContext context)
	{
		this.context = context;
	}

	public void setClosedListener(ClosedListenerImpl closedListener) {
		this.closedListener = closedListener;
	}

	public void setEndListeners(List<IEndListener> endListeners) {
		this.endListeners = endListeners;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}


	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public float getMyFrameRate() {
		return myFrameRate;
	}

	public void setMyFrameRate(float myFrameRate) {
		this.myFrameRate = myFrameRate;
	}



	public void setDrawer(IDrawer drawer) {
		this.drawer = drawer;
	}

	public IDrawer getDrawer() {
		return drawer;
	}

	private boolean enableMonitor = true;

	public boolean isEnableMonitor() {
		return enableMonitor;
	}

	public void setEnableMonitor(boolean enableMonitor) {
		this.enableMonitor = enableMonitor;
	}

	private SimpleMonitor stepMonitor = null;
	private SimpleMonitor drawMonitor = null;

	public void setup()
	{
		super.setup();
		size(xSize,ySize);

		cursor(PApplet.CROSS);

		MonitorInit mInit = new MonitorInit();
		mInit.dropFirstsMax = 0;
		mInit.dropFirstsMin = 2;
		mInit.dropLower = 1;
		mInit.dropUpper = -1;

		noSmooth();

		mInit.timeUnit = SimpleMonitor.NS;

		stepMonitor = new SimpleMonitor("Processing-Monitor","engine.step",mInit);

		mInit.dropFirstsMax = 0;
		drawMonitor = new SimpleMonitor("Processing-Monitor","draw",mInit);

		stepMonitor.setEnabled(enableMonitor);
		drawMonitor.setEnabled(enableMonitor);

		setStringHelp();

		text(stringHelp,6,15);
		background(255);

		frameRate(myFrameRate);

		if(!goStop.isGo())
			noLoop();
	}


	private void setStringHelp()
	{

		String[] names = this.context.drawerSet();
		stringHelp = new String("Press:\n");
		for(int i=0; i< names.length; i++)
			stringHelp = stringHelp.concat("" + i + " for toggle " + names[i] +" drawer\n" );

		stringHelp = stringHelp.concat("p for pause and resume\n");
		stringHelp = stringHelp.concat("a for add a body\n");
		stringHelp = stringHelp.concat("r for remove a body\n");
		stringHelp = stringHelp.concat("i for simulation info\n");

	}


	private boolean dragging = false;

	//private Vec2 pool1 = new Vec2();


	@Override
	public void mousePressed() {

		if(dragging)
			return;

		Body currBody = engine.getBodyList().firstBody;

		mouseForce.point.x = mouseX;
		mouseForce.point.y = mouseY;

		while(currBody != null)
		{
			if(currBody.containsGlobal(mouseForce.point))
			{
				dragging = true;

				mouseForce.init(currBody, engine.getGravityModule());

				engine.addForce(mouseForce);

				if(!goStop.isGo() && !done)
					redraw();

				return;
			}

			currBody = currBody.next;
		}
	}

	@Override
	public void mouseDragged() {
		if(dragging)
		{
			mouseForce.point.x = mouseX;
			mouseForce.point.y = mouseY;

			if(!goStop.isGo() && !done)
				redraw();
		}
	}


	@Override
	public void mouseReleased() {
		if(dragging)
		{
			dragging = false;
			engine.removeForce(mouseForce);
			mouseForce.clear();

			if(!goStop.isGo() && !done)
				redraw();
		}
	}

	private boolean drawHelp = false;
	private boolean drawInfo = false;
	private boolean inPause = false;

	@Override
	public void keyPressed() {
		super.keyPressed();
		

		if(key == 'h')
		{
			drawHelp = !drawHelp;
			if(!goStop.isGo() && !done)
				redraw();
		}

		if(key == 'i')
		{
			drawInfo =!drawInfo;
			if(!goStop.isGo() && !done)
				redraw();
		}

	}


	@Override
	public void keyReleased(){

		if(key == 'p')
		{
			inPause = (!goStop.isGo() && !done);
			
			if(inPause)
				redraw();			
		}

		if(context != null && key >= '0' && key <='9')
		{
			int value = Character.getNumericValue(key);


			String[] names = context.drawerSet();

			if(value < names.length)
			{
				try {
					if(context.isActive(names[value]))
					{
						//System.out.println("disactivating: " + names[value]);
						logger.info("disactivating: " + names[value]);
						context.disactivate(names[value]);
					}
					else
					{
						//System.out.println("activating: " + names[value]);
						logger.info("activating: " + names[value]);
						context.activate(names[value]);
					}

					if(!goStop.isGo() && !done)
						redraw();

					return;

				} catch (SimulatorException e) {
					e.printStackTrace();
				}
			}
		}


		if(key == 'r')
		{
			Body toRemove = engine.getBodyList().firstBody;

			if(toRemove != null)
			{

				Body result = engine.removeBody(toRemove);

				if(result != null)
				{
					//System.out.println("Body deleted");
					logger.info("Body deleted");
					if(!goStop.isGo() && !done)
						redraw();
				}
				else
				{
					//System.out.println("Body not deleted");
					logger.info("Body not deleted");
				}
			}
			else
			{
				//System.out.println("No more bodies to remove");
				logger.info("No more bodies to remove");
			}

		}


		if(key == 'a')
		{
			BodyInit bodyInit = new BodyInit();

			bodyInit.angularDamping = 0.05f;
			bodyInit.linearDamping = 0;
			bodyInit.startAngularVelocity = 0;
			bodyInit.startLinearVelocity = new Vec2(0,0);
			bodyInit.globalOrigin = new Vec2(width/2f,height/2f);

			Body body = engine.createBody(bodyInit);

			if(body == null)
			{
				//System.out.println("Can't add a body");
				logger.warn("Can't add a body");
			}

			Polygon polygon = new Polygon();
			//polygon.addVertex(new Vec2(-70,-20));
			//polygon.addVertex(new Vec2(120,-1));
			//polygon.addVertex(new Vec2(120,1));
			//polygon.addVertex(new Vec2(-70,20));
			//polygon.endPolygon();
			polygon.setAsBox(-20, -20, 20, 20);


			BindInit bindInit = new BindInit();
			bindInit.body = body;
			bindInit.shape = new Circle(new Vec2(0,0), 15);//polygon;
			bindInit.friction = 0.5f;
			bindInit.restituion = 1;
			bindInit.density = 0.1f;

			Bind bind = engine.createBind(bindInit);

			SpringInit init = new SpringInit();

			init.k = body.mass *engine.getGravityModule();
			init.l = 2;
			init.zeta = 0.7f;

			//SpringPointForce force = new SpringPointForce(new Vec2(width/2f + 60, height/2f),body,new Vec2(60,0),init);

			//engine.addForce(force);

			if(bind == null)
			{
				//System.out.println("Can't add a bind");
				logger.warn("Can't add a bind");
			}
			else
			{
				if(!goStop.isGo() && !done)
					redraw();
			}


		}
	}


	private boolean done = false;


	@Override
	public void run() {

		try {
			super.run();
		} catch (Throwable e) {
			EndType endType = EndType.EXCEPTION;
			endType.throwable = e;
			if(endListeners != null)
			{
				for(IEndListener listener : endListeners)
					listener.notifyEnd(endType,simulationInfo);
			}

			closedListener.alreadyNotified();
		}
	}




	public void draw()
	{
		if(done)
		{
			//System.out.println("[Applet]: Simulation endend!");
			logger.info("Simulation ended!");
			goStop.setGo(false);
			return;
		}
		//background(10,10,10);
		background(backgroundColor);
		//int x = 0xadff2f;


		if(inPause)
		{
			fill(255,0,0);
			text("PAUSE",width -60,height-20);
		}


		if(stringHelp != null && drawHelp)
		{
			fill(175, 238, 238);
			text(stringHelp,6,15);
		}


		//long starTime = System.currentTimeMillis();

		if(goStop.isGo())
		{
			stepMonitor.preCall();

			totalSimulated = engine.step(simulationInfo.simulationStep);

			stepMonitor.postCall();
		}
		
		if(drawInfo)
		{
			fill(175, 238, 238);
			text("frameRate: " + frameRate +"\n"+
					"time: " + totalSimulated+ "\n"+
					"shapes: " + engine.getGlobalShapes().size() +"\n" +
					"bodies: " + engine.getBodyList().bodiesNumber +"\n"+
					"forces: " + engine.getForcesList().size() +"\n"+
				  "contacts: " + engine.getContactSize(),width-130,30);
		}

		drawMonitor.preCall();

		if(drawer != null)
			drawer.draw();

		drawMonitor.postCall();





		if(simulationInfo.type == SimulationType.TIME_DRIVEN && totalSimulated >= simulationInfo.simulationTime)
		{
			done = true;
			fill(255,0,0);
			text("ENDED",width -60,height-20);
			goStop.setGo(false);

			//if(true)
			//throw new RuntimeException("FUUUUU...");

			
			stepMonitor.collect();
			MonitorLogger stepLogger = new MonitorLogger(stepMonitor);
			stepLogger.log();
			drawMonitor.collect();
			MonitorLogger drawLogger = new MonitorLogger(drawMonitor);
			drawLogger.log();


			EndType endType = EndType.TIME_ELAPSED;

			if(endListeners != null)
			{
				for(IEndListener listener : endListeners)
					listener.notifyEnd(endType,simulationInfo);
			}

			closedListener.alreadyNotified();
		}
	}



}
