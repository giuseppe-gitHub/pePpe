package it.gius.pePpe.distance;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.distance.cache.WitnessCacheManager;
//import it.gius.pePpe.distance.strategy.DistanceStrategy;
import it.gius.pePpe.distance.strategy.bridge.DistanceStrategyBridge;

import org.jbox2d.common.Vec2;

public class GjkDistanceWitnessCacheStrategyImpl implements IDistance {

	
	private WitnessCacheManager cacheManager = new WitnessCacheManager();
	//private DistanceStrategy strategy = new DistanceStrategy();
	private DistanceStrategyBridge strategy = new DistanceStrategyBridge();
	
	private IdList<BindAABBNode> globalShapes;
	
	@Override
	public void init(IdList<BindAABBNode> globalShapes) {
		this.globalShapes = globalShapes;

	}

	@Override
	public void distance(short idA, short idB, DistanceSolution sol) {
		
		DistanceWitnessCache cache = cacheManager.get(idA, idB);
		
		if(cache == null)
			cache = new DistanceWitnessCache();
		
		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}
		
		Bind bindA = globalShapes.get(idA).bind; 
		Bind bindB = globalShapes.get(idB).bind;
		
		strategy.get(bindA.phShape.shape, bindB.phShape.shape).distance(bindA, bindB, cache, sol);
		
		cacheManager.put(idA, idB, cache);

	}

	@Override
	public void distance(short id, Vec2 q, DistanceSolution sol) {
		Bind bind = globalShapes.get(id).bind; 
		
		strategy.get(bind.phShape.shape).distance(bind,q,sol);


	}

	@Override
	public boolean overlap(short idA, short idB, OverlapSolution sol) {
		DistanceWitnessCache cache = cacheManager.get(idA, idB);
		
		if(cache == null)
			cache = new DistanceWitnessCache();
		
		if(idA > idB)
		{
			short temp = idA;
			idA = idB;
			idB = temp;
		}
		
		Bind bindA = globalShapes.get(idA).bind; 
		Bind bindB = globalShapes.get(idB).bind;
		
		boolean result = strategy.get(bindA.phShape.shape, bindB.phShape.shape).overlap(bindA, bindB, cache, sol);
		
		cacheManager.put(idA, idB, cache);
		
		return result;
	}

	@Override
	public void remove(short id) {
		
		cacheManager.remove(id);
	}

}
