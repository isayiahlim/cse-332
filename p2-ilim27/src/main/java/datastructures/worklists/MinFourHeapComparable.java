package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;

    public MinFourHeapComparable() {
        data = (E[])new Comparable[10];
        size = 0;
    }

    @Override
    public boolean hasWork() {
        return size != 0;
    }

    @Override
    public void add(E work) {
        if(size == data.length) { //resize
            E[] copy = data;
            data = (E[])new Comparable[copy.length * 2];
            for(int i = 0; i < copy.length; i ++) {
                data[i] = copy[i];
            }
        }
        int curr = size;
        int parent = (size + 3)/4 -1;
        if(parent < 0) { parent = 0; }
        while(curr != parent && parent >= 0 && work.compareTo(data[parent]) < 0) { //percolates up
            data[curr] = data[parent];
            curr = parent;
            parent = (parent + 3)/4 - 1;
        } //stops when reaches root or when parent is less than current
        data[curr] = work;
        size ++;
    }

    @Override
    public E peek() {
        if(!hasWork()) {
            throw new NoSuchElementException("cannot peek an empty stack");
        }
        return data[0];
    }

    @Override
    public E next() { //gets the top element, removes then restores heap property
//        if(size*4 < data.length) { //resizes downwards to save space
//            E[] temp = data;
//            data = (E[])new Comparable[temp.length/4];
//            for(int i = 0; i < data.length; i ++) {
//                data[i] = temp[i];
//            }
//        }
        if(!hasWork()) {
            throw new NoSuchElementException("cannot get next item from an empty stack");
        }
        E next = data[0];
        int parent = 0;
        E last = data[size-1]; //last element stored
        int first = 1, second = 2, third = 3, fourth = 4;
        while(4*(parent+1) - 3 < size) {
            E min = last;
            int path = parent;
            if(first < size && data[first].compareTo(min) < 0) { //finds min child to switch with root
                min = data[first];
                path = first;
            }
            if(second < size && data[second].compareTo(min) < 0) {
                min = data[second];
                path = second;
            }
            if(third < size && data[third].compareTo(min) < 0) {
                min = data[third];
                path = third;
            }
            if(fourth < size && data[fourth].compareTo(min) < 0) {
                min = data[fourth];
                path = fourth;
            }
            if(path == parent) { //if no child is less than root
                break;
            }
            data[parent] = min;
            parent = path;
            first = path*4 + 1;
            second = path*4 +2;
            third = path*4 +3;
            fourth = path*4 +4;
        }
        data[parent] = last; //places the last element
        size --;
        return next;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        data = (E[])new Comparable[10];
    }
}
