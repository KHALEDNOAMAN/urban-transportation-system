package graph;

/**
 * Maintains all graph edges in a manually resized array for MST and undo access.
 */
public class EdgeStore {
    private Edge[] edges;
    private int size;
    private int activeCount;

    /**
     * Creates an empty edge store with initial array capacity.
     */
    public EdgeStore() {
        edges = new Edge[16];
    }

    /**
     * Adds a new active edge and returns the stored edge object.
     */
    public Edge add(int firstStationId, int secondStationId, double weight, int creationOrder) {
        ensureCapacity(size + 1);
        Edge edge = new Edge(size, firstStationId, secondStationId, weight, creationOrder);
        edges[size] = edge;
        size++;
        activeCount++;
        return edge;
    }

    /**
     * Returns an edge by ID, or null if the ID is outside the used range.
     */
    public Edge get(int edgeId) {
        if (edgeId < 0 || edgeId >= size) {
            return null;
        }
        return edges[edgeId];
    }

    /**
     * Deactivates an edge so algorithms ignore it without changing historical IDs.
     */
    public boolean deactivate(int edgeId) {
        Edge edge = get(edgeId);
        if (edge == null || !edge.isActive()) {
            return false;
        }
        edge.deactivate();
        activeCount--;
        return true;
    }

    /**
     * Returns the number of edge slots ever assigned.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the number of edges still active in the graph.
     */
    public int activeCount() {
        return activeCount;
    }

    /**
     * Returns the edge stored at a raw array index.
     */
    public Edge getAtIndex(int index) {
        return get(index);
    }

    /**
     * Clears all stored edge references and resets counts.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            edges[i] = null;
        }
        size = 0;
        activeCount = 0;
    }

    /**
     * Grows the edge array when another edge would exceed current capacity.
     */
    private void ensureCapacity(int required) {
        if (required <= edges.length) {
            return;
        }
        Edge[] next = new Edge[edges.length * 2];
        copyEdges(edges, next, edges.length);
        edges = next;
    }

    /**
     * Copies edge references with an explicit loop to avoid prohibited array helpers.
     */
    private void copyEdges(Edge[] source, Edge[] destination, int count) {
        int index = 0;
        while (index < count) {
            destination[index] = source[index];
            index++;
        }
    }
}
