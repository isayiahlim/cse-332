package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        if(array == null || array.length == 0) {
            return;
        }
        if(k == 0) {
            for(int i = 0; i < array.length; i++) {
                array[i] = null;
            }
            return;
        }

        int maxSize = k;
        if(k > array.length) {
            maxSize = array.length;
        }
        WorkList<E> heap = new MinFourHeap<>(comparator);

        //iterate through every element in array, if greater than current heap min, add
        for(int i = 0; i < maxSize; i++) {
            heap.add(array[i]);
        }
        for(int i = maxSize; i < array.length; i++) {
            if(comparator.compare(array[i], heap.peek()) > 0) { //keep a max of k elements
                heap.next();
                heap.add(array[i]);
            }
            array[i] = null;
        }

        for(int i = 0; i < maxSize; i++) {
            array[i] = heap.next();
        }
    }
}
