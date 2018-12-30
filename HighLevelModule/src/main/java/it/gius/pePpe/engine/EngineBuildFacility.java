package it.gius.pePpe.engine;

import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.shapes.Shape;

import org.jbox2d.common.Vec2;

public class EngineBuildFacility {

	private BindInit bindInitProto;
	private BodyInit bodyInitProto;
	
	public EngineBuildFacility() {
		bindInitProto = new BindInit();
		bodyInitProto = new BodyInit();
	}
	
	public BindInit getBindInitProto() {
		return bindInitProto;
	}
	
	public BodyInit getBodyInitProto() {
		return bodyInitProto;
	}
	
	public void setBindInitProto(BindInit bindInitProto) {
		this.bindInitProto = bindInitProto;
	}
	public void setBodyInitProto(BodyInit bodyInitProto) {
		this.bodyInitProto = bodyInitProto;
	}
	
	public Bind addSingleShapeBody(PhysicEngine engine, Shape shape, Vec2 bodyGlobalCenter)
	{
		return addSingleShapeBody(engine, shape, bodyGlobalCenter, 0);
	}
	
	public Bind addSingleShapeBody(PhysicEngine engine, Shape shape, Vec2 bodyGlobalCenter, float angle)
	{
		//TODO to test
		BodyInit shapeBodyInit = bodyInitProto.clonePrototype();
		shapeBodyInit.globalOrigin.set(bodyGlobalCenter);
		shapeBodyInit.angle = angle;

		Body shapeBody = engine.createBody(shapeBodyInit);

		BindInit shapeBindinit = bindInitProto.clonePrototype();
		shapeBindinit.body = shapeBody;
		shapeBindinit.shape = shape;

		Bind result = engine.createBind(shapeBindinit);
		
		return result;
	}
	
	public Bind addShapeToBody(PhysicEngine engine, Body body, Shape shape)
	{
		BindInit shapeBindInit = bindInitProto.clonePrototype();
		shapeBindInit.body = body;
		shapeBindInit.shape = shape;
		
		Bind result = engine.createBind(shapeBindInit);
		
		return result;
	}
	
	//TODO add spring creation (single and double body)
	
}
