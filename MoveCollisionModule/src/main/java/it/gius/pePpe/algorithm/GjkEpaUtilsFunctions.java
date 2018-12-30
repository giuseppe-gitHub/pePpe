package it.gius.pePpe.algorithm;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;
//import it.gius.pePpe.shapes.Polygon;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 * @opt all
 */
public class GjkEpaUtilsFunctions {


	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();

	/**
	 * @hidden
	 */
	public GjkEpaUtilsFunctions() {
		
	}


	public void nearestEdgeInsidePointSimplex2D(Simplex simplex, Vec2 q, SimplexEdge solutionEdge)
	{
		
		solutionEdge.distance = Float.MAX_VALUE;
		
		Vec2 normal = pool1;
		
		Vec2 e = pool2;
		
		Vec2 qa = pool3;
		
		for(int i=0; i< simplex.currentDim; i++)
		{
			int i1 =i;
			int i2 = i+1 < simplex.currentDim ? i+1 : 0;
			
			Vec2 vsA = simplex.vs[i1];
			Vec2 vsB = simplex.vs[i2];
			
			//e.set(vsB);
			//e.subLocal(vsA);
			e.x = vsB.x -vsA.x;
			e.y = vsB.y - vsA.y;
			//Vec2 e = vsB.sub(vsA);
			
			//qa.set(vsA);
			//qa.subLocal(q);
			qa.x = vsA.x - q.x;
			qa.y = vsA.y -q.y;
			//Vec2 qa = vsA.sub(q);
			
			
			if(simplex.winding == SystemCostants.CLOCKWISE_WINDING)
				Vec2.crossToOut(1.0f, e, normal);//normal.set(-e.y,e.x);//normal.set(e.y,-e.x);
			else
				Vec2.crossToOut(-1.0f, e, normal);//normal.set(e.y,-e.x);//normal.set(-e.y,e.x);
			
		/*	if(normal.x == 0) //-0 sign problem
				normal.x = 0;
			
			if(normal.y == 0) //-0 sign problem
				normal.y = 0;*/
			
			normal.normalize();
			
			//float distance = Vec2.dot(normal, qa);
			float distance = normal.x*qa.x + normal.y*qa.y;
			
			/*********************************************/
			/*if(distance <0)
			{
				//continue;
				distance = -distance;
				normal.negateLocal();
				Vec2 temp = simplex.vs[i1];
				simplex.vs[i1] = simplex.vs[i2];
				simplex.vs[i2] = temp;
			}*/
			if(distance <0)
			{
				throw new RuntimeException("distance < 0: " + distance + ", normal.x: " + normal.x +", normal.y: " + normal.y +
						", qa.x: " + qa.x +", qa.y: " +qa.y + ", simplex.dim:" + simplex.currentDim);
			}
			
			/**********************************************/
			
			if(distance < solutionEdge.distance)
			{
				solutionEdge.indexB = i2;
				solutionEdge.distance = distance;
				solutionEdge.normal.set(normal);
			}
				
		}
	}
	
	
	
	public float g(Vec2 currSol,Vec2 q, Vec2 support/*IGjkGeneralShape shape*/)
	{

		
		//Vec2 diff = currSol.sub(q);
		Vec2 diff = pool2;
		diff.x = currSol.x - q.x;
		diff.y = currSol.y - q.y;
		//Vec2 diffOpposite = diff.negate();
		Vec2 diffOpposite = pool3;
		diffOpposite.x = -diff.x;
		diffOpposite.y = -diff.y;
		
		//int index = supportPointIndex(poly, diffOpposite);
		
		//Vec2 difSupport = poly.getVertex(index).sub(q);
		//Vec2 difSupport = pool1;
		Vec2 difSupport = pool1;
		difSupport.set(support);
		//shape.supportPoint(diffOpposite,difSupport);
		
		difSupport.subLocal(q);
		
		float result = Vec2.dot(diff, diff) + Vec2.dot(difSupport, diffOpposite);
		
		
		return result;
	}
	
	
	/*public float gOrigin(Vec2 currSol,Polygon poly1, Polygon poly2)
	{

		
		Vec2 diff = currSol;
		Vec2 diffOpposite = diff.mul(-1);
		
		int index2 = supportPointIndex(poly2, diffOpposite);
		int index1 = supportPointIndex(poly1, diff);
		
		Vec2 support1 = poly1.getVertex(index1);
		Vec2 support2 = poly2.getVertex(index2);
		
		Vec2 supportTotal = support2.sub(support1);
		
		float result = Vec2.dot(diff, diff) + Vec2.dot(supportTotal, diffOpposite);
		
		
		return result;
	}*/
	
	/*public float g(Vec2 currSol,Vec2 q,Polygon poly)
	{

		
		Vec2 diff = currSol.sub(q);
		Vec2 diffOpposite = diff.mul(-1);
		
		int index = supportPointIndex(poly, diffOpposite);
		
		Vec2 difSupport = poly.getVertex(index).sub(q);
		
		float result = Vec2.dot(diff, diff) + Vec2.dot(difSupport, diffOpposite);
		
		
		return result;
	}*/
	

