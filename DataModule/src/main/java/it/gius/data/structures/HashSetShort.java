package it.gius.data.structures;

public class HashSetShort extends HashSet<Short> {
	
	public HashSetShort() {
		super(Short.class);
	}
	
	@Override
	protected int getHash(Short t) {
		
		int key = t;
		
		key += ~(key << 15);
		key ^=  (key >> 10);
		key +=  (key << 3);
		key ^=  (key >> 6);
		key += ~(key << 11);
		key ^=  (key >> 16);
		return key & mask;
	}
	


}
