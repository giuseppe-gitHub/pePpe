package it.gius.pePpe.data.shapes;

import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.PhysicData;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class Circle extends Shape {

	public final float rr;
	
	private VertexIndexWitness cWitness = new VertexIndexWitness();
	
	private Vec2 pool1 = new Vec2();
	
	public Circle(Vec2 center, float radius) {
		
		if(radius <=0)
			throw new IllegalArgumentException("Radius must be positive");
		
		this.centroid.set(center);
		
		this.radius = radius;
		this.maxInnerDistance = 2*radius;
		cWitness.index = 0;
		
		this.rr = radius*radius;
	}
	
	//@Override
	public Vec2 getVertex(int index) {
		return centroid;
	}
	
	@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, VertexIndexWitness wOut) {
		supportPoint.set(centroid);
		wOut.set(cWitness);
	}

	@Override
	public void computeBox(AABoundaryBox result, Transform transform)
			throws BadShapeException {
		Vec2 newCenter = pool1;
		Transform.mulToOut(transform, centroid, newCenter);
		
		result.lowerBound.x = newCenter.x - radius;
		result.lowerBound.y = newCenter.y -radius;
		result.upperBound.x = newCenter.x + radius;
		result.upperBound.y = newCenter.y +radius;

	}
	
	

	public Circle clone()
	{
		Circle other = new Circle(centroid, radius);
		
		return other;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circle other = (Circle) obj;
		
		return (this.centroid.equals(other.centroid) && this.radius == other.radius);
	}


	@Override
	public void fromWitnessToVec(VertexIndexWitness witness, Vec2 result) {
		result.set(centroid);
	}

	/*@Override
	public Witness getWitnessInstance() {
		return cWitness;
	}*/

	@Override
	public int getDim() {
		return -1;
	}

	@Override
	public boolean contains(Vec2 point) {
		Vec2 distance = pool1;
		distance.set(centroid);
		distance.subLocal(point);
		return (distance.lengthSquared() < rr);
	}

	@Override
	public void getPhysicsData(PhysicData out, float density)
			throws BadShapeException {

		float area = rr*MathUtils.PI;
		
		out.mass = area*density;
		out.massCenter.set(centroid);
		
		float centerIz = area * rr / 2.0f;
		
		//parallel axis theorem
		out.Iz = centerIz*density +out.mass* out.massCenter.lengthSquared();
	}

}