	/*public int supportPointIndex(Polygon polygon,Vec2 dir)
	{
		int index = 0;

		float maxDistance =Vec2.dot(polygon.getVertex(index), dir);  

		for(int i=1; i<polygon.getVerticesCount();i++)
		{
			float curValue = Vec2.dot(polygon.getVertex(i), dir);
			if(curValue > maxDistance)
			{
				index = i;
				maxDistance = curValue;
			}

		}

		return index;
	}*/

	/*public Vec2 supportPoint(Polygon poly1,Polygon poly2,Vec2 dir)
	{
		
		Vec2 sup = new Vec2();

		return sup;
	}*/
	
	public  void nearestPointSimplex2D(Simplex simplex, Vec2 q, SimplexSolution solution)
	{

		//if simplex composed by only a point
		if(simplex.currentDim == Simplex.IS_0_SIMPLEX)
			point(simplex.vs[0],solution);


		//if simplex is a segment
		if(simplex.currentDim == Simplex.IS_1_SIMPLEX)
			segment(simplex.vs,q,solution);

		//if simplex is a triangle
		if(simplex.currentDim == Simplex.IS_2_SIMPLEX)
			triangle(simplex.vs,q,solution);

	}


	private  void point(Vec2 vertex, SimplexSolution sol)
	{
		sol.verticesInSolution[0].set(vertex);
		sol.coefficients[0] = 1;

		sol.numVerticesUsed = 1;
	}


	private  void segment(Vec2[] simplex, Vec2 q, SimplexSolution sol)
	{
		//Vec2 tangent = simplex[1].sub(simplex[0]);
		Vec2 tangent = pool1;
		tangent.x = simplex[1].x - simplex[0].x;
		tangent.y = simplex[1].y - simplex[0].y;
		float normalizeFactor = 1/(tangent.length());
		tangent.mulLocal(normalizeFactor);

		//Vec2 qa = q.sub(simplex[0]);
		Vec2 qa = pool2;
		qa.x = q.x - simplex[0].x;
		qa.y = q.y - simplex[0].y;
		float v = Vec2.dot(qa, tangent) *normalizeFactor;

		if(v <=0)
		{
			point(simplex[0],sol);
			return;
		}

		//Vec2 qb = simplex[1].sub(q);
		Vec2 qb = pool3;
		qb.x = simplex[1].x - q.x;
		qb.y = simplex[1].y - q.y;
		float u = Vec2.dot(qb, tangent) *normalizeFactor;

		if(u <=0)
		{
			point(simplex[1],sol);
			return;
		}

		sol.verticesInSolution[0].set(simplex[0]);
		sol.verticesInSolution[1].set(simplex[1]);

		sol.coefficients[0] = u;
		sol.coefficients[1] = v;

		sol.numVerticesUsed = 2;

	}
	


