package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    private Comparator<E> comparator;

    public MinFourHeap(Comparator<E> c) {
        this.data = (E[]) new Object[10];
        this.size = 0;
        this.comparator = c;
    }

    //additional constructor for buildHeap
    public MinFourHeap(Comparator<E> c, E[] d) {
        this.comparator = c;
        buildHeap(d);
    }

    @Override
    public boolean hasWork() {
        return this.size != 0;
    }

    @Override
    public void add(E work) {
        if(size == data.length) { //resize
            E[] copy = data;
            data = (E[])new Object[copy.length * 2];
            for(int i = 0; i < copy.length; i ++) {
                data[i] = copy[i];
            }
        }
        int curr = size;
        int parent = (size + 3)/4 -1;
        if(parent < 0) { parent = 0; }
        while(curr != parent && parent >= 0 && comparator.compare(work,(data[parent])) < 0) { //percolates up
            data[curr] = data[parent];
            curr = parent;
            parent = (parent + 3)/4 - 1;
        } //stops when reaches root or when parent is less than current
        data[curr] = work;
        size ++;
    }

    private void buildHeap(E[] array) {
        data = array;
        int n = data.length;
        size = n;
        for (int i = n / 4 - 1; i >= 0; i--) {
            heapify(i, n);
        }
    }

    private void heapify(int i, int n) {
        int smallest = i;
        for (int j = 1; j <= 4; j++) { //looks at each child
            int child = 4 * i + j;
            if (child < n && comparator.compare(data[child], data[smallest]) < 0) {
                smallest = child;
            }
        }

        if (smallest != i) {
            E temp = data[i];
            data[i] = data[smallest];
            data[smallest] = temp;
            heapify(smallest, n);
        }
    }

    @Override
    public E peek() {
        if(!hasWork()) {
            throw new NoSuchElementException("cannot peek an empty stack");
        }
        return data[0];
    }

    @Override
    public E next() {
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
            if(first < size && comparator.compare(data[first],min) < 0) { //finds min child to switch with root
                min = data[first];
                path = first;
            }
            if(second < size && comparator.compare(data[second],min) < 0) {
                min = data[second];
                path = second;
            }
            if(third < size && comparator.compare(data[third],min) < 0) {
                min = data[third];
                path = third;
            }
            if(fourth < size && comparator.compare(data[fourth],min) < 0) {
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
        data = (E[]) new Object[10];
    }
}
