package paralleltasks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxInTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    final int lo, hi;

    private List<Map<Integer, Integer>> g;
    private int[] dist;
    private int[] distCopy;
    private int[] pred;

    public RelaxInTask(List<Map<Integer, Integer>> g, int[] dist, int[] distCopy, int lo, int hi, int[] pred) {
        this.g = g;
        this.dist = dist;
        this.distCopy = distCopy;
        this.lo = lo;
        this.hi = hi;
        this.pred = pred;
    }

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential();
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxInTask left = new RelaxInTask(g, dist, distCopy, lo, mid, pred);
            RelaxInTask right = new RelaxInTask(g, dist, distCopy, mid, hi, pred);
            left.fork();
            right.compute();
            left.join();
        }
    }

    public void sequential() {
        for (int i = lo; i < hi; i++) {
            // Iterate over all vertices to find incoming edges
            for (int j = 0; j < g.size(); j++) {
                if (g.get(j) != null && g.get(j).containsKey(i)) {
                    int weight = g.get(j).get(i);
                    if (distCopy[i] != Integer.MAX_VALUE && distCopy[i] + weight < dist[j]) {
                        dist[j] = distCopy[i] + weight;
                        pred[j] = i;
                    }
                }
            }
        }
    }

    public static void parallel(List<Map<Integer, Integer>> g, int[] dist, int[] distCopy, int lo, int hi, int[] pred) {
        pool.invoke(new RelaxInTask(g, dist, distCopy, lo, hi, pred));
    }

}
