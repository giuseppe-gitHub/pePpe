package it.gius.pePpe.testSuit.simulations;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.tracers.PointTracer;
import it.gius.pePpe.data.tracers.TimeFilterTracer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.forces.IForce;
import it.gius.pePpe.forces.SpringBodyForce;
import it.gius.pePpe.forces.SpringInit;
import it.gius.pePpe.forces.SpringPointForce;
import it.gius.pePpe.graph.endNotifiers.ChartGenericTracerNotifier;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class MovingSimulation implements ISimulation {

	private PhysicEngine engine = null;
	private SimulationInfo simInfo;

	public MovingSimulation() {
		simInfo = new SimulationInfo();
		simInfo.simulationStep = 0.1f;
		simInfo.simulationTime = 100;
		simInfo.type = SimulationType.TIME_DRIVEN;
	}


	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {
		SimpleNotifier notifier = new SimpleNotifier();

		notifier.notifyEnd(endType, simInfo);

		ChartGenericTracerNotifier ctn = new ChartGenericTracerNotifier(name, 500, 500);
		ctn.addTracer(tracer, new String[]{"x","y"});

		ctn.notifyEnd(endType, simInfo);

	}

	private String name = "Moving";

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public IProperties getProperties() {
		return null;
	}

	@Override
	public String getDescription() {
		return "Bodies attached with springs";
	}

	private Polygon poly1 = null, poly2 = null, poly3 = null;
	private Circle bigCircle = null;
	private PointTracer tracer = null;

	@Override
	public void reinit(ConfigurationFactory conFactory) throws SimulatorException {
		engine = new PhysicEngine();
		engine.setCollisionFree(false);

		engine.init(conFactory);

		if(poly1 == null || poly2 == null || poly3 == null || bigCircle == null)
			initShapes();


		BodyInit bodyInit = new BodyInit();

		bodyInit.angularDamping = 0.005f;
		bodyInit.linearDamping = 0.002f;
		bodyInit.globalOrigin = new Vec2(130,260);
		bodyInit.angle = (float)Math.PI/4;
		bodyInit.startAngularVelocity = 0;
		bodyInit.startLinearVelocity = new Vec2(0,0);


		Body body1 = engine.createBody(bodyInit);

		if(body1 == null)
		{
			throw new SimulatorException("Error on adding body");
		}

		BindInit bindInit = new BindInit();
		bindInit.body = body1;
		bindInit.density  = 1.1f;//3.9f;
		bindInit.friction = 0;
		bindInit.restituion = 0.8f;
		bindInit.shape = poly1;
		engine.createBind(bindInit);


		BindInit bindInit2 = bindInit.clonePrototype();
		bindInit2.body = body1;
		bindInit2.shape = poly2;
		engine.createBind(bindInit2);



		BodyInit bodyInit2 = bodyInit.clonePrototype();

		bodyInit2.globalOrigin = new Vec2(280,110);
		bodyInit2.angle = (float)Math.PI/6;


		Body body2 = engine.createBody(bodyInit2);


		BindInit bindInit3 = bindInit.clonePrototype();
		bindInit3.density = 3.9f;
		bindInit3.body = body2;
		bindInit3.shape = poly3;
		engine.createBind(bindInit3);


		BindInit bindInit4 = bindInit.clonePrototype();
		bindInit4.density = 3.9f;
		bindInit4.body = body2;
		bindInit4.shape = bigCircle;
		//Bind bind4 = new Bind(bindInit4);
		engine.createBind(bindInit4);



		BodyInit bodyInit3 = bodyInit.clonePrototype();

		bodyInit3.globalOrigin = new Vec2(380,210);
		bodyInit3.startAngularVelocity = 0.3f;


		Body body3 = engine.createBody(bodyInit3);


		BindInit bindInit5 = bindInit.clonePrototype();
		bindInit5.density =1.9f;
		bindInit5.body = body3;
		bindInit5.shape = new Circle(new Vec2(5,5), 15);//ellipse2;
		engine.createBind(bindInit5);

		Polygon floor = new Polygon();
		floor.addVertex(new Vec2(0,0));
		floor.addVertex(new Vec2(600,0));
		floor.addVertex(new Vec2(600,30));
		floor.addVertex(new Vec2(0,30));
		floor.endPolygon();

		BodyInit bodyInit4 = bodyInit.clonePrototype();

		bodyInit4.globalOrigin = new Vec2(0,600);
		bodyInit4.fixed = true;


		Body body4 = engine.createBody(bodyInit4);


		BindInit bindInit6 = bindInit.clonePrototype();
		bindInit6.density =3f;
		bindInit6.body = body4;
		bindInit6.shape = new Edge(new Vec2(50,0), new Vec2(650,0));//floor;
		engine.createBind(bindInit6);



		Body bodies[] = new Body[2];

		bodies[0] = body1;

		bodies[1] = body2;

		engine.setGravity(0,9.81f);


		SpringInit springInit = new SpringInit();
		springInit.l = 15;
		springInit.k = 0.01f*body2.mass * engine.getGravityModule();
		springInit.zeta = 0.01f;
		IForce springForce = new SpringPointForce(new Vec2(345,177), body2, /*new Vec2(-15,-39)*/new Vec2(87,38), springInit);



		engine.addForce(springForce);

		//Vec2 o = new Vec2(0,0);

		springInit.l = 20;
		springInit.zeta = 0.1f;
		springInit.k = 0.002f*(body1.mass + body2.mass)* engine.getGravityModule();
		IForce springBodies = new SpringBodyForce(body1, new Vec2(10,10), body2, new Vec2(-30,-14), springInit);
		engine.addForce(springBodies);

		springInit.k = 0.02f*(body2.mass + body3.mass)* engine.getGravityModule();


		tracer = new PointTracer(body2,new Vec2(0,0));
		tracer.name = "CirclePolyBodyTracer";

		TimeFilterTracer timeTracer = new TimeFilterTracer(tracer, 0.3f);

		engine.addTracer(timeTracer);


	}

	private void initShapes()
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
			poly1 = null;
		}


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
			poly2 = null;
		}


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
			poly3 = null;
		}

		bigCircle = new Circle(new Vec2(-10,-10), 30);

	}


	@Override
	public void clear() {
		engine = null;
		tracer = null;
	}

	@Override
	public PhysicEngine getEngine() {
		return engine;
	}

	@Override
	public SimulationInfo defaultSimulationInfo() {
		return simInfo;
	}
}
