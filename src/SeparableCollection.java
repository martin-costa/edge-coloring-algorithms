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
            vertexColorToUComponent.put(new Pair<>(uComponent.vertices[i], uComponent.missingColors[i]), uComponent);
        }
    }

    // Gets the UComponent for a given vertex and its missing color
    public UComponent getUComponent(int vertex, int missingColor) {
        return vertexColorToUComponent.get(new Pair<>(vertex, missingColor));
    }
}

class UComponent {

    // The vertices in the u-component
    protected int[] vertices;

    // The missing colors of the vertices in the u-component
    protected int[] missingColors;

    // Initializes the u-component with vertices and their missing colors
    public UComponent(int[] vertices, int[] missingColors) {
        this.vertices = vertices;
        this.missingColors = missingColors;
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
}

class UEdge extends UComponent {

    public UEdge(int u, int v, int colorAtU, int colorAtV) {
        super(new int[] {u, v}, new int[] {colorAtU, colorAtV});
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
}
