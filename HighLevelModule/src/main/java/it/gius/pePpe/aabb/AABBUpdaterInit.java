package it.gius.pePpe.aabb;

public class AABBUpdaterInit {
	
	public boolean useEnlargeAABB;
	public float enlargeFactor;
	
	public boolean isUseEnlargeAABB() {
		return useEnlargeAABB;
	}
	
	public float getEnlargeFactor() {
		return enlargeFactor;
	}
	
	public void setEnlargeFactor(float enlargeFactor) {
		this.enlargeFactor = enlargeFactor;
	}
	
	public void setUseEnlargeAABB(boolean useEnlargeAABB) {
		this.useEnlargeAABB = useEnlargeAABB;
	}

}
