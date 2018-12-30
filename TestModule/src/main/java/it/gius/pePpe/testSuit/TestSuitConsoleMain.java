package it.gius.pePpe.testSuit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import org.apache.log4j.PropertyConfigurator;

import it.gius.data.structures.HashSet;
import it.gius.pePpe.simulator.SimulatorException;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.SimulationInfo.SimulationType;
import it.gius.pePpe.testSuit.configurationInit.ConfigurationException;
import it.gius.pePpe.testSuit.configurationInit.FileConFactoryInit;
import it.gius.pePpe.testSuit.property.IProperties;
import it.gius.pePpe.testSuit.propertyGui.GuiPropertiesException;
import it.gius.pePpe.testSuit.propertyGui.GuiPropertySet;





public class TestSuitConsoleMain {


	private static HashSet<ISimulation> setSimulations;
	private static SimulationLoader searcher = new SimulationLoader();
	private static SimulationStarter starter = new SimulationStarter();
	private static FileConFactoryInit conFactoryInit = new FileConFactoryInit();
	private static BufferedReader reader = null;

	public static void main(String[] args) {

		initLogger();

		reader = new BufferedReader(new InputStreamReader(System.in));

		searcher.init();


		/*try {
			searcher.addJar("C:/Users/giuseppe/Documents/pePpe-simulations/pePpeSimulationsTest.jar");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		updateSetSimulation();
		list();
		System.out.print("Select simulation (with number): ");
		String choiche = null;
		try {
			while((choiche = reader.readLine()) != null)
			{
				if(choiche.compareTo("exit") == 0)
					break;

				int value = -1;
				try {
					value = Integer.valueOf(choiche);
				} catch (NumberFormatException e1) {
				}

				if(value <= 0 || value > setSimulations.size())
				{
					System.out.println("Wrong input");
				}
				else
				{
					value--;
					//retrieving the requested simulation from the array
					ISimulation sim = setSimulations.elements[value];
					//reset all simulation data
					sim.clear();
					//set SimulationInfo (from console if required)
					setSimInfo(sim);

					//set properties of this simulation
					setPropertiesBean(sim);

					try {
						starter.startSimulation(sim, conFactoryInit, simInfo);
						System.out.println("Simulation started");
						Thread.sleep(1500);
					} catch (ConfigurationException e) {
						e.printStackTrace();
					} catch (SimulatorException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}


				}

				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println();
				list();
				System.out.print("Select simulation (with number): ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println("Closing console");


		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static void initLogger()
	{
		URL configURL = Thread.currentThread().getContextClassLoader().getResource("log4jConsoleMain.properties");
		PropertyConfigurator.configure(configURL);

	}

	private static void updateSetSimulation()
	{
		if(setSimulations == null)
			setSimulations = new HashSet<ISimulation>(ISimulation.class);
		else
			setSimulations.clear();

		searcher.search();

		for(ISimulation simulation: searcher)
			setSimulations.add(simulation);

	}

	private static void list()
	{

		for(int i= 0; i<setSimulations.size(); i++)
		{
			int value = i+1;
			ISimulation simulation = setSimulations.elements[i];
			System.out.println(value + ") " + simulation.getName() +", description: " + simulation.getDescription());
		}

	}

	private static SimulationInfo simInfo = new SimulationInfo();

	private static void setSimInfo(ISimulation simulation)
	{

		simInfo.set(simulation.defaultSimulationInfo());

		String choiche = null;

		try {
			System.out.print("Change default simulation info (y,n): ");
			choiche = reader.readLine();

			if(choiche.length() >=1 && choiche.charAt(0) == 'n')
				return;

			//simulation step configuration
			System.out.print("Simulation step: ");
			choiche = reader.readLine();
			try {
				float simStep = Float.valueOf(choiche);
				simInfo.simulationStep = simStep;
			} catch (NumberFormatException e1) {
				System.out.println("Wrong input. Using default step");
			}

			//simulation type configuration
			System.out.print("Simulation type ('t': time driven, 'u': user driven): ");
			choiche = reader.readLine();
			if(choiche.length() >= 1)
			{
				char type = choiche.charAt(0);

				if(type == 't')
					simInfo.type = SimulationType.TIME_DRIVEN;

				if(type == 'u')
					simInfo.type = SimulationType.USER_DRIVEN;

				if(type != 'u' && type != 't')
					System.out.println("Wrong input. Using default type");
			}
			else
				System.out.println("Wrong input. Using default type");


			//simulation type configuration
			if(simInfo.type == SimulationType.TIME_DRIVEN)
			{
				System.out.print("Simulation time: ");
				choiche = reader.readLine();
				try {
					float simTime = Float.valueOf(choiche);
					simInfo.simulationTime = simTime;
				} catch (NumberFormatException e1) {
					System.out.println("Wrong input. Using default max time");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error in simulation info setting. Using default values");
			simInfo.set(simulation.defaultSimulationInfo());
		}

	}

	private static GuiPropertySet gps = new GuiPropertySet();

	private static void setPropertiesBean(ISimulation simulation)
	{
		IProperties propBean = simulation.getProperties();
		if(propBean != null)
		{
			Semaphore semaphore = new Semaphore(0);
			System.out.println("Initializing view to set the simulation's properties");
			try {
				JFrame frame = gps.setPropertyWithGui(simulation.getName(), 100,100, simulation.getProperties(), true);
				SyncConsoleWindowListener listener = new SyncConsoleWindowListener(semaphore);
				frame.addWindowListener(listener);
			} catch (GuiPropertiesException e) {
				System.out.println("Failed to set properties with view");
			}
			
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class SyncConsoleWindowListener extends WindowAdapter
	{
		private Semaphore semaphore;
		
		public SyncConsoleWindowListener(Semaphore semaphore) {
			this.semaphore = semaphore;
		
		}
		
		
		
		@Override
		public void windowClosed(WindowEvent e) {
			try {
				semaphore.release();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
	}


}
