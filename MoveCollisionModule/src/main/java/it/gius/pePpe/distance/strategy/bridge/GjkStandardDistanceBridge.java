package it.gius.pePpe.distance.strategy.bridge;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.distance.pool.GjkPool;
import it.gius.pePpe.distance.strategy.IInternal2ShapesDistance;
import it.gius.pePpe.distance.strategy.IInternalPointShapesDistance;
import it.gius.pePpe.distance.strategy2.GJKStandardDistance2;

public class GjkStandardDistanceBridge implements IInternal2ShapesDistance,IInternalPointShapesDistance{
	
	GJKStandardDistance2 wrappedDistance;
	
	
	public GjkStandardDistanceBridge(GjkPool pool, GjkStandardAlgorithm standard, EpaAlgorithm epa) {
		
		wrappedDistance = new GJKStandardDistance2(pool, standard, epa);
	}
	
	
	@Override
	public void distance(Bind bind, Vec2 q, DistanceSolution sol) {
		
		wrappedDistance.distance(bind.phShape.shape, bind.body.transform, q, sol);
		
	}
	
	@Override
	public void distance(Bind bindA, Bind bindB, DistanceWitnessCache witnesses,
			DistanceSolution sol) {
		
		wrappedDistance.distance(bindA.phShape.shape, bindA.body.transform,
				bindB.phShape.shape, bindB.body.transform, witnesses, sol);
		
	}
	
	
	@Override
	public boolean overlap(Bind bindA, Bind bindB, DistanceWitnessCache witnesses,
			OverlapSolution sol) {
		
		return wrappedDistance.overlap(bindA.phShape.shape, bindA.body.transform,
				bindB.phShape.shape, bindB.body.transform, witnesses, sol);
	}

}
