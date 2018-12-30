package it.gius.pePpe.distance.strategy2;

import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.shapes.Shape;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public interface IInternalPointShapesDistance2 {
	
	public void distance(Shape shape, Transform transform, Vec2 q, DistanceSolution sol);

}
