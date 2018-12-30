package it.gius.pePpe.collision.manifoldBuild.clipping;

import it.gius.pePpe.data.shapes.VerticesShape;

import org.jbox2d.common.Vec2;

public class Clipping {
	
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();
	private Vec2 pool4 = new Vec2();
	private Vec2 pool5 = new Vec2();
	private Vec2 pool6 = new Vec2();
	//private Vec2 pool7 = new Vec2();
	
	private ClipUtils clipUtils = new ClipUtils();
	
	private ClipPoint[] startingPoints;
	private ClipPoint[] firstClipResult;
	
	
	public Clipping() {
		startingPoints = new ClipPoint[2];
		startingPoints[0] = new ClipPoint();
		startingPoints[1] = new ClipPoint();

		firstClipResult = new ClipPoint[2];
		firstClipResult[0] = new ClipPoint();
		firstClipResult[1] = new ClipPoint();

		
	}
	
	
	/**
	 * 
	 * @param edgeA
	 * @param edgeB
	 * @param refEdgeOnB
	 * @param normalDepth must always point from A to B
	 * @param shapeA
	 * @param shapeB
	 * @param result
	 */
	public void clipShapes(ClipEdge edgeA, ClipEdge edgeB, boolean refEdgeOnB, Vec2 normalDepth, VerticesShape shapeA, VerticesShape shapeB,
			 ClippingResult result)
	{
		Vec2 edgeVa = pool2;
		Vec2 edgeVb = pool3;

		edgeA.getEdgeVector(edgeVa);
		edgeB.getEdgeVector(edgeVb);

		ClipEdge refEdge,incEdge;
		Vec2 refVersor = pool4;

		VerticesShape refShape,incShape;
		//Transform refTransform;//,incTransform;

		//boolean refEdgeOnB;
		//Vec2 cc = pool7;
		Vec2 refNormal = pool6;

		if(!refEdgeOnB/*clipUtils.mostPerpendicular(edgeVa, edgeVb, normalDepth) == 1*/)
		{
			refEdge = edgeA;
			refVersor.set(edgeVa);
			refVersor.normalize();
			incEdge = edgeB;
			refShape = shapeA;
			incShape = shapeB;
			//refTransform = transformA;
			//incTransform = transformB;
			//refEdgeOnB = false;
			/*cc.set(ab);
			cc.negateLocal();*/
			refNormal.set(normalDepth);
			refNormal.negateLocal();
		}
		else
		{
			refEdge = edgeB;
			refVersor.set(edgeVb);
			refVersor.normalize();
			incEdge = edgeA;
			refShape = shapeB;
			incShape = shapeA;
			//refTransform = transformB;
			//incTransform = transformA;
			//refEdgeOnB = true;
			//cc.set(ab);
			refNormal.set(normalDepth);
		}


		startingPoints[0].index = incEdge.edgeIndex;
		startingPoints[0].point.set(incEdge.p1);
		startingPoints[0].refPointIndex = -1;
		//startingPoints[0].internal = false;


		startingPoints[1].index = incEdge.edgeIndex+1 != incShape.getDim() ? incEdge.edgeIndex+1 : 0;
		startingPoints[1].point.set(incEdge.p2);
		startingPoints[1].refPointIndex = -1;
		//startingPoints[1].internal = false;

		float o1 = Vec2.dot(refVersor, refEdge.p1);
		int refPoin1Index = refEdge.edgeIndex;
		int pn =clipUtils.clip(startingPoints[0], startingPoints[1], refVersor, o1, refPoin1Index, firstClipResult);
		
		if(pn < 2)
		{
			result.size = 0;
			return ;
		}

		float o2 = Vec2.dot(refVersor, refEdge.p2);
		Vec2 refVersorNegated = pool5;
		refVersorNegated.x = -refVersor.x;
		refVersorNegated.y = -refVersor.y;
		int refPoint2Index = refEdge.edgeIndex+1 != refShape.getDim() ? refEdge.edgeIndex+1 : 0;
		pn =clipUtils.clip(firstClipResult[0], firstClipResult[1], refVersorNegated, -o2, refPoint2Index, result.clipPoints);
		
		if(pn <2)
		{
			result.size = 0;
			return ;
		}



		/*Vec2.crossToOut(1.0f, refVersor, refNormal);
		
		float direction = Vec2.dot(refNormal, cc);
		if(direction < 0)
			refNormal.negateLocal();*/
		
	
		//bug for polygon edge collision (edge could give the wrong normal)
		/* The normal must point towards the inside of the reference shape*/
		  /*Mat22.mulToOut(refTransform.R, refShape.getNormal(refEdge.edgeIndex), refNormal);
		  refNormal.negateLocal();*/

		float max = Vec2.dot(refNormal,refEdge.max);

		float depth1 = Vec2.dot(refNormal, result.clipPoints[0].point) - max;
		float depth2 = Vec2.dot(refNormal, result.clipPoints[1].point) - max;

		result.clipPoints[0].depth = depth1;
		result.clipPoints[1].depth = depth2;
		result.size = 2;
		result.refNormal.set(refNormal);
		result.refNormalNegated.set(refNormal);
		result.refNormalNegated.negateLocal();
		
		return ;
		
		
	}

}
