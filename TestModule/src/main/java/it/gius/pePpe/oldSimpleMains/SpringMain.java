package it.gius.pePpe.oldSimpleMains;


import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.tracers.PointTracer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.forces.IForce;
//import it.gius.pePpe.forces.SinFieldForce;
import it.gius.pePpe.forces.SpringInit;
import it.gius.pePpe.forces.SpringPointForce;
import it.gius.pePpe.graph.endNotifiers.ChartGenericTracerNotifier;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;
//import it.gius.pePpe.simulator.endNotifiers.TracersNotify;

import org.jbox2d.common.Vec2;

public class SpringMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PhysicEngine engine = new PhysicEngine();
		
		//engine.setSingleStepIntegrator(new RK4Integrator());
		//engine.setIntegrator(new MixedIntegrator());
		
		//ConfigurationFactory confFactory = ConfigurationFactory.getDefaultInstance();//new ConfigurationFactory();
		ConfigurationFactory confFactory = new ConfigurationFactory();
		
		try {
			//confFactory.init("beans-pePpe.xml","springTest.properties");
			confFactory.init("springTest.properties");
			
			engine.init(confFactory);
		} catch (SimulatorException e1) {
			e1.printStackTrace();
			System.exit(1);
		}catch (Exception e2) {
			e2.printStackTrace();
			System.exit(1);
		} 
 

		Polygon polygon = new Polygon();
		/*polygon.addVertex(new Vec2(-10,-10));
		polygon.addVertex(new Vec2(10,-10));
		polygon.addVertex(new Vec2(10,10));
		polygon.addVertex(new Vec2(-10,10));
		polygon.endPolygon();*/
		float d = 10;
		polygon.setAsBox(-d, -d, d, d);

		BodyInit bodyInit = new BodyInit();

		bodyInit.angularDamping = 0f;
		bodyInit.linearDamping = 0;
		bodyInit.globalOrigin = new Vec2(350,200);
		bodyInit.startAngularVelocity = 0; // 0.01f;
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
		bindInit.density  = 0.1f;
		bindInit.friction = 0;
		bindInit.restituion = 1;
		bindInit.shape = polygon;
		//Bind bind1 = new Bind(bindInit);
		engine.createBind(bindInit);


		SpringInit springInit = new SpringInit();
		springInit.l = 60;
		springInit.k = 0.6f;
		springInit.zeta = 0.0f;
		IForce springForce = new SpringPointForce(new Vec2(350,100), body1, new Vec2(0,0), springInit);
		
		engine.setGravity(0,0);

		engine.addForce(springForce);
		
		BodyInit bodyInit2 = bodyInit.clonePrototype();
		bodyInit2.globalOrigin.set(0,150);
		bodyInit2.startAngularVelocity = 0;
		
		Body edgeBody = engine.createBody(bodyInit2);
		
		BindInit bindInit2 = bindInit.clonePrototype();
		bindInit2.body = edgeBody;
		bindInit2.shape = new Edge(new Vec2(50,0), new Vec2(450,0));
		
		engine.createBind(bindInit2);

		/*SinFieldForce windForce = new SinFieldForce(engine);
		windForce.amplitude = 30f;
		windForce.omega = 0.8f;
		windForce.offset = 25f;*/
		
		
		//engine.addForce(windForce);
		
		PointTracer poinTracer = new PointTracer(body1,new Vec2(5,5));
		poinTracer.name = "SpringBodyCenter";
		
		engine.addTracer(poinTracer);
		
		PointTracer[] tracers = new PointTracer[1];
		tracers[0] = poinTracer;

		//confFactory.init();

		ISimulator simulator = confFactory.getSimulator();

		SimulationInfo info = new SimulationInfo();
		//info.type= SimulationType.USER_CONTROLLED;
		info.type= SimulationType.TIME_DRIVEN;
		info.simulationStep = 0.034f;//0.45f;
		info.simulationTime = 300;
		info.simulationName = "simpleSpring";

		try {
			simulator.init(engine, info);

			simulator.addEndListener(new SimpleNotifier());
			
			//TracersNotify tn = new TracersNotify(tracers,"springPoint.csv");
			//ChartPointTracersNotifier ctn = new ChartPointTracersNotifier(tracers, "springPoint");
			ChartGenericTracerNotifier ctn = new ChartGenericTracerNotifier("springPoint", 500, 500);
			for(int i=0; i<tracers.length;i++)
				ctn.addTracer(tracers[i], new String[]{"x","y"});
			
			//simulator.addEndListener(tn);
			simulator.addEndListener(ctn);

			simulator.start();
		} catch (SimulatorException e) {
			e.printStackTrace();
			System.exit(1);
		}


	}
	


}
