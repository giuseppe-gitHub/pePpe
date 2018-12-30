package it.gius.pePpe.oldSimpleMains;


import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.tracers.DistanceTracer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.forces.ConstantSingleBodyForce;
import it.gius.pePpe.forces.IForce;
import it.gius.pePpe.forces.SpringBodyForce;
import it.gius.pePpe.forces.SpringInit;
import it.gius.pePpe.graph.endNotifiers.ChartGenericTracerNotifier;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;

public class TrainMain {

	/**
	 * @param args
	 */
	public static int N_SQUARES = 3;
	public static int SQUARE_SIDE = 20;

	public static void main(String[] args) {

		PhysicEngine engine = new PhysicEngine();


		ConfigurationFactory confFactory = new ConfigurationFactory();


		try {
			//confFactory.init("beans-pePpe.xml","springTest.properties");
			confFactory.init("trainTest.properties");

			engine.setCollisionFree(true);
			engine.init(confFactory);

		} catch (SimulatorException e1) {
			e1.printStackTrace();
			System.exit(1);
		} catch (Exception e2) {
			e2.printStackTrace();
			System.exit(1);
		}

		engine.setGravity(0,0);
		
		Polygon square = new Polygon();

		square.setAsBox(-SQUARE_SIDE, -SQUARE_SIDE, SQUARE_SIDE, SQUARE_SIDE);

		Body[] bodies = new Body[N_SQUARES];

		for(int i=0; i< N_SQUARES; i++)
		{

			BodyInit bodyInit = new BodyInit();

			bodyInit.angularDamping = 0f;
			bodyInit.linearDamping = 0;
			bodyInit.globalOrigin = new Vec2(i*4*SQUARE_SIDE + 50,200);
			bodyInit.startAngularVelocity = 0;
			bodyInit.startLinearVelocity = new Vec2(0,0);


			//Body body1 = new Body(bodyInit);
			bodies[i] = engine.createBody(bodyInit);

			if(bodies[i] == null)
			{
				System.out.println("Error on adding body");
				System.exit(1);
			}

			BindInit bindInit = new BindInit();
			bindInit.body = bodies[i];
			bindInit.density  = 0.1f;
			bindInit.friction = 0;
			bindInit.restituion = 1;
			bindInit.shape = square.clone();
			//Bind bind1 = new Bind(bindInit);
			engine.createBind(bindInit);
			
		}
		
		IForce[] springs = new IForce[N_SQUARES-1];
		
		DistanceTracer[] tracers = new DistanceTracer[N_SQUARES -1];
		
		for(int i=0; i< N_SQUARES-1; i++)
		{
			SpringInit springInit = new SpringInit();
			springInit.l = SQUARE_SIDE*2 +4;
			springInit.k = 0.00035f*(bodies[i].mass + bodies[i+1].mass)* 9.81f;//engine.getGravityModule();
			springInit.zeta = 0.1f;
			springs[i]= new SpringBodyForce(bodies[i], new Vec2(SQUARE_SIDE-2,0), bodies[i+1], new Vec2(-(SQUARE_SIDE-2),0), springInit);
			
			engine.addForce(springs[i]);
			
			tracers[i] = new DistanceTracer(bodies[i], new Vec2(0,0),bodies[i+1],new Vec2(0,0));
			int j = i+1;
			tracers[i].name = "Distance Wagons " + i + ", " +j; 
			engine.addTracer(tracers[i]);
		}
		
		IForce  trainEngine = new ConstantSingleBodyForce(bodies[N_SQUARES-1], new Vec2(SQUARE_SIDE-2,0), new Vec2(20,0));
		engine.addForce(trainEngine);
		
		
		ISimulator simulator = confFactory.getSimulator();

		SimulationInfo info = new SimulationInfo();
		//info.type= SimulationType.USER_CONTROLLED;
		info.type= SimulationType.TIME_DRIVEN;
		info.simulationStep = 0.12f;
		info.simulationTime = 200;		
		info.simulationName = "Train";

		try {
			simulator.init(engine, info);
			
			//ChartDistanceTracerNotifier ctn = new ChartDistanceTracerNotifier(tracers, "Wagons Distances");
			ChartGenericTracerNotifier ctn = new ChartGenericTracerNotifier("Wagons Distances", 500, 500);
			for(int i=0; i< tracers.length; i++)
				ctn.addTracer(tracers[i], new String[]{"distance"});
			
			simulator.addEndListener(new SimpleNotifier());
			simulator.addEndListener(ctn);

			simulator.start();


		} catch (SimulatorException e) {
			e.printStackTrace();
		}

	}

}
