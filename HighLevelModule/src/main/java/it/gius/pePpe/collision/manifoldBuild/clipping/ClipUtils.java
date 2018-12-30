package it.gius.pePpe.collision.manifoldBuild.clipping;

import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.VerticesShape;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class ClipUtils {

	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	
	private Vec2 pool3 = new Vec2();
	private Vec2 pool4 = new Vec2();
	
	private Vec2 pool5 = new Vec2();
	
	private ClipPoint poolClip = new ClipPoint();
	
	private static final float PERPENDICULAR_EPSILON = 0.01f;
	
	private static final float THRESHOLD_D = 0.00001f;
	/*
	 * This method works properly only for normal and edge that are results
	 * of Epa and the first part of the clipping algorithm
	 */
	/**
	 * 
	 * @param edge1
	 * @param edge2
	 * @param normal
	 * @return 1 if the solution is edge, 2 if the solution is edge2
	 */
	public int mostPerpendicular(Vec2 edge1, Vec2 edge2, Vec2 normal )
	{
		

		Vec2 tangent = pool5;
		tangent.x = normal.y;
		tangent.y = -normal.x;
				
		if(tangent.y == 0)
			tangent.y = 0;
		
		float edge1Tangent = MathUtils.abs(Vec2.dot(edge1, tangent));
		float edge2Tangent = MathUtils.abs(Vec2.dot(edge2, tangent));
		
		if(edge1Tangent < PERPENDICULAR_EPSILON && edge2Tangent < PERPENDICULAR_EPSILON)
			throw new IllegalArgumentException("Both edges are almost parallel to the normal");
		
		if(edge2Tangent < PERPENDICULAR_EPSILON )
			return 1;
		
		if(edge1Tangent < PERPENDICULAR_EPSILON )
			return 2;
		
		
		float edge1Normal = MathUtils.abs(Vec2.dot(edge1, normal));
		float edge2Normal = MathUtils.abs(Vec2.dot(edge2, normal));
		
		if(edge1Normal < PERPENDICULAR_EPSILON && edge2Normal > PERPENDICULAR_EPSILON)
			return 1;
		
		if(edge2Normal < PERPENDICULAR_EPSILON && edge1Normal > PERPENDICULAR_EPSILON)
			return 2;
		
		
		if(edge1Normal/edge1Tangent <= edge2Normal/edge2Tangent)
			return 1;
		else
			return 2;
		
	}
	
	
	public int clip(ClipPoint p1, ClipPoint p2, Vec2 normal, float threshold, int refPointIndex, ClipPoint[] result) throws IllegalArgumentException
	{
		if(result == null || result.length < 2)
			throw new IllegalArgumentException("Bad result array");
	
		int size = 0;
		
		float d1 = Vec2.dot(normal, p1.point) - threshold;
		float d2 = Vec2.dot(normal, p2.point) - threshold;
		
		if(d1 <0 && d1 > -THRESHOLD_D) {
			d1 = 0;
		}
		
		if(d2 <0 && d2 > -THRESHOLD_D) {
			d2 = 0;
		}
		
		int freePos = -1;
		
		if(d1 >=0)
		{
			result[0].set(p1);
			size++;
			
			freePos = 1;
		}
		
		if(d2 >=0)
		{
			result[1].set(p2);
			size++;
			
			freePos = 0;
		}
		
		
		if(d1 * d2 < 0)
		{
			Vec2 edge = pool1;
			
			edge.x = p2.point.x -p1.point.x;
			edge.y = p2.point.y - p1.point.y;
			
			float u = d1 / (d1 - d2);
			
			edge.mulLocal(u);
			edge.addLocal(p1.point);
			
			if(freePos <0 )
				throw new IllegalArgumentException("Found less than 2 points");
			
			ClipPoint middleResult = poolClip;
			
			middleResult.point.set(edge);
			middleResult.index = -1;
			middleResult.refPointIndex = refPointIndex;
			//middleResult.internal = true;
			
			result[freePos].set(middleResult);
			size++;
		}
		
		/*if(size != 2)
			throw new IllegalArgumentException("Found more or less than 2 points");*/
		return size;
	}
	
	public void bestEdge(VerticesShape shape, Transform transform, Vec2 normal, ClipEdge edgeResult)
	{
		if(shape instanceof Edge)
			toClipEdge((Edge)shape, transform, normal, edgeResult);
		
		if(shape instanceof Polygon)
			bestEdge((Polygon)shape, transform, normal, edgeResult);
	}
	
	
	
	private VertexIndexWitness wOut = new VertexIndexWitness();
	
	private void toClipEdge(Edge edge, Transform transform, Vec2 normal, ClipEdge edgeResult)
	{
		Vec2 normalLocal = pool1;
		Vec2 v = pool2;
		
		Mat22.mulTransToOut(transform.R, normal, normalLocal);

		edge.supportPoint(normalLocal, v, wOut);
		
		edgeResult.edgeIndex = 0;
		Transform.mulToOut(transform, v, edgeResult.max);
		Transform.mulToOut(transform, edge.point1, edgeResult.p1);
		Transform.mulToOut(transform, edge.point2, edgeResult.p2);
	}
	
	
	private void bestEdge(Polygon poly, Transform transform, Vec2 normal, ClipEdge edgeResult)
	{
		Vec2 normalLocal = pool1;
		Vec2 v = pool2;
		
		Mat22.mulTransToOut(transform.R, normal, normalLocal);
		
		poly.supportPoint(normalLocal, v, wOut);
		int indexV = wOut.index;
		
		int prevVIndex = indexV != 0 ? indexV -1 : poly.getDim()-1;
		Vec2 prevV =  poly.getVertex(prevVIndex);
		Vec2 nextV = indexV != poly.getDim()-1 ? poly.getVertex(indexV+1) : poly.getVertex(0);
		
		Vec2 nextEdge = pool3;
		nextEdge.set(v);
		nextEdge.subLocal(nextV);
		
		Vec2 prevEdge = pool4;
		prevEdge.set(v);
		prevEdge.subLocal(prevV);
		
		if(mostPerpendicular(prevEdge, nextEdge, normalLocal) == 1)
		{
			/*
			 * Transform points to global coordinates
			 */
			Transform.mulToOut(transform, v, edgeResult.max);
			Transform.mulToOut(transform, prevV, edgeResult.p1);
			edgeResult.p2.set(edgeResult.max);//Transform.mulToOut(transform, v, edgeResult.p2);
			edgeResult.edgeIndex = prevVIndex;
		}
		else
		{
			Transform.mulToOut(transform, v, edgeResult.max);
			edgeResult.p1.set(edgeResult.max);//Transform.mulToOut(transform, v, edgeResult.p1);
			Transform.mulToOut(transform, nextV, edgeResult.p2);
			edgeResult.edgeIndex = indexV;
		}
		
	}
	
}
