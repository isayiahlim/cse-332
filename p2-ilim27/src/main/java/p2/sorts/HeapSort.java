package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;
import java.util.Dictionary;

public class HeapSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        if(array == null || array.length == 0) {
            return;
        }
        MinFourHeap<E> heap = new MinFourHeap<>(comparator);
        for(E element : array) {
            heap.add(element);
        }
//        MinFourHeap<E> heap = new MinFourHeap<>(comparator, array);
        System.out.println(heap);
        for(int i = 0; i < array.length; i++) {
            array[i] = heap.next();
        }
    }
}
