package paralleltasks;

import cse332.exceptions.NotYetImplementedException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private List<Map<Integer, Integer>> g;
    private int[] dist;
    private int[] distCopy;
    private int[] pred;
    private ReentrantLock[] locks;

    public RelaxOutTaskLock(List<Map<Integer, Integer>> g, int[] dist, int[] distCopy, int lo, int hi, int[] pred, ReentrantLock[] locks) {
        this.g = g;
        this.dist = dist;
        this.distCopy = distCopy;
        this.lo = lo;
        this.hi = hi;
        this.pred = pred;
        this.locks = locks;
    }

    private int lo;
    private int hi;

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential();
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxOutTaskLock left = new RelaxOutTaskLock(g, dist, distCopy, lo, mid, pred, locks);
            RelaxOutTaskLock right = new RelaxOutTaskLock(g, dist, distCopy, mid, hi, pred, locks);
            left.fork();
            right.compute();
            left.join();
        }
    }

    public void sequential() {
        for (int i = lo; i < hi; i++) {
            if(g.get(i) != null) {
                for (int j : g.get(i).keySet()) {
                    if (distCopy[i] == Integer.MAX_VALUE) {
                        distCopy[i] = g.get(i).get(j);
                    }
                    else {
                        locks[i].lock();
                        if (distCopy[i] + g.get(i).get(j) < dist[j]) {
                            dist[j] = distCopy[i] + g.get(i).get(j);
                            pred[j] = i;
                        }
                        locks[i].unlock();
                    }
                }
            }
        }
    }

    public static void parallel(List<Map<Integer, Integer>> g, int[] dist, int[] distCopy,
                                int lo, int hi, int[] pred, ReentrantLock[] locks) {
        pool.invoke(new RelaxOutTaskLock(g, dist, distCopy, lo, hi, pred, locks));
    }
}
