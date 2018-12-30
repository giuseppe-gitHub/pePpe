package it.gius.pePpe.distance.pool;

import org.jbox2d.common.Transform;

import it.gius.pePpe.algorithm.gjk.shapes.GjkMinkDiffShape;
import it.gius.pePpe.algorithm.gjk.shapes.GjkSingleShape;
import it.gius.pePpe.data.cache.DistanceWitnessCache;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

public class GjkPool {


	private GjkSingleShape singleShape = null;

	private GjkMinkDiffShape minkDiffShape = null;
	private GjkSingleShape containedShapeA = null;
	private GjkSingleShape containedShapeB = null;

	public GjkPool() {
	}

	public void setSingleShape(Bind bindA) {
		if(singleShape == null)
		{
			singleShape = new GjkSingleShape(bindA);
			return;
		}

		singleShape.setBind(bindA);
	}

	public void setSingleShape(Shape shape,Transform transform)
	{
		if(singleShape == null)
		{
			singleShape = new GjkSingleShape(shape,transform);
			return;
		}

		singleShape.setShape(shape);
		singleShape.setTransform(transform);
	}



	public void setDoubleShape(Bind bindA, Bind bindB)
	{
		if(minkDiffShape == null)
		{
			containedShapeA = new GjkSingleShape(bindA);
			containedShapeB = new GjkSingleShape(bindB);

			minkDiffShape = new GjkMinkDiffShape(containedShapeA, containedShapeB);
			return;
		}

		containedShapeA.setBind(bindA);
		containedShapeB.setBind(bindB);

		//minkDiffShape.witnessA = bindA.phShape.shape.getWitnessInstance().clone();
		//minkDiffShape.witnessB = bindB.phShape.shape.getWitnessInstance().clone();
		if(minkDiffShape.tpWitness == null)
			minkDiffShape.tpWitness = new TwoPointWitness();
		else
			minkDiffShape.tpWitness.reset();

	}

	public void setDoubleShape(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB)
	{
		if(minkDiffShape == null)
		{
			containedShapeA = new GjkSingleShape(shapeA,transformA);
			containedShapeB = new GjkSingleShape(shapeB,transformB);

			minkDiffShape = new GjkMinkDiffShape(containedShapeA, containedShapeB);
		}
		else
		{
			containedShapeA.setShape(shapeA);
			containedShapeA.setTransform(transformA);
			containedShapeB.setShape(shapeB);
			containedShapeB.setTransform(transformB);
		}
		
		//minkDiffShape.witnessA = shapeA.getWitnessInstance().clone();
		//minkDiffShape.witnessB = shapeB.getWitnessInstance().clone();
		
		if(minkDiffShape.tpWitness == null)
			minkDiffShape.tpWitness = new TwoPointWitness();
		else
			minkDiffShape.tpWitness.reset();
	}


	public void setDoubleShape(Bind bindA, Bind bindB, DistanceWitnessCache cache)
	{
		if(minkDiffShape == null)
		{
			containedShapeA = new GjkSingleShape(bindA);
			containedShapeB = new GjkSingleShape(bindB);

			minkDiffShape = new GjkMinkDiffShape(containedShapeA, containedShapeB);

		}
		else
		{
			containedShapeA.setBind(bindA);
			containedShapeB.setBind(bindB);
		}

		//minkDiffShape.witnessA = cache.witnessA.clone();
		//minkDiffShape.witnessB = cache.witnessB.clone();
		
		if(minkDiffShape.tpWitness == null)
			minkDiffShape.tpWitness = new TwoPointWitness();
		
		if(cache.witness != null)
			minkDiffShape.tpWitness.set(cache.witness);

	}

	public void setDoubleShape(Shape shapeA, Transform transformA, Shape shapeB, Transform transformB, DistanceWitnessCache cache)
	{
		if(minkDiffShape == null)
		{
			containedShapeA = new GjkSingleShape(shapeA,transformA);
			containedShapeB = new GjkSingleShape(shapeB,transformB);

			minkDiffShape = new GjkMinkDiffShape(containedShapeA, containedShapeB);
		}
		else
		{
			containedShapeA.setShape(shapeA);
			containedShapeA.setTransform(transformA);
			containedShapeB.setShape(shapeB);
			containedShapeB.setTransform(transformB);
		}

		//minkDiffShape.witnessA = cache.witnessA.clone();
		//minkDiffShape.witnessB = cache.witnessB.clone();
		if(minkDiffShape.tpWitness == null)
			minkDiffShape.tpWitness = new TwoPointWitness();
		
		if(cache.witness != null)
			minkDiffShape.tpWitness.set(cache.witness);
	}



	public GjkSingleShape getSingleShape() {
		return singleShape;
	}

	public GjkMinkDiffShape getDoubleShape()
	{
		return minkDiffShape;
	}


	public VertexIndexWitness getWitnessA()
	{
		return minkDiffShape.tpWitness.firstWitness;
	}

	public VertexIndexWitness getWitnessB()
	{
		return minkDiffShape.tpWitness.secondWitness;
	}
	
	public TwoPointWitness getWitness()
	{
		return minkDiffShape.tpWitness;
	}

}
