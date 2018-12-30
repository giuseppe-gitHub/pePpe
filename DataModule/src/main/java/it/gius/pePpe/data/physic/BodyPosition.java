package it.gius.pePpe.data.physic;

import org.jbox2d.common.Vec2;

public class BodyPosition {

	public Vec2 globalCenter = new Vec2();
	public float angle = 0;
	
	
	public BodyPosition clone()
	{
		BodyPosition result = new BodyPosition();
		result.globalCenter.set(this.globalCenter);
		result.angle = this.angle;
		
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BodyPosition other = (BodyPosition) obj;
		if (Float.floatToIntBits(angle) != Float.floatToIntBits(other.angle))
			return false;
		if (globalCenter == null) {
			if (other.globalCenter != null)
				return false;
		} else if (!globalCenter.equals(other.globalCenter))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "( center: " + globalCenter + ", angle: " +angle + ")";
	}




	public void set(BodyPosition other)
	{
		this.globalCenter.set(other.globalCenter);
		this.angle = other.angle;
	}
}
