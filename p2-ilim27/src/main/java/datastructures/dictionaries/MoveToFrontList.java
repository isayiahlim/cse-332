package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;

import java.util.Iterator;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {
    protected ListNode root;

    public MoveToFrontList(){
        root = null;
        size = 0;
    }

    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null for insert");
        }
        V existing = find(key);
        if(existing == null) {
            root = new ListNode(key, value, root);
            size ++;
        } else {
            root.value = value;
        }
        return existing;
    }

    @Override
    public V find(K key) {
        if(key == null) {
            throw new IllegalArgumentException("Key cannot be null for find");
        }
        if(root != null && root.key.equals(key)) {
            return root.value;
        }
        ListNode temp = root;
        ListNode prev = null;
        while(temp != null && !temp.key.equals(key)) {
            prev = temp;
            temp = temp.next;
        }
        if(prev == null || temp == null) {
            return null;
        }
        V value = temp.value;
        prev.next = temp.next;
        temp.next = root;
        root = temp;
        return value;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MoveToFrontListIterator();
    }

    private class MoveToFrontListIterator extends SimpleIterator<Item<K, V>> {
        private MoveToFrontList.ListNode curr;

        public MoveToFrontListIterator() {
            if (MoveToFrontList.this.root != null) {
                this.curr = MoveToFrontList.this.root;
            }
        }

        @Override
        public boolean hasNext() {
            return this.curr != null;
        }

        @Override
        public Item<K, V> next() {
            Item<K, V> item = new Item<>((K)this.curr.key, (V)this.curr.value);
            this.curr = curr.next;
            return item;
        }
    }

    private class ListNode extends Item<K, V> {
        public ListNode next;

        public ListNode() {
            this(null,null, null);
        }

        public ListNode(K key, V value) {
            this(key, value, null);
        }

        public ListNode(K key, V value, ListNode next) {
            super(key, value);
            this.next = next;
        }
    }

}
