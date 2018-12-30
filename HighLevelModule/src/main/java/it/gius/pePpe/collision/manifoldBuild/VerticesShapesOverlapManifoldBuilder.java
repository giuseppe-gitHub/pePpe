package it.gius.pePpe.collision.manifoldBuild;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.collision.manifoldBuild.clipping.ClipPoint;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipUtils;
import it.gius.pePpe.collision.manifoldBuild.clipping.Clipping;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClippingResult;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipEdge;
import it.gius.pePpe.data.shapes.VerticesShape;
import it.gius.pePpe.manifold.ContactManifold;

public class VerticesShapesOverlapManifoldBuilder {

	private ClipUtils clipUtils;
	private ClipEdge edgeA, edgeB;

	/*private ClipPoint[] startingPoints;
	private ClipPoint[] firstClipResult;
	private ClipPoint[] secondClipResult;*/

	private Clipping clipping = new Clipping();
	private ClippingResult clipResult;
	
	private ManifoldBuildFeatureUtil manifoldUtil = new ManifoldBuildFeatureUtil();

	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();
	/*private Vec2 pool4 = new Vec2();
	private Vec2 pool5 = new Vec2();
	private Vec2 pool6 = new Vec2();*/
	//private Vec2 pool7 = new Vec2();
	private Vec2 pool8 = new Vec2();
	private Vec2 pool9 = new Vec2();

	public VerticesShapesOverlapManifoldBuilder() {
		clipUtils = new ClipUtils();
		edgeA = new ClipEdge();
		edgeB = new ClipEdge();

		/*startingPoints = new ClipPoint[2];
		startingPoints[0] = new ClipPoint();
		startingPoints[1] = new ClipPoint();

		firstClipResult = new ClipPoint[2];
		firstClipResult[0] = new ClipPoint();
		firstClipResult[1] = new ClipPoint();

		secondClipResult = new ClipPoint[2];
		secondClipResult[0] = new ClipPoint();
		secondClipResult[1] = new ClipPoint();*/
		clipResult = new ClippingResult();
		
	}

	public void buildManifold(VerticesShape vShapeA, Transform transformA, VerticesShape vShapeB, Transform transformB,
			Vec2 normalDepth, float depth, ContactManifold manifold)
	{
		buildManifold(vShapeA, transformA, vShapeB, transformB, normalDepth, depth, 0, manifold);
	}


	public void buildManifold(VerticesShape vShapeA, Transform transformA, VerticesShape vShapeB, Transform transformB,
			Vec2 normalDepth, float depth, float threshold, ContactManifold manifold)
	{
		clipUtils.bestEdge(vShapeA, transformA, normalDepth, edgeA);

		Vec2 normalNegated = pool1;
		normalNegated.set(normalDepth);
		normalNegated.negateLocal();

		clipUtils.bestEdge(vShapeB, transformB, normalNegated, edgeB);

		Vec2 edgeVa = pool2;
		Vec2 edgeVb = pool3;

		edgeA.getEdgeVector(edgeVa);
		edgeB.getEdgeVector(edgeVb);

		
		//Vec2 ab = pool4;
		/*Vec2 globalACenter = pool5;
		Vec2 globalBCenter = pool6;
		
		Transform.mulToOut(transformA, vShapeA.centroid, globalACenter);
		Transform.mulToOut(transformB, vShapeB.centroid, globalBCenter);
		
		ab.x = globalBCenter.x - globalACenter.x;
		ab.y = globalBCenter.y - globalACenter.y;
		
		//if the 2 center are almost identical, choose a casual direction
		if(ab.lengthSquared() < SystemCostants.SQRT_EPSILON)
		{
			ab.x = 1;
			ab.y = 0;
		}*/
		
		
		ClipEdge refEdge,incEdge;
		//Vec2 refVersor = pool4;

		VerticesShape refVShape,incVShape;
		Transform refTransform,incTransform;

		boolean refEdgeOnB;

		if(clipUtils.mostPerpendicular(edgeVa, edgeVb, normalDepth) == 1)
		{
			refEdge = edgeA;
			/*refVersor.set(edgeVa);
			refVersor.normalize();*/
			incEdge = edgeB;
			refVShape = vShapeA;
			incVShape = vShapeB;
			refTransform = transformA;
			incTransform = transformB;
			refEdgeOnB = false;
		}
		else
		{
			refEdge = edgeB;
			/*refVersor.set(edgeVb);
			refVersor.normalize();*/
			incEdge = edgeA;
			refVShape = vShapeB;
			incVShape = vShapeA;
			refTransform = transformB;
			incTransform = transformA;
			refEdgeOnB = true;
		}

		clipping.clipShapes(edgeA, edgeB, refEdgeOnB, normalDepth, vShapeA, vShapeB, clipResult);


		int j = 0;
		for(int i=0; i<clipResult.size; i++)
		{
			if(-clipResult.clipPoints[i].depth < threshold)
			{
				ClipPoint currPoint = clipResult.clipPoints[i];
				
				//extracting localPoint from shape instead of converting the globalPoint from ClipPoint to avoid calcuation error.
				Vec2 localPoint = (currPoint.index >=0) ? incVShape.getVertex(currPoint.index) : pool8;
				Vec2 otherLocalPoint = (currPoint.refPointIndex >=0) ? refVShape.getVertex(currPoint.refPointIndex) : pool9;
				
				manifoldUtil.fromClipPointToContactPoint(currPoint, normalDepth, normalNegated, 
						localPoint, otherLocalPoint, refEdgeOnB,incTransform, refTransform, incEdge, refEdge, manifold.points[j]);
				j++;
			}
		}
		
		manifold.size = j;

	}

}

