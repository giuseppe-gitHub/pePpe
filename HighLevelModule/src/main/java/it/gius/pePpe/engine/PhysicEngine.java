package it.gius.pePpe.engine;



import org.jbox2d.common.Vec2;

import it.gius.data.structures.IdDoubleArrayList;
import it.gius.data.structures.IdList;
import it.gius.pePpe.aabb.AABBUpdater;
import it.gius.pePpe.aabb.IAABBManager;
//import it.gius.pePpe.collision.ICollision;
import it.gius.pePpe.configuration.ConfigurationFactory;
//import it.gius.pePpe.constraints.NaiveCollisionRensponse;
import it.gius.pePpe.constraints.CollisionResponse;
import it.gius.pePpe.contact.Contact;
import it.gius.pePpe.contact.ContactManager;
//import it.gius.pePpe.data.GlobalData;
//import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.aabb.AABBPair;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindAABBNode;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.physic.Body;
import it.gius.pePpe.data.physic.BodyInit;
import it.gius.pePpe.data.physic.BodyList;
import it.gius.pePpe.data.physic.IGetAccesData;
import it.gius.pePpe.data.physic.IdGroupBinds;
import it.gius.pePpe.data.physic.PhysicClassAcces;
import it.gius.pePpe.data.tracers.ITracer;
//import it.gius.pePpe.distance.IDistance;
import it.gius.pePpe.distance.IDistance2;
import it.gius.pePpe.forces.ForceManager;
import it.gius.pePpe.forces.IForce;
import it.gius.pePpe.integrator.IDoubleStepIntegrator;
import it.gius.pePpe.integrator.ISingleStepIntegrator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.tracers.TracersManager;
import it.gius.utils.IdGenerator;
/**
 * 
 * @author giuseppe
 *
 * Facade
 */
public class PhysicEngine implements IGetAccesData{

	public static final int DEFAULT_ARRAY_SIZE = 100;

	//private GlobalData globalData;
	private IdList<BindAABBNode> globalShapes;
	private BodyList bodyList;
	
	private IdGenerator bodiesIdGenerator;

	private ISingleStepIntegrator singleStepIntegrator;
	private IDoubleStepIntegrator doubleStepIntegrator;

	//private IDistance distanceModule;
	private IDistance2 distanceModule2;
	private IAABBManager aabbManager;
	private AABBUpdater aabbManagerHandler;
	private ForceManager forceManager;
	private TracersManager tracersManager;
	private ContactManager contactManager;
	
	private CollisionResponse collisionResponse = new CollisionResponse();
	
	
	private boolean locked = false;

	private TimeStep timeStep;

	//private NaiveCollisionRensponse collisionRensponse;
	//AABBPair collisionPairs[] = new AABBPair[DEFAULT_ARRAY_SIZE];

	private IPreStep preStep = null;
	private IPostStep postStep = null;


	//public HashMap<Short, HashSetBind> bodyBindsMap;
	private IdGroupBinds bodyBinds;
	
	
	private PhysicClassAcces access = null;
	
	@Override
	public void setAccess(PhysicClassAcces access) {
		this.access = access;
	}


	public PhysicEngine() {
		//globalData = new GlobalData(DEFAULT_ARRAY_SIZE);
		globalShapes = new IdDoubleArrayList<BindAABBNode>(BindAABBNode.class, DEFAULT_ARRAY_SIZE,4);
		bodyList = new BodyList(); 
		bodiesIdGenerator = new IdGenerator();

		//bodyBindsMap = new HashMap<Short, HashSetBind>(Short.class, HashSetBind.class);
		bodyBinds = new IdGroupBinds();

		//confFactory = ConfigurationFactory.getDefaultInstance();

		//distanceModule = confFactory.getDefaultDistance();

		//aabbManager = confFactory.getDefaultAABBManager();

		timeStep = new TimeStep();
		forceManager = new ForceManager();
		tracersManager = new TracersManager();

		//singleStepIntegrator = new EulerIntegrator();
		//doubleStepIntegrator = new EulerIntegrator();

		//collisionRensponse = new NaiveCollisionRensponse();
	}


	private boolean inited = false;

	public void init(ConfigurationFactory confFactory) throws SimulatorException
	{
		if(inited)
			throw new RuntimeException("Engine already initialized");

		PhysicClassAcces.getIstance(this);
		//this.confFactory = confFactory;
		timeStep.time = 0;

		//distanceModule = confFactory.getDistance();
		distanceModule2 = confFactory.getDistance2();
		aabbManager = confFactory.getAABBManager();

		singleStepIntegrator = confFactory.getSingleStepIntegrator();
		doubleStepIntegrator = confFactory.getDoubleStepIntegrator();

		aabbManager.init(/*globalData.*/globalShapes);
		aabbManagerHandler = confFactory.getAAABBUpdater();//new AABBUpdater(aabbManager, globalShapes);
		aabbManagerHandler.init(globalShapes);
		//distanceModule.init(globalShapes);

		forceManager.init(bodyList);

		singleStepIntegrator.init(bodyList,forceManager);
		doubleStepIntegrator.init(bodyList,forceManager);

		//collisionRensponse.setEngine(this);
		//FullDistanceCollision collision = new FullDistanceCollision(distanceModule2, confFactory.getCollisionInit());
		//ICollision collision = confFactory.getCollision();
		contactManager = confFactory.getContactManager();//= new ContactManager(collision, confFactory.getContactManagerInit());
		contactManager.init(globalShapes);

		inited = true;
	}

