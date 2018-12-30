package it.gius.pePpe.distance.strategy.bridge;

import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.distance.strategy.IInternal2ShapesDistance;
import it.gius.pePpe.distance.strategy2.DistanceCircleCircle2;

public class DistanceCircleCircleBridge implements IInternal2ShapesDistance{
	
	private DistanceCircleCircle2 wrappedDistance;
	
	public DistanceCircleCircleBridge() {
		
		wrappedDistance = new DistanceCircleCircle2();
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
