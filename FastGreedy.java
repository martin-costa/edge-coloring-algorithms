import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class FastGreedy implements EdgeColoringAlgorithm {

    private float epsilon;

    // Constructor to initialize the FastGreedy algorithm that computes a (2 + epsilon)Delta-edge coloring
    public FastGreedy(float epsilon) {

        this.epsilon = epsilon;
    }

    @Override
    public EdgeColoring color(Graph graph) {

        // Create an empty (2 + epsilon)Delta-edge coloring
        int maxColor = ((int)Math.ceil(2 + epsilon)) * graph.maxDegree();

        EdgeColoring coloring = new EdgeColoring(graph, maxColor);

        // Greedily color each edge
        Random rand = new Random();

        HashMap<Integer, Set<Integer>> edges = graph.getEdges();

        for (Integer u : edges.keySet()) {
            for (Integer v : edges.get(u)) {

                // While the edge is not colored, randomly sample a color
                while (coloring.getEdgeColor(u, v) == 0) {
                    
                    Integer color = rand.nextInt(maxColor) + 1;

                    // If the color is missing for both vertices, color the edge
                    if (coloring.isMissing(u, color) && coloring.isMissing(v, color)) {
                        coloring.setEdgeColor(u, v, color);
                    }
                }
            }
        }

        return coloring;
    }
}