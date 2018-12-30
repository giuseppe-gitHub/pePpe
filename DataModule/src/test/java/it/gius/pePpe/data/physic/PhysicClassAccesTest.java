package it.gius.pePpe.data.physic;

import junit.framework.TestCase;

public class PhysicClassAccesTest extends TestCase implements IGetAccesData {

	public void testGetIstance() {
		PhysicClassAcces.getIstance(this);
		
		if(access == null)
			fail();
	}
	
	
	private PhysicClassAcces access = null;
	
	@Override
	public void setAccess(PhysicClassAcces access) {
		this.access = access;
		
	}

}
