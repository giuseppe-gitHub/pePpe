package it.gius.pePpe.drawer.processing;

import it.gius.pePpe.drawer.AbstractDrawer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.processing.util.MyAbstractPApplet;

public abstract class AbstractProcessingDrawer extends AbstractDrawer implements IProcessingDrawer {

	protected String name;
	protected MyAbstractPApplet applet;
	protected PhysicEngine engine;
	
	protected int defaultHexAlpha = 0xFF000000;
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public abstract void draw();

	@Override
	public void setApplet(MyAbstractPApplet applet) {
		this.applet = applet;

	}

	@Override
	public void setEngine(PhysicEngine engine) {
		this.engine = engine;

	}

}
