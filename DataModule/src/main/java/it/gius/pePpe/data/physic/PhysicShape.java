package it.gius.pePpe.data.physic;

import it.gius.data.structures.IdGetSet;
import it.gius.pePpe.data.shapes.Shape;

public class PhysicShape implements IdGetSet {

	
	//public short globalBindId; //TOD remove globalBindId frome here, keep Body->Bind reference elsewhere (PhysicEngine class) 
	
	public short localBodyId;
		
	@Override
	public short getId() {
		return localBodyId;
	}
	
	@Override
	public void setId(short id) {
		this.localBodyId = id;
		
	}
	
	PhysicShape() {
		
	}
	
	
	public Shape shape;

	public float friction;
	public float restitution;

	public float density;
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhysicShape other = (PhysicShape) obj;
		if (Float.floatToIntBits(density) != Float
				.floatToIntBits(other.density))
			return false;
		if (Float.floatToIntBits(friction) != Float
				.floatToIntBits(other.friction))
			return false;
		if (Float.floatToIntBits(restitution) != Float
				.floatToIntBits(other.restitution))
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (!shape.equals(other.shape))
			return false;
		return true;
	}

	public void getPhysicData(PhysicData data)
	{
		shape.getPhysicsData(data, density);
	}
	
	
}
