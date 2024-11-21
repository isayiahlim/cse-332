package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    private E[] data;
    private int curr;
    public ArrayStack() {
        data = (E[])new Object[10];
        curr = -1;
    }

    @Override
    public void add(E work) {
        if(curr == data.length - 1) {
            E[] temp = (E[])new Object[data.length *2];
            for(int i = 0; i < data.length; i ++) {
                temp[i] = data[i];
            }
            data = temp;
        }
        data[curr+1] = work;
        curr ++;
    }

    @Override
    public E peek() {
        if(size() == 0) {
            throw new NoSuchElementException();
        }
        return data[curr];
    }

    @Override
    public E next() {
        if(size() == 0) {
            throw new NoSuchElementException();
        }
        E temp = data[curr];
        curr --;
        return temp;
    }

    @Override
    public int size() {
        return curr + 1;
    }

    @Override
    public void clear() {
        curr = -1;
    }
}
