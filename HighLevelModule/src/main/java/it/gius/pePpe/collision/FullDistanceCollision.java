package it.gius.pePpe.collision;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.collision.manifoldBuild.ManifoldBuildFeatureUtil;
import it.gius.pePpe.collision.manifoldBuild.PointIDFeature;
import it.gius.pePpe.collision.manifoldBuild.ShapeCircleManifoldFiller;
import it.gius.pePpe.collision.manifoldBuild.VerticesShapesOverlapManifoldBuilder;
import it.gius.pePpe.data.cache.CollisionCache;
import it.gius.pePpe.data.cache.CollisionCache.CacheType;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.distance.OverlapSolution;

import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.VerticesShape;
import it.gius.pePpe.distance.IDistance2;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactManifold.ContactType;

public class FullDistanceCollision implements ICollision {

	private IDistance2 distance;
	private float maxContactDistance = 3;
	//TODO externalize this
	private float positiveThreshold = 0.4f;

	private VerticesShapesOverlapManifoldBuilder verticesShapesyBuilder = new VerticesShapesOverlapManifoldBuilder();
	private ShapeCircleManifoldFiller shapeCircleBuilder = new ShapeCircleManifoldFiller();
	private ManifoldBuildFeatureUtil featureUtil = new ManifoldBuildFeatureUtil();

	public FullDistanceCollision(IDistance2 distance, CollisionInit collisionInit) {

		if(collisionInit.maxContactDistance < 0)
			throw new IllegalArgumentException("maxContactDistance shouldnt be negative");

		this.distance = distance;
		this.maxContactDistance = collisionInit.maxContactDistance;
	}


	public void setMaxContactDistance(float maxContactDistance) {
		this.maxContactDistance = maxContactDistance;
	}

	/* (non-Javadoc)
	 * @see it.gius.pePpe.contact.ICollision#getMaxContactDistance()
	 */
	@Override
	public float getMaxContactDistance() {
		return maxContactDistance;
	}


	@Override
	public void collide(/*BindPair bindPair,*/Shape shapeA, Transform transformA, Shape shapeB, Transform transformB,
			CollisionCache cache, ContactManifold manifoldOut)
	{
		/*Shape shapeA = bindPair.bindA.phShape.shape;
		Shape shapeB = bindPair.bindB.phShape.shape;
		Transform transformA = bindPair.bindA.body.transform;
		Transform transformB = bindPair.bindB.body.transform;*/
		DistanceWitnessCache distanceCache = null;
		if(cache != null)
		{
			if(cache.distanceCache == null)
				cache.distanceCache = new DistanceWitnessCache();
			distanceCache = cache.distanceCache;

			cache.type = CacheType.DISTANCE;
		}


		manifoldOut.size = 0;

		if(shapeA instanceof VerticesShape && shapeB instanceof VerticesShape)
			collideVerticesShapes((VerticesShape)shapeA, transformA,(VerticesShape) shapeB, transformB, distanceCache, manifoldOut);


		if(shapeB instanceof Circle)
			collideShapeCircle(shapeA,transformA, (Circle)shapeB, transformB, distanceCache, false, manifoldOut);


		if(shapeA instanceof Circle && !(shapeB instanceof Circle))
			collideShapeCircle(shapeB, transformB, (Circle)shapeA, transformA, distanceCache, true, manifoldOut);

	}

	private OverlapSolution oSol = new OverlapSolution();
	private PointIDFeature localFeature = new PointIDFeature();
	private PointIDFeature otherLocalFeature = new PointIDFeature();

