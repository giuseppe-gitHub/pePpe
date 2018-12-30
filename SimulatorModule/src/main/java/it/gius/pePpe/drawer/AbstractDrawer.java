package it.gius.pePpe.drawer;

public abstract class AbstractDrawer implements IDrawer {

	protected boolean enable = false;
	

	public abstract String getName();

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable = enable;
	}



	public abstract void draw();

}
