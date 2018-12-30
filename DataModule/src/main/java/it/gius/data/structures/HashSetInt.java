package it.gius.data.structures;

public class HashSetInt extends HashSet<Integer> {
	
	
	public HashSetInt() {
		super(Integer.class);
	}
	
	@Override
	protected int getHash(Integer t) {
		
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
