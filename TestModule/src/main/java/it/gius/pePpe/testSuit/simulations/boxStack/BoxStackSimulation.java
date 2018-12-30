package it.gius.pePpe.testSuit.simulations.boxStack;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class BoxStackSimulation implements ISimulation {

	private PhysicEngine engine = null;
	private SimulationInfo info = null;
	private BoxStackProperties properties = new BoxStackProperties();

	public BoxStackSimulation() {
		info = new SimulationInfo();
		//info.type= SimulationType.USER_CONTROLLED;
		info.type= SimulationType.USER_DRIVEN;
		info.simulationStep = 0.05f;//0.01f; 
		info.simulationTime = 200;		
		info.simulationName = "BoxStack";
	}

	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {
		SimpleNotifier notifier = new SimpleNotifier();

		notifier.notifyEnd(endType, simInfo);


	}

	private String name = "Box stacking";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "Box stacking on floor";
	}

	@Override
	public IProperties getProperties() {
		return properties;
	}

	/*With 0.2 the boxes start interpenetrating. The box at the bottom
	 *slowly slip through the floor.
	 */
	//public static float RESTITUTION = 0.6f;//0.2f;
	public static float DENSITY = 1f;

	public static float DISTANCE = 15;

	public static float HEIGHT_BOX = 20;

	@Override
	public void reinit(ConfigurationFactory conFactory)
			throws SimulatorException {
		engine = new PhysicEngine();
		engine.init(conFactory);

		engine.setCollisionFree(false);
		engine.setGravity(0, 9.81f);

		BodyInit floorInit = new BodyInit();
		floorInit.fixed = true;
		floorInit.angle = 0;
		floorInit.globalOrigin = new Vec2(0,600);
		floorInit.startAngularVelocity = 0;
		floorInit.startLinearVelocity = new Vec2(0,0);

		Body floorBody = engine.createBody(floorInit);

		BindInit floorBindInit = new BindInit();
		floorBindInit.body = floorBody;
		floorBindInit.density = DENSITY;
		floorBindInit.friction = 0;
		floorBindInit.restituion = properties.getRestitution();
		floorBindInit.shape = new Edge(new Vec2(50,0), new Vec2(650,0));

		/*Bind floorBind = */engine.createBind(floorBindInit);

		Polygon polyProto = new Polygon();
		initPoly(polyProto);

		int numBoxes = properties.getNumBoxes();
		for(int i=0; i<numBoxes; i++)
		{
			Polygon box = polyProto.clone();

			addBox(engine, box, new Vec2(300,570 - (i*2*HEIGHT_BOX + i*DISTANCE)));
		}

	}

	private void initPoly(Polygon polygon)
	{
		if(properties.isLargeBoxes())
			polygon.setAsBox(-50, -HEIGHT_BOX, 50, HEIGHT_BOX);
		else
			polygon.setAsBox(-30, -HEIGHT_BOX, 30, HEIGHT_BOX);
	}

	private void addBox(PhysicEngine engine, Polygon polygon, Vec2 bodyGlobalCenter)
	{
		BodyInit boxBodyInit = new BodyInit();
		boxBodyInit.angle = 0f;
		boxBodyInit.angularDamping = 0;
		boxBodyInit.linearDamping = 0;
		boxBodyInit.fixed = false;
		boxBodyInit.globalOrigin = new Vec2();
		boxBodyInit.globalOrigin.set(bodyGlobalCenter);
		boxBodyInit.startAngularVelocity = 0;
		boxBodyInit.startLinearVelocity = new Vec2(0,0);

		Body boxBody = engine.createBody(boxBodyInit);

		BindInit boxBindinit = new BindInit();
		boxBindinit.body = boxBody;
		boxBindinit.density = DENSITY;
		boxBindinit.restituion = properties.getRestitution();
		boxBindinit.friction = 0;
		boxBindinit.shape = polygon;

		engine.createBind(boxBindinit);
	}

	@Override
	public void clear() {
		engine = null;

	}

	@Override
	public PhysicEngine getEngine() {
		return engine;
	}

	@Override
	public SimulationInfo defaultSimulationInfo() {
		return info;
	}

}
