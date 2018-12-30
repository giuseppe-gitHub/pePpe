package it.gius.pePpe.collision;

public class CollisionInit {
	
	public float maxContactDistance;
	
	public float getMaxContactDistance() {
		return maxContactDistance;
	}
	
	public void setMaxContactDistance(float maxContactDistance) {
		this.maxContactDistance = maxContactDistance;
	}
	
	@Override
	public String toString() {
		return "(maxContactDistance: " + maxContactDistance + ")";
	}

}
