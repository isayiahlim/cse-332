package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E> extends FixedSizeFIFOWorkList<E> {
    private E[] data;
    private int next;
    private int end;
    private int size;
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        this.data = (E[]) new Object[capacity];
        next = 0;
        end = 0;
        size = 0;
    }

    @Override
    public void add(E work) {
        if(isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        if (next != end || end != 0 || data[end] != null) {
            end = (end + 1) % data.length;
        }
        data[end] = work;
        size++;
    }

    @Override
    public E peek() {
        if(!hasWork()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return data[next];
    }

    @Override
    public E peek(int i) {
        if(!hasWork()) {
            throw new NoSuchElementException("Queue is empty");
        }
        if(i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Invalid parameter i (must be in range of queue)");
        }
        i = (i+next) % data.length;
        return data[i];
    }

    @Override
    public E next() {
        if(!hasWork()) {
            throw new NoSuchElementException("Queue is empty");
        }
        E temp = data[next];
        data[next] = null;
        next ++;
        next = next% data.length;
        size --;
        return temp;
    }

    @Override
    public void update(int i, E value) {
        if(!hasWork()) {
            throw new NoSuchElementException("Queue is empty");
        }
        if(i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Invalid parameter i (must be in range of queue)");
        }
        i = (i+next) % data.length;
        data[i] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        next = 0;
        end = 0;
        data[next] = null;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        throw new NotYetImplementedException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            // Uncomment the line below for p2 when you implement equals
            // FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;

            // Your code goes here

            throw new NotYetImplementedException();
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        throw new NotYetImplementedException();
    }
}
