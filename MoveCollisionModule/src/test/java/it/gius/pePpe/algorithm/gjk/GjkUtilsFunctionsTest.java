package it.gius.pePpe.algorithm.gjk;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.GjkEpaUtilsFunctions;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;
import it.gius.pePpe.algorithm.gjk.Simplex.SimplexEdge;

import org.jbox2d.common.Vec2;

import junit.framework.TestCase;

public class GjkUtilsFunctionsTest extends TestCase {

	
	private Simplex simplex; 

	public GjkUtilsFunctionsTest() {
		simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);

		simplex.vs[0].set(-24, -16);
		simplex.vs[1].set(42, -36);
		simplex.vs[2].set(24, -16);
		
		simplex.currentDim = Simplex.LARGER_2D_SIMPLEX;

	}
	
	public void testNearestEdgeInsidePointSimplex2D() {
		Simplex curSimplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		
		curSimplex.vs[0].set(-10, 1);
		curSimplex.vs[1].set(10, 1);
		curSimplex.vs[2].set(0, -10);
		
		curSimplex.currentDim = Simplex.LARGER_2D_SIMPLEX;
		
		curSimplex.computeWinding();
		
		GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();
		
		Vec2 origin = SystemCostants.ORIGIN2D;
		
		SimplexEdge solution = new SimplexEdge();
		
		utils.nearestEdgeInsidePointSimplex2D(curSimplex, origin, solution);
		
		System.out.println("solution.distance: " + solution.distance);
		System.out.println("solution.normal: " +solution.normal);
		
		if(solution.distance != 1)
			fail();
		
		/*if(!solution.normal.equals(new Vec2(0,1)))
			fail();*/
		assertVec2(solution.normal, new Vec2(0,1));
	}
	
	private void assertVec2(Vec2 expected, Vec2 actual)
	{
		if(expected.x != actual.x)
			fail();
		
		if(expected.y != actual.y)
			fail();
	}
	
	public void testInsideSolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(20,-18));

		assertTrue(solution.numVerticesUsed == 3);

		float sum = 0;
		for(int i=0; i<3;i++)
		{
			assertTrue(solution.coefficients[i] >0 && solution.coefficients[i]<1);
			sum+= solution.coefficients[i];
		}
			
		
		assertTrue(sum == 1);

	}
	
	public void testZOrigin()
	{
		simplex.vs[0].set(32.64f, 18.22f);
		simplex.vs[1].set(50.64f, -36.78f);
		simplex.vs[2].set(40.64f, -16.78f);
		
		simplex.currentDim = Simplex.LARGER_2D_SIMPLEX;
		
		SimplexSolution solution = testNewQ(new Vec2(0,0));
		
		System.out.println("num vertices used: " + solution.numVerticesUsed);
	}
	
	public void testOnEdgeACSolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(0,0));

		assertTrue(solution.numVerticesUsed == 2);

		float sum = 0;
		for(int i=0; i<2;i++)
		{
			assertTrue(solution.coefficients[i] >0 && solution.coefficients[i]<1);
			sum+= solution.coefficients[i];
		}
		
		assertTrue(sum >= 0.9999 & sum <=1.00001);

		assertEquals(simplex.vs[0], solution.verticesInSolution[0]);
		assertEquals(simplex.vs[2], solution.verticesInSolution[1]);
	}


	public void testOnEdgeBCSolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(42,-27));

		assertTrue(solution.numVerticesUsed == 2);

		float sum = 0;
		for(int i=0; i<2;i++)
		{
			assertTrue(solution.coefficients[i] >0 && solution.coefficients[i]<1);
			sum+= solution.coefficients[i];
		}
		
		assertTrue(sum >= 0.9999 & sum <=1.00001);

		assertEquals(simplex.vs[1], solution.verticesInSolution[0]);
		assertEquals(simplex.vs[2], solution.verticesInSolution[1]);
	}

	public void testOnEdgeABSolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(-24,-18));

		assertTrue(solution.numVerticesUsed == 2);

		float sum = 0;
		for(int i=0; i<2;i++)
		{
			assertTrue(solution.coefficients[i] >0 && solution.coefficients[i]<1);
			sum+= solution.coefficients[i];
		}
		
		assertTrue(sum >= 0.9999 & sum <=1.00001);

		assertEquals(simplex.vs[0], solution.verticesInSolution[0]);
		assertEquals(simplex.vs[1], solution.verticesInSolution[1]);
	}

	
	public void testOnlyASolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(-30,0));

		assertTrue(solution.numVerticesUsed + solution.coefficients[0] == 2);

		assertEquals(simplex.vs[0], solution.verticesInSolution[0]);
	}
	
	
	public void testOnlyBSolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(42,-40));


		assertTrue(solution.numVerticesUsed + solution.coefficients[0] == 2);

		assertEquals(simplex.vs[1], solution.verticesInSolution[0]);
	}
	
	
	public void testOnlyCSolution()
	{
		SimplexSolution solution = testNewQ(new Vec2(42,27));


		assertTrue(solution.numVerticesUsed + solution.coefficients[0] == 2);

		assertEquals(simplex.vs[2], solution.verticesInSolution[0]);
	}



	private SimplexSolution testNewQ(Vec2 q)
	{
		GjkEpaUtilsFunctions utils = new GjkEpaUtilsFunctions();

		System.out.println("Test Point: " + q);


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

		System.out.println();
		return solution;

	}



}
