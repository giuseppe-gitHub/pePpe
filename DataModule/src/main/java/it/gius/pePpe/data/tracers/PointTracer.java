package it.gius.pePpe.data.tracers;

import it.gius.pePpe.data.physic.Body;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class PointTracer extends AbstractTracer<PosVelTraceData>{

	private Vec2 localBodyPoint = new Vec2();
	private Body body;
	
	
	public PointTracer(Body body, Vec2 localBodyPoint) {
		super();

		if(!body.containsLocal(localBodyPoint))
			throw new RuntimeException("Point is not contained in body");
		
		classData = PosVelTraceData.class;
		
		this.body = body;
		this.localBodyPoint.set(localBodyPoint);
		
	}
	
	/*@Override
	protected DataTrace[] newArray(int dim) {
	
		return (DataTrace[])Array.newInstance(DataTrace.class, dim);
	}*/
	protected PosVelTraceData newT() {
		return new PosVelTraceData();
	};
	
	private Vec2 currGlobalBodyPoint = new Vec2();
	private Vec2 currPointVelocity = new Vec2();
	

	@Override
	protected void fillTrace(DataTrace<PosVelTraceData> currTrace)
	{
		
		Transform.mulToOut(body.transform,localBodyPoint,currGlobalBodyPoint);
		body.getPointVelocity(currGlobalBodyPoint, currPointVelocity);
		
		currTrace.data.x = currGlobalBodyPoint.x;
		currTrace.data.y = currGlobalBodyPoint.y;
		
		currTrace.data.vx = currPointVelocity.x;
		currTrace.data.vy = currPointVelocity.y;
		
	}
	
}
