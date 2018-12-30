package it.gius.pePpe.integrator;

import org.jbox2d.common.Vec2;

import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyList;
import it.gius.pePpe.data.physic.BodyPosition;
import it.gius.pePpe.engine.TimeStep;
import it.gius.pePpe.forces.ForceManager;
import it.gius.pePpe.simulator.SimulatorException;
/**
 * http://en.wikipedia.org/wiki/Runge%E2%80%93Kutta_methods
 * 
 * @author giuseppe
 *
 */

public class RK4Integrator implements ISingleStepIntegrator {

	private BodyList bodyList = null;
	private ForceManager fManager = null;

	private boolean inited = false;

	private static final int DEFAULT_SIZE = 100;


	@Override
	public void init(BodyList bodyList, ForceManager fManager) throws SimulatorException {

		this.bodyList = bodyList;
		this.fManager = fManager;

		inited = true;
	}

	private static class BodyState
	{
		public Vec2 globalCenter = new Vec2();
		public float angle = 0;

		public Vec2 linearVelocity = new Vec2();
		public float angularVelocity = 0;


	}

	private static class Derivative
	{
		public Vec2 force = new Vec2();
		public float torque = 0;

		public Vec2 linearVelocity = new Vec2();
		public float angularVelocity = 0;
	}

	private BodyState[] originalState = new BodyState[DEFAULT_SIZE];
	private Derivative[] k1 = new Derivative[DEFAULT_SIZE];
	private Derivative[] k2 = new Derivative[DEFAULT_SIZE];
	private Derivative[] k3 = new Derivative[DEFAULT_SIZE];
	private Derivative[] k4 = new Derivative[DEFAULT_SIZE];


	private void resizeArrays()
	{


		BodyState[] newOriginalState = new BodyState[bodyList.bodiesNumber*2];
		Derivative[] newK1 = new Derivative[bodyList.bodiesNumber*2];
		Derivative[] newK2 = new Derivative[bodyList.bodiesNumber*2];
		Derivative[] newK3 = new Derivative[bodyList.bodiesNumber*2];
		Derivative[] newK4 = new Derivative[bodyList.bodiesNumber*2];

		for(int i= 0; i < originalState.length; i++)
		{
			newOriginalState[i] = originalState[i];
			newK1[i] = k1[i];
			newK2[i] = k2[i];
			newK3[i] = k3[i];
			newK4[i] = k4[i];
		}

		originalState = newOriginalState;
		k1 = newK1;
		k2 = newK2;
		k3 = newK3;
		k4 = newK4;

	}

	@Override
	public void step(TimeStep timeStep) {
		// TODO test this
		if(!inited)
			throw new RuntimeException("Integrator not yet initialized");

		Body firstBody = bodyList.firstBody;

		float time = timeStep.time;
		float step= timeStep.step;
		evaluateK1UpdateState(time,true);
		evaluate(time,firstBody,originalState, 0.5f*step, k1, k2);
		evaluate(time,firstBody,originalState, 0.5f*step, k2, k3);
		evaluate(time,firstBody,originalState, step, k3, k4);


		/*update state*/

		Body currBody = firstBody;
		int i=0;

		float stepX_inv_6 = 1.0f * step/ 6.0f;

		while(currBody != null)
		{
			if(!currBody.fixed)
			{
				BodyPosition bodyPos = currBody.getBodyPosition();

				bodyPos.globalCenter.x = originalState[i].globalCenter.x + stepX_inv_6 *(k1[i].linearVelocity.x + 2f*(k2[i].linearVelocity.x + 
						k3[i].linearVelocity.x) + k4[i].linearVelocity.x);
				bodyPos.globalCenter.y = originalState[i].globalCenter.y + stepX_inv_6 *(k1[i].linearVelocity.y + 2f*(k2[i].linearVelocity.y + 
						k3[i].linearVelocity.y) + k4[i].linearVelocity.y);
				bodyPos.angle = originalState[i].angle +  stepX_inv_6 * (k1[i].angularVelocity + 2f*(k2[i].angularVelocity +
						k3[i].angularVelocity) + k4[i].angularVelocity);


				currBody.linearVelocity.x = originalState[i].linearVelocity.x + currBody.inv_mass* stepX_inv_6*(k1[i].force.x + 2f*(k2[i].force.x +
						k3[i].force.x) + k4[i].force.x);
				currBody.linearVelocity.y = originalState[i].linearVelocity.y + currBody.inv_mass* stepX_inv_6*(k1[i].force.y + 2f*(k2[i].force.y +
						k3[i].force.y) + k4[i].force.y);

				currBody.angularVelocity = originalState[i].angularVelocity + currBody.inv_Iz * stepX_inv_6 * (k1[i].torque + 2f*(k2[i].torque +
						k3[i].torque) + k4[i].torque);


				currBody.synchronizeTransform();
			}
			currBody = currBody.next;
			i++;
		}


	}


