package it.gius.pePpe.collision.manifoldBuild;

import it.gius.pePpe.collision.manifoldBuild.clipping.ClipEdge;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipPoint;
import it.gius.pePpe.collision.manifoldBuild.clipping.Clipping;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClippingResult;
import it.gius.pePpe.collision.manifoldBuild.sat.SATAlgorithm;
import it.gius.pePpe.collision.manifoldBuild.sat.SATAlgorithm.MaxSeparationEdgeSolution;
import it.gius.pePpe.data.cache.SATCache;
import it.gius.pePpe.data.shapes.VerticesShape;
import it.gius.pePpe.manifold.ContactManifold;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;


public class VerticesShapeSATManifoldBuilder {
	//TODO to test it

	private SATAlgorithm sat = new SATAlgorithm();
	private Clipping clipping = new Clipping();
	private ManifoldBuildFeatureUtil manifoldUtil = new ManifoldBuildFeatureUtil();
	
	private ClippingResult clipResult = new ClippingResult();
	private MaxSeparationEdgeSolution edgeSeparationA = new MaxSeparationEdgeSolution();
	private MaxSeparationEdgeSolution edgeSeparationB = new MaxSeparationEdgeSolution();

	private ClipEdge edgeA = new ClipEdge();
	private ClipEdge edgeB = new ClipEdge();
	
	private Vec2 normalDepth = new Vec2();
	private Vec2 normalNegated = new Vec2(); 


	public void buildManifold(VerticesShape vShapeA, Transform transformA, VerticesShape vShapeB, Transform transformB,
			SATCache cacheInOut, float threshold, ContactManifold manifold)
	{
		sat.maxSeparationEdgeA(vShapeA, transformA, vShapeB, transformB, cacheInOut.edgeA, edgeSeparationA);

		cacheInOut.edgeA = edgeSeparationA.edgeIndex;


		sat.maxSeparationEdgeA(vShapeB, transformB, vShapeA, transformA, cacheInOut.edgeB, edgeSeparationB);

		cacheInOut.edgeB = edgeSeparationB.edgeIndex;


		ClipEdge refEdge,incEdge;
		VerticesShape refVShape,incVShape;
		Transform refTransform,incTransform;

		boolean refEdgeOnB;
		
		if(edgeSeparationA.separation > threshold && edgeSeparationB.separation > threshold)
		{
			manifold.size = 0;
			return;
		}

		if(edgeSeparationA.separation >= edgeSeparationB.separation)
		{
			refVShape = vShapeA;
			incVShape = vShapeB;
			refTransform = transformA;
			incTransform = transformB;
			//getGlobalNormal(vShapeA, transformA, vShapeB, transformB, edgeSeparationA.edgeIndex, normalDepth);
			normalDepth.set(edgeSeparationA.globalNormal);
			refEdgeOnB = false;
		}
		else
		{
			refVShape = vShapeB;
			incVShape = vShapeA;
			refTransform = transformB;
			incTransform = transformA;
			//getGlobalNormal(vShapeB, transformB, vShapeA, transformA, edgeSeparationB.edgeIndex, normalDepth);
			normalDepth.set(edgeSeparationB.globalNormal);
			//the normal given to clipShapes must point from A to B
			normalDepth.negateLocal();
			refEdgeOnB = true;	
		}

		toClipEdge(vShapeA, transformA, edgeSeparationA.edgeIndex, normalDepth, edgeA);
		toClipEdge(vShapeB, transformB, edgeSeparationB.edgeIndex, normalDepth, edgeB);
		
		if(!refEdgeOnB)
		{
			refEdge = edgeA;
			incEdge = edgeB;
		}
		else
		{
			refEdge = edgeB;
			incEdge = edgeA;
		}
		
		clipping.clipShapes(edgeA, edgeB, refEdgeOnB, normalDepth, vShapeA, vShapeB, clipResult);
		
		normalNegated.set(normalDepth);
		normalNegated.negateLocal();
		
		int j = 0;
		for(int i=0; i<clipResult.size; i++)
		{
			if(-clipResult.clipPoints[i].depth < threshold)
			{
				ClipPoint currPoint = clipResult.clipPoints[i];
				
				//extracting localPoint from shape instead of converting the globalPoint from ClipPoint to avoid calcuation error.
				Vec2 localPoint = (currPoint.index >=0) ? incVShape.getVertex(currPoint.index) : null;
				Vec2 otherLocalPoint = (currPoint.refPointIndex >=0) ? refVShape.getVertex(currPoint.refPointIndex) : null;
				
				manifoldUtil.fromClipPointToContactPoint(currPoint, normalDepth, normalNegated, 
						localPoint, otherLocalPoint, refEdgeOnB,incTransform, refTransform, incEdge, refEdge, manifold.points[j]);
				j++;
			}
		}
		
		manifold.size = j;

	}
	
	
	
	
	private Vec2 pool1 = new Vec2();	
	
	private void toClipEdge(VerticesShape vShape, Transform transform, int indexEdge, Vec2 globalNormal, ClipEdge edgeResult)
	{
		int dim = vShape.getDim();
		int indexVertex1 = indexEdge;
		int indexVertex2 = (indexEdge+1 < dim)? indexEdge+1 : 0;
		
		Vec2 vertex1 = vShape.getVertex(indexVertex1);
		Vec2 vertex2 = vShape.getVertex(indexVertex2);
		
		Vec2 localNormal = pool1;
		Mat22.mulTransToOut(transform.R, globalNormal, localNormal);
		
		float d1 = Vec2.dot(vertex1, localNormal);
		float d2 = Vec2.dot(vertex2, localNormal);
		
		Transform.mulToOut(transform, vertex1, edgeResult.p1);
		Transform.mulToOut(transform, vertex2, edgeResult.p2);
		
		if(d1 >= d2)
			Transform.mulToOut(transform, vertex1, edgeResult.max);
		else
			Transform.mulToOut(transform, vertex2, edgeResult.max);
		
		edgeResult.edgeIndex = indexEdge;
		
	}
}

