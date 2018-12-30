package it.gius.pePpe.algorithm.gjk;

import java.util.Arrays;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.data.shapes.witness.TwoPointWitness;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 * @opt all
 */
public class Simplex {

	/**
	 * @hidden
	 */
	public static final int LARGER_2D_SIMPLEX = 3;
	/**
	 * @hidden
	 */
	public static final int LARGER_3D_SIMPLEX = 4;
	/**
	 * @hidden
	 */
	public static final int UNBOUNDED_SIMPLEX = -1;
	/**
	 * @hidden
	 */
	public static final int IS_0_SIMPLEX = 1;
	/**
	 * @hidden
	 */
	public static final int IS_1_SIMPLEX = 2;
	/**
	 * @hidden
	 */
	public static final int IS_2_SIMPLEX = LARGER_2D_SIMPLEX;
	/**
	 * @hidden
	 */
	public static final int VEC_STANDARD_UNBOUNDED_DIMENSION = 15;

	public Vec2[] vs;
	public TwoPointWitness witnesses[];

	public int winding = -1;

	public int type = 0;

	public int currentDim = 0;

	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();


	public boolean correctType(int type)
	{
		return type == LARGER_2D_SIMPLEX || type == LARGER_3D_SIMPLEX || type == UNBOUNDED_SIMPLEX;
	}


	public Simplex(int type) {

		if(!correctType(type))
			throw new IllegalArgumentException();

		this.type = type;

		if(type == UNBOUNDED_SIMPLEX)
		{
			vs = new Vec2[VEC_STANDARD_UNBOUNDED_DIMENSION];
			witnesses = new TwoPointWitness[VEC_STANDARD_UNBOUNDED_DIMENSION];
		}
		else
		{
			vs = new Vec2[type];
			witnesses = new TwoPointWitness[type];
		}

		for(int i=0; i< vs.length;i++)
		{
			vs[i] = new Vec2();
			witnesses[i] = null;
		}
	}

	public void set(Simplex other)
	{
		if(this.vs.length < other.currentDim)
		{
			this.vs = new Vec2[other.currentDim];
			this.witnesses = new TwoPointWitness[other.currentDim];

			for(int i=0; i<this.vs.length; i++)
			{
				vs[i] = new Vec2();
				witnesses[i] = new TwoPointWitness();
			}
		}

		for(int i=0; i<other.currentDim; i++)
		{
			vs[i].set(other.vs[i]);

			if(other.witnesses[i] != null)
			{
				if(witnesses[i] == null)
					witnesses[i] = new TwoPointWitness();
				witnesses[i].set(other.witnesses[i]);
			}
		}

		this.currentDim = other.currentDim;

	}

	public void computeWinding()
	{
		if(currentDim <= IS_1_SIMPLEX)
			return;

		float solution = 0;

		for(int i=0;i < currentDim && solution == 0;i++)
		{
			int i1 = i;
			int i2 = (i+1) % currentDim;
			int i3 = (i+2) % currentDim;

			Vec2 vs1 = vs[i1];
			Vec2 vs2 = vs[i2];
			Vec2 vs3 = vs[i3];

			Vec2 vs21 = pool1;
			vs21.set(vs1);
			vs21.subLocal(vs2);

			Vec2 vs23 = pool2;
			vs23.set(vs3);
			vs23.subLocal(vs2);

			solution = Vec2.cross(vs21, vs23);

			//System.out.println("solution: " + solution);	
		}

		if(solution >= 0)
			winding = SystemCostants.CLOCKWISE_WINDING;
		else
			winding = SystemCostants.COUNTER_CLOCKWISE_WINDING;

	}


	public void insert(Vec2 insertPoint, TwoPointWitness tpWitness, int index)
	{
		if(index >= currentDim)
			throw new IllegalArgumentException();

		if(vs.length == currentDim) //not more space left
			updateArrays();


		int i1 = index > 0 ? index -1 : currentDim-1;
		int i2 = index;

		Vec2 vs1 = vs[i1];
		Vec2 vs2 = insertPoint;
		Vec2 vs3 = vs[i2];

		Vec2 vs21 = pool1;
		vs21.set(vs1);
		vs21.subLocal(vs2);

		Vec2 vs23 = pool2;
		vs23.set(vs3);
		vs23.subLocal(vs2);
		
		float solution = Vec2.cross(vs21, vs23);

		if((solution > 0 && winding == SystemCostants.COUNTER_CLOCKWISE_WINDING) ||
				solution <0 && winding == SystemCostants.CLOCKWISE_WINDING)
		{
			vs[i2].set(insertPoint);
		}		
		else
		{

			for(int i=currentDim; i> index;i--)
			{
				vs[i] = vs[i-1];
				witnesses[i] = witnesses[i-1];
			}

			currentDim++;

			vs[index] = new Vec2();
			vs[index].set(insertPoint);
		}

		if(tpWitness != null)
		{
			if(witnesses[index] == null)
				witnesses[index] = new TwoPointWitness();
			
			witnesses[index].set(tpWitness);
		}
		//else
			//witnesses[index] = null;

	}



	private void updateArrays()
	{

		Vec2 newVecArray[] = new Vec2[vs.length*2];
		TwoPointWitness newWitArray[] = new TwoPointWitness[vs.length*2];

		for(int i=0; i < vs.length; i++)
		{
			newVecArray[i] = vs[i];
			newWitArray[i] = witnesses[i];
		}

		vs = newVecArray;
		witnesses = newWitArray;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentDim;
		result = prime * result + Arrays.hashCode(vs);
		result = prime * result + Arrays.hashCode(witnesses);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Simplex other = (Simplex) obj;
		if (currentDim != other.currentDim)
			return false;
		//if (!Arrays.equals(vs, other.vs))
		//return false;
		for(int i=0; i<currentDim; i++)
		{
			if(!vs[i].equals(other.vs[i]))
				return false;

			if(witnesses[i] == null)
			{
				if(other.witnesses[i]!= null)
					return false;
			}
			else if(!witnesses[i].equals(other.witnesses[i]))
				return false;
		}
		//if (!Arrays.equals(witnesses, other.witnesses))
		//return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "[";
		for(int i=0; i<currentDim-1; i++)
			result = result.concat(vs[i] +", ");
		
		result = result.concat(vs[currentDim-1] + "]");
			
		return result;
	}



	public static class SimplexEdge{

		public SimplexEdge() {
			normal = new Vec2();
		}

		public int indexB;

		public float distance;
		public Vec2 normal;

		//debug data
		public int iterations = -1;

		public void set(SimplexEdge other)
		{
			this.indexB = other.indexB;
			this.distance = other.distance;
			this.normal.set(other.normal);
			this.iterations = other.iterations;
		}
		

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SimplexEdge other = (SimplexEdge) obj;
			if (Float.floatToIntBits(distance) != Float
					.floatToIntBits(other.distance))
				return false;
			if (indexB != other.indexB)
				return false;
			if (normal == null) {
				if (other.normal != null)
					return false;
			} else if (!normal.equals(other.normal))
				return false;
			return true;
		}




	}

}
