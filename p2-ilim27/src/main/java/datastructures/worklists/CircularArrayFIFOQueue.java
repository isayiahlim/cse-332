package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    private E[] data;
    private int next;
    private int end;
    private int size;
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        this.data = (E[]) new Comparable[capacity];
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
//        System.out.print("this = ");
//        for(E el : this) {
//            System.out.print(el);
//        }
//        System.out.println();
//        System.out.print("other = ");
//        for(E el : other) { //debug: make sure other is correct
//            System.out.print(el);
//        }System.out.println();
        int i = 0;
        while(i < size && i < other.size()) {
            if(!peek(i).equals(other.peek(i))) {
                return peek(i).compareTo(other.peek(i));
            }
            i++;
        }
//        System.out.println("this size = " + size + " other size = " + other.size());
        if(this.size == other.size()) {
            return 0;
        }
        return i < size ? 1 : -1;
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
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            // Your code goes here
            if(other.size() != size) {
                return false;
            }
            int i = 0;
            while(i < size && i < other.size()) {
                if(!peek(i).equals(other.peek(i))) {
                    return false;
                }
                i++;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        int result = 0;
        for(int i = 0; i < size; i ++) { //only creates hash code for elements
            result = 31 * result + (data[(i + next) % data.length].hashCode());
        }
        return result;
    }
}
