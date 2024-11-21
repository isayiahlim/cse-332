package main;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight
     */
    public static List<Map<Integer, Integer>> parse(int[][] adjMatrix) {
        List<Map<Integer, Integer>> adjMatrixList = new ArrayList<Map<Integer, Integer>>();
        for(int i = 0; i < adjMatrix.length; i++) {
            adjMatrixList.add(i, null); // add a null map at each index in the arrayList
            for(int j = 0; j < adjMatrix[i].length; j++) {
                if(i != j && adjMatrix[i][j] != GraphUtil.INF) { // if there is an edge
                    if (adjMatrixList.get(i) == null) { // if the map in the list at that index isn't instantiated, add a new map
                        adjMatrixList.remove(i);
                        adjMatrixList.add(i, new HashMap<Integer, Integer>());
                    }
                    adjMatrixList.get(i).put(j, adjMatrix[i][j]); // put the edge cost corresponding to each edge
                }
            }
        }
        return adjMatrixList;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight with incoming edges
     */
    public static List<Map<Integer, Integer>> parseInverse(int[][] adjMatrix) {
        List<Map<Integer, Integer>> adjMatrixList = new ArrayList<>();
        for(int i = 0; i < adjMatrix.length; i++) {
            adjMatrixList.add(i, null);
            for(int j = 0; j < adjMatrix[i].length; j++) {
                if(i != j && adjMatrix[j][i] != GraphUtil.INF) {
                    if (adjMatrixList.get(i) == null) { // if the map in the list at that index isn't instantiated, add a new map
                        adjMatrixList.remove(i);
                        adjMatrixList.add(i, new HashMap<Integer, Integer>());
                    }
                    adjMatrixList.get(i).put(j, adjMatrix[j][i]); // put the edge cost corresponding to each edge
                }
            }
        }
        return adjMatrixList;
    }

}
