import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    
    // Number of vertices in the graph
    private int n;

    // Numer of edges in the graph
    private int m;

    // Maximum degree of the graph
    private int maxDegree;

    // Map to store the edges of the graph
    private HashMap<Integer, Set<Integer>> edges;

    public Graph(int n) {
        this.n = n;
        this.m = 0;
    }

    public Graph(HashMap<Integer, Set<Integer>> edges) {
        this.n = edges.size();
        this.edges = edges;

        int maxDegree = 0;
        int m = 0;
        for (Integer u : edges.keySet()) {
            Set<Integer> neighbors = edges.get(u);
            m += neighbors.size();
            maxDegree = Math.max(maxDegree, neighbors.size());
        }
        this.m = m / 2; // Each edge is counted twice
        this.maxDegree = maxDegree;
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

    // Get the edges of the graph
    public HashMap<Integer, Set<Integer>> getEdges() {
        return edges;
    }
}

// class Edge {

//     // TODO: Implement Edge class so that (u,v) = (v,u)

//     // Vertices of the edge
//     private int u;
//     private int v;

//     public Edge(int u, int v) {
//         this.u = u;
//         this.v = v;
//     }

//     public int u() {
//         return u;
//     }

//     public int v() {
//         return v;
//     }

//     @Override
//     public String toString() {
//         return "(" + u + ", " + v + ")";
//     }
// }

class Edge {
    private final int u;
    private final int v;

    public Edge(int u, int v) {
        // Always store the smaller vertex first for consistency
        if (u < v) {
            this.u = u;
            this.v = v;
        } else {
            this.u = v;
            this.v = u;
        }
    }

    public int u() {
        return u;
    }

    public int v() {
        return v;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return u == edge.u && v == edge.v;
    }

    @Override
    public int hashCode() {
        // Order does not matter: use a commutative hash
        return Integer.hashCode(u) * 31 + Integer.hashCode(v);
    }

    @Override
    public String toString() {
        return "(" + u + ", " + v + ")";
    }
}