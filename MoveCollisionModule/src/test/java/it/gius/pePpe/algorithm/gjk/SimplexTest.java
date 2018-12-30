package it.gius.pePpe.algorithm.gjk;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;
import junit.framework.TestCase;

public class SimplexTest extends TestCase {



	public void testComputeWindingClockWise1() {

		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.vs[0].set(0,0);
		simplex.vs[1].set(1,1);
		simplex.vs[2].set(2,0);
		simplex.currentDim = Simplex.IS_2_SIMPLEX;
		
		simplex.computeWinding();
		
		
		
		if(simplex.winding != SystemCostants.CLOCKWISE_WINDING)
			fail();
	}
	
	public void testComputeWindingCounterClockWise2() {

		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.vs[0].set(-14.8f,-14);
		simplex.vs[1].set(45.1f,26);
		simplex.vs[2].set(15.1f,16);
		simplex.currentDim = Simplex.IS_2_SIMPLEX;
		
		simplex.computeWinding();
		
		
		
		if(simplex.winding != SystemCostants.COUNTER_CLOCKWISE_WINDING)
			fail();
	}


	public void testComputeWindingCounterClockWise() {
		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.vs[0].set(0,0);
		simplex.vs[1].set(1,1);
		simplex.vs[2].set(0,2);
		simplex.currentDim = Simplex.IS_2_SIMPLEX;
		
		simplex.computeWinding();
		
		
		
		if(simplex.winding != SystemCostants.COUNTER_CLOCKWISE_WINDING)
			fail();
	}
	
	public void testComputeWindingClockWiseWithSameDirection() {

		Simplex simplex = new Simplex(Simplex.UNBOUNDED_SIMPLEX);
		simplex.vs[0].set(0,0);
		simplex.vs[1].set(1,1);
		simplex.vs[2].set(2,2);
		simplex.vs[3].set(3,0);
		simplex.currentDim = Simplex.IS_2_SIMPLEX+1;
		
		simplex.computeWinding();
		
		
		
		if(simplex.winding != SystemCostants.CLOCKWISE_WINDING)
			fail();
	}
	
	
	public void testInsert()
	{
		Simplex simplex = new Simplex(Simplex.UNBOUNDED_SIMPLEX);
		simplex.vs[0].set(0,0);
		simplex.vs[1].set(1,1);
		simplex.vs[2].set(2,2);
		simplex.vs[3].set(3,0);
		simplex.currentDim = Simplex.IS_2_SIMPLEX+1;
		
		simplex.insert(new Vec2(33,33),null,2);
		
		
		Simplex simplexSolution = new Simplex(Simplex.UNBOUNDED_SIMPLEX);
		simplexSolution.vs[0].set(0,0);
		simplexSolution.vs[1].set(1,1);
		simplexSolution.vs[2].set(33,33);
		simplexSolution.vs[3].set(2,2);
		simplexSolution.vs[4].set(3,0);
		simplexSolution.currentDim = Simplex.IS_2_SIMPLEX+2;
		
		if(!simplex.equals(simplexSolution))
			fail();
		
	}
	

	
	public void testSet()
	{
		Simplex simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.vs[0].set(0,0);
		simplex.vs[1].set(1,1);
		simplex.vs[2].set(2,2);
		simplex.witnesses[0] = new TwoPointWitness();
		simplex.witnesses[1] = new TwoPointWitness();
		simplex.witnesses[2] = new TwoPointWitness();
		simplex.currentDim = Simplex.IS_2_SIMPLEX;
		
		
		Simplex simplexB = new Simplex(Simplex.UNBOUNDED_SIMPLEX);
		simplexB.vs[0].set(0,0);
		simplexB.vs[1].set(1,1);
		simplexB.vs[2].set(33,33);
		simplexB.vs[3].set(2,2);
		simplexB.vs[4].set(3,0);
		simplexB.currentDim = Simplex.IS_2_SIMPLEX+2;
		
		simplexB.set(simplex);
		if(!simplex.equals(simplexB))
			fail();
	
	}
	

}
