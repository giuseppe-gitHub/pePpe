package it.gius.pePpe.distance.cache;


public class SingleShapeNode {
	
	public short idShape;
	
	public PairNode rootPair = null;

	@Override
	public int hashCode() {
		int key = ((int)idShape); 
		
		key += ~(key << 15);
		key ^=  (key >> 10);
		key +=  (key << 3);
		key ^=  (key >> 6);
		key += ~(key << 11);
		key ^=  (key >> 16);
		return key;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleShapeNode other = (SingleShapeNode) obj;
		if (idShape != other.idShape)
			return false;
		return true;
	}
	
	
	

	


}
