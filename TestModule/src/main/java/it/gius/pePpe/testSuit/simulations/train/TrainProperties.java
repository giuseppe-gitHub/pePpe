package it.gius.pePpe.testSuit.simulations.train;

import it.gius.pePpe.testSuit.property.IProperties;

public class TrainProperties implements IProperties{
	
	
	private int numWagon;
	private int wagonSquareSide;
	
	@Override
	public void toDefaultValues() {
		numWagon = 3;
		wagonSquareSide = 20;

	}

	public int getNumWagon() {
		return numWagon;
	}

	public void setNumWagon(int numWagon) {
		if(numWagon <= 0 || numWagon > 6) {
			throw new IllegalArgumentException("Wagons must be between 1 and 6");
		}
		this.numWagon = numWagon;
	}

	public int getWagonSquareSide() {
		return wagonSquareSide;
	}

	public void setWagonSquareSide(int wagonSquareSide) {
		if(wagonSquareSide < 15 || wagonSquareSide > 35) {
			throw new IllegalArgumentException("Square Side must be beween 15 and 60");
		}
		this.wagonSquareSide = wagonSquareSide;
	}
	
	
	

}

