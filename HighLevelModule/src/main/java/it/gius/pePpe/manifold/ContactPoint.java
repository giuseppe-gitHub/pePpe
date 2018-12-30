package it.gius.pePpe.manifold;

import org.jbox2d.common.Vec2;

public class ContactPoint {
	
	public float distance; // >0 or <0
	
	public Vec2 normalGlobal = new Vec2();
	
	public Vec2 localPoint = new Vec2();
	public boolean pointOnShapeB = true;
	
	/*
	 * It is a function of the other parameters, it's being used during the update of the contact point
	 */
	public Vec2 otherLocalPoint = new Vec2(); // otherGlobalPoint = globalPoint + disance*normal
	
	public ContactPointID pointID = new ContactPointID();
	
	public Vec2 globalPoint = new Vec2();
	public Vec2 otherGlobalPoint = new Vec2();
	
	public float accumulatedImpulse = 0;
	
	
	public void set(ContactPoint other)
	{
		this.distance = other.distance;
		this.normalGlobal.set(other.normalGlobal);
		this.localPoint.set(other.localPoint);
		this.otherLocalPoint.set(other.otherLocalPoint);
		this.pointID.set(other.pointID);
		this.pointOnShapeB = other.pointOnShapeB;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactPoint other = (ContactPoint) obj;
		if (distance != other.distance)
			return false;
		
		if (localPoint == null) {
			if (other.localPoint != null)
				return false;
		} else if (!(localPoint.x == other.localPoint.x && localPoint.y == other.localPoint.y))
			return false;
		
		if (otherLocalPoint == null) {
			if (other.otherLocalPoint != null)
				return false;
		} else if (!(otherLocalPoint.x == other.otherLocalPoint.x && otherLocalPoint.y == other.otherLocalPoint.y))
			return false;
		
		if (normalGlobal == null) {
			if (other.normalGlobal != null)
				return false;
		} else if (!(normalGlobal.x == other.normalGlobal.x && normalGlobal.y == other.normalGlobal.y))
			return false;
		if (pointID == null) {
			if (other.pointID != null)
				return false;
		} else if (!pointID.equals(other.pointID))
			return false;
		if (pointOnShapeB != other.pointOnShapeB)
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		
		return "(p: " + localPoint + ", pd: " + otherLocalPoint + 
				", normal: " + normalGlobal + ", distance: " + distance+ ")";
	}

	
	
}
