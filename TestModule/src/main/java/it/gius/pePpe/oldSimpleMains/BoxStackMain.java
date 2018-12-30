package it.gius.pePpe.oldSimpleMains;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;

public class BoxStackMain {

	/**
	 * @param args
	 */
	public static int N_BOXES = 6;

	/*With 0.2 the boxes start interpenetrating. The box at the bottom
	 *slowly slip through the floor.
	 */
	public static float RESTITUTION = 0.6f;//0.2f;
	public static float DENSITY = 1f;
	
	public static float DISTANCE = 15;
	
	public static float HEIGHT_BOX = 20;

	public static void main(String[] args) {

		PhysicEngine engine = new PhysicEngine();
		engine.setCollisionFree(false);
		engine.setGravity(0, 9.81f);

		ConfigurationFactory conFactory = new ConfigurationFactory();

		try {
			conFactory.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}


		try {
			engine.init(conFactory);
		} catch (SimulatorException e) {
			e.printStackTrace();
			System.exit(1);
		}


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
		floorBindInit.restituion = RESTITUTION;
		floorBindInit.shape = new Edge(new Vec2(50,0), new Vec2(650,0));

		/*Bind floorBind = */engine.createBind(floorBindInit);

		Polygon polyProto = new Polygon();
		initPoly(polyProto);

		for(int i=0; i<N_BOXES; i++)
		{
			Polygon box = polyProto.clone();

			addBox(engine, box, new Vec2(300,570 - (i*2*HEIGHT_BOX + i*DISTANCE)));
		}



		ISimulator simulator = conFactory.getSimulator();

		SimulationInfo info = new SimulationInfo();
		//info.type= SimulationType.USER_CONTROLLED;
		info.type= SimulationType.USER_DRIVEN;
		info.simulationStep = 0.05f;//0.01f; 
		info.simulationTime = 200;		
		info.simulationName = "BoxStack";


		try {
			simulator.init(engine, info);

			simulator.addEndListener(new SimpleNotifier());

			simulator.start();

		} catch (SimulatorException e) {
			e.printStackTrace();
			System.exit(2);
		}

	}


	private static void initPoly(Polygon polygon)
	{
		polygon.setAsBox(-30, -HEIGHT_BOX, 30, HEIGHT_BOX);
	}

	private static void addBox(PhysicEngine engine, Polygon polygon, Vec2 bodyGlobalCenter)
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
		boxBindinit.restituion = RESTITUTION;
		boxBindinit.friction = 0;
		boxBindinit.shape = polygon;

		engine.createBind(boxBindinit);
	}

}
