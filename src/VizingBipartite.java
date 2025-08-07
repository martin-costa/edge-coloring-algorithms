import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class VizingBipartite implements EdgeColoringAlgorithm {
    
    @Override
    public EdgeColoring color(Graph graph) {

        // Compute a Delta coloring for a bipartite graph
        int maxColor = graph.maxDegree();

        // Create an empty edge coloring
        EdgeColoring coloring = new EdgeColoring(graph, maxColor);

        HashMap<Integer, Set<Integer>> edges = graph.getEdges();

        // Vizing's theorem for bipartite graphs
        for (Integer u : edges.keySet()) {
            for (Integer v : edges.get(u)) {

                // Color the edge (u,v)
                Edge edge = new Edge(u, v);

                if (coloring.getEdgeColor(edge) != 0) {
                    // If the edge is already colored, skip it
                    continue;
                }

                Integer c1  = coloring.getMissingColor(u);
                Integer c2  = coloring.getMissingColor(v);

                if (c1 == null) {
                    throw new IllegalStateException("No missing color found for the vertex " + u + ". This should not happen if the graph is bipartite and the algorithm is correct.");
                }
                if (c2 == null) {
                    throw new IllegalStateException("No missing color found for the vertex " + v + ". This should not happen if the graph is bipartite and the algorithm is correct.");
                }
                if (c1 == c2) {
                    // If both vertices have the same missing color, color the edge with that color
                    coloring.setEdgeColor(u, v, c1);
                }
                else {
                    // If they have different missing colors, flip the (c1, c2)-alternating path at v
                    coloring.FlipAlternatingPath(v, c2, c1);
                    if (!coloring.isMissing(u, c1) || !coloring.isMissing(v, c1)) {
                        throw new IllegalStateException("Failed to prime edge by flipping path. This should not happen if the graph is bipartite and the algorithm is correct.");
                    }
                    coloring.setEdgeColor(u, v, c1);
                }
            }
        }

        return coloring;
    }
}
