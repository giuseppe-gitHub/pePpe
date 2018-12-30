package it.gius.pePpe.mains;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.algorithm.GjkEpaUtilsFunctions;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;

public class SimplexUtilitiesTestMain {

	
	public static void main(String[] args)
	{
		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.currentDim = Simplex.IS_1_SIMPLEX;
		simplex.vs[0] = new Vec2(1,1);
		simplex.vs[1] = new Vec2(5,5);
		
		Vec2 q = new Vec2(2,1);
		
		SimplexSolution solution = new SimplexSolution();
		
		GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();
		
		utils.nearestPointSimplex2D(simplex, q, solution);
		
		Vec2 pSol = new Vec2();
		solution.getSolutionPoint(pSol);
		System.out.println("usati " + solution.numVerticesUsed+" vertici");
		System.out.println("soluzione: " + pSol);
		System.out.println("coefficienti: " +solution.coefficients[0] + " " + solution.coefficients[1]);
		
		
		
		Vec2[] triangle = new Vec2[3];
		triangle[0] = new Vec2(0,0);
		triangle[1] = new Vec2(2,0);
		triangle[2] = new Vec2(0,2);
		
		
		q = new Vec2(1,-1);
		
		float totalArea = 0.5f*Vec2.cross(triangle[1].sub(triangle[0]), triangle[2].sub(triangle[0]));

		float areaQ01 = 0.5f*Vec2.cross(triangle[0].sub(q), triangle[1].sub(q));
		
		float wQ01 = areaQ01/totalArea;
		
		System.out.println("wQ01: " + wQ01);
		
		q = new Vec2(-1,1);
		
		float areaQ02 = 0.5f*Vec2.cross(triangle[2].sub(q), triangle[0].sub(q));

		float wQ02 = areaQ02/totalArea;

		System.out.println("wQ02: " + wQ02);
		
		
		q = new Vec2(2,2);
		
		float areaQ12 = 0.5f*Vec2.cross(triangle[1].sub(q), triangle[2].sub(q));

		float wQ12 = areaQ12/totalArea;

		System.out.println("wQ12: " + wQ12);
	}
}