	private void collideVerticesShapes(VerticesShape shapeA, Transform transformA, VerticesShape shapeB, Transform transformB, DistanceWitnessCache cache,
			ContactManifold manifoldOut)
	{
		manifoldOut.type = ContactType.POLYPOLY;

		boolean overlap = distance.overlap(shapeA, transformA, shapeB, transformB, cache, oSol);

		if(oSol.distanceDepth > maxContactDistance)
		{
			manifoldOut.size = 0;
			return;
		}

		if(!overlap && oSol.distanceDepth < positiveThreshold)
		{
			verticesShapesyBuilder.buildManifold(shapeA, transformA, shapeB, transformB,
					oSol.normal, -oSol.distanceDepth, maxContactDistance, manifoldOut);

			if(manifoldOut.size == 0)
				fillManifold(shapeA, transformA, shapeB, transformB, oSol.p1, 
						oSol.p2, oSol.normal, oSol.distanceDepth, false, manifoldOut);
			return;
		}

		if(overlap /*|| oSol.distanceDepth < positiveThreshold*/)
		{
			verticesShapesyBuilder.buildManifold(shapeA, transformA, shapeB, transformB,
					oSol.normal, -oSol.distanceDepth, maxContactDistance, manifoldOut);

		}
		else
		{
			fillManifold(shapeA, transformA, shapeB, transformB, oSol.p1, 
					oSol.p2, oSol.normal, oSol.distanceDepth, false, manifoldOut);

		}

	}


	private void collideShapeCircle(Shape shape,Transform transformShape, Circle circle, Transform transformCircle, DistanceWitnessCache cache,
			boolean flipped, ContactManifold manifoldOut)
	{

		if(shape instanceof Circle)
			manifoldOut.type = ContactType.CIRCLECIRCLE;
		else
			manifoldOut.type = ContactType.POLYCIRCLE;

		boolean overlap;
		if(!flipped)
			overlap = distance.overlap(shape, transformShape, circle, transformCircle, cache, oSol);
		else
			overlap = distance.overlap(circle, transformCircle, shape, transformShape, cache, oSol);


		if(oSol.distanceDepth > maxContactDistance)
		{
			manifoldOut.size = 0;
			return;
		}

		if(overlap)
		{
			shapeCircleBuilder.fillManifold(shape, transformShape, circle, transformCircle, flipped,
					oSol.normal, -oSol.distanceDepth, manifoldOut);
		}
		else
		{
			manifoldOut.size = 1;
			if(!flipped)
			{
				oSol.normal.negateLocal();
				fillManifold(circle, transformCircle, shape, transformShape, oSol.p2, 
						oSol.p1, oSol.normal, oSol.distanceDepth, true, manifoldOut);

			}
			else
			{
				fillManifold(circle, transformCircle, shape, transformShape, oSol.p1,
						oSol.p2, oSol.normal, oSol.distanceDepth, false, manifoldOut);
			}
		}

	}


	private void fillManifold(Shape shape, Transform transform, Shape otherShape, Transform otherTransform, 
			Vec2 globalPoint, Vec2 otherGlobalPoint, Vec2 normal, float distanceDepth, boolean pointOnShapeB, ContactManifold manifoldOut)
	{
		manifoldOut.size = 1;
		Transform.mulTransToOut(transform, globalPoint, manifoldOut.points[0].localPoint);
		Transform.mulTransToOut(otherTransform, otherGlobalPoint, manifoldOut.points[0].otherLocalPoint);
		manifoldOut.points[0].distance = distanceDepth;
		manifoldOut.points[0].normalGlobal.set(normal);
		manifoldOut.points[0].pointOnShapeB = pointOnShapeB;

		featureUtil.buildFeature(shape, manifoldOut.points[0].localPoint, localFeature);
		featureUtil.buildFeature(otherShape, manifoldOut.points[0].otherLocalPoint, otherLocalFeature);

		if(!pointOnShapeB)
		{
			manifoldOut.points[0].pointID.featureA = localFeature.feature;
			manifoldOut.points[0].pointID.vertexA = localFeature.vertex;
			manifoldOut.points[0].pointID.featureB = otherLocalFeature.feature;
			manifoldOut.points[0].pointID.vertexB = otherLocalFeature.vertex;
		}
		else
		{
			manifoldOut.points[0].pointID.featureB = localFeature.feature;
			manifoldOut.points[0].pointID.vertexB = localFeature.vertex;
			manifoldOut.points[0].pointID.featureA = otherLocalFeature.feature;
			manifoldOut.points[0].pointID.vertexA = otherLocalFeature.vertex;
		}
	}
}
