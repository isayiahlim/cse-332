package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import cse332.sorts.InsertionSort;

import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        if(array == null || array.length == 0) {
            return;
        }
        sort(array, 0, array.length-1, comparator);
    }

    private static <E> void sort(E[] array, int low, int high, Comparator<E> comparator) {
        if(high - low < 10) { //arbitrary cutoff for switching to insertion sort
//            InsertionSort insertionSort = new InsertionSort();
            insertionSort(array, comparator, low, high);
        }
        else {
            //finds the pivot and the index of the pivot
            int pivotIndex = low;
            E pivot = array[low];
            int middleIndex = low + (high - low) / 2;
            if (comparator.compare(array[low], array[high]) > 0) { // Find median of three values
                if (comparator.compare(array[high], array[middleIndex]) > 0) {
                    pivot = array[high];
                    pivotIndex = high;
                } else if (comparator.compare(array[low], array[middleIndex]) > 0) {
                    pivot = array[middleIndex];
                    pivotIndex = middleIndex;
                }
            } else {
                if (comparator.compare(array[low], array[middleIndex]) > 0) {
                    pivot = array[low];
                    pivotIndex = low;
                } else if (comparator.compare(array[high], array[middleIndex]) > 0) {
                    pivot = array[middleIndex];
                    pivotIndex = middleIndex;
                }
            }

            //puts pivot in first element
            array[pivotIndex] = array[low];
            //array[low] = pivot;

            //partition in place
            int left = low + 1;
            int right = high;
            while (left < right) {
                if (comparator.compare(pivot, array[left]) > 0) {
                    left++;
                }
                else if (comparator.compare(pivot, array[right]) <= 0) {
                    right--;
                }
                else {
                    E temp = array[left];
                    array[left] = array[right];
                    array[right] = temp;
                }
            }

            //put pivot back in
            if (comparator.compare(pivot, array[left]) < 0) {
                left--;
            }
            E temp = array[left];
            array[left] = pivot;
            array[low] = temp;
            sort(array, low, left - 1, comparator);
            sort(array, left + 1, high, comparator);
        }
    }

    private static <E> void insertionSort(E[] array, Comparator<E> comparator, int low, int high) {
        for(int i = low + 1; i <= high; i++) {
            E temp = array[i];
            int j = i;
            while(j > low && comparator.compare(array[j-1], temp) > 0) {
                array[j] = array[j-1];
                j--;
            }
            array[j] = temp;
        }
    }
}
