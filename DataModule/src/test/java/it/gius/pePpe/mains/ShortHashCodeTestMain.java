package it.gius.pePpe.mains;

public class ShortHashCodeTestMain {

	
	public static void main(String[] args)
	{
		Short x = 1;
		Short y = 101;
		Short z = 30021;
		Short a = 543;
		
		int mask = 7;
		
		System.out.println(x.hashCode() & mask);
		System.out.println(y.hashCode() & mask);
		System.out.println(z.hashCode() & mask);
		System.out.println(a.hashCode() & mask);
		
		System.out.println();
		System.out.println(hashCode(x) & mask);
		System.out.println(hashCode(y) & mask);
		System.out.println(hashCode(z) & mask);
		System.out.println(hashCode(a) & mask);
	}
	
	
	//Thomas Wang Hash (cuttable hash for little HashTable)
	public static int hashCode(short s) {
		
		int key = (int)s; 
		
		key += ~(key << 15);
		key ^=  (key >> 10);
		key +=  (key << 3);
		key ^=  (key >> 6);
		key += ~(key << 11);
		key ^=  (key >> 16);
		return key;
	}
}
