package hasOver;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HasOver {
    /**
     * Use the ForkJoin framework to write the following method in Java.
     *
     * Returns true if arr has any elements strictly larger than val.
     * For example, if arr is [21, 17, 35, 8, 17, 1], then
     * main.java.hasOver(21, arr) == true and main.java.hasOver(35, arr) == false.
     *
     * Your code must have O(n) work, O(lg n) span, where n is the length of arr, and actually use the sequentialCutoff
     * argument.
     */
    public static boolean hasOver(int val, int[] arr, int sequentialCutoff) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new HasOverTask(val, arr, sequentialCutoff, 0, arr.length));
    }

    public static Boolean sequentialHasOver(int val, int[] arr, int lo, int hi) {
        for(int i = lo; i < hi; i++) {
            if(arr[i] > val) {
                return true;
            }
        }
        return false;
    }

    public static class HasOverTask extends RecursiveTask<Boolean> {
        private final int val;
        private final int[] arr;
        private final int sequentialCutoff;
        private final int lo, hi;

        public HasOverTask(int val, int[] arr, int sequentialCutoff, int lo, int hi) {
            this.val = val;
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.sequentialCutoff = sequentialCutoff;
        }

        protected Boolean compute() {
            if(hi - lo <= sequentialCutoff) {
                return sequentialHasOver(val, arr, lo, hi);
            }
            int mid = lo + (hi-lo)/2;
            HasOverTask left = new HasOverTask(val, arr, sequentialCutoff, lo, mid);
            HasOverTask right = new HasOverTask(val, arr, sequentialCutoff, mid, hi);
            left.fork();
            Boolean rightB = right.compute();
            Boolean leftB = left.join();
            return rightB || leftB;
        }
    }

    private static void usage() {
        System.err.println("USAGE: HasOver <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        int val = 0;
        int[] arr = null;

        try {
            val = Integer.parseInt(args[0]);
            String[] stringArr = args[1].replaceAll("\\s*", "").split(",");
            arr = new int[stringArr.length];
            for (int i = 0; i < stringArr.length; i++) {
                arr[i] = Integer.parseInt(stringArr[i]);
            }
            System.out.println(hasOver(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }

    }
}
