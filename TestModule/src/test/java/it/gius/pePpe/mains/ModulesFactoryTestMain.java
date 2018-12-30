package it.gius.pePpe.mains;




import it.gius.pePpe.configuration.ConfigurationFactory;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.pePpe.simulator.IDrawContext;
import it.gius.pePpe.simulator.ISimulator;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;

public class ModulesFactoryTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConfigurationFactory factory = new ConfigurationFactory();
		
		try {
			factory.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		ISimulator simulator = factory.getSimulator();
		
		PhysicEngine engine = new PhysicEngine();
		SimulationInfo info = new SimulationInfo();
		
		try {
			simulator.init(engine, info);
			IDrawContext context = simulator.getDrawContext();
			String[] names = context.drawerSet();
			
			for(String name: names)
			{
				boolean enable =context.isActive(name);
				if(enable)
					System.out.println("Drawer: " + name + " is enable");
				else
					System.out.println("Drawer: " + name + " is not enable");
				
			}
			
		} catch (SimulatorException e) {
			e.printStackTrace();
		}

	}

}