	public void integrateVelocity(TimeStep timeStep) {

		if(!inited)
			throw new RuntimeException("Integrator not yet initialized");

		Body firstBody = bodyList.firstBody;

		float time = timeStep.time;
		float step = timeStep.step;
		evaluateK1UpdateState(time,true);
		evaluate(time,firstBody,originalState, 0.5f*step, k1, k2);
		evaluate(time,firstBody,originalState, 0.5f*step, k2, k3);
		evaluate(time,firstBody,originalState, step, k3, k4);


		/*update state*/

		Body currBody = firstBody;
		int i=0;

		float multiplier = 1.0f * step/ 6.0f;

		while(currBody != null)
		{
			BodyPosition bodyPos = currBody.getBodyPosition();

			bodyPos.globalCenter.x = originalState[i].globalCenter.x;
			bodyPos.globalCenter.y = originalState[i].globalCenter.y;
			bodyPos.angle = originalState[i].angle;


			currBody.linearVelocity.x = originalState[i].linearVelocity.x + currBody.inv_mass* multiplier*(k1[i].force.x + 2f*(k2[i].force.x +
					k3[i].force.x) + k4[i].force.x);
			currBody.linearVelocity.y = originalState[i].linearVelocity.y + currBody.inv_mass* multiplier*(k1[i].force.y + 2f*(k2[i].force.y +
					k3[i].force.y) + k4[i].force.y);

			currBody.angularVelocity = originalState[i].angularVelocity + currBody.inv_Iz * multiplier * (k1[i].torque + 2f*(k2[i].torque +
					k3[i].torque) + k4[i].torque);

			currBody.linearVelocity.x -= currBody.linearVelocity.x*currBody.linearDamping;
			currBody.linearVelocity.y -= currBody.linearVelocity.y*currBody.linearDamping;

			currBody.angularVelocity -= currBody.angularVelocity*currBody.angularDamping;

			currBody = currBody.next;
			i++;
		}

	}



	public void integratePosition(TimeStep timeStep) {
		if(!inited)
			throw new RuntimeException("Integrator not yet initialized");

		Body firstBody = bodyList.firstBody;

		float time = timeStep.time;
		float step = timeStep.step;
		evaluateK1UpdateState(time,true);
		evaluate(time,firstBody,originalState, 0.5f*step, k1, k2);
		evaluate(time,firstBody,originalState, 0.5f*step, k2, k3);
		evaluate(time,firstBody,originalState, step, k3, k4);


		/*update state*/

		Body currBody = firstBody;
		int i=0;

		float multiplier = 1.0f * step/ 6.0f;

		while(currBody != null)
		{
			BodyPosition bodyPos = currBody.getBodyPosition();

			bodyPos.globalCenter.x = originalState[i].globalCenter.x + multiplier *(k1[i].linearVelocity.x + 2f*(k2[i].linearVelocity.x + 
					k3[i].linearVelocity.x) + k4[i].linearVelocity.x);
			bodyPos.globalCenter.y = originalState[i].globalCenter.y + multiplier *(k1[i].linearVelocity.y + 2f*(k2[i].linearVelocity.y + 
					k3[i].linearVelocity.y) + k4[i].linearVelocity.y);
			bodyPos.angle = originalState[i].angle +  multiplier * (k1[i].angularVelocity + 2f*(k2[i].angularVelocity +
					k3[i].angularVelocity) + k4[i].angularVelocity);


			currBody.linearVelocity.x = originalState[i].linearVelocity.x;
			currBody.linearVelocity.y = originalState[i].linearVelocity.y;

			currBody.angularVelocity = originalState[i].angularVelocity;


			currBody.synchronizeTransform();

			currBody = currBody.next;
			i++;
		}


	}




