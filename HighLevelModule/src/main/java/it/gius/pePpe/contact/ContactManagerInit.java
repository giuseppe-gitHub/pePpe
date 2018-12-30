package it.gius.pePpe.contact;

public class ContactManagerInit {
	
	public boolean caching;
	
	public float translationThreshold;
	public boolean warmStart;
	public boolean removeOldPointsInUpdate;
	public int poolStackSize;
	
	
	public float getTranslationThreshold() {
		return translationThreshold;
	}
	
	public void setTranslationThreshold(float translationThreshold) {
		this.translationThreshold = translationThreshold;
	}
	
	public boolean isCaching() {
		return caching;
	}
	
	public void setCaching(boolean caching) {
		this.caching = caching;
	}
	
	public boolean isWarmStart() {
		return warmStart;
	}
	
	public void setWarmStart(boolean warmStart) {
		this.warmStart = warmStart;
	}
	
	public boolean isRemoveOldPointsInUpdate() {
		return removeOldPointsInUpdate;
	}
	
	public void setRemoveOldPointsInUpdate(boolean removeOldPointsInUpdate) {
		this.removeOldPointsInUpdate = removeOldPointsInUpdate;
	}
	
	public int getPoolStackSize() {
		return poolStackSize;
	}
	
	public void setPoolStackSize(int poolStackSize) {
		this.poolStackSize = poolStackSize;
	}
	
	@Override
	public String toString() {
		return "(" +(caching ? "caching, " : "") + (warmStart ? "warmStart, " : "") +
				(removeOldPointsInUpdate ? "removeOlds, " : "") +"translation: " + translationThreshold + ")";
	}

}
