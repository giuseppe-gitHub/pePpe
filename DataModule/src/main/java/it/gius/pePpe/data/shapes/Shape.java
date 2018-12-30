package it.gius.pePpe.data.shapes;

import it.gius.pePpe.SupportPoint;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.PhysicData;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;


/**
 * @author giuseppe
 * @depend - <convert> - it.gius.pePpe.data.shapes.witness.Witness
 * @depend - <new> - it.gius.pePpe.data.aabb.AABoundaryBox
 * @depend - <new> - it.gius.pePpe.data.physic.ShapePhysicData
 * @opt all 
 */
public abstract class Shape implements SupportPoint{
	
	public Vec2 centroid;
	
	public float maxInnerDistance;
	public float radius;
	
	public Object userData;
	
	private Transform identity;
	
	
	public Shape() {
		centroid = new Vec2();
		
		identity = new Transform();
		identity.setIdentity();
	}

	public abstract void computeBox(AABoundaryBox result, Transform transform) throws BadShapeException;
	
	public void computeBox(AABoundaryBox result) throws BadShapeException
	{
		computeBox(result, identity);
	}
	
	//TODO to check better if this method can be deleted from here
//	public abstract Vec2 getVertex(int index);
	
	public abstract void fromWitnessToVec(VertexIndexWitness witness, Vec2 result);
	
	//public abstract Witness getWitnessInstance();
	
	//TODO to move this in VerticesShape, inter here "isCurved"
	public abstract int getDim();
	
	
	public abstract boolean equals(Object other);
	
	
	
	public abstract boolean contains(Vec2 point);
	
	/**
	 * 
	 * @param out data (mass, moment of inertia, centroid) in the parent coordinate system. Moment of inertia is of the origin
	 * @param density
	 * @throws BadShapeException
	 */
	public abstract void getPhysicsData(PhysicData out, float density) throws BadShapeException;
	
}
