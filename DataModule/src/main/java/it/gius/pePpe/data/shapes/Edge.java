package it.gius.pePpe.data.shapes;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.PhysicData;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class Edge extends VerticesShape {


	public Vec2 point1 = new Vec2();
	public Vec2 point2 = new Vec2();
	
	private Vec2 normal = new Vec2();
	private Vec2 p12 = new Vec2();
	private Vec2 p21 = new Vec2();

	private VertexIndexWitness witness = new VertexIndexWitness();

	public Edge(Vec2 point1,Vec2 point2) {

		this.point1.set(point1);
		this.point2.set(point2);
				
		p12.set(point2);
		p12.subLocal(point1);
		
		p21.x = -p12.x;
		p21.y = -p21.y;
		
		normal.x = p12.y;
		normal.y = -p12.x;
		
		normal.normalize();
		
		this.centroid.set(point1);
		Vec2 p12Div2 = pool1;
		p12Div2.set(p12);
		p12Div2.mulLocal(0.5f);
		
		this.centroid.addLocal(p12Div2);
		
		this.maxInnerDistance = p12.length();
		this.radius = this.maxInnerDistance * 0.5f;
		

		
	}

	@Override
	public String toString() {
		return "(p1: " + point1 + ", p2: " + point2 + ")";
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (point1 == null) {
			if (other.point1 != null)
				return false;
		} else if (!point1.equals(other.point1))
			return false;
		if (point2 == null) {
			if (other.point2 != null)
				return false;
		} else if (!point2.equals(other.point2))
			return false;
		return true;
	}



	@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, VertexIndexWitness wOut) {

		float distance1 = point1.x*d.x + point1.y*d.y;
		float distance2 = point2.x*d.x + point2.y*d.y;

		if(distance1 > distance2)
		{
			supportPoint.set(point1);
			witness.index = 0;
		}
		else
		{
			supportPoint.set(point2);
			witness.index = 1;
		}

		if(wOut != null)
			wOut.set(witness);
	}

	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();

	@Override
	public void computeBox(AABoundaryBox result, Transform transform)
			throws BadShapeException {
		Vec2 newPoint1 = pool1;
		Vec2 newPoint2 = pool2;

		Transform.mulToOut(transform, point1, newPoint1);
		Transform.mulToOut(transform, point2, newPoint2);

		result.lowerBound.x = Math.min(newPoint1.x, newPoint2.x);
		result.lowerBound.y = Math.min(newPoint1.y, newPoint2.y);

		result.upperBound.x = Math.max(newPoint1.x, newPoint2.x);
		result.upperBound.y = Math.max(newPoint1.y, newPoint2.y);
	}

	@Override
	public void fromWitnessToVec(VertexIndexWitness witness, Vec2 result) {
		VertexIndexWitness ppWitness = witness;

		if(ppWitness.index == 0)
			result.set(point1);
		else
			result.set(point2);

	}
	
	public Vec2 getNormal()
	{
		return normal;
	}
	
	@Override
	public Vec2 getNormal(int index) {
		if( index == 0)
		return normal;
		
		return null;
	}
	
	@Override
	public Vec2 getVertex(int index)
	{
		if(index == 0)
			return point1;
		
		if(index == 1)
			return point2;
		
		return null;
	}
	

	/*@Override
	public Witness getWitnessInstance() {
		witness.index = 0;
		return witness;
	}*/

	@Override
	public int getDim() {
		return 2;
	}

	@Override
	public boolean contains(Vec2 point) {
		Vec2 distance1 = pool1;
		distance1.set(point);
		distance1.subLocal(point1);
		
		float d = Vec2.dot(distance1, normal);
		if(Math.abs(d) < SystemCostants.EPSILON)
		{
			float u = Vec2.dot(distance1, p12);
			
			if(u <=0)
				return false;
			
			Vec2 distance2 = pool2;
			
			distance2.set(point);
			distance2.subLocal(point2);
			
			float v = Vec2.dot(distance2, p21);
			
			if(v <=0 )
				return false;
			
			return true;
		}
		
		return false;
	}

	@Override
	public void getPhysicsData(PhysicData out, float density)
			throws BadShapeException {

		out.mass = density*maxInnerDistance;
		
		out.massCenter.set(centroid);
		
		out.Iz = out.mass* radius*radius +out.mass* out.massCenter.lengthSquared();
		
	}

}
