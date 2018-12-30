package it.gius.pePpe.simulator;

/**
 * 
 * @author giuseppe
 * @opt all
 */
public interface IDrawContext {
	
	public String info();
	
	public String[] drawerSet();
	
	public Brush getBrush();
	
	public void addRenderable(IRenderable renderable);
	public void removeRenderable(IRenderable renderable);
	
	public boolean exist(String drawer);
	
	public boolean isActive(String drawer) throws SimulatorException;
	
	public void activate(String drawer) throws SimulatorException;
	public void disactivate(String drawer) throws SimulatorException;

}
