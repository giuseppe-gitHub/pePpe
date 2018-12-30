package it.gius.pePpe.algorithm.gjk.shapes;


import org.jbox2d.common.Vec2;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;


/**
 * 
 * @author giuseppe
 * @has 0..* shapes 2 it.gius.pePpe.algorithm.gjk.IGjkContainedShape
 * @composed 1 - 3 it.gius.pePpe.algorithm.gjk.Simplex
 * @composed 1 - 2 it.gius.pePpe.shapes.witness.Witness
 */
public class GjkMinkDiffShape /*implements IGjkAlgorithmShape*/{

	public GjkSingleShape shapeA,shapeB;
	//private Vec2 startPointA,startPointB;

	//witnesses for the next call
	//public /*int*/ VertexIndexWitness witnessA, witnessB;
	public TwoPointWitness tpWitness;

	private Simplex simplexA,simplexB,simplexZ;
	private Vec2 cSupportA,cSupportB,cSupportZ; 

	//private Simplex simplexATemp,simplexBTemp;

	private Vec2 startingPointA, startingPointB;

	public GjkMinkDiffShape(GjkSingleShape A,GjkSingleShape B) {
		this.shapeA = A;
		this.shapeB = B;

		//suggestIndexA = suggestIndexB = null/*0*/;

		//witnessA = new VertexIndexWitness();//shapeA.getShape().getWitnessInstance().clone();
		//witnessB = new VertexIndexWitness();//.getShape().getWitnessInstance().clone();
		tpWitness = new TwoPointWitness();


		simplexA = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplexB = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplexZ = new Simplex(Simplex.LARGER_2D_SIMPLEX);

		cSupportA = new Vec2();
		cSupportB = new Vec2();
		cSupportZ = new Vec2();

		//simplexATemp = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		//simplexBTemp = new Simplex(Simplex.LARGER_2D_SIMPLEX);

		startingPointA = new Vec2();
		startingPointB = new Vec2();

	}
	
	/*@Override
	public boolean containsComplexCurve() {
		
		return shapeA.containsComplexCurve() || shapeB.containsComplexCurve();
	}*/


	//@Override
	public float getMaxInnerDistance() {
		
		return shapeA.getMaxInnerDistance() + shapeB.getMaxInnerDistance();
	}

	/*
	 * This variant create problem for the epa algorithm. Epa needs to have edge of the minkosky sum, but
	 * this way the starting point could be an internal one
	 * */
	/*@Override
	public Simplex getStartingSimplex() {

		shapeA.getStartingPoint(witnessA, startingPointA);
		simplexA.vs[0].set(startingPointA);
		simplexA.witnesses[0] = witnessA.clone();
		simplexA.currentDim = Simplex.IS_0_SIMPLEX;

		//Vec2 startPointB = shapeB.getStartingPoint(suggestIndexB);
		shapeB.getStartingPoint(witnessB, startingPointB);
		simplexB.vs[0].set(startingPointB);
		simplexB.witnesses[0] = witnessB.clone();
		simplexB.currentDim = Simplex.IS_0_SIMPLEX;

		//simplexZ.vs[0].set(startingPointB.sub(startingPointA));
		simplexZ.vs[0].x = startingPointB.x - startingPointA.x;
		simplexZ.vs[0].y = startingPointB.y - startingPointA.y;
		simplexZ.currentDim = Simplex.IS_0_SIMPLEX;
		return simplexZ;
	}*/
	
	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();
	private Vec2 pool4 = new Vec2();
	
	
	/*
	 * (non-Javadoc)
	 * @see it.gius.pePpe.algorithm.gjk.IGjkAlgorithmShape#getStartingSimplex()
	 * 
	 * Obtaining the starting simplex this way (using the supportPoint feature) assure to start always with an edge of the minkowsky sum
	 * 
	 */
	//@Override
	public Simplex getStartingSimplex() {
		
		Vec2 tempPointA = pool1;
		Vec2 tempPointB = pool2;
		
		shapeA.getStartingPoint(tpWitness.firstWitness, tempPointA);
		shapeB.getStartingPoint(tpWitness.secondWitness, tempPointB);
		
		Vec2 d = pool3;
		d.x = tempPointB.x - tempPointA.x;
		d.y = tempPointB.y - tempPointA.y;
		
		d.negateLocal();
		
		
		shapeB.supportPoint(d,startingPointB, tpWitness.secondWitness);
		pool4.x =-d.x;
		pool4.y=-d.y;
		shapeA.supportPoint(pool4/*SystemCostants.ORIGIN2D.sub(d)*/,startingPointA, tpWitness.firstWitness);

		simplexA.vs[0].set(startingPointA);
		//simplexA.witnesses[0].set(witnessA);
		simplexA.currentDim = Simplex.IS_0_SIMPLEX;

		//Vec2 startPointB = shapeB.getStartingPoint(suggestIndexB);
		simplexB.vs[0].set(startingPointB);
		//simplexB.witnesses[0].set(witnessB);
		simplexB.currentDim = Simplex.IS_0_SIMPLEX;

		//simplexZ.vs[0].set(startingPointB.sub(startingPointA));
		simplexZ.vs[0].x = startingPointB.x - startingPointA.x;
		simplexZ.vs[0].y = startingPointB.y - startingPointA.y;
		
		if(simplexZ.witnesses[0] == null)
			simplexZ.witnesses[0] = new TwoPointWitness();
		
		simplexZ.witnesses[0].set(tpWitness);
		
		simplexZ.currentDim = Simplex.IS_0_SIMPLEX;


		
		return simplexZ;
	}
	

