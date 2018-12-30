package it.gius.pePpe.drawer;

import java.util.ArrayList;
import java.util.List;

import it.gius.pePpe.render.IRenderer;
import it.gius.pePpe.simulator.IRenderable;


public class DefaultRenderer extends AbstractDrawer implements IRenderer {

	private IRenderable[] renderables = new IRenderable[0];
	private List<IRenderable> listRenderable = null;
	
	public DefaultRenderer() {
		listRenderable = new ArrayList<IRenderable>();
				
	}
	
	public void addRenderable(IRenderable renderable)
	{
		listRenderable.add(renderable);
		renderables = listRenderable.toArray(renderables);
	}
	
	public void removeRenderable(IRenderable renderable)
	{
		if(listRenderable.remove(renderable))
			renderables = listRenderable.toArray(renderables);
	}
	
	
	@Override
	public String getName() {
		return "Default Renderer";
	}

	private int i,n;
	
	@Override
	public void draw() {
		
		n = listRenderable.size();
		for(i =0; i< n; i++)
			renderables[i].render();
	}

}
