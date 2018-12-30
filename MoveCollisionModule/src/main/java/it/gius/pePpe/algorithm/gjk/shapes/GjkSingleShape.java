package it.gius.pePpe.algorithm.gjk.shapes;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.pePpe.algorithm.gjk.SimplexSolution;
import it.gius.pePpe.data.distance.DistanceSolution;
//import it.gius.pePpe.data.shapes.Ellipse;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

public class GjkSingleShape /*implements IGjkAlgorithmShape,IGjkContainedShape*/{

	protected Simplex simplex;
	protected Vec2 currentSupportPoint;

	protected Shape shape;
	//protected Bind bind;
	private Transform transform;
	
	protected Vec2 pool1 = new Vec2();
	
	public GjkSingleShape(Shape shape) {
		currentSupportPoint = new Vec2();
		simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		
		this.shape = shape;
		transform = null;
	}
	
	
	
	public GjkSingleShape(Shape shape, Transform transform) {
		currentSupportPoint = new Vec2();
		simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		
		this.shape = shape;
		this.transform = transform;
	}



	public GjkSingleShape(Bind bind)
	{
		this(bind.phShape.shape);
		//this.bind = bind;
		this.transform = bind.body.transform;
	}
	
	public void setBind(Bind bind) {
		//this.bind = bind;
		this.transform = bind.body.transform;
		this.shape = bind.phShape.shape;
	}
	
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}


	public void setTransform(Transform transform) {
		this.transform = transform;
	}



	private Vec2 dr = new Vec2();
	
	//@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, VertexIndexWitness wOut) {
		dr.set(d);
		
		/*if(transform == null)
			System.out.println("transform == null !!!!!!");*/
		
		if(/*bind*/transform != null)
			Mat22.mulTransToOut(/*bind.body.*/transform.R, d, dr);
		
		shape.supportPoint(dr, supportPoint, wOut);
		
		if(/*bind*/transform != null)
			Transform.mulToOut(/*bind.body.*/transform, supportPoint,supportPoint);
		
	}
	
	//@Override
	public Shape getShape() {
		return shape;
	}
	
	//@Override
	public int getDim() {
		return shape.getDim();
	}
	
	/*@Override
	public boolean containsComplexCurve() {
		
		return (shape instanceof Ellipse);
	}*/
	
	public void getStartingPoint(VertexIndexWitness witness, Vec2 result) {
		
		shape.fromWitnessToVec(witness, result);

		if(transform/*bind*/ != null)
			Transform.mulToOut(/*bind.body.*/transform, result, result);
	}
	
	//@Override
	public float getMaxInnerDistance() {
		return shape.maxInnerDistance;
	}
	
	
	private VertexIndexWitness witness = new VertexIndexWitness();
	//@Override
	public Simplex getStartingSimplex() {
		Vec2 startingPoint = pool1;
		getStartingPoint(witness, startingPoint);
		simplex.vs[0].set(startingPoint);
		simplex.currentDim = Simplex.IS_0_SIMPLEX;
		return simplex;
	}

	//@Override
	public Simplex getCurrentSimplex() {
		return simplex;
	}

	//@Override
	public Simplex updateSimplex(SimplexSolution simpSol, Vec2 newSupportPoint) {

		boolean supportAlreadyIn = false;

		currentSupportPoint.set(newSupportPoint);

		for(int i =0; i< simpSol.numVerticesUsed;i++)
		{
			simplex.vs[i].set(simpSol.verticesInSolution[i]);
			if(simplex.vs[i].equals(currentSupportPoint))
				supportAlreadyIn = true;
		}

		simplex.currentDim = simpSol.numVerticesUsed;

		if(supportAlreadyIn)
			return null;

		if(simplex.currentDim < Simplex.LARGER_2D_SIMPLEX)
		{
			simplex.vs[simpSol.numVerticesUsed].set(currentSupportPoint);
			simplex.currentDim++;
		}

		return simplex;
	}
	
	//@Override
	/*public void storeWitness(SimplexSolution simpSol) {
		//Not doing store here cause is meaningless
		 
	}*/

	//@Override
	public void fillSolution(Vec2 pointSol, Vec2 q, SimplexSolution npSol,
			DistanceSolution sol) {

		//point inside the poly
		if(npSol.numVerticesUsed == Simplex.LARGER_2D_SIMPLEX)
		{
			sol.p1.set(q);
			sol.p2.set(q);
			sol.distance = 0;
		}
		else
		{
			sol.p1.set(pointSol);
			sol.p2.set(q);
		}
		
		sol.otherData = simplex;


		/*int index = 0;
				for(;index <poly.getVerticesCount();index++)
				{
					for(int j=0; j<npSol.numVerticesUsed && !npSol.equals(poly.getVertex(index)); j++);
				}

				if(index< poly.getVerticesCount())
					startPointIndex = index;
		 */
	}

}