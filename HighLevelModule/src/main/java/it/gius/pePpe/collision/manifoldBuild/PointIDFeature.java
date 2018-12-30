package it.gius.pePpe.collision.manifoldBuild;

public class PointIDFeature {

	public int feature;
	public boolean vertex;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointIDFeature other = (PointIDFeature) obj;
		if (feature != other.feature)
			return false;
		if (vertex != other.vertex)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return (vertex ? "Vertex: " : "Edge: ") + feature;
		
	}
	
	
}
