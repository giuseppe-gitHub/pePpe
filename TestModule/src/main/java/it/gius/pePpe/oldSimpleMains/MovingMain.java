package it.gius.pePpe.oldSimpleMains;


import org.jbox2d.common.Vec2;

//import it.gius.pePpe.applets.MovingDistanceApplet;
import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;
//import it.gius.pePpe.data.shapes.Ellipse;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.tracers.PointTracer;
import it.gius.pePpe.data.tracers.TimeFilterTracer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.forces.IForce;
import it.gius.pePpe.forces.SpringBodyForce;
import it.gius.pePpe.forces.SpringInit;
import it.gius.pePpe.forces.SpringPointForce;
import it.gius.pePpe.graph.endNotifiers.ChartGenericTracerNotifier;
//import it.gius.pePpe.integrator.RK4Integrator;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;

public class MovingMain {

	/**
	 * @param args
	 */
	static Polygon poly1,poly2,poly3 = null;

	//static Ellipse ellipse,ellipse2;

	@SuppressWarnings("all")
	public static void main(String[] args) {



		PhysicEngine engine = new PhysicEngine();
		
		//engine.setSingleStepIntegrator(new RK4Integrator());
		
		ConfigurationFactory confFactory = new ConfigurationFactory();
		
		try {
			confFactory.init();
		} catch (Exception e2) {
			e2.printStackTrace();
			System.out.println("Shutdown system for initializaion problem");
			System.exit(1);
		}
		
		engine.setCollisionFree(false);
		try {
			engine.init(confFactory);
		} catch (SimulatorException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		initShapes();

		BodyInit bodyInit = new BodyInit();

		bodyInit.angularDamping = 0.005f;
		bodyInit.linearDamping = 0.002f;
		bodyInit.globalOrigin = new Vec2(130,260);
		bodyInit.angle = (float)Math.PI/4;
		bodyInit.startAngularVelocity = 0;
		bodyInit.startLinearVelocity = new Vec2(0,0);


		//Body body1 = new Body(bodyInit);
		Body body1 = engine.createBody(bodyInit);
		
		if(body1 == null)
		{
			System.out.println("Error on adding body");
			System.exit(1);
		}

		BindInit bindInit = new BindInit();
		bindInit.body = body1;
		bindInit.density  = 1.1f;//3.9f;
		bindInit.friction = 0;
		bindInit.restituion = 0.8f;
		bindInit.shape = poly1;
		//Bind bind1 = new Bind(bindInit);
		Bind bind1 = engine.createBind(bindInit);
		
		System.out.println("bind1.localBodyId: " +bind1.phShape.localBodyId);

		BindInit bindInit2 = bindInit.clonePrototype();
		bindInit2.body = body1;
		bindInit2.shape = poly2; //new Ellipse(20, 10, new Vec2(0,0));
		//Bind bind2 = new Bind(bindInit2);
		Bind bind2 = engine.createBind(bindInit2);
		
		System.out.println("bind2.localBodyId: " +bind2.phShape.localBodyId);


		//body1.addBind(bind1); doing this inside createBind

		//body1.addBind(bind2); doing this inside createBind

		//body1.transform.R.set((float)Math.PI/4);
		
		/* ---------------------->>>*///body1.setAngle((float)Math.PI/4);


		BodyInit bodyInit2 = bodyInit.clonePrototype();

		bodyInit2.globalOrigin = new Vec2(280,110);
		bodyInit2.angle = (float)Math.PI/6;


		//Body body2 = new Body(bodyInit2);
		Body body2 = engine.createBody(bodyInit2);


		BindInit bindInit3 = bindInit.clonePrototype();
		bindInit3.density = 3.9f;
		bindInit3.body = body2;
		bindInit3.shape = poly3;
		//Bind bind3 = new Bind(bindInit3);
		Bind bind3 = engine.createBind(bindInit3);
		
		System.out.println("bind3.localBodyId: " +bind3.phShape.localBodyId);

		BindInit bindInit4 = bindInit.clonePrototype();
		bindInit4.density = 3.9f;
		bindInit4.body = body2;
		bindInit4.shape = new Circle(new Vec2(-10,-10), 30);//ellipse;
		//Bind bind4 = new Bind(bindInit4);
		Bind bind4 = engine.createBind(bindInit4);
		
		System.out.println("bind4.localBodyId: " +bind4.phShape.localBodyId);

		//body2.addBind(bind3);

		//body2.addBind(bind4);

		//body2.transform.R.set((float)Math.PI/6);
		//body2.setAngle((float)Math.PI/6);
		
		BodyInit bodyInit3 = bodyInit.clonePrototype();

		bodyInit3.globalOrigin = new Vec2(380,210);
		bodyInit3.startAngularVelocity = 0.3f;


		//Body body2 = new Body(bodyInit2);
		Body body3 = engine.createBody(bodyInit3);
		

		BindInit bindInit5 = bindInit.clonePrototype();
		bindInit5.density =1.9f;
		bindInit5.body = body3;
		bindInit5.shape = new Circle(new Vec2(5,5), 15);//ellipse2;
		//Bind bind4 = new Bind(bindInit4);
		Bind bind5 = engine.createBind(bindInit5);
		
		Polygon floor = new Polygon();
		floor.addVertex(new Vec2(0,0));
		floor.addVertex(new Vec2(600,0));
		floor.addVertex(new Vec2(600,30));
		floor.addVertex(new Vec2(0,30));
		floor.endPolygon();
		
		BodyInit bodyInit4 = bodyInit.clonePrototype();

		bodyInit4.globalOrigin = new Vec2(0,600);
		bodyInit4.fixed = true;


		//Body body2 = new Body(bodyInit2);
		Body body4 = engine.createBody(bodyInit4);
		

		BindInit bindInit6 = bindInit.clonePrototype();
		bindInit6.density =3f;
		bindInit6.body = body4;
		bindInit6.shape = new Edge(new Vec2(50,0), new Vec2(650,0));//floor;
		//Bind bind4 = new Bind(bindInit4);
		Bind bind6 = engine.createBind(bindInit6);
		//body4.inv_mass = 0;
		//body4.inv_Iz = 0;
		

		Body bodies[] = new Body[2];

		bodies[0] = body1;
		
		bodies[1] = body2;
		
		//engine.removeBody(body1);
		engine.setGravity(0,9.81f);
		//engine.setGravity(0,0);
		
		//IForce attractionForce = new AttractionForce(body1, body2, 5.8f);
		
		SpringInit springInit = new SpringInit();
		springInit.l = 15;
		springInit.k = 0.01f*body2.mass * engine.getGravityModule();
		springInit.zeta = 0.01f;
		IForce springForce = new SpringPointForce(new Vec2(345,177), body2, /*new Vec2(-15,-39)*/new Vec2(87,38), springInit);
		

		//engine.addForce(attractionForce);
		
		engine.addForce(springForce);
		
		Vec2 o = new Vec2(0,0);
		
		springInit.l = 20;
		springInit.zeta = 0.1f;
		springInit.k = 0.002f*(body1.mass + body2.mass)* engine.getGravityModule();
		IForce springBodies = new SpringBodyForce(body1, new Vec2(10,10), body2, new Vec2(-30,-14), springInit);
		engine.addForce(springBodies);
		
		springInit.k = 0.02f*(body2.mass + body3.mass)* engine.getGravityModule();
		//springBodies = new SpringBodyForce(body2, new Vec2(-20,10), body3, new Vec2(30,5), springInit);
		//engine.addForce(springBodies);

		//engine.init(bodies,2);

		PointTracer tracer = new PointTracer(body2,new Vec2(0,0));
		tracer.name = "CirclePolyBodyTracer";
		
		
		TimeFilterTracer timeTracer = new TimeFilterTracer(tracer, 0.3f);
		
		//engine.addTracer(tracer);
		engine.addTracer(timeTracer);

		//confFactory.init();
		
		/*engine.removeBody(body1);
		engine.removeBody(body3);
		engine.removeBind(bind4);*/

		ISimulator simulator = confFactory.getSimulator();

		SimulationInfo info = new SimulationInfo();
		//info.type= SimulationType.USER_CONTROLLED;
		info.type= SimulationType.USER_DRIVEN;
		info.simulationStep = 0.1f;
		info.simulationTime = 200;		
		info.simulationName = "Moving";

		try {
			simulator.init(engine, info);
			
			//ChartPointTracersNotifier ctn = new ChartPointTracersNotifier(new PointTracer[]{tracer}, "EllipsePolyBodyTracer");
			ChartGenericTracerNotifier ctn = new ChartGenericTracerNotifier("CirclePolyBodyTracer", 500, 500);
			ctn.addTracer(tracer, new String[]{"x","y"});
			
			simulator.addEndListener(new SimpleNotifier());
			simulator.addEndListener(ctn);

			simulator.start();

			/*	IDrawContext context = simulator.getDrawContext();
			Set<String> names = context.drawerSet();
			String[] namesString = new String[names.size()];

			namesString  = names.toArray(namesString);


			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("acitavting: " + namesString[2]);
			context.activate(namesString[2]);


			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("acitavting: " + namesString[0]);

			context.activate(namesString[0]);

			System.out.println("disacitavting: " + namesString[1]);

			context.disactivate(namesString[1]);*/

		} catch (SimulatorException e) {
			e.printStackTrace();
		}


	}
	

	private static void initShapes()
	{
		poly1 = new Polygon();
		poly1.userData = "poly1";

		//convex polygon
		poly1.addVertex(new Vec2(0,3));
		poly1.addVertex(new Vec2(-17,22));
		poly1.addVertex(new Vec2(20,32));
		poly1.addVertex(new Vec2(30,12));


		try {
			poly1.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 1");
			poly1 = null;
		}

		//poly1.setPosition(new Vec2(10,10));
		//poly1.velocity.set(0.5f,0);	


		poly2 = new Polygon();
		
		poly2.userData = "poly2";

		//convex polygon
		poly2.addVertex(new Vec2(20,15));
		poly2.addVertex(new Vec2(12,50));
		poly2.addVertex(new Vec2(60,50));
		poly2.addVertex(new Vec2(50,20));


		try {
			poly2.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 2");
			poly2 = null;
		}

		//poly2.setPosition(new Vec2(5,5));
		//poly2.velocity.set(-0.25f,0);	



		poly3 = new Polygon();
		poly3.userData = "poly3";

		//convex polygon
		poly3.addVertex(new Vec2(0,0));
		poly3.addVertex(new Vec2(0,20));
		poly3.addVertex(new Vec2(30,50));
		poly3.addVertex(new Vec2(50,50));
		poly3.addVertex(new Vec2(90,40));
		poly3.addVertex(new Vec2(75,20));


		try {
			poly3.endPolygon();
		} catch (BadShapeException e) {
			e.printStackTrace();
			System.out.println("bad shape 3");
			poly3 = null;
		}

		//poly3.setPosition(new Vec2(5,5));
		//poly2.velocity.set(-0.25f,0);	


		//ellipse = new Ellipse(50,25,new Vec2(0,0),(float)Math.PI/4);
		//new Vec2(20,12)
		//ellipse2 = new Ellipse(55,30,new Vec2(0,0),3*(float)Math.PI/4);

	}

}
