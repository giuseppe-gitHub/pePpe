package it.gius.pePpe.algorithm.gjk;

import org.jbox2d.common.Vec2;

/**
 * 
 * @author giuseppe
 * @opt attributes
 */
public class SimplexSolution {
	
	public int numVerticesUsed = -1;
	
	public Vec2[] verticesInSolution;
	
	public float[] coefficients;
	
	
	@Override
	public String toString() {
		String result = "[";
		for(int i=0; i<numVerticesUsed-1; i++)
			result = result.concat(verticesInSolution[i] +", ");
		
		if(numVerticesUsed > 0)
			result = result.concat(verticesInSolution[numVerticesUsed-1] + "]");
		else
			result = result.concat("]");
			
		return result;
	}
	
	public SimplexSolution() {
		verticesInSolution = new Vec2[Simplex.LARGER_2D_SIMPLEX];
		coefficients = new float[Simplex.LARGER_2D_SIMPLEX];
		
		for(int i=0; i<Simplex.LARGER_2D_SIMPLEX;i++)
			verticesInSolution[i] = new Vec2();
	}

	public void getSolutionPoint(Vec2 result)
	{
		//Vec2 result = new Vec2();
		result.setZero();
		
		for(int i=0; i<numVerticesUsed; i++)
		{
			//result.addLocal(verticesInSolution[i].mul(coefficients[i]));
			result.x += verticesInSolution[i].x *coefficients[i];
			result.y += verticesInSolution[i].y * coefficients[i];
		}
		
		
		//return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplexSolution other = (SimplexSolution) obj;
		if (numVerticesUsed != other.numVerticesUsed)
			return false;
	
		for(int i=0; i< numVerticesUsed; i++)
			if(coefficients[i] != other.coefficients[i] || !verticesInSolution[i].equals(other.verticesInSolution[i]))
				return false;
		/*if (!Arrays.equals(coefficients, other.coefficients))
			return false;
	
		if (!Arrays.equals(verticesInSolution, other.verticesInSolution))
			return false;*/
		return true;
	}

	public void set(SimplexSolution simpSol)
	{
		this.numVerticesUsed = simpSol.numVerticesUsed;
		
		for(int i=0; i< this.numVerticesUsed; i++)
		{
			this.verticesInSolution[i].set(simpSol.verticesInSolution[i]);
			this.coefficients[i] = simpSol.coefficients[i];
		}
	}
	
	/*public Vec2 getSupportDirection(Vec2 q)
	{
		Vec2 n = new Vec2();
		Vec2 pSol = getSolutionPoint();
		if(numVerticesUsed == 1)
			return q.sub(pSol);
		
		if(numVerticesUsed == 2)
		{
			Vec2 tangent = verticesInSolution[0].sub(verticesInSolution[1]);
			Vec2 normal = new Vec2();
			normal.x = tangent.y;
			normal.y = -tangent.x;
			
			float length = Vec2.dot(q, q.sub(verticesInSolution[0]));
		}
		return n;
		
	}*/
}
