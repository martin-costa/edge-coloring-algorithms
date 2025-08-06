import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EdgeColoring {

    // Graph instance
    private Graph graph;

    // Map to store the edges of the graph
    private HashMap<Integer, Set<Integer>> edges;

    // Number of vertices in the graph
    private int n;

    // Number of edges in the graph
    private int m;

    // Maximum degree of the graph
    private int maxDegree;

    // Map to store the edges of the graph
    private HashMap<Edge, Integer> coloring;

    // Map from (vertex, color) to edges incident on the vertex with that color
    private HashMap<Pair<Integer, Integer>, Integer> vertexColorToEdge;

    // Maintains the set of missing colors for each vertex
    private HashMap<Integer, Set<Integer>> missingColors;

    // Max number of colors used by the algorithm
    private int maxColor;

    // Use 0 to denote uncolored, and integers in [1, maxColor] for colors

    public EdgeColoring(Graph graph, int maxColor) {

        // Store the graph instance and its properties
        this.graph = graph;
        this.edges = graph.getEdges();
        this.n = graph.n();
        this.m = graph.m();
        this.maxDegree = graph.maxDegree();

        // Initialize the data structures
        this.coloring = new HashMap<Edge, Integer>();

        for (Integer u : edges.keySet()) {
            for (Integer v : edges.get(u)) {
                Edge edge = new Edge(u, v);
                this.coloring.put(edge, 0); // Initialize all edges as uncolored
            }
        }

        this.vertexColorToEdge = new HashMap<Pair<Integer, Integer>, Integer>();
        
        this.missingColors = new HashMap<Integer, Set<Integer>>();
        for (Integer vertex : edges.keySet()) {
            Set<Integer> colors = new HashSet<>();
            for (int color = 1; color <= maxColor; color++) {
                colors.add(color); // Initialize with all possible colors
            }
            missingColors.put(vertex, colors);
        }

        // The maximum color used by the algorithm
        this.maxColor = maxColor;
    }

    // Constructor for EdgeColoring with default maxColor
    public EdgeColoring(Graph graph) {
        this(graph, graph.maxDegree() + 1);
    }

    public boolean uncolorEdge(Edge edge) {

        // If input invalid or no change needed
        if (!coloring.containsKey(edge) || coloring.get(edge) == 0) {
            return false;
        }

        int color = coloring.get(edge);

        // Remove the edge from the vertexColorToEdge map
        int u = edge.u();
        int v = edge.v();
        vertexColorToEdge.remove(new Pair<>(u, color));
        vertexColorToEdge.remove(new Pair<>(v, color));
        missingColors.get(u).add(color);
        missingColors.get(v).add(color);

        coloring.put(edge, 0); // Uncolor the edge
        coloring.put(new Edge(v, u), 0); // Uncolor the reverse edge

        return true;
    }

    public boolean setEdgeColor(Edge edge, int color) {

        // If input invalid or no change needed
        if (!coloring.containsKey(edge) || color < 0 || color == coloring.get(edge)) {
            return false;
        }

        // Check for conflicts
        int u = edge.u();
        int v = edge.v();

        if (vertexColorToEdge.containsKey(new Pair<>(u, color)) || vertexColorToEdge.containsKey(new Pair<>(v, color))) {
            return false; // Color already used at one of the vertices
        }

        this.uncolorEdge(edge); // Uncolor the edge first
        
        // Update the data structures
        coloring.put(edge, color);
        coloring.put(new Edge(v, u), color);
        vertexColorToEdge.put(new Pair<>(u, color), v);
        vertexColorToEdge.put(new Pair<>(v, color), u);
        missingColors.get(u).remove(color);
        missingColors.get(v).remove(color);

        return true;
    }

    public boolean setEdgeColor(int u, int v, int color) {
        return setEdgeColor(new Edge(u, v), color);
    }

    // Get the color of an edge
    public Integer getEdgeColor(int u, int v) {
        return coloring.get(new Edge(u, v));
    }
    
    public Integer getEdgeColor(Edge edge) {
        return coloring.get(edge);
    }

    // Get the edge incident on a vertex with a specific color
    public Integer getEdgeWithColorAtVertex(int vertex, int color) {
        return vertexColorToEdge.get(new Pair<>(vertex, color));
    }

    // Get the set of missing colors for a vertex
    public Set<Integer> getAllMissingColors(int vertex) {
        return missingColors.get(vertex);
    }

    // Get a missing color for a vertex
    public Integer getMissingColor(int vertex) {
        Set<Integer> colors = missingColors.get(vertex);
        if (colors.isEmpty()) {
            return null;
        }
        return colors.iterator().next(); // Return an arbitrary missing color
    }

    // Check if a vertex has a missing color
    public boolean isMissing(int vertex, int color) {
        return missingColors.get(vertex).contains(color);
    }

    // Get the edges of the graph
    public HashMap<Integer, Set<Integer>> getEdges() {
        return edges;
    }

    // Get the number of vertices in the graph
    public int n() {
        return n;
    }

    // Get the number of edges in the graph
    public int m() {
        return m;
    }

    // Get the maximum degree of the graph
    public int maxDegree() {
        return maxDegree;
    }
}