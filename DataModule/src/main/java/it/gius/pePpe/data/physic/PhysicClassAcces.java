package it.gius.pePpe.data.physic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class PhysicClassAcces {
	
	private PhysicClassAcces() {
		
	}
	
	private static Set<String> classes;
	
	private static class PathHelper
	{}
	
	private static boolean testMode;
	
	static{
		
		classes = new HashSet<String>();
		try {
			
			//File file = new File("PhysicDataAccess");
			//FileInputStream fIn = new FileInputStream(file);
			InputStreamReader streamReader = new InputStreamReader(PathHelper.class.getClassLoader().getResourceAsStream("PhysicDataAccess"));
			BufferedReader reader = new BufferedReader(streamReader);
			String className;
			while( (className = reader.readLine()) != null)
				classes.add(className);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testMode = false;
		
		String testModeString = System.getProperty("PhysicDataTestMode");
		if(testModeString != null)
			testMode = true;
	}
	
	
	public static void getIstance(IGetAccesData user) throws RuntimeException
	{
		if(testMode)
			throw new RuntimeException("Trying using normal access in test mode");
		
		if(!authenticate(user))
			throw new RuntimeException("Failed to authenticate");
		
		user.setAccess(new PhysicClassAcces());
	}
	
	
	private static boolean authenticate(IGetAccesData user)
	{
		String canName = user.getClass().getCanonicalName();
		if(classes.contains(canName))
			return true;
		else
			return false;
	}
	
	private static PhysicClassAcces testIstance = new PhysicClassAcces();
	
	public static PhysicClassAcces getTestIstance()
	{
		if(!testMode)
			throw new RuntimeException("Not in test mode");
		
		return testIstance;
	}
	
	public Body getNewBody(BodyInit bodyInit)
	{
		return new Body(bodyInit);
	}
	
	public Bind getNewBind(BindInit bindInit)
	{
	
		return new Bind(bindInit);
	}
	
	public short addPhysicShapeToBody(Body body, PhysicShape phShape)
	{
		return body.addPhysicShape(phShape);
	}
	
	public boolean removePhysicShapeFromBody(Body body, PhysicShape phShape)
	{
		return body.removePhysicShape(phShape);
	}
	

}
