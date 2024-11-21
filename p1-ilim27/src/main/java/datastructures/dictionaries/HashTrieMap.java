package datastructures.dictionaries;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<A, HashTrieNode>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
        size = 0;
    }

    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("key/value cannot be null when inserting");
        }
        HashTrieNode curr = (HashTrieNode)root;
        Iterator<A> iter = key.iterator();
        while(iter.hasNext()) {
            A next = iter.next();
            if(!curr.pointers.containsKey(next)) { //check if we need to add new key
               curr.pointers.put(next, new HashTrieNode());
            }
            curr = curr.pointers.get(next); //follow the path to insert
        }
        V replaced = curr.value;
        if(replaced == null) {
            size ++;
        }
        curr.value = value;
        return replaced;
    }

    @Override
    public V find(K key) {
        if(key == null) {
            throw new IllegalArgumentException("cannot find a null key");
        }
        Iterator<A> iter = key.iterator();
        HashTrieNode curr = (HashTrieNode) root;
        while(iter.hasNext()) { //if iter doesn't have next
            A next = iter.next();
            if(curr.pointers.containsKey(next)) {
                curr = curr.pointers.get(next);
            } else {
                return null;
            }
        }
        return curr.value;
    }

    @Override
    public boolean findPrefix(K key) {
        if(key == null) {
            throw new IllegalArgumentException("cannot find a null prefix");
        }
        HashTrieNode curr = (HashTrieNode) root;
        if(key.isEmpty()) {
            return root.value != null || !curr.pointers.isEmpty();
        }
        Iterator<A> iter = key.iterator();
        while(iter.hasNext()) {
            A next = iter.next();
            if(curr.pointers.containsKey(next)) {
                curr = curr.pointers.get(next);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void delete(K key) {
        if(key == null) {
            throw new IllegalArgumentException("cannot delete a null key");
        }
        if(key.isEmpty()) {
            V temp = root.value;
            if(temp != null) {
                root.value = null;
                size --;
            }
            if(((HashTrieNode)root).pointers.isEmpty()) {
                clear();
            }
            return;
        }
        Iterator<A> iter = key.iterator();
        HashTrieNode curr = (HashTrieNode) root;
        HashTrieNode lastValid = null;
        A lastValidChar = null;
        while(iter.hasNext()) {
            A next = iter.next();
            if(curr.pointers.containsKey(next)) {
                if(curr.pointers.size() > 1 || curr.value != null) {
                    lastValid = curr;
                    lastValidChar = next;
                }
                curr = curr.pointers.get(next);
                if(!iter.hasNext()) {
                    V replace = curr.value;
                    if(replace != null) {
                        size --;
                        curr.value = null;
                    }
                    if(curr.pointers.isEmpty()) {
                        if(lastValid == null) {
                            clear();
                        } else {
                            lastValid.pointers.remove(lastValidChar);
                        }
                    }
                }

            } else {
                return;
            }
        }
    }

    @Override
    public void clear() {
        this.root = new HashTrieNode();
        size = 0;
    }
}