	//@Override
	public Simplex getCurrentSimplex() {
		return simplexZ;
	}
	

	private float distance1(Vec2 a, Vec2 b)
	{
		return (Math.abs(a.x -b.x) + Math.abs(a.y-b.y));
	}
	
	//@Override
	public Simplex updateSimplex(SimplexSolution simpSol, Vec2 newSupportPoint,TwoPointWitness witnessIn) {
		boolean supportAlreadyIn = false;

		//Witness witnessSupB = shapeB.supportPoint(d,cSupportB);
		//Witness witnessSupA = shapeA.supportPoint(SystemCostants.ORIGIN2D.sub(d),cSupportA);
		if(!cSupportZ.equals(newSupportPoint))
		{
			//System.out.println("!cSupportZ.equals(newSupportPoint)");
			cSupportZ.set(newSupportPoint);
			shapeB.getStartingPoint(witnessIn.secondWitness, cSupportB);
			shapeA.getStartingPoint(witnessIn.firstWitness, cSupportA);
		}
		//cSupportZ.set(cSupportB);
		//cSupportZ.subLocal(cSupportA);


		int i,j;
		
		float epsilon = 0;
		
		/*if(containsComplexCurve())
			epsilon = SystemCostants.SQRT_EPSILON*10;
		else*/
			epsilon = SystemCostants.SQRT_EPSILON;

		for(i = 0; i< simpSol.numVerticesUsed; i++)
		{
			for(j=0; !simplexZ.vs[j].equals(simpSol.verticesInSolution[i]);j++);

			//simplexA.vs[i].set(simplexA.vs[j]);
			simplexA.vs[i].x = simplexA.vs[j].x;
			simplexA.vs[i].y = simplexA.vs[j].y;
			//simplexA.witnesses[i] = simplexA.witnesses[j];
			//simplexB.vs[i].set(simplexB.vs[j]);
			simplexB.vs[i].x = simplexB.vs[j].x;
			simplexB.vs[i].y = simplexB.vs[j].y;
			//simplexB.witnesses[i] = simplexB.witnesses[j];

			//simplexZ.vs[i].set(simplexB.vs[i].sub(simplexA.vs[i]));
			//simplexZ.vs[i].set(simplexZ.vs[j]); //??
			simplexZ.vs[i].x = simplexZ.vs[j].x;
			simplexZ.vs[i].y = simplexZ.vs[j].y;
			
			simplexZ.witnesses[i] = simplexZ.witnesses[j];

			if(simplexZ.vs[i].equals(cSupportZ))
				supportAlreadyIn = true;
			
			if(distance1(simplexZ.vs[i],cSupportZ) < epsilon)
				supportAlreadyIn = true;
			
		}


		simplexA.currentDim = simpSol.numVerticesUsed;
		simplexB.currentDim = simpSol.numVerticesUsed;
		simplexZ.currentDim = simpSol.numVerticesUsed;



		//witnessA = simplexA.witnesses[simpSol.numVerticesUsed-1];
		//witnessB = simplexB.witnesses[simpSol.numVerticesUsed-1];
	
		if(supportAlreadyIn)
			return null;

		if(simplexZ.currentDim < Simplex.LARGER_2D_SIMPLEX)
		{
			int index = simpSol.numVerticesUsed;
			//simplexA.vs[simpSol.numVerticesUsed].set(cSupportA);
			simplexA.vs[index].x = cSupportA.x;
			simplexA.vs[index].y = cSupportA.y;
			//simplexA.witnesses[simpSol.numVerticesUsed] = twoPwitness.firstWitness.clone();//witnessSupA.clone();
			simplexA.currentDim++;

			//simplexB.vs[simpSol.numVerticesUsed].set(cSupportB);
			simplexB.vs[index].x = cSupportB.x;
			simplexB.vs[index].y = cSupportB.y;
			//simplexB.witnesses[simpSol.numVerticesUsed] = twoPwitness.secondWitness.clone();//witnessSupB.clone();
			simplexB.currentDim++;

			//simplexZ.vs[simpSol.numVerticesUsed].set(cSupportZ);
			simplexZ.vs[index].x = cSupportZ.x;
			simplexZ.vs[index].y = cSupportZ.y;
			
			
			if(simplexZ.witnesses[simpSol.numVerticesUsed] == null)
				simplexZ.witnesses[simpSol.numVerticesUsed] = new TwoPointWitness();
			
			simplexZ.witnesses[simpSol.numVerticesUsed].set(tpWitness);
			
			simplexZ.currentDim++;
		}

		return simplexZ;
	}


	
	//@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, TwoPointWitness witnessOut) {

