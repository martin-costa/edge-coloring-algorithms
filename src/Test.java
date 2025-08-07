import java.util.*;
import java.io.*;
import java.lang.*;

public class Test {

    // main function to run tests
    public static void main(String[] args) {

        Graph graph = createRandomGraph(100, 300, true);

        FastGreedy fastGreedy = new FastGreedy(0.1f);

        VizingBipartite vizingBipartite = new VizingBipartite();

        EdgeColoring edgeColoring = vizingBipartite.color(graph);

        edgeColoring.isValid(true);

        System.out.print("Number of vertices: " + graph.n() + 
                           ", Number of edges: " + graph.m() +
                           ", Max degree: " + graph.maxDegree() + "\n");

        System.out.print("Max color used: " + edgeColoring.maxColor() + "\n");

        // System.out.print("Path flipping test successful: " + testPathFlip() + "\n");

        // EdgeColoring edgeColoring = new EdgeColoring(graph);

        // System.out.print("Number of vertices: " + graph.n() + 
        //                    ", Number of edges: " + graph.m() +
        //                    ", Max degree: " + graph.maxDegree() + "\n");

        // edgeColoring.setEdgeColor(new Edge(20, 11), 1);
        // edgeColoring.setEdgeColor(new Edge(20, 12), 2);
        // edgeColoring.setEdgeColor(new Edge(20, 12), 0);
    }

    public static Graph createRandomGraph(int n, float m, boolean bipartite) {

        // Make sure that n is even if bipartite
        if (bipartite && n % 2 != 0) {
            n++;
        }

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

            if (bipartite) {
                // Ensure bipartite condition by making u even and v odd
                if (u % 2 == 1) u--;
                if (v % 2 == 0) v++;
            }

            if (u != v && !edges.get(u).contains(v)) {
                edges.get(u).add(v);
                edges.get(v).add(u); // Undirected graph, add both directions
                edgesAdded++;
            }
        } // This is only efficient if m = o(n^2)

        Graph graph = new Graph(edges);
        return graph;
    }

    public static boolean testPathFlip() {

        int n = 100; // Number of vertices

        // Create a path in the graph
        HashMap<Integer, Set<Integer>> edges = new HashMap<Integer, Set<Integer>>();

        // Initialize the edges map with empty sets for each vertex
        for (int i = 0; i < n; i++) {
            edges.put(i, new HashSet<Integer>());
            if (i < n - 1) {
                edges.get(i).add(i + 1); // Connect each vertex to the next
            }
            if (i > 0) {
                edges.get(i).add(i - 1); // Connect each vertex to the previous
            }
        }

        Graph graph = new Graph(edges);

        // Create an edge coloring with 2 colors
        EdgeColoring edgeColoring = new EdgeColoring(graph, 2);

        for (int i = 0; i < n - 1; i++) {
            edgeColoring.setEdgeColor(i, i + 1, (i % 2) + 1); // Alternate colors
        }

        // Flip the colors along the path
        edgeColoring.FlipAlternatingPath(0, 1, 2);

        for (int i = 0; i < n - 1; i++) {
            int expectedColor = ((i + 1) % 2) + 1; // Colors should be flipped
            if (edgeColoring.getEdgeColor(i, i + 1) != expectedColor) {
                System.out.println("Test failed at edge (" + i + ", " + (i + 1) + ")");
                return false;
            }
        }

        return true; // All colors flipped correctly
    }
}