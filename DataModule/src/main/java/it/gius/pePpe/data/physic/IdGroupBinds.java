package it.gius.pePpe.data.physic;

import it.gius.data.structures.HashMap;

public class IdGroupBinds {
	
	//TODO to test
	
	private HashMap<Short, HashSetBind> idGroupBindsMap;
	
	public IdGroupBinds() {
		idGroupBindsMap = new HashMap<Short, HashSetBind>(Short.class, HashSetBind.class);
	}
	
	
	public void put(short groupId, Bind bind)
	{
		HashSetBind bindSet = idGroupBindsMap.get(groupId);
		
		if(bindSet == null)
		{
			bindSet = new HashSetBind();
			idGroupBindsMap.put(groupId, bindSet);
		}
		
		bindSet.add(bind);
	}
	
	public void removeGroup(short idGroup)
	{
		idGroupBindsMap.remove(idGroup);
	}
	
	public Bind remove(short groupId, Bind bind)
	{
		HashSetBind bindSet = idGroupBindsMap.get(groupId);
		
		if(bindSet == null)
			return null;
		
		if(bindSet.remove(bind))
			return bind;
		else
			return null;
	}
	
	public Bind[] toLongerArray(short groupId)
	{
		HashSetBind bindSet = idGroupBindsMap.get(groupId);
		
		if(bindSet != null)
			return bindSet.elements;
		
		return null;
	}

	public int groupSize(short groupId)
	{
		HashSetBind bindSet = idGroupBindsMap.get(groupId);
		
		if(bindSet != null)
			return bindSet.size();
		
		return 0;
	}
}
