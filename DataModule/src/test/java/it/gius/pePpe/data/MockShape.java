package it.gius.pePpe.data;

import java.util.Random;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.PhysicData;
import it.gius.pePpe.data.shapes.BadShapeException;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

public class MockShape extends Shape {
	
	
	private Random random = new Random(System.currentTimeMillis());
	
	//mock method
	public void computeBox(AABoundaryBox box, Transform transform)
	{
		box.lowerBound.x = random.nextInt(400);
		box.lowerBound.y = random.nextInt(400);
		box.upperBound.x = box.lowerBound.x + 2+random.nextInt(20); 
		box.upperBound.y = box.lowerBound.y + 2+random.nextInt(20);
	}
	
	
	
	@Override
	public boolean equals(Object other) {
		if(this == other)
			return true;
		
		return false;
	}
	
	@Override
	public void fromWitnessToVec(VertexIndexWitness witness, Vec2 result) {
		result.set(random.nextFloat()*100,random.nextFloat()*200);
	}
	
	@Override
	public boolean contains(Vec2 point) {
		return false;
	}
	
	@Override
	public void getPhysicsData(PhysicData out, float density)
			throws BadShapeException {
	
		
	}
	
	@Override
	public int getDim() {
		return 0;
	}
	
	/*@Override
	public Witness getWitnessInstance() {
		return null;
	}*/
	
	//@Override
	public Vec2 getVertex(int index) {
		return null;
	}
	
	@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, VertexIndexWitness wOut) {
		return;
	}

}
