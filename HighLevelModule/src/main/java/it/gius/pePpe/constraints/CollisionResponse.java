package it.gius.pePpe.constraints;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.contact.Contact;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.manifold.ContactPoint;

public class CollisionResponse {

	private CollisionResponseUtil util = new CollisionResponseUtil();

	private float negativeThreshold = 0.3f;
	private float positiveThreshold = 0.15f;

	private int maxIterations = 15;


	private Vec2 pool1 = new Vec2();
	private float impulses[] = new float[2];
	private boolean pointCollision[] = new boolean[2];

	public void solveVelocity(Contact[] contacts, int contactsNum)
	{
		for(int i=0; i<contactsNum; i++)
		{
			Contact contact = contacts[i];
			for(int j=0; j< contact.manifold.size; j++)
				contact.manifold.points[j].accumulatedImpulse = 0;
		}

		boolean had_collision;
		int iterations = 0;
		do
		{
			had_collision = false;
			for(int i=0; i< contactsNum;i++)
			{
				Contact contact = contacts[i];
				Bind bindA = contact.bindA;
				Bind bindB = contact.bindB;
				Body bodyA = bindA.body;
				Body bodyB = bindB.body;
				for(int j=0; j<contact.manifold.size; j++)
				{
					ContactPoint contactPoint = contact.manifold.points[j];
					if(contactPoint.distance <= positiveThreshold && util.colliding(bodyA, bodyB, contactPoint, negativeThreshold))
					{
						had_collision = true;
						float impulse = util.calcImpulse(bindA, bindB, contactPoint);
						contactPoint.accumulatedImpulse += impulse;

						impulses[j] = impulse;
						pointCollision[j] = true;

					}
					else
						pointCollision[j] = false;
				}

				for(int j=0; j < contact.manifold.size; j++)
				{
					if(pointCollision[j])
					{
						ContactPoint contactPoint = contact.manifold.points[j];
						Vec2 normal = pool1;
						normal.set(contactPoint.normalGlobal);

						/*if((bodyA.inv_mass == 0 && bodyA.inv_Iz == 0) || (bodyB.inv_mass == 0 && bodyB.inv_Iz == 0))
						impulse = 2*impulse;*/

						if(!contactPoint.pointOnShapeB)
						{
							util.updateVelocity(bodyA, contactPoint.globalPoint, -impulses[j], normal);
							util.updateVelocity(bodyB, contactPoint.otherGlobalPoint, impulses[j], normal);
						}
						else
						{
							util.updateVelocity(bodyB, contactPoint.globalPoint, -impulses[j], normal);
							util.updateVelocity(bodyA, contactPoint.otherGlobalPoint, impulses[j], normal);

						}
					}

				}

			}
			//iterations++;
		}
		while(had_collision && iterations < maxIterations);
	}
}
