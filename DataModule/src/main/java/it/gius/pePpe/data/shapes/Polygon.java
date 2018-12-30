package it.gius.pePpe.data.shapes;

import it.gius.pePpe.SystemCostants;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.PhysicData;
import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;


/**
 * 
 * @author giuseppe
 * @depend - <convert> - it.gius.pePpe.data.shapes.witness.PolyPointWitness
 */
public final class Polygon extends VerticesShape{

	private Vec2[] vertices;
	private int verticesNum;
	private Vec2[] normals;
	private Vec2[] tangents;

	public int winding;

	private static int defaultArraySize = 10;
	private int arrayMult = 1;

	private boolean done = false;


	//private VertexIndexWitness polyPointWitness = new VertexIndexWitness();
	private float Iz;
	private float area;

	private Vec2 pool1 = new Vec2();

	//public Vec2 velocity = new Vec2();

	public Polygon() {
		super();
		vertices = new Vec2[defaultArraySize*arrayMult];
		verticesNum = 0;
		//velocity.setZero();
	}


	public void reset()
	{
		done = false;
		verticesNum = 0;
		//velocity.setZero();
	}


	public Polygon clone()
	{
		if(!done)
			return null;

		Polygon other = new Polygon();

		other.verticesNum = this.verticesNum;

		other.vertices = new Vec2[other.verticesNum];

		for(int i=0; i< this.verticesNum; i++)
		{
			other.vertices[i] = new Vec2();
			other.vertices[i].set(this.vertices[i]);
		}	

		other.endPolygon();

		return other;
	}

	/*public void setVelocity(Vec2 velocity)
	{
		if(velocity != null)
			this.velocity = velocity;
	}*/



	@Override
	public void  fromWitnessToVec(VertexIndexWitness witness, Vec2 result) {

		/*if(witness == null)
		{
			result.set(vertices[0]);
			return;
		}*/

		VertexIndexWitness pwit = witness;

		if(witness != null && pwit.index >=0 && pwit.index < verticesNum)
			result.set(vertices[pwit.index]);
		else
			throw new IllegalArgumentException();

	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Polygon other = (Polygon) obj;
		if (verticesNum != other.verticesNum)
			return false;

		boolean arraysEquals = true;
		for(int i=0; i<verticesNum; i++)
		{
			if(!vertices[i].equals(other.vertices[i]))
			{
				arraysEquals = false;
				break;
			}
		}

		if(!arraysEquals)
			return false;
		/*if (!Arrays.equals(vertices, other.vertices))
			return false;*/

		return true;
	}


	/*@Override
	public Witness getWitnessInstance() {
		polyPointWitness.index = 0;
		return polyPointWitness;
	}*/


	/*@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, VertexIndexWitness wOut) {

		int index = 0;

		float maxDistance = vertices[0].x*d.x + vertices[0].y*d.y;

		float curValue = 0;
		int n = verticesNum;
		for(int i=1; i<n;i++)
		{
			curValue = vertices[i].x*d.x + vertices[i].y*d.y;
			if(curValue > maxDistance)
			{
				index = i;
				maxDistance = curValue;
			}
			//DID: break cycle if find a local (that is also a global) optimal solution

		}

		supportPoint.set(vertices[index]);

		//polyPointWitness.index = index;
		wOut.index = index;
	}*/

	@Override
	public void supportPoint(Vec2 d, Vec2 supportPoint, VertexIndexWitness wOut) {

		int index = 0;
		int n = verticesNum;

		float previous = vertices[n-1].x*d.x + vertices[n-1].y*d.y;
		float curValue = vertices[0].x*d.x + vertices[0].y*d.y;


		float next = 0;

		for(int i=0; i<n;i++)
		{
			int nextI = (i != n-1) ? i+1 : 0;
			next = vertices[nextI].x*d.x + vertices[nextI].y*d.y;
			if(curValue >= previous)
			{
				if(curValue > next || (curValue > previous && curValue == next) )
				{
					//local optimal solution (for convexity is also a global optimal so break)
					index = i;
					break;
				}
			}
			
			previous = curValue;
			curValue = next;

		}

		supportPoint.set(vertices[index]);

		wOut.index = index;
	}

