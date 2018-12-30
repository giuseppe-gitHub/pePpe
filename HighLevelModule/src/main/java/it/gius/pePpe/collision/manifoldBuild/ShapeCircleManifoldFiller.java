package it.gius.pePpe.collision.manifoldBuild;


import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactPointID;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class ShapeCircleManifoldFiller {
	
	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();
	private Vec2 pool4 = new Vec2();
	private Vec2 pool5 = new Vec2();
	//private Vec2 pool6 = new Vec2();
	
	private PointIDFeature shapeFeature = new PointIDFeature();
	private ManifoldBuildFeatureUtil featureUtil = new ManifoldBuildFeatureUtil();

	/*
	 * Only for overlapping shapes
	 */
	public void fillManifold(Shape shape, Transform shapeTransform, Circle circle, Transform  circleTransform, boolean flipped,
			Vec2 normalDepth, float depth, ContactManifold manifold)
	{
		Vec2 circleGlobalCenter = pool1;
		
		Transform.mulToOut(circleTransform, circle.centroid, circleGlobalCenter);
		
		Vec2 circleGlobalPoint = pool2;
		Vec2 circleLocalPoint = pool4;
		circleGlobalPoint.set(normalDepth);
		circleGlobalPoint.mulLocal(circle.radius);
		
		if(!flipped)
			circleGlobalPoint.negateLocal();
		
		circleGlobalPoint.addLocal(circleGlobalCenter);
		Transform.mulTransToOut(circleTransform, circleGlobalPoint, circleLocalPoint);
		
		Vec2 shapeGlobalPoint = pool3;
		Vec2 shapeLocalPoint = pool5;
		
		shapeGlobalPoint.set(normalDepth);
		shapeGlobalPoint.mulLocal(depth);
		
		if(flipped)
			shapeGlobalPoint.negateLocal();
		
		shapeGlobalPoint.addLocal(circleGlobalPoint);
		
		Transform.mulTransToOut(shapeTransform, shapeGlobalPoint, shapeLocalPoint);
		
		
		manifold.size = 1;
		//manifold.type = ContactType.POLYCIRCLE;
		manifold.points[0].distance = -depth;
		/*
		 * Always circle point as localPoint
		 */
		manifold.points[0].localPoint.set(circleLocalPoint);
		manifold.points[0].normalGlobal.set(normalDepth);
		if(!flipped) //(flipped) for otherGlobalPoint = globalPoint + abs(disance)*normal; (!flipped) for otherGlobalPoint = globalPoint + disance*normal;
			manifold.points[0].normalGlobal.negateLocal();
		
		manifold.points[0].otherLocalPoint.set(shapeLocalPoint);
		manifold.points[0].pointOnShapeB = !flipped;
		ContactPointID pointID = manifold.points[0].pointID;
		
		featureUtil.buildFeature(shape, shapeLocalPoint, shapeFeature);
		
		if(!flipped)
		{
			pointID.featureA = shapeFeature.feature;
			pointID.vertexA = shapeFeature.vertex;
			pointID.featureB = 0;
			pointID.vertexB = true;
		}
		else
		{
			pointID.featureA = 0;
			pointID.vertexA = true;
			pointID.featureB = shapeFeature.feature;
			pointID.vertexB = shapeFeature.vertex;
		}
	}


}
