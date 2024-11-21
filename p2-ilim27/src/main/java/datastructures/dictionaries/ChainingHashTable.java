package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import datastructures.worklists.ArrayStack;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

/**
 * - You must implement a generic chaining hashtable. You may not
 *   restrict the size of the input domain (i.e., it must accept
 *   any key) or the number of inputs (i.e., it must grow as necessary).
 *
 * - ChainingHashTable should rehash as appropriate (use load factor as shown in lecture!).
 *
 * - ChainingHashTable must resize its capacity into prime numbers via given PRIME_SIZES list.
 *   Past this, it should continue to resize using some other mechanism (primes not necessary).
 *
 * - When implementing your iterator, you should NOT copy every item to another
 *   dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;
    private int sizeIndex;
    private Dictionary<K,V>[] data;
    static final int[] PRIME_SIZES =
            {11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197573, 395147};

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        sizeIndex = 1;
        data = new Dictionary[PRIME_SIZES[0]];
    }


    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("key and value must be non-null");
        }
        int index = Math.abs(key.hashCode() % data.length);
        if(data[index] == null) { //add new chain in array
            data[index] = newChain.get();
        }
        V replaced = data[index].insert(key, value); //store element to return
        if(replaced == null) { //increment num elements
            size ++;
        }
        if(size == data.length) { //rehash
            rehash();
        }
        return replaced;
    }

    private void rehash() {
        int tableSize;
        if(sizeIndex == PRIME_SIZES.length) { //resize
            tableSize = data.length * 2 + 1;
        } else {
            tableSize = PRIME_SIZES[sizeIndex];
            sizeIndex ++;
        }
        Dictionary<K, V>[] old = data;
        data = new Dictionary[tableSize];
        for (int i = 0; i < old.length; i++) {
            if (old[i] != null) {
                for (Item<K, V> item : old[i]) {
//                        insert(item.key, item.value);
                    int index = Math.abs(item.key.hashCode() % data.length);
                    if(data[index] == null) { //add new chain in array
                        data[index] = newChain.get();
                    }
                    V replaced = data[index].insert(item.key, item.value); //store element to return
                }
            }
        }
    }

    @Override
    public V find(K key) {
        if(key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
        int index = Math.abs(key.hashCode() % data.length);
        if(data[index] == null) {
            return null;
        }
        return data[index].find(key);
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTableIterator();
    }

    private class ChainingHashTableIterator extends SimpleIterator<Item<K, V>> {
        private Iterator<Item<K,V>> current; //use an iterator for each dictionary in data instead?
        private int index;

        public ChainingHashTableIterator() {
            index = 0;
            current = getIterator();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item<K, V> next() {
            Item<K, V> result = current.next();
            if(!current.hasNext()) {
                index ++;
                current = getIterator();
            }
            return result;
        }

        private Iterator<Item<K,V>> getIterator() {
            while(index < data.length && data[index] == null) {
                index ++;
            }
            if(index >= data.length) {
                return null;
            }
            return data[index].iterator();
        }
    }

//    /**
//     * Temporary fix so that you can debug on IntelliJ properly despite a broken iterator
//     * Remove to see proper String representation (inherited from Dictionary)
//     */
//    @Override
//    public String toString() {
//        return "ChainingHashTable String representation goes here.";
//    }
}
