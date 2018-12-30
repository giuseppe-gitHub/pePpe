package it.gius.pePpe.manifold;

import it.gius.pePpe.MathUtils;



public class ContactPointID {
	

	public int featureA;
	public boolean vertexA;
	
	public int featureB;
	public boolean vertexB;
	

	public void set(ContactPointID other)
	{
		this.featureA = other.featureA;
		this.vertexA = other.vertexA;
		this.featureB = other.featureB;
		this.vertexB = other.vertexB;
	}
	
	@Override
	public int hashCode() {
		int featuresHash = featureA | (featureB << Short.SIZE);
		featuresHash = MathUtils.cuttableHashCode(featuresHash);
		
		int edgeVertexData = (vertexA ? 0xFF000000 : 0x00FF0000) ^
							 (vertexB ? 0x0000FF00 : 0x000000FF);
		
		return MathUtils.cuttableHashCode(featuresHash ^ edgeVertexData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactPointID other = (ContactPointID) obj;
		if (featureA != other.featureA)
			return false;
		if (featureB != other.featureB)
			return false;
		if (vertexA != other.vertexA)
			return false;
		if (vertexB != other.vertexB)
			return false;
		return true;
	}


	@Override
	public String toString() {
		
		return "( A: " + (vertexA ? "vertex " : "edge ") + featureA + ", " +
				"B: " + (vertexB ? "vertex " : "edge ") + featureB + ")";
	}
	
	
	
}
