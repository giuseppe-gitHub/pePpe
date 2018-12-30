package it.gius.pePpe.collision.manifoldBuild.clipping;

import org.jbox2d.common.Vec2;

public class ClippingResult {
	
	public ClipPoint[] clipPoints;
	public int size;
	public Vec2 refNormal = new Vec2();
	public Vec2 refNormalNegated = new Vec2();
	
	public ClippingResult() {
		clipPoints = new ClipPoint[2];
		clipPoints[0] = new ClipPoint();
		clipPoints[1] = new ClipPoint();
		
		size = 0;
	}

}
