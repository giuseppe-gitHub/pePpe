package it.gius.pePpe.testSuit.property;

/**
 * Class holding the properties of the simulation
 * It must has a constructor without argument. 
 * @author giuseppe
 *
 */
public interface IProperties {
	
	/**
	 * Set the instance values to their default.
	 * It can be used by the system to initialize a new (or internal) instance.
	 */
	public void toDefaultValues();

}