	private boolean collisionFree = false;

	public boolean isCollisionFree() {
		return collisionFree;
	}

	public void setCollisionFree(boolean collisionFree) {
		this.collisionFree = collisionFree;
	}

	public ForceManager getForceManager() {
		return forceManager;
	}

	public ISingleStepIntegrator getSingleStepIntegrator() {
		return singleStepIntegrator;
	}

	public void setSingleStepIntegrator(ISingleStepIntegrator integrator) {
		if(inited)
			throw new RuntimeException("Engine already initialized!");

		this.singleStepIntegrator = integrator;
	}

	public IDoubleStepIntegrator getDoubleStepIntegrator() {
		return doubleStepIntegrator;
	}

	public void setDoubleStepIntegrator(
			IDoubleStepIntegrator doubleStepIntegrator) {
		if(inited)
			throw new RuntimeException("Engine already initialized!");

		this.doubleStepIntegrator = doubleStepIntegrator;
	}

	
	public Contact[] getContacts()
	{
		return contactManager.toLongerArray();
	}
	
	public int getContactSize()
	{
		return contactManager.size();
	}

	private BindAABBNode getNodefromBind(Bind bind)
	{
		BindAABBNode node = new BindAABBNode();
		node.bind = bind;
		node.box = new AABoundaryBox();

		//bind.getAABoundaryBox(node.box);
		aabbManagerHandler.fillAABB(node);

		return node;
	}

	public IdGroupBinds getBodyBindsReferences() {
		return bodyBinds;
	}
	
	/*public void distance(short idA,short idB, DistanceSolution sol)
	{
		distanceModule.distance(idA, idB, sol);
	}

	public void distance(short id, Vec2 q, DistanceSolution sol)
	{
		distanceModule.distance(id, q, sol);
	}

	public boolean overlap(short idA,short idB, OverlapSolution sol)
	{
		return distanceModule.overlap(idA, idB, sol);
	}*/

	public IDistance2 getDistanceModule()
	{
		return distanceModule2;
	}


	/*public GlobalData getGlobalData() {
		return globalData;
	}*/
	public IdList<BindAABBNode> getGlobalShapes()
	{
		return /*globalData.*/globalShapes;
	}

	public BodyList getBodyList()
	{
		return bodyList;
	}



	public void addForce(IForce force)
	{
		forceManager.addForce(force);
	}

	public void removeForce(IForce force)
	{
		forceManager.removeForce(force);
	}


	public IdList<IForce> getForcesList()
	{
		return forceManager.getForceList();
	}


	public Vec2 getGravity() {
		return forceManager.getGravity();
	}

	public float getGravityModule() {
		return forceManager.getGravityModule();
	}

	public void setGravity(Vec2 gravity) {
		forceManager.setGravity(gravity);
	}

	public void setGravity(float x, float y)
	{
		forceManager.setGravity(x, y);
	}


	public void addTracer(ITracer tracer)
	{
		tracersManager.addTracer(tracer);
	}

	public void removeTracer(ITracer tracer)
	{
		tracersManager.removeTracer(tracer);
	}



	public Body createBody(BodyInit bodyInit)
	{
		//TODO to test createBody

		if(!inited)
			throw new RuntimeException("Engine not yet initialized.");

		if(locked)
			return null;

		Body body = access.getNewBody(bodyInit);//new Body(bodyInit);


		if(bodyList.firstBody == null)
		{
			bodyList.firstBody = bodyList.lastBody = body;
			body.next = null;
			body.prev = null;
		}
		else
		{
			bodyList.lastBody.next = body;
			body.prev = bodyList.lastBody;
			bodyList.lastBody = body;
			body.next = null;
		}


		body.globalId = (short)bodiesIdGenerator.nextId();

		bodyList.bodiesNumber++;

		return body;
	}


	public Body removeBody(Body body)
	{
		//TODO test removeBody

		//body.removeListener(listener);
		if(!inited)
			throw new RuntimeException("Engine not yet initialized.");

		if(locked)
			return null;


		/*int size = body.forcesId.size();
		Short[] forceIds = null;

		//TDO solve weird dependency between forces and bodies
		if(size > 0)
			forceIds= body.forcesId.elements.clone();

		for(int i=0; i< size; i++)
		{
			forceManager.removeForce(forceIds[i]);
		}*/
		forceManager.removingBody(body.globalId);



		int size = body.phShapeList.size();

		//Bind[] bindArray = bodyBindsMap.get(body.globalId).elements;//body.phShapeList.toLongerArray();
		Bind[] bindArray = bodyBinds.toLongerArray(body.globalId);
		//PhysicShape[] phShapeArray =body.phShapeList.toLongerArray();

		for(int j =0; j < size; j++)
		{
			Bind bind = bindArray[j];
			//PhysicShape phShape = phShapeArray[j];
			bind.removed = true;

			BindAABBNode node = globalShapes.get(/*phShape.globalBindId*/bind.globalId);	

			aabbManager.removeAABB(node.box);

			//distanceModule.remove(node.id);

			globalShapes.remove(node.id);

		}

		if(body == bodyList.firstBody)
			bodyList.firstBody = body.next;

		if(body == bodyList.lastBody)
			bodyList.lastBody = body.prev;

		if(body.prev != null)
			body.prev.next = body.next;

		if(body.next != null)
			body.next.prev = body.prev;
		
		
		//bodyBindsMap.remove(body.globalId);
		bodyBinds.removeGroup(body.globalId);

		bodyList.bodiesNumber--;

		return body;
	}



