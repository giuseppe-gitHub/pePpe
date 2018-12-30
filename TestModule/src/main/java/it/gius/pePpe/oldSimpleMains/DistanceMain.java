package it.gius.pePpe.oldSimpleMains;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Circle;
//import it.gius.pePpe.data.shapes.Ellipse;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;

import org.jbox2d.common.Vec2;

public class DistanceMain {

	private static enum ShapeType
	{
		POLY,CIRCLE,EDGE;
		
		public static ShapeType get(char c)
		{
			if( c == 'p')
				return POLY;
			
			if(c == 'c')
				return CIRCLE;
			
			if(c == 'e')
				return EDGE;
			
			return null;
		}
	}

	public static void main(String[] args) {

		ShapeType firstType = ShapeType.POLY;
		ShapeType secondType = ShapeType.POLY;

		float angularVelocity = 0;


		if(args.length >= 1)
		{
			char firstChar = args[0].charAt(0);
			char secondChar = args[0].charAt(1);
			
			firstType = ShapeType.get(firstChar);
			if(firstType == null)
				firstType = ShapeType.POLY;
			secondType = ShapeType.get(secondChar);
			if(secondType == null)
				secondType = ShapeType.POLY;

			if(args.length >=2)
			{

				if(args[1].compareTo("r") == 0)
					angularVelocity = 0.02f;
			}
		}



		PhysicEngine engine = new PhysicEngine();

		ConfigurationFactory confFactory = new ConfigurationFactory();

		engine.setCollisionFree(false);
		try {
			confFactory.init("distanceTest.properties");
			//confFactory.init();

			engine.init(confFactory);
		} catch (SimulatorException e1) {
			e1.printStackTrace();
			System.exit(1);
		}catch (Exception e2) {
			e2.printStackTrace();
			System.exit(1);
		}

		Polygon poly1,poly2 = null;
		//Ellipse ellipse1,ellipse2 = null;
		Circle circle1 = null, circle2 = null;

		Shape shape1 = null,shape2 = null;

		poly1 = new Polygon();
		poly1.setAsBox(-20, -20, 20, 20);

		poly2 = new Polygon();
		poly2.setAsBox(-30, -30, 30, 30);

		//ellipse1 = new Ellipse(55, 28, new Vec2(0,0));
		circle1 = new Circle(new Vec2(0,0), 40);
		//ellipse2 = new Ellipse(35, 20, new Vec2(0,0));
		circle2 = new Circle(new Vec2(0,0), 27);
		
		Edge edge1, edge2;
		
		edge1 = new Edge(new Vec2(-20,0), new Vec2(20,0));
		edge2 = new Edge(new Vec2(-30,0), new Vec2(30,0));

		if(firstType == ShapeType.POLY)
			shape1 = poly1;
		
		if(firstType == ShapeType.CIRCLE)
			shape1 = circle1;//ellipse1;
		
		if(firstType == ShapeType.EDGE)
			shape1 = edge1;

		if(secondType == ShapeType.POLY)
			shape2 = poly2;

		if(secondType == ShapeType.CIRCLE)
			shape2 = circle2;//ellipse2;
		
		if(secondType == ShapeType.EDGE)
			shape2 = edge2;

		BodyInit bodyInit = new BodyInit();

		bodyInit.angularDamping = 0f;
		bodyInit.linearDamping = 0f;
		bodyInit.globalOrigin = new Vec2(350,200);
		bodyInit.startAngularVelocity = angularVelocity;
		bodyInit.startLinearVelocity = new Vec2(-0.6f,0f);

		
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
		bindInit.shape = shape1;

		engine.createBind(bindInit);

		BodyInit bodyInit2 = bodyInit.clonePrototype();
		bodyInit2.globalOrigin = new Vec2(200,200);
		bodyInit2.startLinearVelocity = new Vec2(0.5f,0f);
		bodyInit2.startAngularVelocity = 0;
		//bodyInit2.angle = (float)Math.PI/4f;

		Body body2 = engine.createBody(bodyInit2);

		if(body2 == null)
		{
			System.out.println("Error on adding body");
			System.exit(1);
		}


		BindInit bindInit2 = bindInit.clonePrototype();
		bindInit2.body = body2;
		bindInit2.shape = shape2;

		engine.createBind(bindInit2);

		engine.setGravity(0, 0);

		ISimulator simulator = confFactory.getSimulator();

		SimulationInfo info = new SimulationInfo();
		//info.type= SimulationType.USER_CONTROLLED;
		info.type= SimulationType.USER_DRIVEN;
		info.simulationStep = 0.45f;
		info.simulationTime = 350;
		info.simulationName = "simpleDistance";

		try {
			simulator.init(engine, info);

			simulator.addEndListener(new SimpleNotifier());

			simulator.start();
		} catch (SimulatorException e) {
			e.printStackTrace();
			System.exit(1);
		}





	}

}
