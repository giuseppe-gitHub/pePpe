package it.gius.pePpe.mains;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.algorithm.GjkEpaUtilsFunctions;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;

public class GjkUtilsTriangleTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();
		
		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.currentDim = Simplex.LARGER_2D_SIMPLEX;
		
		simplex.vs[0].set(-24, -16);
		simplex.vs[1].set(42, -36);
		simplex.vs[2].set(24, -16);
		
		//Vec2 q = new Vec2(0,0);
		Vec2 q = new Vec2(42,27);
		//Vec2 q = new Vec2(42,-27);
		
		SimplexSolution solution = new SimplexSolution();
		
		utils.nearestPointSimplex2D(simplex, q, solution);
		
		System.out.println(solution.numVerticesUsed);
		for(int i=0; i <solution.numVerticesUsed;i++)
		{
			System.out.println("coefficient: " + solution.coefficients[i]);
			System.out.println("vertex: " + solution.verticesInSolution[i]);
		}

	}

}
