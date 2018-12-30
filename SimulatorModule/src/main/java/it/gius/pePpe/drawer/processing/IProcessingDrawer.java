package it.gius.pePpe.drawer.processing;

import it.gius.pePpe.drawer.DrawerProperties;
import it.gius.pePpe.drawer.IExternalDrawer;
import it.gius.pePpe.engine.PhysicEngine;
import it.gius.processing.util.MyAbstractPApplet;

public interface IProcessingDrawer extends IExternalDrawer {

	public void setApplet(MyAbstractPApplet applet);
	
	public void setEngine(PhysicEngine engine);
	
	public void setDrawerProperties(DrawerProperties drawerProperties);
	
}
