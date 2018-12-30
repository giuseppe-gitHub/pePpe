package it.gius.pePpe.data.physic;

import it.gius.pePpe.MathUtils;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.aabb.IContained;

public class Bind /*implements IdGetSet*/implements IContained{

	public short globalId;
	
	public boolean removed = false;
		
	/*@Override
	public short getId() {
		return globalId;
	}
	
	@Override
	public void setId(short id) {
		this.globalId = id;
		
	}*/
	
	public PhysicShape phShape;
	public Body body;
	
	//public float friction;
	//public float restitution;
	
	//private float density;
	
	Bind(BindInit bi) {
		
		if(bi.friction < 0 || bi.friction > 1)
			throw new IllegalArgumentException("Friction must be between 0 and 1.");
		
		if(bi.restituion < 0 || bi.restituion > 1)
			throw new IllegalArgumentException("Restitution must be between 0 and 1");
		
		this.body = bi.body;
		
		phShape = new PhysicShape();
		
		this.phShape.shape = bi.shape;
		
		this.phShape.density = bi.density;
		this.phShape.friction = bi.friction;
		this.phShape.restitution = bi.restituion;
	}
	

	public void cloneBindInit(BindInit bindInit)
	{
		bindInit.density = this.phShape.density;
		bindInit.friction = this.phShape.friction;
		bindInit.restituion = this.phShape.restitution;
	}
	
	@Override
	public int hashCode() {
		return MathUtils.cuttableHashCode((int)globalId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bind other = (Bind) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (/*!body.equals(other.body)*/ body != other.body)
			return false;
		if (phShape == null) {
			if (other.phShape != null)
				return false;
		} else if (!phShape.equals(other.phShape))
			return false;
		return true;
	}


	public void getAABoundaryBox(AABoundaryBox box)
	{
		phShape.shape.computeBox(box,body.transform);
		//box.transform(body.getTransform());
	}
}
