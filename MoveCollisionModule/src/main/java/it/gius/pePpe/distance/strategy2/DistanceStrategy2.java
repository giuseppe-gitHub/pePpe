package it.gius.pePpe.distance.strategy2;

import it.gius.pePpe.algorithm.epa.EpaAlgorithm;
import it.gius.pePpe.algorithm.gjk.GjkStandardAlgorithm;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.distance.pool.GjkPool;



public class DistanceStrategy2 {
	
	private GJKStandardDistance2 standardDistance;
	private GJKCircleOtherDistance2 circleOtherDistance;
	private DistanceCircleCircle2 circleCircleDistance;
	
	private GjkPool gjkPool;
	
	private GjkStandardAlgorithm standardAlgorithm;
	private EpaAlgorithm epaAlgorithm;
	
	
	public DistanceStrategy2() {
		gjkPool = new GjkPool();
		standardAlgorithm = new GjkStandardAlgorithm();
		epaAlgorithm = new EpaAlgorithm();
		
		standardDistance = new  GJKStandardDistance2(gjkPool, standardAlgorithm, epaAlgorithm);
		circleOtherDistance = new GJKCircleOtherDistance2(gjkPool, standardAlgorithm, epaAlgorithm);
		circleCircleDistance = new DistanceCircleCircle2();
	}
	
	
	public IInternal2ShapesDistance2 get(Shape shapeA, Shape shapeB)
	{
		if((shapeA instanceof Edge ||shapeA instanceof Polygon) && 
				(shapeB instanceof Edge || shapeB instanceof Polygon))
			return standardDistance;

		if((shapeA instanceof Circle && !(shapeB instanceof Circle)) || 
				(!(shapeA instanceof Circle) && (shapeB instanceof Circle)))
			return circleOtherDistance;

		if(shapeA instanceof Circle && shapeB instanceof Circle)
			return circleCircleDistance;

		return null;

	}
	
	public IInternalPointShapesDistance2 get(Shape shape)
	{
		if(shape instanceof Edge || shape instanceof Polygon)
			return standardDistance;
		
		if(shape instanceof Circle)
			return circleOtherDistance;

		return null;

	}

}
