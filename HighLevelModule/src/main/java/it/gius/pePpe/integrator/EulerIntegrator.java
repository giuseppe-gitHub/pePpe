package it.gius.pePpe.integrator;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyList;
import it.gius.pePpe.data.physic.BodyPosition;
import it.gius.pePpe.engine.TimeStep;
import it.gius.pePpe.forces.ForceManager;
import it.gius.pePpe.simulator.SimulatorException;

/**
 * Semi-implicit Euler integrator
 * @author giuseppe
 *
 */
public class EulerIntegrator implements ISingleStepIntegrator, IDoubleStepIntegrator {


	private BodyList bodyList = null;
	private ForceManager fManager = null;

	private boolean inited = false;

	@Override
	public void init(BodyList bodyList,ForceManager fManager) throws SimulatorException{
		if(inited)
			throw new SimulatorException("Integrator already initialized");

		this.bodyList = bodyList;

		this.fManager = fManager;

		inited = true;

	}

	//private Vec2 poolAcceleration = new Vec2();

	@Override
	public void step(TimeStep timeStep) {

		if(!inited)
			throw new RuntimeException("Integrator not yet initialized");

		fManager.clearForces();
		fManager.applyForces(timeStep.time);

		float step = timeStep.step;


		Body currentBody = bodyList.firstBody;

		while(currentBody != null)
		{
			if(!currentBody.fixed)
			{
				Vec2 force = currentBody.getForce();
				float torque = currentBody.getTorque();

				float stepXInvMass = step*currentBody.inv_mass;

				//currentBody.linearVelocity.x += (force.x - currentBody.linearDamping*currentBody.linearVelocity.x)*stepXInvMass;
				//currentBody.linearVelocity.y += (force.y - currentBody.linearDamping*currentBody.linearVelocity.y)*stepXInvMass;

				//float linearDenom = 1f + stepXInvMass*currentBody.linearDamping;

				Vec2 currLinearVelocity = currentBody.linearVelocity;
				currLinearVelocity.x += force.x*stepXInvMass;// / linearDenom;
				currLinearVelocity.y += force.y*stepXInvMass;// / linearDenom;

				currLinearVelocity.x -= currLinearVelocity.x*currentBody.linearDamping;
				currLinearVelocity.y -= currLinearVelocity.y*currentBody.linearDamping;

				float stepXinvIz = step*currentBody.inv_Iz;

				//float angularDenom = 1f + currentBody.angularDamping*stepXinvIz;

				//currentBody.angularVelocity += (torque - currentBody.angularDamping*currentBody.angularVelocity)*stepXinvIz;
				currentBody.angularVelocity += torque*stepXinvIz;// / angularDenom;
				currentBody.angularVelocity -= currentBody.angularVelocity*currentBody.angularDamping;


				BodyPosition bodyPosition = currentBody.getBodyPosition();

				bodyPosition.globalCenter.x += currentBody.linearVelocity.x*step;
				bodyPosition.globalCenter.y += currentBody.linearVelocity.y*step;

				bodyPosition.angle += currentBody.angularVelocity*step;

				currentBody.synchronizeTransform();
			}
			currentBody = currentBody.next;
		}

	}


	@Override
	public void integrateVelocity(TimeStep timeStep) {

		if(!inited)
			throw new RuntimeException("Integrator not yet initialized");

		fManager.clearForces();
		fManager.applyForces(timeStep.time);

		float step = timeStep.step;

		Body currentBody = bodyList.firstBody;

		while(currentBody != null)
		{
			if(!currentBody.fixed)
			{
				Vec2 force = currentBody.getForce();
				float torque = currentBody.getTorque();

				float stepXInvMass = step*currentBody.inv_mass;

				Vec2 currLinearVelocity = currentBody.linearVelocity;
				currLinearVelocity.x += force.x*stepXInvMass;
				currLinearVelocity.y += force.y*stepXInvMass;

				currLinearVelocity.x -= currLinearVelocity.x*currentBody.linearDamping;
				currLinearVelocity.y -= currLinearVelocity.y*currentBody.linearDamping;

				float stepXinvIz = step*currentBody.inv_Iz;

				currentBody.angularVelocity += torque*stepXinvIz;
				currentBody.angularVelocity -= currentBody.angularVelocity*currentBody.angularDamping;

			}
			currentBody = currentBody.next;
		}
	}

	@Override
	public void integratePosition(TimeStep timeStep) {
		if(!inited)
			throw new RuntimeException("Integrator not yet initialized");

		float step= timeStep.step;

		Body currentBody = bodyList.firstBody;

		while(currentBody != null)
		{
			if(!currentBody.fixed)
			{

				BodyPosition bodyPosition = currentBody.getBodyPosition();

				bodyPosition.globalCenter.x += currentBody.linearVelocity.x*step;
				bodyPosition.globalCenter.y += currentBody.linearVelocity.y*step;

				bodyPosition.angle += currentBody.angularVelocity*step;

				currentBody.synchronizeTransform();
			}

			currentBody = currentBody.next;
		}

	}

}
