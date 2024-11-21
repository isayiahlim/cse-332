package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    private ListNode next;
    private ListNode end;
    private int size;

    public ListFIFOQueue() {
        this.next = null;
        this.end = null;
        size = 0;
    }

    @Override
    public void add(E work) {
        if(end == null) {
            end = new ListNode(work);
        }
        else {
            end.next = new ListNode(work);
            end = end.next;
        }
        if(next == null) {
            next = end;
        }
        size ++;
    }

    @Override
    public E peek() {
        if(next == null) {
            throw new NoSuchElementException();
        }
        return next.data;
    }

    @Override
    public E next() {
        if(next == null) {
            throw new NoSuchElementException();
        }
        E temp = next.data;
        next = next.next;
        size --;
        return temp;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        next = null;
        end = null;
        size = 0;
    }

    //private inner node class representing a linked list node
    private class ListNode {
        public E data;
        public ListNode next;

        //constructor taking in a data field
        public ListNode(E data){
            this(data, null);
        }

        //constructor taking in data and a next field
        public ListNode(E data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }
}