	private void resizeArray()
	{
		if(verticesNum < vertices.length)
			return;

		arrayMult++;
		Vec2[] newArray = new Vec2[defaultArraySize*arrayMult];
		for(int i=0; i < verticesNum; i++)
		{
			newArray[i] = vertices[i];
		}
		vertices = newArray;
	}

	/*public void setPosition(Vec2 newCenter)
	{
		if(!done)
			return;

		Vec2 oldV1 = vertices[0];
		Vec2 vc = oldV1.sub(this.centroid);

		this.centroid.set(newCenter);

		vertices[0] = centroid.add(vc);

		Vec2 d = vertices[0].sub(oldV1);

		for(int i=1; i<verticesNum; i++)
			vertices[i].addLocal(d);


	}*/

	public void setAsBox(float lowX, float lowY, float upX, float upY) throws BadShapeException
	{
		if(done)
			throw new BadShapeException("Polygon already done");

		if(lowX >= upX || lowY >= upY)
			throw new BadShapeException("Bad parameters");

		if(vertices.length < 4)
			vertices =  new Vec2[4];

		for(int i=0; i< vertices.length;i++)
		{
			if(vertices[i] == null)
				vertices[i] = new Vec2();
		}

		vertices[0].set(lowX,lowY);
		vertices[1].set(upX,lowY);
		vertices[2].set(upX,upY);
		vertices[3].set(lowX,upY);

		verticesNum = 4;

		endPolygon();

	}


	public boolean addVertex(Vec2 p)
	{
		if(!done)
		{
			if(vertices[verticesNum] == null)
				vertices[verticesNum] = new Vec2();

			vertices[verticesNum].set(p);
			verticesNum++;
			resizeArray();
			return true;
		}

		return false;
	}

	public int getDim()
	{
		return verticesNum;
	}

	@Override
	public String toString() {

		String result = "[";

		for(int i=0; i<verticesNum-1;i++)
			result = result.concat(vertices[i] + ", ");

		result = result.concat(vertices[verticesNum-1] + "]");

		return result;
	}


	@Override
	public Vec2 getVertex(int index)
	{
		return vertices[index];//.clone();
	}

	public Vec2 getNormal(int firstIndex)
	{
		return normals[firstIndex];
	}

	public Vec2 getTangent(int firstIndex)
	{
		return tangents[firstIndex];
	}


	public boolean contains(Vec2 point)
	{
		Vec2 difference = pool1;

		for(int i=0; i< verticesNum; i++)
		{
			difference.set(point);
			difference.subLocal(vertices[i]);

			final float dot = Vec2.dot(difference, normals[i]);

			if(dot >=0)
				return false;
		}

		return true;
	}

	public void endPolygon() throws BadShapeException
	{
		if(!isConvex())
			throw new BadShapeException();


		normals = new Vec2[verticesNum];
		tangents = new Vec2[verticesNum];

		Vec2 difference = pool1;

		for(int i=0;i<verticesNum;i++)
		{
			int i1 = i;
			int i2 = i+1 < verticesNum ? i+1 : 0;

			difference.set(vertices[i2]);
			difference.subLocal(vertices[i1]);

			if(winding == SystemCostants.CLOCKWISE_WINDING)
				normals[i] = new Vec2(-difference.y,difference.x);
			else
				normals[i] = new Vec2(difference.y,-difference.x);

			tangents[i] = new Vec2(difference.x,difference.y);

			normals[i].normalize();
			tangents[i].normalize();
		}



		centroid.setZero();		
		area = 0;
		float Ixx = 0, Iyy = 0;

		Vec2 p1,p2;
		for(int i=0; i < verticesNum; i++)
		{
			p1 = vertices[i];
			p2 = i+1 < verticesNum ? vertices[i+1] : vertices[0];

			float triangleAreaDoubled = Vec2.cross(p1, p2);

			area += (triangleAreaDoubled / 2.0f);

			centroid.x += triangleAreaDoubled * ( 0.0f + p1.x + p2.x);
			centroid.y += triangleAreaDoubled * ( 0.0f + p1.y + p2.y);

			Ixx += triangleAreaDoubled * (p1.y*p1.y + p1.y*p2.y + p2.y*p2.y);
			Iyy += triangleAreaDoubled * (p1.x*p1.x + p1.x*p2.x + p2.x*p2.x);


			/* alternative method for moments
			 * Ixx -= (p2.x - p1.x)*(p1.y + p2.y) * (p1.y*p1.y + p2.y*p2.y);
			 * Iyy += (p2.y - p1.y)*(p1.x + p2.x) * (p1.x * p1.x + p2.x*p2.x);
			 */

		}

		centroid.mulLocal(1/(area*6.0f));

		if(area <0)
			area = -area;

		Ixx = Ixx/12.0f;
		Iyy = Iyy/12.0f;

		//already relative to the origin
		Iz = StrictMath.abs(Ixx + Iyy);

		//maxInnerDistance = maxInnerDistance();
		radius = radius();
		maxInnerDistance = 2*radius;

		done= true;
	}