		shapeB.supportPoint(d,cSupportB, witnessOut.secondWitness);
		pool1.x =-d.x;
		pool1.y=-d.y;
		shapeA.supportPoint(pool1/*SystemCostants.ORIGIN2D.sub(d)*/,cSupportA, witnessOut.firstWitness);

		//supportPoint.set(cSupportB);
		//supportPoint.subLocal(cSupportA);
		supportPoint.x = cSupportB.x -cSupportA.x;
		supportPoint.y = cSupportB.y -cSupportA.y;

		//cSupportZ.set(supportPoint);
		cSupportZ.x = supportPoint.x;
		cSupportZ.y = supportPoint.y;

		//return twoPwitness;
	}
	
	//@Override
	public void storeWitness(SimplexSolution simpSol) {
		tpWitness = getUsedWitness( simplexZ, simpSol);
		//witnessB = getUsedWitness(simplexB, simplexZ, simpSol);
	}
	
	
	private TwoPointWitness getUsedWitness(Simplex simplexZ, SimplexSolution simpSol)
	{
		
		int j=0;
		int lastUsed = simpSol.numVerticesUsed-1;
			for( j=0; !simplexZ.vs[j].equals(simpSol.verticesInSolution[lastUsed]);j++);
			
		return simplexZ.witnesses[j];//simplexAB.witnesses[j];
	}

	//@Override
	public void fillSolution(Vec2 pointSol, Vec2 q, SimplexSolution simpSol, DistanceSolution sol) {

		getPointSolution(simplexA, simplexZ, simpSol, sol.p1);
		getPointSolution(simplexB, simplexZ, simpSol, sol.p2);


		//debug data
		sol.otherData = simplexZ;


		if(simpSol.numVerticesUsed == Simplex.LARGER_2D_SIMPLEX)
			sol.distance = 0;

	}



	private void getPointSolution(Simplex simplexAB, Simplex simplexZ, SimplexSolution npS, Vec2 pABSol)
	{
		pABSol.setZero();
		int i,j=0;
		Vec2 vs = null;
		float coeff = 0;
		for(i=0; i<npS.numVerticesUsed;i++)
		{

			for( j=0; !simplexZ.vs[j].equals(npS.verticesInSolution[i]);j++);

			//pABSol.addLocal(simplexAB.vs[j].mul(npS.coefficients[i]));
			vs = simplexAB.vs[j];
			coeff = npS.coefficients[i];
			pABSol.x += vs.x * coeff;
			pABSol.y += vs.y * coeff;

		}
		//System.out.println("j: " +j +", numVerticesUsed: " + npS.numVerticesUsed);

	}


	//@Override
	public int getDim() {
		int dimA = shapeA.getDim();
		int dimB = shapeB.getDim();
		
		if(dimA <0 && dimB > 0)
			return dimB + dimB;
		
		if(dimB <0 && dimA >0)
			return dimA + dimA;
		
		if(dimA <0 && dimB <0)
			return -1;
		
		return dimA * dimB;
	}

}
