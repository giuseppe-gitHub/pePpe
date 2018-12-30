package it.gius.pePpe.collision.manifoldBuild.sat;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.shapes.VerticesShape;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

public class SATAlgorithm {


	public static class MaxSeparationEdgeSolution implements Comparable<MaxSeparationEdgeSolution>
	{
		public int edgeIndex;
		public float separation; //>=0 or <0
		public Vec2 globalNormal = new Vec2();

		@Override
		public String toString() {
			return "( edge: " + edgeIndex + ", separation: " + separation + ", normal: " + globalNormal + ")";
		}

		public void set(MaxSeparationEdgeSolution other)
		{
			this.edgeIndex = other.edgeIndex;
			this.separation = other.separation;
			this.globalNormal.set(other.globalNormal);
		}

		/**
		 * Note: this class has a natural ordering that is inconsistent with equals.
		 */
		@Override
		public int compareTo(MaxSeparationEdgeSolution o) {
			if(this.separation > o.separation)
				return 1;

			if(this.separation < o.separation)
				return -1;

			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MaxSeparationEdgeSolution other = (MaxSeparationEdgeSolution) obj;
			if (edgeIndex != other.edgeIndex)
				return false;
			if (globalNormal == null) {
				if (other.globalNormal != null)
					return false;
			} else if (globalNormal.x != other.globalNormal.x || globalNormal.y != other.globalNormal.y)
				return false;
			if (Float.floatToIntBits(separation) != Float
					.floatToIntBits(other.separation))
				return false;
			return true;
		}



	}

	private MaxSeparationEdgeSolution msesCurr = new MaxSeparationEdgeSolution();
	private MaxSeparationEdgeSolution msesPrev = new MaxSeparationEdgeSolution();
	private MaxSeparationEdgeSolution msesTemp = new MaxSeparationEdgeSolution();
	private MaxSeparationEdgeSolution msesNext = new MaxSeparationEdgeSolution();


	public void maxSeparationEdgeA(VerticesShape shapeA, Transform transformA, VerticesShape shapeB, Transform transformB, 
			int startingEdge, MaxSeparationEdgeSolution result)
	{
		int sizeA = shapeA.getDim();

		if(sizeA == 2)
		{			
			separation(shapeA, transformA, shapeB, transformB, 0,result);
			return;
		}


		int index;
		//use of the witness
		if(startingEdge >= 0 && startingEdge < sizeA)
			index = startingEdge;
		else
			index = startingEdge = 0;

		int previous = (index-1 >=0) ? index-1 : sizeA-1;
		int next = (index+1 < sizeA) ? index+1 : 0;

		separation(shapeA, transformA, shapeB, transformB, previous, msesPrev);
		separation(shapeA, transformA, shapeB, transformB, index, msesCurr);
		separation(shapeA, transformA, shapeB, transformB, next, msesNext);

		if(msesCurr.separation > msesPrev.separation && msesCurr.separation > msesNext.separation)
		{
			result.set(msesCurr);
			return;
		}

		boolean movingForward;


		if(msesNext.separation > msesPrev.separation) //moving forward
		{
			if(msesCurr.separation != msesNext.separation)
				msesPrev.set(msesCurr);

			msesCurr.set(msesNext);
			//index = next;
			movingForward = true;
		}
		else //moving backward
		{
			msesTemp.set(msesPrev);
			if(msesCurr.separation != msesPrev.separation)
				msesPrev.set(msesCurr);
			else
				msesPrev.set(msesNext);

			msesCurr.set(msesTemp);
			//index = previous;
			movingForward = false;
		}

		for(;;)
		{
			index = msesCurr.edgeIndex;
			if(movingForward)
				next = (index+1 < sizeA) ? index+1 : 0;
			else
				next = (index-1 < 0) ? sizeA-1 : index-1;

			separation(shapeA, transformA, shapeB, transformB, next, msesNext);
			if(msesCurr.separation > msesPrev.separation && msesCurr.separation > msesNext.separation)
				//local (and global) optimal found
				break;

			if(msesCurr.separation != msesNext.separation) //to handle equals solution   _ _  --> _
				msesPrev.set(msesCurr);

			msesCurr.set(msesNext);		
		}

		result.set(msesCurr);

	}



