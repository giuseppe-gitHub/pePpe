package it.gius.pePpe.data.shapes.witness;

import it.gius.pePpe.MathUtils;

public class VertexIndexWitness {
	
	
	public int index;
	
	public VertexIndexWitness() {
		index = 0;
	}
	
	public void set(VertexIndexWitness other)
	{
		this.index = other.index;
	}
	
	public void reset()
	{
		this.index = 0;
	}
	
	@Override
	public int hashCode() {
		int key = index;
		key = MathUtils.cuttableHashCode(key);
		return key;
	}
	
	public VertexIndexWitness clone()  {
		VertexIndexWitness clone = new VertexIndexWitness();
		clone.index = index;
		return clone;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertexIndexWitness other = (VertexIndexWitness) obj;
		if (index != other.index)
			return false;
		return true;
	}
	
	

}
