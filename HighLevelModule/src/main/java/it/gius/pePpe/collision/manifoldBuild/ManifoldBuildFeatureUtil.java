package it.gius.pePpe.collision.manifoldBuild;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.MathUtils;
import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipPoint;
import it.gius.pePpe.collision.manifoldBuild.clipping.ClipEdge;
import it.gius.pePpe.data.shapes.Circle;
import it.gius.pePpe.data.shapes.Edge;
import it.gius.pePpe.data.shapes.Polygon;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.manifold.ContactPoint;

public class ManifoldBuildFeatureUtil {

	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();

	private Vec2[] edgePool;

	public ManifoldBuildFeatureUtil() {
		edgePool = new Vec2[2];

		/*edgePool[0] = new Vec2();
		edgePool[1] = new Vec2();*/
	}


	public void buildFeature(Shape shape, Vec2 singleLocalPoint, PointIDFeature featureOut)
	{
		if(shape instanceof Polygon)
			buildFeaturePolygon((Polygon)shape, singleLocalPoint, featureOut);

		if(shape instanceof Circle)
		{
			featureOut.feature = 0;
			featureOut.vertex = true;
		}

		if(shape instanceof Edge)
		{
			buildFeatureEdge((Edge)shape, singleLocalPoint, featureOut);
		}
	}



	private void buildFeatureEdge(Edge edge, Vec2 singleLocalPoint, PointIDFeature featureOut)
	{
		edgePool[0] = edge.point1;
		edgePool[1] = edge.point2;

		int nearest = nearestVertex(edgePool, 2, singleLocalPoint);

		if(MathUtils.manhattanDistance(edgePool[nearest], singleLocalPoint) < SystemCostants.SQRT_EPSILON)
		{
			featureOut.feature = nearest;
			featureOut.vertex = true;

			return;
		}
		else
		{
			featureOut.feature = 0;
			featureOut.vertex = false;
		}


	}

	private void buildFeaturePolygon(Polygon polygon, Vec2 singleLocalPoint, PointIDFeature featureOut)
	{
		int a = nearestVertex(polygon, singleLocalPoint);
		int polyDim = polygon.getDim();
		int b = a !=0 ? a-1 : polyDim-1;
		int c = a+1 != polyDim ? a+1 : 0;


		if(MathUtils.manhattanDistance(polygon.getVertex(a), singleLocalPoint) < SystemCostants.SQRT_EPSILON)
		{
			featureOut.feature = a;
			featureOut.vertex = true;
			return;
		}

		featureOut.vertex = false;

		Vec2 acNormal = polygon.getNormal(a);

		Vec2 cVertex = polygon.getVertex(c);
		Vec2 slpC = pool1;
		slpC.x = cVertex.x - singleLocalPoint.x;
		slpC.y = cVertex.y - singleLocalPoint.y;
		float dotSlpC = org.jbox2d.common.MathUtils.abs(Vec2.dot(acNormal, slpC));


		if(dotSlpC < SystemCostants.SQRT_EPSILON)
			featureOut.feature = a;
		else
		{
			featureOut.feature = b;

			if(SystemCostants.DEBUG)
			{
				Vec2 baNormal = polygon.getNormal(b);

				Vec2 bVertex = polygon.getVertex(b);
				Vec2 slpB = pool1;
				slpB.x = bVertex.x - singleLocalPoint.x;
				slpB.y = bVertex.y - singleLocalPoint.y;
				float dotSlpB = org.jbox2d.common.MathUtils.abs(Vec2.dot(baNormal, slpB));
				assert(dotSlpB < SystemCostants.SQRT_EPSILON);

			}
		}

	}



	private int nearestVertex(Vec2[] vertices, int dim, Vec2 localPoint)
	{
		float minDistance = Float.MAX_VALUE;
		float currDistance;
		int vertexSolution = -1;
		for(int i=0; i<dim; i++)
		{
			Vec2 currVertex = vertices[i];
			currDistance = MathUtils.manhattanDistance(currVertex, localPoint);
			if(currDistance <minDistance)
			{
				minDistance = currDistance;
				vertexSolution = i;
				if(minDistance < SystemCostants.EPSILON)
					break;
			}
		}

		return vertexSolution;
	}


	private int nearestVertex(Polygon polygon, Vec2 localPoint)
	{
		float minDistance = Float.MAX_VALUE;
		float currDistance;
		int vertexSolution = -1;
		int polyDim = polygon.getDim();
		for(int i=0; i<polyDim; i++)
		{
			Vec2 currVertex = polygon.getVertex(i);
			currDistance = MathUtils.manhattanDistance(currVertex, localPoint);
			if(currDistance <minDistance)
			{
				minDistance = currDistance;
				vertexSolution = i;
				if(minDistance < SystemCostants.EPSILON)
					break;
			}
		}

		return vertexSolution;
	}


	/*
	 * In this method the contact ID will always be a vertex and an edge
	 */
	public void fromClipPointToContactPoint(ClipPoint clipPointIn, Vec2 normalDepth, Vec2 normalNegated, Vec2 localPoint, Vec2 otherLocalPoint,
			boolean refEdgeOnB, Transform incTransform, Transform refTransform,	
			ClipEdge incEdge, ClipEdge refEdge, ContactPoint contactPointOut)
	{
		contactPointOut.distance = -clipPointIn.depth;

		if(clipPointIn.index >=0)
		{
			//is a vertex
			contactPointOut.localPoint.set(localPoint);
			fillFeature(refEdgeOnB, clipPointIn.index, true, contactPointOut);
		}
		else
		{
			//is an edge
			Transform.mulTransToOut(incTransform, clipPointIn.point, contactPointOut.localPoint);
			fillFeature(refEdgeOnB, incEdge.edgeIndex, false, contactPointOut);
		}



		if(refEdgeOnB) /*refEdgeOnB -> incident edge on A -> localPoints on A*/
			//(!refEdgeOnB) for otherGlobalPoint = globalPoint + abs(disance)*normal; (refEdgeOnB) for otherGlobalPoint = globalPoint + disance*normal;
			contactPointOut.normalGlobal.set(normalDepth);
		else
			contactPointOut.normalGlobal.set(normalNegated);

		/*if(clipPointIn.depth < 0) //not overlap point
			contactPointOut.normal.negateLocal();*/

		if(clipPointIn.refPointIndex >=0)
		{
			//is a vertex
			contactPointOut.otherLocalPoint.set(otherLocalPoint);
			fillFeature(!refEdgeOnB, clipPointIn.refPointIndex, true, contactPointOut);
		}
		else
		{
			//is an edge
			Vec2 otherGlobalPoint = pool2;
			otherGlobalPoint.set(contactPointOut.normalGlobal);
			otherGlobalPoint.mulLocal(contactPointOut.distance);

			otherGlobalPoint.addLocal(clipPointIn.point);
			Transform.mulTransToOut(refTransform, otherGlobalPoint, contactPointOut.otherLocalPoint);

			fillFeature(!refEdgeOnB, refEdge.edgeIndex, false, contactPointOut);
		}

		contactPointOut.pointOnShapeB = !refEdgeOnB;

	}
	

	private void fillFeature(boolean fillA, int featureValue, boolean vertex, ContactPoint contactPointOut)
	{
		if(fillA)
		{
			contactPointOut.pointID.featureA = featureValue;
			contactPointOut.pointID.vertexA = vertex;
		}
		else
		{
			contactPointOut.pointID.featureB = featureValue;
			contactPointOut.pointID.vertexB = vertex;
		}
	}

}
