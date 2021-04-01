package hash;

import java.util.Iterator;

import linkedListDataStructure.LinkedList2;

public class Hash<K, V> implements Iterable<K>{
	
	private class HashElement<K, V> implements Comparable<HashElement<K, V>> {
		K key;
		V value;
		
		public HashElement(K key, V value) {
			this.key = key;
			this.value = value;
		}

		
		@SuppressWarnings("unchecked")
		@Override
		public int compareTo(HashElement<K, V> o) {
			return ((Comparable<K>)this.key).compareTo(o.key);
		}
	}
	
	private class IteratorHelper<T> implements Iterator<T>{

		T[] keys;
		int position;
		
		public IteratorHelper() {
			keys = (T[]) new Object[numElement];
			int p = 0;
			for(int i = 0; i < tableSize; i++) {
				for(HashElement<K, V> he : array[i]) {
					keys[p++] = (T) he.key;
				}
			}
			position = 0;
		}
		
		
		@Override
		public boolean hasNext() {
			return position < keys.length;
		}

		@Override
		public T next() {
			if(hasNext()) {
				return keys[position++];
			} else return null;
		}
		
	}
	
	LinkedList2<HashElement<K, V>>[] array;
	int numElement, tableSize;
	double maxLoadFactor;
	
	public Hash(int tableSize) {
		this.tableSize = tableSize;
		array = (LinkedList2<HashElement<K, V>>[])new LinkedList2[tableSize];
		for(int i = 0; i < tableSize; i++) {
			array[i] = new LinkedList2<HashElement<K, V>>();
		}
		numElement = 0;
		maxLoadFactor = 0.75;
	}
	
	public boolean add(K key, V value) {
		if(loadFactor() > maxLoadFactor) {
			resize(tableSize * 2);
		}
		HashElement<K, V> he = new HashElement<>(key, value);
		int hashIndex = (key.hashCode() & 0x7FFFFFFF) % tableSize;
		array[hashIndex].addFirst(he);
		numElement++;
		return true;
		
	}
	
	public boolean remove(K key) {
		HashElement<K, V> he = new HashElement<>(key, null);
		int hashIndex = (key.hashCode() & 0x7FFFFFFF) % tableSize;
		array[hashIndex].remove(he);
		numElement--;
		return true;
		
		
	}

	private void resize(int new_tableSize) {
		LinkedList2<HashElement<K, V>>[] new_array = (LinkedList2<HashElement<K, V>>[])new LinkedList2[new_tableSize];
		for(int i = 0; i < tableSize; i++) {
			array[i] = new LinkedList2<HashElement<K, V>>();
		}
		for(K key : this) {
			V value = getValue(key);
			HashElement<K, V> he = new HashElement<>(key, value);
			int hashIndex = (key.hashCode() & 0x7FFFFFFF) % new_tableSize;
			new_array[hashIndex].addFirst(he);
		}
		array = new_array;
		tableSize = new_tableSize;
	}

	public V getValue(K key) {
		int hashIndex = (key.hashCode() & 0x7FFFFFFF) % tableSize;
		for(HashElement<K,V> he : array[hashIndex]) {
			if(((Comparable<K>)he.key).compareTo(key) == 0) {
				return  he.value;
			}
		}
		return null;
	}

	private double loadFactor() {
		return numElement / tableSize;
	}

	@Override
	public Iterator<K> iterator() {
		return new IteratorHelper();
	}

}
