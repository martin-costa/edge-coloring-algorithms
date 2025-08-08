import java.util.*;
import java.io.*;
import java.lang.*;

public class Test {

    // main function to run tests
    public static void main(String[] args) {

        getMemoryStatistics();

        // Create a random graph for testing
        System.out.println("Constructing a random graph...");
        Graph graph = createRandomGraph(4000, 0.2f, true);

        getMemoryStatistics();

        // Vizing
        System.out.println("Running Vizing's algorithm...");

        Vizing vizing = new Vizing();
        EdgeColoring edgeColoring1 = vizing.color(graph);
        edgeColoring1.isValid(true);

        getMemoryStatistics();

        // Bipartite Vizing
        System.out.println("Running Vizing's Bipartite algorithm...");

        VizingBipartite vizingBipartite = new VizingBipartite();
        EdgeColoring edgeColoring2 = vizingBipartite.color(graph);
        edgeColoring2.isValid(true);

        getMemoryStatistics();

        // Greedy
        System.out.println("Running Greedy algorithm...");

        FastGreedy fastGreedy = new FastGreedy(0.1f);
        EdgeColoring edgeColoring3 = fastGreedy.color(graph);
        edgeColoring3.isValid(true);

        getMemoryStatistics();
    }

    public static Graph createRandomGraph(int n, float density, boolean bipartite) {

        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Density must be between 0 and 1");
        }

        // Make sure that n is even if bipartite
        if (bipartite && n % 2 != 0) {
            n++;
        }

        // Calculate the number of edges based on the density
        int m = (int) (density * n * (n - 1) / 2); // Maximum number of edges in a complete graph

        if (bipartite) {
            m = (int) (density * (n / 2) * (n / 2 - 1)); // Maximum edges in a bipartite graph
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

    public static void getMemoryStatistics() {

        // Get the singleton instance of the Runtime class
        Runtime runtime = Runtime.getRuntime();

        // Run garbage collection to get a more accurate measure of used memory
        runtime.gc();

        long totalMemory = runtime.totalMemory(); // Total memory allocated to the JVM
        long freeMemory = runtime.freeMemory();   // Free memory within the allocated total
        long usedMemory = totalMemory - freeMemory;
        long usedMemoryMB = usedMemory / (1024L * 1024L); // Convert to MB
        System.out.println("Used Memory: " + usedMemoryMB + " MB");
    }
}