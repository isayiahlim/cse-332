package provided;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import p2.sorts.QuickSort;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuickSortTests {

	@Test()
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
	public void test_sort_integerSorted_correctSort() {
		Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		Integer[] arr_sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		QuickSort.sort(arr, Integer::compareTo);
//		for(int i : arr) {
//			System.out.print(i + " ");
//		}
		for(int i = 0; i < arr.length; i++) {
			assertEquals(arr_sorted[i], arr[i]);
		}
	}

	@Test()
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
	public void test_sort_integerRandom_correctSort() {
		Integer[] arr = {3, 1, 4, 5, 9, 2, 6, 7, 8};
		Integer[] arr_sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		QuickSort.sort(arr, Integer::compareTo);
//		for(int i : arr) {
//			System.out.print(i + " ");
//		}
		for(int i = 0; i < arr.length; i++) {
			assertEquals(arr_sorted[i], arr[i]);
		}
	}
}