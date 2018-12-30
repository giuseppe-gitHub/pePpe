package it.gius.pePpe.data.shapes;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class ShapeUtils {

	private Vec2 pool1 = new Vec2();
	
	public void mulToOutPolygon(Polygon polyIn, Transform transform, Polygon polyOut)
	{
		for(int i=0; i<polyIn.getDim(); i++)
		{
			Transform.mulToOut(transform, polyIn.getVertex(i), pool1);
			polyOut.addVertex(pool1);
		}
	}
	
	public void mulTransToOutPolygon(Polygon polyIn, Transform transform, Polygon polyOut)
	{
		for(int i=0; i<polyIn.getDim(); i++)
		{
			Transform.mulTransToOut(transform, polyIn.getVertex(i), pool1);
			polyOut.addVertex(pool1);
		}
	}
}