	public Bind createBind(BindInit bindInit)
	{
		//TODO to test createBind
		if(!inited)
			throw new RuntimeException("Engine not yet initialized.");

		if(locked)
			return null;

		Bind bind = access.getNewBind(bindInit);//new Bind(bindInit);
		bind.removed = false;
		Body body = bindInit.body;

		//short localBodyId = body.addBind(bind);
		short localBodyId = access.addPhysicShapeToBody(body, bind.phShape);//body.addPhysicShape(bind.phShape);

		//bind.localBodyId = localBodyId;
		bind.phShape.localBodyId = localBodyId;

		//ShapesListNode slNode = getShapeListNodeFromBind(bind);
		BindAABBNode node = getNodefromBind(bind);

		globalShapes.add(node);

		aabbManager.addAABB(node.box, node.id);

		//bind.phShape.globalBindId = bind.globalId;


		/*HashSetBind bindSet = bodyBindsMap.get(body.globalId);

		if(bindSet == null)
		{
			bindSet = new HashSetBind();
			bodyBindsMap.put(body.globalId, bindSet);
		}

		bindSet.add(bind);*/
		bodyBinds.put(body.globalId, bind);

		return bind;
	}


	public Bind removeBind(Bind bind)
	{
		//TODO to test removeBind
		if(!inited)
			throw new RuntimeException("Engine not yet initialized.");

		if(locked)
			return null;

		Body body = bind.body;

		/*if(!body.removeBind(bind))
			throw new IllegalAccessError();*/
		if(!access.removePhysicShapeFromBody(body, bind.phShape)/*!body.removePhysicShape(bind.phShape)*/)
			throw new RuntimeException("Requested to remove a Bind not linked to the given Body");

		
		/*HashSetBind bindSet = bodyBindsMap.get(body.globalId);

		if(bindSet == null)
			throw new RuntimeException();

		bindSet.remove(bind);*/
		bodyBinds.remove(body.globalId, bind);

		
		
		forceManager.updatingBody(body.globalId);

		BindAABBNode node = globalShapes.get(bind.globalId);

		aabbManager.removeAABB(node.box);

		//distanceModule.remove(node.id);

		globalShapes.remove(node.id);
		
		bind.removed = true;

		return bind;
	}


	public boolean isLocked() {
		return locked;
	}

	/*public void setLocked(boolean locked) {
		this.locked = locked;
	}*/

	public void setPostStep(IPostStep postStep) {
		this.postStep = postStep;
	}

	public void setPreStep(IPreStep preStep) {
		this.preStep = preStep;
	}


	public float step(float step)
	{

		timeStep.step = step;
		if(preStep != null)
			preStep.preStep(timeStep);

		locked = true;
		
		aabbManager.updatePairs();

		tracersManager.trace(timeStep.time);

		
		if(!collisionFree)
		{
			AABBPair[] pairs = aabbManager.getPairs();
			int aabbSize = aabbManager.getPairsNumber();
			contactManager.updateContacts(pairs,aabbSize);

			doubleStepIntegrator.integrateVelocity(timeStep);

			//collisionRensponse.react(collisionPairs, collSize);
			//TODO collision response
			Contact[] contacts = contactManager.toLongerArray();
			int contactsNum = contactManager.size();
			collisionResponse.solveVelocity(contacts, contactsNum);
			
			doubleStepIntegrator.integratePosition(timeStep);
		}
		else
			singleStepIntegrator.step(timeStep);

		
		aabbManagerHandler.updateAllAABB();
		/*aabbManager.updatePairs();
		AABBPair[] pairs = aabbManager.getPairs();
		int aabbSize = aabbManager.getPairsNumber();
		contactManager.updateContacts(pairs,aabbSize);*/


		locked = false;

		/* Calculating contacts before updating position give the user inaccurate contacts in the postStep.
		 * No easy solution found. If contacts would be calculated at the end of the step (n) and would be 
		 * passed to the constraint solver during the step (n+1), the contact generation wouldn't keep track 
		 * of the bodies and binds added (or removed) between (n) and (n+1)
		 * Generating contacts 2 times in a step (before and after changing positions) would solve both problems,
		 * but it would (highly?) worsen the performance.
		 * */
		if(postStep != null)
			postStep.postStep(timeStep);

		timeStep.time = timeStep.time + step;

		return timeStep.time;
	}


}
