package it.gius.pePpe.collision;

import org.jbox2d.common.Transform;

import it.gius.pePpe.data.cache.CollisionCache;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.manifold.ContactManifold;

public interface ICollision {

	//TODO delete this
	public abstract float getMaxContactDistance();

	/*public abstract void collide(BindPair bindPair, DistanceWitnessCache cache,
			ContactManifold manifoldOut);*/

	public abstract void collide(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB,
					CollisionCache cache, ContactManifold manifoldOut);
}