package it.gius.pePpe.manifold;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

public class ContactManifold {

	public ContactPoint points[]; 

	public int size = 0;

	public ContactType type;

	public enum ContactType {
		POLYPOLY(2),POLYCIRCLE(1),CIRCLECIRCLE(1);

		public final int maxSize;

		private ContactType(int maxSize) {
			this.maxSize = maxSize;	
		}

	}

	public ContactManifold() {
		points = new ContactPoint[2];

		points[0] = new ContactPoint();
		points[1] = new ContactPoint();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactManifold other = (ContactManifold) obj;
		if (size != other.size)
			return false;
		if (type != other.type)
			return false;
		for(int i=0; i<size; i++)
			if(!points[i].equals(other.points[i]))
				return false;
		return true;
	}

	/**
	 * 
	 * @param pointID
	 * @return the point position in the array if present, a value < 0 instead
	 */
	public int getPointPosition(ContactPointID pointID)
	{
		for(int i=0; i<size; i++)
			if(points[i].pointID.equals(pointID))
				return i;

		return -1;
	}

	/*public int addPoint(ContactPoint newPoint)
	{

		int insertIndex;

		if(size == type.maxSize)
		{
			insertIndex = whereToInsertPoint();
		}
		else
		{
			insertIndex = size;
			size++;
		}

		points[insertIndex].set(newPoint);

		return insertIndex;
	}*/

	private Vec2 globalPoint = new Vec2();
	private Vec2 otherGlobalPoint = new Vec2();
	private Vec2 distanceVector = new Vec2();
	private Vec2 projectedPoint = new Vec2();

	public void refreshAllContactPoints(float maxDistance, float translationThreshold, Transform transformA, Transform transformB)
	{
		int initSize = size;
		for(int i=initSize-1; i>=0;i--)
		{
			refreshSingleContactPoint(i, maxDistance, translationThreshold, transformA, transformB);
		}

	}

	public void refreshSingleContactPoint(int index, float maxDistance, float translationThreshold, Transform transformA, Transform transformB)
	{

		if(index <0 || index >= size)
			return;

		ContactPoint currPoint = points[index];
		if(!currPoint.pointOnShapeB)
		{
			Transform.mulToOut(transformA, currPoint.localPoint, globalPoint);
			Transform.mulToOut(transformB, currPoint.otherLocalPoint, otherGlobalPoint);
		}
		else
		{
			Transform.mulToOut(transformB, currPoint.localPoint, globalPoint);
			Transform.mulToOut(transformA, currPoint.otherLocalPoint, otherGlobalPoint);
		}
		// otherGlobalPoint = globalPoint + disance*normal
		distanceVector.x = otherGlobalPoint.x - globalPoint.x;
		distanceVector.y = otherGlobalPoint.y - globalPoint.y;

		currPoint.distance = Vec2.dot(distanceVector, currPoint.normalGlobal);

		if(currPoint.distance > maxDistance)
		{
			removePoint(index);
			return;
		}

		//calculate translation from normal
		projectedPoint.x = globalPoint.x + currPoint.normalGlobal.x *currPoint.distance;
		projectedPoint.y = globalPoint.y + currPoint.normalGlobal.y *currPoint.distance;
		projectedPoint.x = projectedPoint.x -otherGlobalPoint.x;
		projectedPoint.y = projectedPoint.y -otherGlobalPoint.y;
		float translation2 = Vec2.dot(projectedPoint,projectedPoint);

		if(translation2 > (translationThreshold*translationThreshold))
			removePoint(index);
	}


	@SuppressWarnings("unused")
	private int whereToInsertPoint()
	{
		float maxDistance = points[0].distance;

		int indexResult = 0;
		float currDistance;

		for(int i=1; i<size; i++)
		{
			currDistance = points[i].distance;
			if( currDistance> maxDistance)
			{
				indexResult = i;
				maxDistance = currDistance;
			}
		}

		return indexResult;	
	}


	public void removePoint(int index)
	{
		if(index == size-1)
		{
			size--;
			return;
		}

		ContactPoint temp;

		temp = points[index];
		points[index] = points[size-1];
		points[size-1] = temp;
		size--;

	}


}
