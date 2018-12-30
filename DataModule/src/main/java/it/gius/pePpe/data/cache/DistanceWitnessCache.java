package it.gius.pePpe.data.cache;

import it.gius.pePpe.data.shapes.witness.TwoPointWitness;

public class DistanceWitnessCache {

	public TwoPointWitness witness = new TwoPointWitness();
	
	
	@Override
	protected  DistanceWitnessCache clone() {
		DistanceWitnessCache other = new DistanceWitnessCache();
		
		other.witness.set(this.witness);
		
		return other;
		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistanceWitnessCache other = (DistanceWitnessCache) obj;
		if (witness == null) {
			if (other.witness != null)
				return false;
		} else if (!witness.equals(other.witness))
			return false;
		return true;
	}
	
	
	
}
