package getLongestSequence;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class GetLongestSequence {
    /**
     * Use the ForkJoin framework to write the following method in Java.
     *
     * Returns the length of the longest consecutive sequence of val in arr.
     * For example, if arr is [2, 17, 17, 8, 17, 17, 17, 0, 17, 1], then
     * getLongestSequence(17, arr) == 3 and getLongestSequence(35, arr) == 0.
     *
     * Your code must have O(n) work, O(lg n) span, where n is the length of arr, and actually use the sequentialCutoff
     * argument. We have provided you with an extra class SequenceRange. We recommend you use this class as
     * your return value, but this is not required.
     */
    public static int CUTOFF;
    public static final ForkJoinPool POOL = new ForkJoinPool();

    public static int getLongestSequence(int val, int[] arr, int sequentialCutoff) {
        /* TODO: Edit this with your code */
        GetLongestSequence.CUTOFF = sequentialCutoff;
        SequenceRange temp = POOL.invoke(new GetLongestSequenceTask(val, arr, 0, arr.length));
        return temp.longestRange;
    }

    /* TODO: Add a sequential method and parallel task here */
    public static SequenceRange sequentialGLST(int val, int[] arr, int lo, int hi) {
        SequenceRange temp = new SequenceRange(0,0,0);
        int tempMax = 0;
        for(int i = lo; i < hi; i ++) {
            if(arr[i] == val) {
                if(i - lo == temp.matchingOnLeft) {
                    temp.matchingOnLeft ++;
                }
                tempMax ++;
                if(tempMax > temp.longestRange) {
                    temp.longestRange = tempMax;
                }
                temp.matchingOnRight ++;
            }
            else {
                tempMax = 0;
                temp.matchingOnRight = 0;
            }

        }
        return temp;
    }

    public static class GetLongestSequenceTask extends RecursiveTask<SequenceRange> {
        private int val;
        private int[] arr;
        private int lo;
        private int hi;

        public GetLongestSequenceTask(int val, int[] arr, int lo, int hi) {
            this.val = val;
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected SequenceRange compute() {
            if(hi - lo <= CUTOFF) {
                return sequentialGLST(val, arr, lo, hi);
            }
            int mid = (hi-lo)/2 + lo;
            GetLongestSequenceTask left = new GetLongestSequenceTask(val, arr, lo, mid);
            GetLongestSequenceTask right = new GetLongestSequenceTask(val, arr, mid, hi);
            left.fork();
            SequenceRange rightSeq = right.compute();
            SequenceRange leftSeq = left.join();

            int middleMax = rightSeq.matchingOnLeft + leftSeq.matchingOnRight;
            int max = Math.max(leftSeq.longestRange, rightSeq.longestRange);
            max = Math.max(max, middleMax);
            max = Math.max(max, leftSeq.matchingOnLeft);
            max = Math.max(max, rightSeq.matchingOnRight);
            if(leftSeq.matchingOnLeft == mid-lo) {
                leftSeq.matchingOnLeft += rightSeq.matchingOnLeft;
            }
            if(rightSeq.matchingOnRight == hi - mid) {
                rightSeq.matchingOnRight += leftSeq.matchingOnRight;
            }
            return new SequenceRange(leftSeq.matchingOnLeft,rightSeq.matchingOnRight,max);
        }
    }

    private static void usage() {
        System.err.println("USAGE: GetLongestSequence <number> <array> <sequential cutoff>");
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
            System.out.println(getLongestSequence(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
