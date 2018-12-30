package it.gius.pePpe.testSuit.simulations.train;

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
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class TrainSimulation implements ISimulation {

	
	private PhysicEngine engine = null;
	private SimulationInfo simInfo;
	private DistanceTracer[] tracers;
	
	private TrainProperties properties = new TrainProperties();
	
	
	public TrainSimulation() {
		simInfo = new SimulationInfo();
		simInfo.simulationStep = 0.12f;
		simInfo.simulationTime = 200;	
		simInfo.type = SimulationType.TIME_DRIVEN;
	}
	
	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {
		SimpleNotifier notifier = new SimpleNotifier();

		notifier.notifyEnd(endType, simInfo);

		ChartGenericTracerNotifier ctn = new ChartGenericTracerNotifier("Wagons Distances", 500, 500);
		for(int i=0; i< tracers.length; i++)
			ctn.addTracer(tracers[i], new String[]{"distance"});

		ctn.notifyEnd(endType, simInfo);
	}

	private String name = "Train";
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "Bodies attached with springs, pulled by first body. Simulation Traced";
	}

	@Override
	public IProperties getProperties() {
		return properties;
	}

	@Override
	public void reinit(ConfigurationFactory conFactory) throws SimulatorException {
		engine = new PhysicEngine();
		engine.setCollisionFree(false);

		engine.init(conFactory);
		
		
		
		engine.setGravity(0,0);
		
		Polygon square = new Polygon();

		square.setAsBox(-properties.getWagonSquareSide(), -properties.getWagonSquareSide(), properties.getWagonSquareSide(), properties.getWagonSquareSide());

		Body[] bodies = new Body[properties.getNumWagon()];

		for(int i=0; i< properties.getNumWagon(); i++)
		{

			BodyInit bodyInit = new BodyInit();

			bodyInit.angularDamping = 0f;
			bodyInit.linearDamping = 0;
			bodyInit.globalOrigin = new Vec2(i*4*properties.getWagonSquareSide() + 50,200);
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
		
		IForce[] springs = new IForce[properties.getNumWagon()-1];
		
		tracers = new DistanceTracer[properties.getNumWagon() -1];
		
		for(int i=0; i< properties.getNumWagon()-1; i++)
		{
			SpringInit springInit = new SpringInit();
			springInit.l = properties.getWagonSquareSide()*2 +4;
			springInit.k = 0.00035f*(bodies[i].mass + bodies[i+1].mass)* 9.81f;//engine.getGravityModule();
			springInit.zeta = 0.1f;
			springs[i]= new SpringBodyForce(bodies[i], new Vec2(properties.getWagonSquareSide()-2,0), bodies[i+1], new Vec2(-(properties.getWagonSquareSide()-2),0), springInit);
			
			engine.addForce(springs[i]);
			
			tracers[i] = new DistanceTracer(bodies[i], new Vec2(0,0),bodies[i+1],new Vec2(0,0));
			int j = i+1;
			tracers[i].name = "Distance Wagons " + i + ", " +j; 
			engine.addTracer(tracers[i]);
		}
		
		IForce  trainEngine = new ConstantSingleBodyForce(bodies[properties.getNumWagon()-1], new Vec2(properties.getWagonSquareSide()-2,0), new Vec2(20,0));
		engine.addForce(trainEngine);

	}

	@Override
	public void clear() {
		engine = null;
		tracers = null;
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
