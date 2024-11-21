package getLeftMostIndex;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class GetLeftMostIndex {
    /**
     * Use the ForkJoin framework to write the following method in Java.
     *
     * Returns the index of the left-most occurrence of needle in haystack (think of needle and haystack as
     * Strings) or -1 if there is no such occurrence.
     *
     * For example, main.java.getLeftMostIndex("cse332", "Dudecse4ocse332momcse332Rox") == 9 and
     * main.java.getLeftMostIndex("sucks", "Dudecse4ocse332momcse332Rox") == -1.
     *
     * Your code must actually use the sequentialCutoff argument. You may assume that needle.length is much
     * smaller than haystack.length. A solution that peeks across subproblem boundaries to decide partial matches
     * will be significantly cleaner and simpler than one that does not.
     */
    public static int CUTOFF;
    public static final ForkJoinPool POOL = new ForkJoinPool();

    public static int getLeftMostIndex(char[] needle, char[] haystack, int sequentialCutoff) {
        CUTOFF = sequentialCutoff;
        return POOL.invoke(new GetLeftMostIndexTask(needle, haystack, 0, haystack.length));
    }

    /* TODO: Add a sequential method and parallel task here */
    public static int sequentialGLMI(char[] needle, char[] haystack, int lo, int hi) {
        for(int i = lo; i < hi; i++) {
            boolean found = false;
            if(haystack[i] == needle[0]) {
                found = true;
                for(int j = 1; j < needle.length; j++) {
                    if(i + j >= haystack.length) {
                        return -1;
                    }
                    if(needle[j] != haystack[i + j]) {
                        found = false;
                        break;
                    }
                }
                if(found) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static class GetLeftMostIndexTask extends RecursiveTask<Integer> {
        int lo, hi;
        char[] needle, haystack;

        public GetLeftMostIndexTask(char[] needle, char[] haystack, int lo, int hi) {
            this.needle = needle;
            this.haystack = haystack;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected Integer compute() {
            if(hi - lo <= CUTOFF) {
                return sequentialGLMI(needle, haystack, lo, hi);
            }
            int mid = lo + (hi - lo) / 2;
            GetLeftMostIndexTask left = new GetLeftMostIndexTask(needle, haystack, lo, mid);
            GetLeftMostIndexTask right = new GetLeftMostIndexTask(needle, haystack, mid, hi);
            left.fork();
            Integer rightResult = right.compute();
            Integer leftResult = left.join();

            if(leftResult == -1) {
                return rightResult;
            }
            if(rightResult == -1) {
                return leftResult;
            }
            return Math.min(leftResult, rightResult);
        }
    }

    private static void usage() {
        System.err.println("USAGE: GetLeftMostIndex <needle> <haystack> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        char[] needle = args[0].toCharArray();
        char[] haystack = args[1].toCharArray();
        try {
            System.out.println(getLeftMostIndex(needle, haystack, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
