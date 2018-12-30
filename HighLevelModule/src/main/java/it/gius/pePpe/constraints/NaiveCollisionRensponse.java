package it.gius.pePpe.constraints;

import org.jbox2d.common.Vec2;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.engine.PhysicEngine;

public class NaiveCollisionRensponse {
	
	
	private PhysicEngine engine;
	
	public NaiveCollisionRensponse() {
		
	}
	
	
	public PhysicEngine getEngine() {
		return engine;
	}
	
	public void setEngine(PhysicEngine engine) {
		this.engine = engine;
	}
	
	
	private Vec2 pool1 = new Vec2();
	private Vec2 pool2 = new Vec2();
	
	public void react(AABBPair[] pairs, int pairsNum)
	{
		IdList<BindAABBNode> listNodes = this.engine.getGlobalShapes();
		Vec2 vecAB = pool1;
		
		
		for(int i=0; i< pairsNum; i++)
		{
		   BindAABBNode nodeA = listNodes.get(pairs[i].idShapeA);	
		   BindAABBNode nodeB = listNodes.get(pairs[i].idShapeB);
		   
		   Vec2 globalA = nodeA.bind.body.getGlobalCenter();
		   Vec2 globalB = nodeB.bind.body.getGlobalCenter();
		   
		   vecAB.set(globalB);
		   vecAB.subLocal(globalA);
		   
		   vecAB.normalize();
		   
		   Vec2 velB = nodeB.bind.body.linearVelocity;
		   float diffB = Vec2.dot(vecAB, velB);
		   
		   Vec2 diffVelB = pool2;
		   
		   diffVelB.set(vecAB);
		   diffVelB.mulLocal(2*diffB);
		   
		   velB.subLocal(diffVelB);
		   
		   
		   Vec2 velA = nodeA.bind.body.linearVelocity;
		   float diffA = Vec2.dot(vecAB, velA);
		   
		   Vec2 diffVelA = pool2;
		   
		   diffVelB.set(vecAB);
		   diffVelB.mulLocal(2*diffA);
		   
		   velA.subLocal(diffVelA);
		   
		}
	}

}
