package it.gius.pePpe.data;

import it.gius.pePpe.data.physic.Bind;

public class BindPair {

	private short idBindA;
	private short idBindB;
	
	public Bind bindA;
	public Bind bindB;
	
	public BindPair() {
		bindA = null;
		bindB = null;
		idBindA = -1;
		idBindB = -1;
	}
	
	public BindPair(Bind bindA, Bind bindB) {
		super();
		this.idBindA = bindA.globalId;
		this.idBindB = bindB.globalId;
		this.bindA = bindA;
		this.bindB = bindB;
	}
	
	public void set(Bind bindA, Bind bindB)
	{
		this.idBindA = bindA.globalId;
		this.idBindB = bindB.globalId;
		this.bindA = bindA;
		this.bindB = bindB;
	}


	@Override
	public int hashCode() {
		
		int key = ((int)idBindB) | ( ((int)idBindA) <<Short.SIZE); 
		
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
		BindPair other = (BindPair) obj;
		if (bindA == null) {
			if (other.bindA != null)
				return false;
		} else if (!bindA.equals(other.bindA))
			return false;
		if (bindB == null) {
			if (other.bindB != null)
				return false;
		} else if (!bindB.equals(other.bindB))
			return false;
		return true;
	}
	
	
	
	
	
	
}
