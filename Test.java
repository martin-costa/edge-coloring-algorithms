import java.util.*;
import java.io.*;
import java.lang.*;

public class Test {

    // main function to run tests
    public static void main(String[] args) {

        Graph graph = createRandomGraph(100, 300);

        FastGreedy fastGreedy = new FastGreedy(0.1f);
        EdgeColoring edgeColoring = fastGreedy.color(graph);

        // EdgeColoring edgeColoring = new EdgeColoring(graph);

        // System.out.print("Number of vertices: " + graph.n() + 
        //                    ", Number of edges: " + graph.m() +
        //                    ", Max degree: " + graph.maxDegree() + "\n");

        // edgeColoring.setEdgeColor(new Edge(20, 11), 1);
        // edgeColoring.setEdgeColor(new Edge(20, 12), 2);
        // edgeColoring.setEdgeColor(new Edge(20, 12), 0);
    }

    public static Graph createRandomGraph(int n, float m) {

        HashMap<Integer, Set<Integer>> edges = new HashMap<Integer, Set<Integer>>();

        // Initialize the edges map with empty sets for each vertex
        for (int i = 0; i < n; i++) {
            edges.put(i, new HashSet<Integer>());
        }

        // Randomly add edges
        Random rand = new Random();

        int edgesAdded = 0;
        while (edgesAdded < m) {
            int u = rand.nextInt(n);
            int v = rand.nextInt(n);
            if (u != v && !edges.get(u).contains(v)) {
                edges.get(u).add(v);
                edges.get(v).add(u); // Undirected graph, add both directions
                edgesAdded++;
            }
        } // This is only efficient if m = o(n^2)

        Graph graph = new Graph(edges);
        return graph;
    }
}