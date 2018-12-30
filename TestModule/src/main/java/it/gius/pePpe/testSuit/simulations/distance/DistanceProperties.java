package it.gius.pePpe.testSuit.simulations.distance;

import it.gius.pePpe.data.shapes.ShapeType;
import it.gius.pePpe.testSuit.property.IProperties;

public class DistanceProperties implements IProperties {

	private float firstRotationVelocity;
	private float secondRotationVelocity;
	
	private ShapeType firstShapeType;
	private ShapeType secondShapeType;
	
	@Override
	public void toDefaultValues() {
		firstRotationVelocity  = 0;
		secondRotationVelocity = 0;
		
		firstShapeType = ShapeType.POLYGON;
		secondShapeType = ShapeType.POLYGON;
		
	}
	
	public float getFirstRotationVelocity() {
		return firstRotationVelocity;
	}
	
	public float getSecondRotationVelocity() {
		return secondRotationVelocity;
	}
	
	public void setFirstRotationVelocity(float firstRotationVelocity) {
		
		if(firstRotationVelocity < -0.5 || firstRotationVelocity > 0.5)
			throw new IllegalArgumentException("rotation must be 0.5 in module at max");
		
		this.firstRotationVelocity = firstRotationVelocity;
	}
	
	public void setSecondRotationVelocity(float secondRotationVelocity) {
		
		if(secondRotationVelocity < -0.5 || secondRotationVelocity > 0.5)
			throw new IllegalArgumentException("rotation must be 0.5 in module at max");
		
		this.secondRotationVelocity = secondRotationVelocity;
	}

	public void setFirstShapeType(ShapeType firstShapeType) {
		this.firstShapeType = firstShapeType;
	}
	
	public ShapeType getFirstShapeType() {
		return firstShapeType;
	}
	
	public void setSecondShapeType(ShapeType secondShapeType) {
		this.secondShapeType = secondShapeType;
	}
	
	public ShapeType getSecondShapeType() {
		return secondShapeType;
	}
}
