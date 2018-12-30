package it.gius.pePpe.contact;

import it.gius.pePpe.data.cache.CollisionCache;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.manifold.ContactManifold;

public class Contact {

	public Bind bindA = null;
	public Bind bindB = null;
	
	public boolean neww; //true if new or updated during this step
	
	public CollisionCache cache;
	
	public ContactManifold manifold;
	
	public Contact() {
		manifold = new ContactManifold();
		
		cache = new CollisionCache();
	}
	
	public Contact(boolean caching)
	{
		manifold = new ContactManifold();
		
		if(caching)
			cache = new CollisionCache();
		else
			cache = null;
	}
	
}
