package it.gius.pePpe.drawer;

import java.util.HashSet;
import java.util.Set;


public class DrawerSet extends AbstractDrawer{

	private Set<IDrawer> drawers = null;

	private IDrawer[] arrayDrawers = null;


	public void setDrawers(IDrawer[] drawers) {
		this.drawers = new HashSet<IDrawer>();

		for(int i=0; i< drawers.length; i++)
			this.drawers.add(drawers[i]);

		if(this.drawers.size() > 0)
		{
			this.arrayDrawers = new IDrawer[this.drawers.size()];
			this.arrayDrawers = this.drawers.toArray(arrayDrawers);
		}
		else
			this.arrayDrawers = null;
	}

	public boolean contains(IDrawer drawer)
	{
		return drawers.contains(drawer);
	}

	public void addDrawer(IDrawer drawer)
	{
		if(this.drawers == null)
			this.drawers = new HashSet<IDrawer>();

		this.drawers.add(drawer);

		if(this.drawers.size() > 0)
		{
			this.arrayDrawers = new IDrawer[this.drawers.size()];
			this.arrayDrawers = this.drawers.toArray(arrayDrawers);
		}
		else
			this.arrayDrawers = null;
	}

	public void removeDrawer(IDrawer drawer)
	{
		if(this.drawers == null)
			return;

		this.drawers.remove(drawer);

		if(this.drawers.size() > 0)
		{
			this.arrayDrawers = new IDrawer[this.drawers.size()];
			this.arrayDrawers = this.drawers.toArray(arrayDrawers);
		}
		else
			this.arrayDrawers = null;
	}

	@Override
	public String getName() {

		return "drawerSet";
	}

	private int i;

	@Override
	public void draw() {

		if(arrayDrawers != null)
		{
			for(i =0; i< arrayDrawers.length; i++)
				arrayDrawers[i].draw();
		}

	}


}
