package it.gius.pePpe.testSuit.simulations.boxStack;

import it.gius.pePpe.testSuit.property.IProperties;

public class BoxStackProperties implements IProperties {

	private int numBoxes;
	private float restitution;
	private String useless;
	private boolean largeBoxes;
	
	@Override
	public void toDefaultValues() {
		numBoxes = 3;
		restitution = 0.6f;
		useless = new String("is useless");
		largeBoxes = false;
	}
	
	public BoxStackProperties() {
	}
	
	public void setNumBoxes(int numBoxes) throws IllegalArgumentException{
		if(numBoxes <=0 || numBoxes >= 11)
			throw new IllegalArgumentException("boxes must be between 1 and 10");
		
			this.numBoxes = numBoxes;
	}
	
	public int getNumBoxes() {
		return numBoxes;
	}
	
	public void setRestitution(float restitution) {
		if(restitution < 0 || restitution >1)
			throw new IllegalArgumentException("restitution must be between 0 and 1");
		
		this.restitution = restitution;
	}
	
	public float getRestitution() {
		return restitution;
	}
	
	public void setUseless(String useless) {
		this.useless = useless;
	}
	
	public String getUseless() {
		return useless;
	}
	
	public boolean isLargeBoxes() {
		return largeBoxes;
	}
	
	public void setLargeBoxes(boolean largeBoxes) {
		this.largeBoxes = largeBoxes;
	}

}
