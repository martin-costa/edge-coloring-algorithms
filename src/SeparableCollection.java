import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeparableCollection {

    // The edge coloring that this separable collection is built on
    private EdgeColoring edgeColoring;

    // The separable collection of UComponents
    private Set<UComponent> uComponents;

    // Map from vertices and their missing colors to UComponents
    private Map<Pair<Integer, Integer>, UComponent> vertexColorToUComponent;

    public SeparableCollection(EdgeColoring edgeColoring) {
        this.edgeColoring = edgeColoring;
        this.uComponents = new HashSet<>();
        this.vertexColorToUComponent = new HashMap<>();
    }

    public void addUComponent(UComponent uComponent) {
        uComponents.add(uComponent);
        for (int i = 0; i < uComponent.size(); i++) {
            if (vertexColorToUComponent.containsKey(new Pair<>(uComponent.vertices[i], uComponent.missingColors[i]))) {
                throw new IllegalArgumentException("u-component already exists for vertex " + uComponent.vertices[i] + " with color " + uComponent.missingColors[i]);
            }
            vertexColorToUComponent.put(new Pair<>(uComponent.vertices[i], uComponent.missingColors[i]), uComponent);
        }
    }

    // Gets the UComponent for a given vertex and its missing color
    public UComponent getUComponent(int vertex, int missingColor) {
        return vertexColorToUComponent.get(new Pair<>(vertex, missingColor));
    }

    public boolean destroyDamagedComponent(int vertex, int color) {
        // Remove the u-component from the collection
        UComponent uComponent = vertexColorToUComponent.remove(new Pair<>(vertex, color));
        if (uComponent != null) {
            uComponents.remove(uComponent);
            return true;
        }
        return false;
    }
}

abstract class UComponent {

    // The vertices in the u-component
    protected int[] vertices;

    // The missing colors of the vertices in the u-component
    protected int[] missingColors;

    // The edges in the u-component
    protected Edge[] edges;

    // Initializes the u-component with vertices and their missing colors
    public UComponent(int[] vertices, int[] missingColors, Edge[] edges) {
        this.vertices = vertices;
        this.missingColors = missingColors;
        this.edges = edges;
    }

    // Returns the ith vertex of the u-component
    public int getVertex(int i) {
        if (i < 0 || i >= vertices.length) {
            throw new IndexOutOfBoundsException("Index out of bounds for u-component vertices.");
        }
        return vertices[i];
    }

    // Returns the missing color of the ith vertex
    public int getMissingColor(int i) {
        if (i < 0 || i >= missingColors.length) {
            throw new IndexOutOfBoundsException("Index out of bounds for u-component missing colors.");
        }
        return missingColors[i];
    }

    // Size of the u-component
    public int size() {
        return vertices.length;
    }

    public abstract void extend(EdgeColoring coloring, SeparableCollection collection);

    public abstract void socialize(EdgeColoring coloring, int c1, int c2);
}

class UEdge extends UComponent {

    public UEdge(int u, int v, int colorAtU, int colorAtV) {
        super(new int[] {u, v}, new int[] {colorAtU, colorAtV}, new Edge[] {new Edge(u, v)});
    }

    public int u() {
        return vertices[0];
    }

    public int v() {
        return vertices[1];
    }

    public int colorAtU() {
        return missingColors[0];
    }

    public int colorAtV() {
        return missingColors[1];
    }

    public void setColorAtU(int color) {
        missingColors[0] = color;
    }

    public void setColorAtV(int color) {
        missingColors[1] = color;
    }

    public int[] getVertices() {
        return vertices;
    }

    @Override
    public void extend(EdgeColoring coloring, SeparableCollection collection) {
        
        // Flip the alternating path starting from v
        coloring.FlipAlternatingPath(vertices[1], missingColors[0], missingColors[1]);

        // Set the edge color for the edge u-v
        coloring.setEdgeColor(new Edge(vertices[0], vertices[1]), missingColors[0], collection);
    }

    @Override
    public void socialize(EdgeColoring coloring, int c1, int c2) {

    }
}
