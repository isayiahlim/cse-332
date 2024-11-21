package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        int[] dist = new int[g.size()];
        int[] pred = new int[g.size()];
        //initialize
        for (int i = 0; i < g.size(); i++) {
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        dist[source] = 0;

        for(int i = 0; i < g.size(); i++) {
            int[] distCopy = new int[g.size()];
            for(int j = 0; j < g.size(); j++) {
                distCopy[j] = dist[j];
            }
            for(int j = 0; j < g.size(); j++) {
                if(g.get(j) != null) {
                    for (int k : g.get(j).keySet()) {
                        if(distCopy[j] == Integer.MAX_VALUE) {
                            distCopy[j] = g.get(j).get(k);
                        }
                        else if (distCopy[j] + g.get(j).get(k) < dist[k]) {
                            dist[k] = distCopy[j] + g.get(j).get(k);
                            pred[k] = j;
                        }
                    }
                }
            }
        }

        List<Integer> negCycles = GraphUtil.getCycle(pred);
        return negCycles;
    }

}
