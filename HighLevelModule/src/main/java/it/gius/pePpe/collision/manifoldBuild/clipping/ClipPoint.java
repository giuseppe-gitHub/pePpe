package it.gius.pePpe.collision.manifoldBuild.clipping;

import org.jbox2d.common.Vec2;

public class ClipPoint {

	/*
	 * usually a globalPoint
	 */
	public Vec2 point = new Vec2();
	public int index;
	
	
	public int refPointIndex = -1;
	
	public float depth;
	
	/**
	 * if 0 is a vertex
	 */
	//public boolean internal = false;
	
	
	public void set(ClipPoint other)
	{
		this.point.set(other.point);
		this.index = other.index;
		this.refPointIndex = other.refPointIndex;
		this.depth = other.depth;
		//this.internal = other.internal;
	}
	
	
}
