package it.gius.pePpe.mains;

import it.gius.pePpe.aabb.IAABBManager;
import it.gius.pePpe.configuration.ConfigurationFactory;

public class SpringConfTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConfigurationFactory conFactory1 = new ConfigurationFactory();
		ConfigurationFactory conFactory2 = new ConfigurationFactory();
		
		try {
			conFactory1.init();
			conFactory2.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		IAABBManager manager1 = conFactory1.getAABBManager();
		IAABBManager manager2 = conFactory2.getAABBManager();
		
		System.out.println(manager1);
		System.out.println(manager2);
		
		if(manager1 == manager2)
			System.out.println("Same instance");
		else
			System.out.println("Different instances");

	}

}
