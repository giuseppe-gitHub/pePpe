package it.gius.pePpe.data.shapes;

import org.jbox2d.common.Vec2;



public abstract class VerticesShape extends Shape {


	public abstract Vec2 getNormal(int index);
	
	public abstract Vec2 getVertex(int index);
}
