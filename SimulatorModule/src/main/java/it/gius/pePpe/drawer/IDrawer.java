package it.gius.pePpe.drawer;


public interface IDrawer {
	
	public abstract String getName();
	
	public boolean isEnable();
	
	public void setEnable(boolean enable);
	
	public void draw();
}
