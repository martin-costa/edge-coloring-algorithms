import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Vizing implements EdgeColoringAlgorithm {
    
    @Override
    public EdgeColoring color(Graph graph) {

        // Compute a Delta coloring for a bipartite graph
        int maxColor = graph.maxDegree() + 1;

        // Create an empty edge coloring
        EdgeColoring coloring = new EdgeColoring(graph, maxColor);

        HashMap<Integer, Set<Integer>> edges = graph.getEdges();

        // Vizing's theorem
        for (Integer u : edges.keySet()) {
            for (Integer v : edges.get(u)) {

                // Color the edge (u,v)
                Edge edge = new Edge(u, v);

                if (coloring.getEdgeColor(edge) != 0) {
                    // If the edge is already colored, skip it
                    continue;
                }

                VizingFan fan = new VizingFan(u, edge, coloring);
                
                fan.activate();
            }
        }

        return coloring;
    }
}

class VizingFan {

    // Store the coloring
    EdgeColoring coloring;

    // Center vertex of the fan
    int center;
    int primaryColor;
    int secondaryColor;

    // The leaves of the fan
    List<Integer> leaves;
    Map<Integer, Integer> leafToColor;

    // Constructs a Vizing fan at the edge 'edge' with center 'center'
    public VizingFan(int center, Edge edge, EdgeColoring coloring) {

        this.coloring = coloring;

        // Check that edge is uncolored
        if (coloring.getEdgeColor(edge) != 0) {
            throw new IllegalArgumentException("Edge must be uncolored to construct a Vizing fan.");
        }

        // Check that center is incident to edge
        if (edge.u() != center && edge.v() != center) {
            throw new IllegalArgumentException("Center must be incident to the edge to construct a Vizing fan.");
        }

        // Initialize the fan
        this.center = center;
        this.primaryColor = coloring.getMissingColor(center);

        this.leaves = new ArrayList<>();
        this.leafToColor = new HashMap<>();
        
        Set<Integer> leafColors = new HashSet<>();

        Integer newLeaf = (edge.u() == center) ? edge.v() : edge.u();
        this.leaves.add(newLeaf);

        Integer newLeafColor = coloring.getMissingColor(newLeaf);
        this.leafToColor.put(newLeaf, newLeafColor);

        boolean fanComplete = false;

        // While there are no repeated colors, add leaves
        while (!fanComplete) {

            // Edge newEdge = coloring.getEdgeWithColorAtVertex(center, newLeafColor);

            newLeaf = coloring.getEdgeWithColorAtVertex(center, newLeafColor);

            if (newLeaf == null) {
                fanComplete = true; // The fan is of type I
                this.primaryColor = newLeafColor;
                this.secondaryColor = newLeafColor;
                return;
            }

            newLeafColor = coloring.getMissingColor(newLeaf);

            if (newLeafColor == null) {
                throw new IllegalStateException("No missing color found for the vertex " + newLeaf + ". This should not happen if the algorithm is correct.");
            }

            if (leafColors.contains(newLeafColor)) {
                fanComplete = true; // The fan is of type II
                this.secondaryColor = newLeafColor;
            }

            this.leaves.add(newLeaf);
            this.leafToColor.put(newLeaf, newLeafColor);
            leafColors.add(newLeafColor);
        }
    }

    // Activate the Vizing fan by constructing a Vizing chain, extending the coloring to an edge
    public boolean activate() {

        // Rotate the fan so that the uncolored edge has type (primaryColor, secondaryColor)
        Integer currentLeaf = leaves.get(0);

        int i = 0;
        while (leafToColor.get(currentLeaf) != secondaryColor) {

            Integer nextLeaf = leaves.get(i + 1);

            if (nextLeaf == null) {
                throw new IllegalStateException("Failed to find next leaf in the fan. This should not happen if the fan is constructed correctly.");
            }

            // Rotate the uncolored edge to the next leaf
            this.coloring.uncolorEdge(new Edge(center, nextLeaf));
            this.coloring.setEdgeColor(center, currentLeaf, leafToColor.get(currentLeaf));

            currentLeaf = nextLeaf;
            i++;
        }

        // Flip the alternating path starting from the center vertex
        coloring.FlipAlternatingPath(center, primaryColor, secondaryColor);

        // Attempt to color the uncolored edge
        coloring.setEdgeColor(center, currentLeaf, secondaryColor);

        // Check if the edge is now colored
        if (coloring.getEdgeColor(new Edge(center, currentLeaf)) != 0) {
            return true;
        }

        // If the edge is still uncolored, the continue the rotation
        int fanSize = leaves.size();
        while (i < fanSize - 1) {

            currentLeaf = leaves.get(i);
            Integer nextLeaf = leaves.get(i + 1);

            if (nextLeaf == null) {
                throw new IllegalStateException("Failed to find next leaf in the fan. This should not happen if the fan is constructed correctly.");
            }

            // Rotate the uncolored edge to the next leaf
            Integer nextEdgeColor = coloring.getEdgeColor(center, nextLeaf);
            this.coloring.uncolorEdge(center, nextLeaf);
            this.coloring.setEdgeColor(center, currentLeaf, nextEdgeColor);

            currentLeaf = nextLeaf;

            i++;
        }

        // Color the last edge
        return this.coloring.setEdgeColor(center, currentLeaf, secondaryColor);
    }
}