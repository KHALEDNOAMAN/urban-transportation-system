package graph;

/**
 * Represents one entry in a station's adjacency list for an undirected route.
 */
public class AdjacencyNode {
    private final int neighborStationId;
    private final int edgeId;
    private final double weight;

    /**
     * Creates an adjacency entry pointing to a neighboring station through one edge.
     */
    public AdjacencyNode(int neighborStationId, int edgeId, double weight) {
        this.neighborStationId = neighborStationId;
        this.edgeId = edgeId;
        this.weight = weight;
    }

    /**
     * Returns the station ID reached by this adjacency entry.
     */
    public int getNeighborStationId() {
        return neighborStationId;
    }

    /**
     * Returns the global edge ID shared by both directions of this route.
     */
    public int getEdgeId() {
        return edgeId;
    }

    /**
     * Returns the route weight used by shortest-path algorithms.
     */
    public double getWeight() {
        return weight;
    }
}
