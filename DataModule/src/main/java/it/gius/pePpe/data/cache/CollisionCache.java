package it.gius.pePpe.data.cache;

public class CollisionCache {

	public DistanceWitnessCache distanceCache;
	public SATCache satCache;
	
	public CacheType type;

	public static enum CacheType
	{
		DISTANCE,SAT;
	}

	public void clear()
	{
		if(distanceCache != null)
		{
			if(distanceCache.witness != null)
				distanceCache.witness.reset();
		}
	}

}
