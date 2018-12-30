package it.gius.pePpe.mains;


public class HashValueTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//long l = Long.MAX_VALUE;
		
		//Integer[] array = new Integer[l];
		
		
		System.out.println(Long.SIZE);
		System.out.println(Integer.SIZE);
		System.out.println(Short.SIZE);
		
		System.out.println(Short.MAX_VALUE + " " + Short.MIN_VALUE);
		
		int x = Integer.MAX_VALUE;
		System.out.println(x);
		
		short y = (short)x;
		System.out.println(y);
		
		short a = 34, b= 34*34, c = 34*34*2;
		
		int activePair = 2;
		int mask = nextPowerOfTwo(activePair);
		System.out.println("mask: " +mask);
		mask--;
		
		int first = hashValue(a, b) & mask;
		int second = hashValue(c, b) & mask;
		
		System.out.println("first: " + first);
		System.out.println("second: " + second);
	}
	
	public static int hashValue(short idA,short idB)
	{
		//int key = ((int)idA) | ( ((int)idB) <<Short.SIZE);
		int key = cantorPairing(idA, idB);
		
		return hash32Bits(key);
	}
	
	public static int cantorPairing(short idA,short idB)
	{
		int sum = idA + idB;
		return ((sum)*(sum +1)/2 +idB);
	}
	
	//Thomas Wang hash
	 private static int hash32Bits(int key)
	{
		key += ~(key << 15);
		key ^=  (key >> 10);
		key +=  (key << 3);
		key ^=  (key >> 6);
		key += ~(key << 11);
		key ^=  (key >> 16);
		return key;
	}
	 
		private static int nextPowerOfTwo(int x)
		{
			x |= (x >> 1);
			x |= (x >> 2);
			x |= (x >> 4);
			x |= (x >> 8);
			x |= (x >> 16);
			return x+1;
		}

}
