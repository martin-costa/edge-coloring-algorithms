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

        // The maximum color used by the algorithm
        this.maxColor = maxColor;

        // Initialize the data structures
        this.coloring = new HashMap<Edge, Integer>();

        for (Integer u : edges.keySet()) {
            for (Integer v : edges.get(u)) {
                Edge edge = new Edge(u, v);
                this.coloring.put(edge, 0); // Initialize all edges as uncolored
            }
        }

        this.vertexColorToEdge = new HashMap<Pair<Integer, Integer>, Integer>();
        
        // NOTE: Explicitly initializing the missing colors for each vertex takes O(n * maxColor) time.
        // We only store the missing colors in the range [1, deg(u) + 1], this suffices to always find a missing color.
        this.missingColors = new HashMap<Integer, Set<Integer>>();
        for (Integer vertex : edges.keySet()) {
            int vertexMaxColor = Math.min(edges.get(vertex).size() + 1, maxColor);
            Set<Integer> colors = new HashSet<>();
            for (int color = 1; color <= vertexMaxColor; color++) {
                colors.add(color); // Initialize with all possible colors
            }
            missingColors.put(vertex, colors);
        }
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

        return true;
    }

    public boolean uncolorEdge(int u, int v) {
        return uncolorEdge(new Edge(u, v));
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
        return vertexColorToEdge.get(new Pair<>(vertex, color)) == null;
    }

    // Ensures 'color' is not missing at vertex 
    public boolean addBlockedColor(int vertex, int color) {

        if (vertexColorToEdge.containsKey(new Pair<>(vertex, color))) {
            throw new IllegalArgumentException("Color " + color + " is already used at vertex " + vertex);
        }
        
        return missingColors.get(vertex).remove(color);
    }

    // Make 'color' missing at vertex
    public void removeBlockedColor(int vertex, int color) {

        if (vertexColorToEdge.containsKey(new Pair<>(vertex, color))) {
            throw new IllegalArgumentException("Color " + color + " is already used at vertex " + vertex);
        }

        missingColors.get(vertex).add(color);
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

    // Get the maximum color used by the algorithm
    public int maxColor() {
        return maxColor;
    }

    // Flip the (c1, c2)-alternating path starting from vertex
    public boolean FlipAlternatingPath(int vertex, int c1, int c2) {

        if (c1 == c2) {
            return true; // No need to flip if colors are the same
        }
        
        boolean missingC1 = isMissing(vertex, c1);
        boolean missingC2 = isMissing(vertex, c2);

        if (missingC1 && missingC2) {
            return true; // Alternating path is empty
        }

        if (!missingC1 && !missingC2) {
            return false; // Vertex has both colors, no (c1,c2)-alternating path starts here
        }

        // Determine the current color to start flipping
        Integer nextColor = null;
        if (!missingC1) {
            nextColor = c1;
        }
        if (!missingC2) {
            nextColor = c2;
        }

        int currentVertex = vertex;

        // Construct the alternating path
        List<Edge> path = new ArrayList<>();
        
        while (!isMissing(currentVertex, nextColor)) {

            // Get the edge incident on currentVertex with nextColor
            Integer neighbor = getEdgeWithColorAtVertex(currentVertex, nextColor);
            if (neighbor == null) {
                throw new IllegalStateException("Algorithm failed to flip path: No edge with color " + nextColor + " was found incident on vertex " + currentVertex);
            }

            Edge edge = new Edge(currentVertex, neighbor);
            path.add(edge);

            // Move to the neighbor vertex
            currentVertex = neighbor;

            // Alternate the color for the next iteration
            nextColor = (nextColor == c1) ? c2 : c1;
        }

        // Flip the colors along the path. Note that we must ensure that the coloring is valid at all times.
        HashMap<Edge, Integer> newColors = new HashMap<>();
        
        // Uncolor the edges in the path
        for (Edge edge : path) {
            int currentColor = getEdgeColor(edge);
            int newColor = (currentColor == c1) ? c2 : c1;
            newColors.put(edge, newColor);
            uncolorEdge(edge);
        }

        // Set the new colors for the edges in the path
        for (Edge edge : path) {
            int newColor = newColors.get(edge);
            if (!setEdgeColor(edge, newColor)) {
                throw new IllegalStateException("Failed to set new color " + newColor + " for edge " + edge + ". This should not happen if the algorithm is correct.");
            }
        }

        return true; // Successfully flipped the alternating path
    }

    // Checks if the edge coloring is valid
    public boolean isValid(boolean verbose) {
        
        boolean valid = true;
        boolean complete = true;

        for (Integer u : edges.keySet()) {
            Set<Integer> colorsAtU = new HashSet<>();

            for (Integer v : edges.get(u)) {
                Edge edge = new Edge(u, v);

                if (complete && coloring.get(edge) == 0) {
                    complete = false; // Found an uncolored edge
                }
                if (valid && coloring.get(edge) == null) {
                    valid = false; // Edge not found in coloring
                } else if (valid && colorsAtU.contains(coloring.get(edge))) {
                    valid = false; // Duplicate color at vertex u
                } else {
                    colorsAtU.add(coloring.get(edge));
                }
            }
        }

        if (verbose) {
            System.out.println("Edge coloring (valid: " + valid + ", complete: " + complete + ")");
        }

        return valid && complete;
    }
}