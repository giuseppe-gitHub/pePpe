package it.gius.pePpe.collision.manifoldBuild.clipping;



import org.jbox2d.common.Vec2;

public class ClipEdge {

	public Vec2 p1;
	public Vec2 p2;
	public Vec2 max;
	
	public int edgeIndex = -1;

	public ClipEdge() {
		
		this.p1 = new Vec2();
		this.p2 = new Vec2();
		this.max = new Vec2();
	}
	
	public ClipEdge(Vec2 p1, Vec2 p2, Vec2 max, int edgeIndex) {
		super();
		this.p1 = new Vec2();
		this.p1.set(p1);
		this.p2 = new Vec2();
		this.p2.set(p2);
		this.max = new Vec2();
		this.max.set(max);
		this.edgeIndex = edgeIndex;
	}
	
	public void set(ClipEdge edge)
	{
		this.p1.set(edge.p1);
		this.p2.set(edge.p2);
		this.max.set(edge.max);
		this.edgeIndex = edge.edgeIndex;
	}
	
	
	public void getEdgeVector(Vec2 edgeVector)
	{
		edgeVector.x = p2.x - p1.x;
		edgeVector.y = p2.y - p1.y;
	}
	
	@Override
	public String toString() {
		return "p1:" + this.p1 + ", p2:" + this.p2 +
				", max:" +this.max + ", index: " + edgeIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClipEdge other = (ClipEdge) obj;
		if (edgeIndex != other.edgeIndex)
			return false;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}
	
	
	
	
}
