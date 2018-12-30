package it.gius.data.structures;



import java.lang.reflect.Array;

public class HashMap<K,V> {

	public static final int NULL_NODE = -1;

	private int hashTable[];

	private static class ListElement
	{
		public int prev;
		public int next;

		@Override
		public String toString() {
			return "("+prev +", "+ next + ")";
		}
	}

	private ListElement[] lists;

	protected int mask;

	private  K[] keys;
	private V[] values;
	private Class<K> kClass;
	private Class<V> vClass;
	private int elementsNumber;




	public HashMap(Class<K> kClass, Class<V> vClass) {
		this.kClass = kClass;
		this.vClass = vClass;
		mask = 0;
		elementsNumber = 0;

		hashTable = null;
		lists = null;
		keys = null;
		values = null;
	}

	public void clear()
	{
		elementsNumber = 0;
		mask = 0;
		hashTable = null;
		lists = null;
		keys = null;
		values = null;
	}

	@SuppressWarnings("unchecked")
	private void resize()
	{
		int newHashSize;

		if(elementsNumber < 32)
			newHashSize = nextPowerOfTwo(2*elementsNumber+1);
		else
			newHashSize = nextPowerOfTwo(elementsNumber+1);

		int[] newHashTable = new int[newHashSize];

		mask = newHashSize-1; //0...000111...1

		ListElement[] newLists = new ListElement[newHashSize];

		for(int i=0; i< newHashTable.length;i++)
		{
			newHashTable[i]= NULL_NODE;
			newLists[i] = new ListElement();
			newLists[i].prev =NULL_NODE;
			newLists[i].next =NULL_NODE;
		}


		K[] newKeys = null;
		V[] newValues = null;
		try {
			newKeys = (K[])Array.newInstance(kClass, newHashSize);
			newValues = (V[])Array.newInstance(vClass, newHashSize);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		for(int i=0; i< elementsNumber; i++)
		{
			newKeys[i] = keys[i];
			newValues[i] = values[i];
			//int hash = elements[i].hashCode() & mask;
			int hash = getHash(keys[i]);
			int j = newHashTable[hash];
			newLists[i].next = j;

			if(j != NULL_NODE)
				newLists[j].prev = i;

			newHashTable[hash] = i;
		}

		keys = newKeys;
		values = newValues;
		hashTable = newHashTable;
		lists = newLists;

	}



	private int nextPowerOfTwo(int x)
	{
		x |= (x >> 1);
		x |= (x >> 2);
		x |= (x >> 4);
		x |= (x >> 8);
		x |= (x >> 16);
		return x+1;
	}


	protected int getHash(K t)
	{
		return t.hashCode() & mask;
	}


	public V get(K t)
	{
		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		return get(t, hash);
	}
	
	public boolean containsKey(K key)
	{
		int index = getIndex(key);
		
		return (index != NULL_NODE);
	}

	private V get(K t,int hash)
	{
		if(hashTable == null)
			return null;

		int index = hashTable[hash];
		for(;index!= NULL_NODE && !keys[index].equals(t); index = lists[index].next);

		if(index == NULL_NODE)
			return null;

		return values[index];
	}



	public V put(K key,V value)
	{

		//int hash = t.hashCode() & mask;
		int hash = getHash(key);
		int index = getIndex(key, hash);

		if(index != NULL_NODE)
		{
			V oldValue = values[index];
			values[index] = value;
			
			return oldValue;
		}

		if(hashTable == null || elementsNumber>=hashTable.length)
		{
			resize();
			//recompute hash with new mask
			//hash = t.hashCode() & mask;
			hash = getHash(key);
		}


		keys[elementsNumber] = key;
		values[elementsNumber] = value;

		int j = hashTable[hash];
		lists[elementsNumber].next = j;

		if(j != NULL_NODE)
			lists[j].prev = elementsNumber;

		hashTable[hash] = elementsNumber;

		elementsNumber++;

		return null;
	}


	public V remove(K t)
	{
		if(hashTable == null)
			return null;

		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		int index = getIndex(t, hash);

		if(index == NULL_NODE)
			return null;

		int prev = lists[index].prev;
		int next = lists[index].next;

		//cutting element from the structures
		if(prev != NULL_NODE)
			lists[prev].next = next;
		else
			hashTable[hash] = next;

		if(next != NULL_NODE)
			lists[next].prev = prev;

		//sub lastElement with the removed one, also changing all the structures
		int lastIndex = elementsNumber-1;


		if(lastIndex == index)
		{
			lists[index].next = NULL_NODE;
			lists[index].prev = NULL_NODE;
			keys[index] = null;
			V oldValue = values[index];
			values[index] = null;
			elementsNumber--;
			return oldValue;
		}

		K lastKey = keys[lastIndex];
		V lastValue = values[lastIndex];
		//int lastHash = last.hashCode() & mask;
		int lastHash = getHash(lastKey);

		keys[index] = lastKey;
		V oldValue = values[index];
		values[index] = lastValue;
		keys[lastIndex] = null;
		values[lastIndex] = null;


		int lastPrev = lists[lastIndex].prev;
		int lastNext = lists[lastIndex].next;

		lists[lastIndex].prev = NULL_NODE;
		lists[lastIndex].next = NULL_NODE;

		//cut last from his list to re-add it to the start of the list
		//cut
		if(lastPrev != NULL_NODE)
			lists[lastPrev].next = lastNext;
		else
			hashTable[lastHash] = lastNext;

		if(lastNext != NULL_NODE)
			lists[lastNext].prev = lastPrev;

		//re-adding to the start
		lists[index].prev = NULL_NODE;
		int j = hashTable[lastHash];
		lists[index].next = j;

		if(j != NULL_NODE)
			lists[j].prev =index;

		hashTable[lastHash] = index;

		elementsNumber--;

		return oldValue;
	}

	public K find(K key)
	{
		int index = getIndex(key);

		if(index == NULL_NODE)
			return null;

		return keys[index];
	}


	public int getIndex(K t)
	{
		//int hash = t.hashCode() & mask;
		int hash = getHash(t);
		return getIndex(t, hash);
	}

	private int getIndex(K t, int hash)
	{
		if(hashTable == null)
			return NULL_NODE;

		int index = hashTable[hash];
		for(;index!= NULL_NODE && !keys[index].equals(t); index = lists[index].next);


		return index;
	}


	public int size()
	{
		return elementsNumber;
	}
	
	public K[] keyLongerArray()
	{
		return keys;
	}
	
	public V[] valuesLongerArray()
	{
		return values;
	}

}