	private Vec2 poolSepar1 = new Vec2();
	private Vec2 poolSepar2 = new Vec2();
	private Vec2 poolSepar3 = new Vec2();
	private Vec2 poolSepar4 = new Vec2();

	public void separation(VerticesShape shapeA, Transform transformA, VerticesShape shapeB, Transform transformB,
			int edgeOnA, MaxSeparationEdgeSolution solution)
	{
		Vec2 globalNormalAB = poolSepar1;
		globalNormalAB.set(shapeA.getNormal(edgeOnA));
		Mat22.mulToOut(transformA.R, globalNormalAB, globalNormalAB);

		if(shapeA.getDim() == 2)
		{
			Vec2 ab = poolSepar2;
			Vec2 globalCentroidA = poolSepar3;
			Transform.mulToOut(transformA, shapeA.centroid, globalCentroidA);
			Vec2 globalCentroidB = poolSepar4;
			Transform.mulToOut(transformB, shapeB.centroid, globalCentroidB);

			ab.set(globalCentroidB);
			ab.subLocal(globalCentroidA);

			float direction = Vec2.dot(globalNormalAB, ab);
			if(direction <0)
				globalNormalAB.negateLocal();
		}


		float separation = internalSeparation(shapeA, transformA, shapeB, transformB, edgeOnA, globalNormalAB);

		solution.edgeIndex = edgeOnA;
		solution.separation = separation;
		solution.globalNormal.set(globalNormalAB);

	}

	public float separation(VerticesShape shapeA, Transform transformA, VerticesShape shapeB, Transform transformB,
			int edgeOnA)
	{
		Vec2 globalNormalAB = poolSepar1;
		globalNormalAB.set(shapeA.getNormal(edgeOnA));
		Mat22.mulToOut(transformA.R, globalNormalAB, globalNormalAB);

		if(shapeA.getDim() == 2)
		{
			Vec2 ab = poolSepar2;
			Vec2 globalCentroidA = poolSepar3;
			Transform.mulToOut(transformA, shapeA.centroid, globalCentroidA);
			Vec2 globalCentroidB = poolSepar4;
			Transform.mulToOut(transformB, shapeB.centroid, globalCentroidB);

			ab.set(globalCentroidB);
			ab.subLocal(globalCentroidA);

			float direction = Vec2.dot(globalNormalAB, ab);
			if(direction <0)
				globalNormalAB.negateLocal();
		}


		return internalSeparation(shapeA, transformA, shapeB, transformB, edgeOnA, globalNormalAB);


	}


	private Vec2 poolInternalSeparation1 = new Vec2();
	private Vec2 poolInternalSeparation2 = new Vec2();
	private Vec2 poolInternalSeparation3 = new Vec2();
	private Vec2 poolInternalSeparation4 = new Vec2();
	private VertexIndexWitness witness = new VertexIndexWitness();

	private float internalSeparation(VerticesShape shapeA, Transform transformA,
			VerticesShape shapeB, Transform transformB, int edgeOnA, Vec2 globalNormalAB)
	{

		//one of the 2 points of the edge (is indifferent what of the 2)
		Vec2 pointA = poolInternalSeparation4;
		pointA.set(shapeA.getVertex(edgeOnA));

		Vec2 localBNormal = poolInternalSeparation1;
		Mat22.mulTransToOut(transformB.R, globalNormalAB, localBNormal);

		localBNormal.negateLocal();

		Vec2 pointB = poolInternalSeparation2;
		shapeB.supportPoint(localBNormal, pointB, witness);

		//points to globalPoints
		Transform.mulToOut(transformA, pointA, pointA);
		Transform.mulToOut(transformB, pointB, pointB);

		Vec2 diff = poolInternalSeparation3;
		diff.set(pointB);
		diff.subLocal(pointA);

		float separation = Vec2.dot(diff, globalNormalAB);

		return separation;
	}

}
