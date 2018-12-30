package it.gius.pePpe.testSuit.simulations.distance;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;
//import it.gius.pePpe.data.shapes.Circle;
//import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.ShapeType;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.endNotifiers.SimpleNotifier;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class DistanceSimulation implements ISimulation {

	private PhysicEngine engine = null;
	private DistanceProperties distanceProperties = new DistanceProperties();
	private SimulationInfo simInfo;

	public DistanceSimulation() {
		simInfo = new SimulationInfo();
		simInfo.type= SimulationType.TIME_DRIVEN;
		simInfo.simulationStep = 0.45f;
		simInfo.simulationTime = 350;
	}



	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {
		SimpleNotifier notifier = new SimpleNotifier();

		notifier.notifyEnd(endType, simInfo);


	}

	@Override
	public String getName() {
		return "Distance";
	}

	@Override
	public String getDescription() {
		return "Two objects moving with distance drawer activated.";
	}

	@Override
	public IProperties getProperties() {
		return distanceProperties;
	}

	@Override
	public void reinit(ConfigurationFactory conFactory)
			throws SimulatorException {

		engine = new PhysicEngine();

		engine.init(conFactory);
		engine.setCollisionFree(false);
		engine.setGravity(0, 0);

		Polygon poly1,poly2 = null;
		//Ellipse ellipse1,ellipse2 = null;
		//Circle circle1 = null, circle2 = null;

		Shape shape1 = null,shape2 = null;

		if(distanceProperties.getFirstShapeType() == ShapeType.POLYGON)
		{			
			poly1 = new Polygon();
			poly1.setAsBox(-20, -20, 20, 20);
			shape1 = poly1;
		}

		if(distanceProperties.getSecondShapeType() == ShapeType.POLYGON)
		{
			poly2 = new Polygon();
			poly2.setAsBox(-30, -30, 30, 30);
			
			shape2 = poly2;
		}

		if(distanceProperties.getFirstShapeType() == ShapeType.CIRCLE)
		{
			Circle circle1 = new Circle(new Vec2(0,0), 40);
			shape1 = circle1;
		}
		
		if(distanceProperties.getSecondShapeType() == ShapeType.CIRCLE)
		{
			Circle circle2 = new Circle(new Vec2(0,0), 27);
			shape2 = circle2;
		}
		

		if(distanceProperties.getFirstShapeType() == ShapeType.EDGE)
		{
			Edge edge1 = new Edge(new Vec2(-20,0), new Vec2(20,0));
			shape1 = edge1;
		}
		
		if(distanceProperties.getSecondShapeType() == ShapeType.EDGE)
		{
			Edge edge2 = new Edge(new Vec2(-30,0), new Vec2(30,0));
			shape2 = edge2;
		}
		
		BodyInit bodyInit = new BodyInit();

		bodyInit.angularDamping = 0f;
		bodyInit.linearDamping = 0f;
		bodyInit.globalOrigin = new Vec2(350,200);
		bodyInit.startAngularVelocity = distanceProperties.getFirstRotationVelocity();
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
		bodyInit2.startAngularVelocity = distanceProperties.getSecondRotationVelocity();
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
