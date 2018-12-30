package it.gius.pePpe.mains;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.algorithm.GjkEpaUtilsFunctions;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;

public class SimplexTriangleUtilitiesTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		testNewQ(new Vec2(1,1));
		System.out.println();
		testNewQ(new Vec2(1.5f,1.5f));
		System.out.println();
		testNewQ(new Vec2(-2,-2));
		System.out.println();
		testNewQ(new Vec2(-0.5f,5));
		System.out.println();
		testNewQ(new Vec2(-1,6));


	}


	public static void testNewQ(Vec2 q)
	{
		GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();

		System.out.println("Test Point: " + q);
		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);

		simplex.vs[0].set(0,0);
		simplex.vs[1].set(3,0);
		simplex.vs[2].set(0,3);

		simplex.currentDim = Simplex.LARGER_2D_SIMPLEX;


		SimplexSolution solution = new SimplexSolution();

		utils.nearestPointSimplex2D(simplex, q, solution);

		Vec2 p = new Vec2();

		System.out.println("usati " + solution.numVerticesUsed + " vertici per la soluzione");
		for(int i=0; i < solution.numVerticesUsed; i++)
		{
			System.out.println("vertice " +i+": " + solution.verticesInSolution[i]);
			System.out.println("coefficiente: " + solution.coefficients[i]);

			p.addLocal(solution.verticesInSolution[i].mul(solution.coefficients[i]));
		}

		System.out.println("Vertice soluzione: " +p);


	}

}
