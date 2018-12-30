package it.gius.pePpe.collision.manifoldBuild;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.manifold.ContactManifold;
import it.gius.pePpe.manifold.ContactPoint;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class ManifoldTestUtils {
	
	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	private Vec2 pool3 = new Vec2();
	
	public boolean checkPointIDsOnly(ContactManifold m1, ContactManifold m2)
	{
		if(m1.size != m2.size)
			return false;

		for(int i=0; i<m1.size; i++)
			if(!m1.points[i].pointID.equals(m2.points[i].pointID))
				return false;

		return true;
	}
	
	public boolean checkOtherLocalPoint(ContactPoint contactPoint,Transform transform, Transform otherTransform)
	{
		Vec2 result = pool1;
		result.set(contactPoint.normalGlobal);
		//result.mulLocal(MathUtils.abs(contactPoint.distance));
		result.mulLocal(contactPoint.distance);
		Vec2 globalPoint = pool2;
		Transform.mulToOut(transform, contactPoint.localPoint, globalPoint);
		result.addLocal(globalPoint);
		
		Vec2 otherGlobalPoint = pool3;
		Transform.mulToOut(otherTransform, contactPoint.otherLocalPoint, otherGlobalPoint);
		
		if(it.gius.pePpe.MathUtils.manhattanDistance(result, otherGlobalPoint) > 10.0f*SystemCostants.EPSILON)
			return false;
		else
			return true;
		
	}


}