	private void triangle(Vec2[] simplex, Vec2 q, SimplexSolution sol)
	{
		
		Vec2 simplex0 = simplex[0];
		Vec2 simplex1 = simplex[1];
		Vec2 simplex2 = simplex[2];
		
		//Vec2 tangent01 = simplex[1].sub(simplex[0]);
		Vec2 tangent01 = pool1;
		tangent01.x = simplex1.x -simplex0.x;
		tangent01.y = simplex1.y -simplex0.y;
		float normalizeFactor01 = 1.0f/(tangent01.length());
		tangent01.mulLocal(normalizeFactor01);

		//Vec2 vec0q = q.sub(simplex[0]);
		Vec2 vec0q = pool2;
		vec0q.x = q.x - simplex0.x;
		vec0q.y = q.y - simplex0.y;
		float v01 = Vec2.dot(vec0q, tangent01) *normalizeFactor01;

		//Vec2 vecQ1 = simplex[1].sub(q);
		Vec2 vecQ1 = pool3;
		vecQ1.x = simplex1.x - q.x;
		vecQ1.y = simplex1.y - q.y;
		float u01 = Vec2.dot(vecQ1, tangent01) *normalizeFactor01;


		//Vec2 tangent20 = simplex[0].sub(simplex[2]);
		Vec2 tangent20 = pool1;
		tangent20.x = simplex0.x - simplex2.x;
		tangent20.y = simplex0.y - simplex2.y;
		float normalizeFactor20 = 1.0f/(tangent20.length());
		tangent20.mulLocal(normalizeFactor20);

		//Vec2 vecQ0 = simplex[0].sub(q);
		Vec2 vecQ0 = pool2;
		vecQ0.x = simplex0.x - q.x;
		vecQ0.y = simplex0.y - q.y;
		float u02 = Vec2.dot(vecQ0, tangent20) *normalizeFactor20;

		
		//Vec2 vec2Q = q.sub(simplex[2]);
		Vec2 vec2Q = pool3;
		vec2Q.x = q.x -simplex2.x;
		vec2Q.y = q.y -simplex2.y;
		float v02 = Vec2.dot(vec2Q, tangent20) *normalizeFactor20;


		//Vec2 tangent12 = simplex[2].sub(simplex[1]);
		Vec2 tangent12 = pool1;
		tangent12.x = simplex2.x -simplex1.x;
		tangent12.y = simplex2.y - simplex1.y;
		float normalizeFactor12 = 1.0f/(tangent12.length());
		tangent12.mulLocal(normalizeFactor12);

		//Vec2 vec1q = q.sub(simplex[1]);
		Vec2 vec1q = pool2;
		vec1q.x = q.x -simplex1.x;
		vec1q.y = q.y - simplex1.y;
		float v12 = Vec2.dot(vec1q, tangent12) *normalizeFactor12;

		//Vec2 vecQ2 = simplex[2].sub(q);
		Vec2 vecQ2 =pool3;
		vecQ2.x = simplex2.x -q.x;
		vecQ2.y = simplex2.y -q.y;
		float u12 = Vec2.dot(vecQ2, tangent12) *normalizeFactor12;



		if(v01 <= 0 && u02 <= 0)
		{
			point(simplex[0],sol);
			return;
		}

		if(u01 <=0 && v12 <=0)
		{
			point(simplex[1],sol);
			return;
		}

		if(u12 <=0 && v02 <=0)
		{
			point(simplex[2],sol);
			return;
		}

		//calcolo aree
		
		tangent01 = pool1;
		tangent01.x = simplex1.x -simplex0.x;
		tangent01.y = simplex1.y -simplex0.y;
		
		Vec2 tangent02 = pool2;
		tangent02.x = simplex2.x - simplex0.x;
		tangent02.y = simplex2.y - simplex0.y;
		float totalArea = 0.5f*Vec2.cross(tangent01/*simplex[1].sub(simplex[0])*/, tangent02/*simplex[2].sub(simplex[0])*/);
		

		vecQ0 = pool1;
		vecQ0.x = simplex0.x - q.x;
		vecQ0.y = simplex0.y - q.y;
		
		vecQ1 = pool2;
		vecQ1.x = simplex1.x - q.x;
		vecQ1.y = simplex1.y - q.y;
		float areaQ01 = 0.5f*Vec2.cross(vecQ0/*simplex[0].sub(q)*/, vecQ1/*simplex[1].sub(q)*/);

		float wQ01 = areaQ01/totalArea;


		if(u01 >0 && v01 >0 && wQ01 <=0)
		{
			sol.verticesInSolution[0].set(simplex[0]);
			sol.verticesInSolution[1].set(simplex[1]);

			sol.coefficients[0] = u01;
			sol.coefficients[1] = v01;

			sol.numVerticesUsed = 2;

			return;
		}

		vecQ2 =pool1;
		vecQ2.x = simplex2.x -q.x;
		vecQ2.y = simplex2.y -q.y;
		
		vecQ0 = pool2;
		vecQ0.x = simplex0.x - q.x;
		vecQ0.y = simplex0.y - q.y;
		float areaQ02 = 0.5f*Vec2.cross(/*simplex[2].sub(q)*/ vecQ2, vecQ0/*simplex[0].sub(q)*/);

		float vQ02 = areaQ02/totalArea;


		if(u02 >0 && v02 >0 && vQ02 <=0)
		{
			sol.verticesInSolution[0].set(simplex[0]);
			sol.verticesInSolution[1].set(simplex[2]);

			sol.coefficients[0] = v02;
			sol.coefficients[1] = u02;

			sol.numVerticesUsed = 2;

			return;
		}

		
		vecQ1 = pool1;
		vecQ1.x = simplex1.x - q.x;
		vecQ1.y = simplex1.y - q.y;
		
		vecQ2 =pool2;
		vecQ2.x = simplex2.x -q.x;
		vecQ2.y = simplex2.y -q.y;
		float areaQ12 = 0.5f*Vec2.cross(vecQ1/*simplex[1].sub(q)*/, vecQ2/*simplex[2].sub(q)*/);

		float uQ12 = areaQ12/totalArea;


		if(u12 >0 && v12 >0 && uQ12 <=0)
		{
			sol.verticesInSolution[0].set(simplex[1]);
			sol.verticesInSolution[1].set(simplex[2]);

			sol.coefficients[0] = u12;
			sol.coefficients[1] = v12;

			sol.numVerticesUsed = 2;

			return;
		}		

		//point is inside triangle
		if(uQ12 >0 && vQ02 >0 && wQ01>0)
		{
			sol.verticesInSolution[0].set(simplex[0]);
			sol.verticesInSolution[1].set(simplex[1]);
			sol.verticesInSolution[2].set(simplex[2]);

			sol.coefficients[0] = uQ12;
			sol.coefficients[1] = vQ02;
			sol.coefficients[2] = wQ01;

			sol.numVerticesUsed = 3;	
		}

	}

}
