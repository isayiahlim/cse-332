package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskBad;

import java.util.List;
import java.util.Map;

public class OutParallelBad implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        //same setup as outSequential
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        int[] dist = new int[g.size()];
        int[] pred = new int[g.size()];
        //initialize
        for (int i = 0; i < g.size(); i++) {
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
        dist[source] = 0;
        //parallel
        for(int i = 0; i < g.size(); i++) {
            int[] copy = ArrayCopyTask.copy(dist);
            RelaxOutTaskBad.parallel(g, dist, copy, 0, g.size(), pred);
        }
        List<Integer> negCycles = GraphUtil.getCycle(pred);
        return negCycles;
    }
}
