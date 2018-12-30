package it.gius.pePpe.data.tracers;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.physic.Body;

public class DistanceTracer extends AbstractTracer<DistanceData>{
	
	
	private Body bodyA,bodyB;
	private Vec2 localBodyPointA = new Vec2();
	private Vec2 localBodyPointB = new Vec2();
	
	public DistanceTracer(Body bodyA, Vec2 localBodyPointA,Body bodyB, Vec2 localBodyPointB) {
		
		if(!bodyA.containsLocal(localBodyPointA))
			throw new RuntimeException("Point is not contained in bodyA");
		
		if(!bodyB.containsLocal(localBodyPointB))
			throw new RuntimeException("Point is not contained in bodyB");
		
		classData = DistanceData.class;
		
		this.bodyA = bodyA;
		this.localBodyPointA.set(localBodyPointA);
		
		this.bodyB = bodyB;
		this.localBodyPointB.set(localBodyPointB);
		
		
	}
	
	
	private Vec2 currGlobalBodyPointA = new Vec2();
	private Vec2 currGlobalBodyPointB = new Vec2();
	
	private Vec2 ab = new Vec2();
	
	
	@Override
	protected DistanceData newT() {
		return new DistanceData();
	}
	
	@Override
	protected void fillTrace(DataTrace<DistanceData> currTrace) {
			
		Transform.mulToOut(bodyA.transform,localBodyPointA,currGlobalBodyPointA);
		Transform.mulToOut(bodyB.transform,localBodyPointB,currGlobalBodyPointB);
		
		ab.set(currGlobalBodyPointB);
		ab.subLocal(currGlobalBodyPointA);
		
		currTrace.data.xDistance= ab.x;
		currTrace.data.yDistance= ab.y;
		
		currTrace.data.distance = ab.length();
	}

}