	private void evaluateK1UpdateState(float time,boolean updateOriginalState)
	{

		if(bodyList.bodiesNumber >= originalState.length)
			resizeArrays();

		/*original state and k1 derivative*/

		Body currBody = bodyList.firstBody;


		fManager.clearForces();
		fManager.applyForces(time);

		int i = 0;
		while(currBody != null)
		{
			if(updateOriginalState)
			{
				if(originalState[i] == null)
					originalState[i] = new BodyState();

				originalState[i].angle = currBody.getAngle();
				originalState[i].angularVelocity = currBody.angularVelocity;
				originalState[i].globalCenter.set(currBody.getGlobalCenter());
				originalState[i].linearVelocity.x =currBody.linearVelocity.x;
				originalState[i].linearVelocity.y =currBody.linearVelocity.y;
			}

			if(k1[i] == null)
				k1[i] = new Derivative();

			k1[i].angularVelocity = currBody.angularVelocity;
			k1[i].force.set(currBody.getForce());
			k1[i].linearVelocity.x =currBody.linearVelocity.x;
			k1[i].linearVelocity.y =currBody.linearVelocity.y;
			k1[i].torque = currBody.getTorque();


			currBody = currBody.next;
			i++;
		}
	}


	/**
	 * 
	 * http://gafferongames.com/game-physics/integration-basics/
	 * 
	 * @param firsBody currentState
	 * @param step
	 * @param derivativeInput
	 * @param derivativeOutput
	 */
	private void evaluate(float time,Body firstBody ,BodyState[] originalState,float step, Derivative[] derivativeInput, Derivative[] derivativeOutput)
	{

		Body currentBody = firstBody;
		int i=0;


		while(currentBody != null)
		{
			BodyPosition bodyPosition = currentBody.getBodyPosition();

			bodyPosition.globalCenter.x = originalState[i].globalCenter.x + derivativeInput[i].linearVelocity.x*step;
			bodyPosition.globalCenter.y = originalState[i].globalCenter.y + derivativeInput[i].linearVelocity.y*step;
			bodyPosition.angle = originalState[i].angle + derivativeInput[i].angularVelocity*step;

			float stepXInvMass = step*currentBody.inv_mass;
			Vec2 currLinearVelocity = currentBody.linearVelocity;
			currLinearVelocity.x = originalState[i].linearVelocity.x + derivativeInput[i].force.x*stepXInvMass;
			currLinearVelocity.y = originalState[i].linearVelocity.y + derivativeInput[i].force.y*stepXInvMass;

			//float stepXinvIz = step*currentBody.inv_Iz;

			currentBody.angularVelocity = originalState[i].angularVelocity + step*derivativeInput[i].torque*currentBody.inv_Iz;

			currentBody.synchronizeTransform();


			currentBody = currentBody.next;
			i++;
		}

		fManager.clearForces();
		fManager.applyForces(time+step);

		currentBody = firstBody;
		i = 0;

		while(currentBody != null)
		{

			if(derivativeOutput[i] == null)
				derivativeOutput[i] = new Derivative();

			derivativeOutput[i].angularVelocity = currentBody.angularVelocity;
			derivativeOutput[i].linearVelocity.x = currentBody.linearVelocity.x;
			derivativeOutput[i].linearVelocity.y = currentBody.linearVelocity.y;

			derivativeOutput[i].force.set(currentBody.getForce());
			derivativeOutput[i].torque = currentBody.getTorque();


			currentBody = currentBody.next;
			i++;

		}	


	}

}
