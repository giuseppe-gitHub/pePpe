package it.gius.pePpe.testSuit.simulations;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.forces.SinFieldForce;
import it.gius.pePpe.forces.SpringBodyForce;
import it.gius.pePpe.forces.SpringInit;
import it.gius.pePpe.forces.SpringPointForce;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class ClothSimulation implements ISimulation {

	private PhysicEngine engine = null;
	private SimulationInfo simInfo;

	public ClothSimulation() {
		simInfo = new SimulationInfo();
		simInfo.simulationStep = 0.05f;
		simInfo.simulationTime = 100;
		simInfo.type = SimulationType.TIME_DRIVEN;
	}

	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {
		SimpleNotifier notifier = new SimpleNotifier();

		notifier.notifyEnd(endType, simInfo);


	}
	
	private String name = "Single Cloth";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "Single cloth with mass-spring";
	}
	
	@Override
	public IProperties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static final int GRID_SIZE_X = 10;
	public static final int GRID_SIZE_Y = 10;

	public static final float X = 200;
	public static final float Y = 50;

	public static final float POINT_DISTANCE = 15;


	@Override
	public void reinit(ConfigurationFactory conFactory)
			throws SimulatorException {
		
		engine = new PhysicEngine();
		engine.setCollisionFree(true);
		
		engine.init(conFactory);
		
		engine.setGravity(0,9.81f);
		//engine.setGravity(0,0);

		//Ellipse ellipse = new Ellipse(1, 1, new Vec2(0,0),0);
		Circle circle = new Circle(new Vec2(0,0), 1);

		BindInit bindInit = new BindInit();
		bindInit.density = 10f;
		bindInit.friction = 0f;
		bindInit.restituion = 1f;

		BodyInit bodyInit = new BodyInit();
		bodyInit.angularDamping = 0.001f;
		bodyInit.linearDamping = 0.001f;
		bodyInit.startAngularVelocity = 0f;
		bodyInit.startLinearVelocity = new Vec2(0,0);

		Body bodies[][] = new Body[GRID_SIZE_X][GRID_SIZE_Y];

		for(int i=0; i< GRID_SIZE_X; i++)
		{
			for(int j= 0; j< GRID_SIZE_Y; j++)
			{
				bodyInit.globalOrigin.x = X + i*POINT_DISTANCE;
				bodyInit.globalOrigin.y = Y + j*POINT_DISTANCE;
				Body body = engine.createBody(bodyInit);

				bodies[i][j] = body;

				bindInit.body = body;
				bindInit.shape = circle.clone(); //ellipse.clone();
				/*Bind bind=*/ engine.createBind(bindInit);
			//	System.out.println("bind.localBodyId: " +bind.localBodyId); 
			}
		}


		Vec2 o = new Vec2(0,0);
		SpringInit springInit = new SpringInit();
		springInit.k = 2.1f*bodies[0][0].mass * engine.getGravityModule();
		System.out.println("k: " + springInit.k);
		//springInit.k = 600f;
		springInit.l = POINT_DISTANCE;
		springInit.zeta = 0.6f;

		for(int i=0; i< GRID_SIZE_X; i++)
		{
			for(int j=0; j< GRID_SIZE_Y; j++)
			{
				
				SpringBodyForce force = null;
				if(i != GRID_SIZE_X-1)
				{
					//springInit.k = 0.1f*2f*bodies[0][0].mass * engine.getGravityModule();
					force = new SpringBodyForce(bodies[i][j], o,bodies[i+1][j], o, springInit);
					engine.addForce(force);
				}

				if(j != GRID_SIZE_Y-1)
				{
					//springInit.k = 0.1f*2f*bodies[0][0].mass * engine.getGravityModule();
					force = new SpringBodyForce(bodies[i][j], o,bodies[i][j+1], o, springInit);
					engine.addForce(force);
				}
			}
		}


		springInit.k = 0.75f*GRID_SIZE_X*bodies[0][0].mass * engine.getGravityModule();
		Body body = bodies[0][0];

		SpringPointForce force = new SpringPointForce(body.getGlobalCenter(), body, body.getLocalCenter(), springInit);
		engine.addForce(force);

		body = bodies[GRID_SIZE_X-1][0];

		force = new SpringPointForce(body.getGlobalCenter(), body, body.getLocalCenter(), springInit);
		engine.addForce(force);

		SinFieldForce windForce = new SinFieldForce(engine.getBodyList());
		windForce.amplitude = 70f;
		windForce.omega = 0.8f;
		windForce.offset = 55f;
		
		engine.addForce(windForce);
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
		return simInfo;
	}

}
