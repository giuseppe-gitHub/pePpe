package it.gius.pePpe.distance.strategy;

import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.physic.Bind;

import org.jbox2d.common.Vec2;

public interface IInternalPointShapesDistance {

	public void distance(Bind bind, Vec2 q, DistanceSolution sol);
}
