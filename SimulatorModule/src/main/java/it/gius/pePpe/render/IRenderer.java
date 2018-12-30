package it.gius.pePpe.render;

import it.gius.pePpe.drawer.IDrawer;
import it.gius.pePpe.simulator.IRenderable;

public interface IRenderer extends IDrawer {


	public void addRenderable(IRenderable renderable);
	
	public void removeRenderable(IRenderable renderable);
}
