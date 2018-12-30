package it.gius.pePpe.distance.strategy.bridge;

import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.distance.pool.GjkPool;
import it.gius.pePpe.distance.strategy.IInternal2ShapesDistance;
import it.gius.pePpe.distance.strategy.IInternalPointShapesDistance;

public class DistanceStrategyBridge {

	private GjkStandardDistanceBridge standardDistance;
	private GjkCircleOtherDistanceBridge gjkCircleOtherDistance;
	private DistanceCircleCircleBridge distanceCircleCircle;
	
	private GjkPool gjkPool;

	private GjkStandardAlgorithm gjkStandardAlgorithm;
	private EpaAlgorithm epa;
	
	public DistanceStrategyBridge() {
		gjkPool = new GjkPool();
		epa = new EpaAlgorithm();
		gjkStandardAlgorithm = new GjkStandardAlgorithm();
		
		
		standardDistance = new GjkStandardDistanceBridge(gjkPool, gjkStandardAlgorithm, epa);
		gjkCircleOtherDistance = new GjkCircleOtherDistanceBridge(gjkPool, gjkStandardAlgorithm, epa);
		distanceCircleCircle = new DistanceCircleCircleBridge();
	}
	
	

	public IInternal2ShapesDistance get(Shape shapeA,Shape shapeB)
	{

		if((shapeA instanceof Edge ||shapeA instanceof Polygon) && 
				(shapeB instanceof Edge || shapeB instanceof Polygon))
			return standardDistance;

		if((shapeA instanceof Circle && !(shapeB instanceof Circle)) || 
				(!(shapeA instanceof Circle) && (shapeB instanceof Circle)))
			return gjkCircleOtherDistance;

		if(shapeA instanceof Circle && shapeB instanceof Circle)
			return distanceCircleCircle;

		return null;
	}


	public IInternalPointShapesDistance get(Shape shape)
	{
		if(shape instanceof Edge || shape instanceof Polygon )
			return standardDistance;
		
		if(shape instanceof Circle)
			return gjkCircleOtherDistance;

		return null;
	}


}