	private float radius()
	{
		float max2 =0;

		for(int i=0; i <verticesNum; i++)
		{
			pool1.set(centroid);
			pool1.subLocal(vertices[i]);

			float current2 = pool1.lengthSquared();

			if(current2 > max2)
				max2 = current2;
		}

		float max = (float)StrictMath.sqrt(max2);

		return max;//(max*2);
	}

	@Override
	public void getPhysicsData(PhysicData out, float density)	throws BadShapeException {

		if(!done)
			throw new BadShapeException("Polygon definition not ended yet!");

		out.mass = area*density;

		out.Iz = Iz * density;

		out.massCenter.set(centroid);

	}

	private Vec2 pool2 = new Vec2();

	public boolean isConvex()
	{

		if(done)
			return true;

		if(verticesNum <=2)
			return false;

		float oldSign = 0;
		for(int i = 0; i < verticesNum; i++)
		{
			int i1 = i;
			int i2 = (i+1) % verticesNum;
			int i3 = (i+2) % verticesNum;
			Vec2 v1 = vertices[i1];			
			Vec2 v2 = vertices[i2];
			Vec2 v3 = vertices[i3];

			//Vec2 v12 = v2.sub(v1);
			Vec2 v12 = pool1;
			v12.set(v1);
			v12.subLocal(v2);

			//Vec2 q = v3.sub(v2);
			Vec2 q = pool2;
			q.set(v3);
			q.subLocal(v2);

			float r = Vec2.cross(v12, q);

			if(i != 0)
			{
				if((r>0 && oldSign <0) ||(r<0 && oldSign>0))
					return false;
			}

			oldSign = r;
		}

		if(oldSign >= 0)
			winding = SystemCostants.CLOCKWISE_WINDING;
		else
			winding = SystemCostants.COUNTER_CLOCKWISE_WINDING;

		return true;
	}


	@Override
	public void computeBox(AABoundaryBox result, Transform transform) throws BadShapeException
	{

		if(!done)
			throw new BadShapeException();

		//Vec2 lowerBound = new Vec2();
		//Vec2 upperBound = new Vec2();

		//result.lowerBound.set(vertices[0]);
		//result.upperBound.set(vertices[0]);
		Transform.mulToOut(transform, vertices[0], result.lowerBound);
		Transform.mulToOut(transform, vertices[0], result.upperBound);

		Vec2 currentVertex = pool1;
		for(int i=0; i < verticesNum; i++)
		{
			Transform.mulToOut(transform, vertices[i], currentVertex);

			if(currentVertex.x < result.lowerBound.x)
				result.lowerBound.x = currentVertex.x;

			if(currentVertex.y < result.lowerBound.y)
				result.lowerBound.y = currentVertex.y;

			if(currentVertex.x > result.upperBound.x)
				result.upperBound.x = currentVertex.x;

			if(currentVertex.y > result.upperBound.y)
				result.upperBound.y = currentVertex.y;
		}

		//BoundaryBox bbox = new BoundaryBox(lowerBound,upperBound);

		//return bbox;

	}

	public boolean isFinished()
	{
		return done;
	}

	/*public void move()
	{
		if(!done)
			return;

		centroid.addLocal(velocity);

		for(int i=0; i< verticesNum; i++)
		{
			vertices[i].addLocal(velocity);
		}
	}*/


	public Vec2 getCentroid() {
		return centroid;//.clone();
	}

}
