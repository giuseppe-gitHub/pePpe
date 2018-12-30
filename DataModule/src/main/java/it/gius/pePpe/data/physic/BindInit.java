package it.gius.pePpe.data.physic;

import it.gius.pePpe.data.shapes.Shape;

public class BindInit {
	
	public Shape shape;
	public Body body;
	
	public float friction;
	public float restituion;
	
	public float density;
	
	
	public BindInit clonePrototype()
	{
		BindInit result = new BindInit();
		
		result.friction = this.friction;
		result.restituion = this.restituion;
		
		result.density = this.density;
		
		return result;
	}

}
