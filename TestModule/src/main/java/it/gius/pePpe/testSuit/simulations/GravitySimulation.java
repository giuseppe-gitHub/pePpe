package it.gius.pePpe.testSuit.simulations;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.testSuit.ISimulation;
import it.gius.pePpe.testSuit.property.IProperties;

public class GravitySimulation  implements ISimulation{


	private PhysicEngine engine = null;
	private SimulationInfo simInfo;

	public GravitySimulation() {
		simInfo = new SimulationInfo();
		simInfo.simulationStep = 0.05f;
		simInfo.simulationTime = 100;
		simInfo.type = SimulationType.TIME_DRIVEN;
	}

	@Override
	public String getName() {
		return "Gravity Simulation";
	}

	@Override
	public String getDescription() {
		return "Effetct of gravity on Rigid Body";
	}

	@Override
	public IProperties getProperties() {
		return null;
	}

	@Override
	public SimulationInfo defaultSimulationInfo() {
		return simInfo;
	}

	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {

	}

	@Override
	public void clear() {
		engine = null;
	}
	
	
	private Polygon poly1 = null, poly2 = null;


	@Override
	public void reinit(ConfigurationFactory conFactory)
			throws SimulatorException {

		engine = new PhysicEngine();
		engine.setCollisionFree(true);


		engine.init(conFactory);

		engine.setGravity(0,9.81f);
		
		if(poly1 == null || poly2 == null )
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
		bindInit2.density = 3;
		engine.createBind(bindInit2);
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

	}
	
	
	
	@Override
	public PhysicEngine getEngine() {
		return engine;
	}

}
