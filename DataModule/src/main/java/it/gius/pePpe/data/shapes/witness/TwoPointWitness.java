package it.gius.pePpe.data.shapes.witness;

/**
 * 
 * @author giuseppe
 * @composed 1 - 2 it.gius.pePpe.shapes.witness.SinglePointWitness
 *
 */
public class TwoPointWitness{
	
	public VertexIndexWitness firstWitness;
	public VertexIndexWitness secondWitness;
	
	public TwoPointWitness() {
		firstWitness = new VertexIndexWitness();
		secondWitness = new VertexIndexWitness();
	}
	
	
	public void set(TwoPointWitness other)
	{
		this.firstWitness.set(other.firstWitness);
		this.secondWitness.set(other.secondWitness);
	}
	
	public void reset()
	{
		this.firstWitness.reset();
		this.secondWitness.reset();
	}
	
	@Override
	public int hashCode() {
		int key = firstWitness.hashCode() + secondWitness.hashCode();
		
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
		TwoPointWitness other = (TwoPointWitness) obj;
		if (firstWitness == null) {
			if (other.firstWitness != null)
				return false;
		} else if (!firstWitness.equals(other.firstWitness))
			return false;
		if (secondWitness == null) {
			if (other.secondWitness != null)
				return false;
		} else if (!secondWitness.equals(other.secondWitness))
			return false;
		return true;
	}
	
	
	

}
