package graph;

/**
 * Stores a single undirected weighted edge in the graph-wide edge store.
 */
public class Edge {
    private final int id;
    private final int firstStationId;
    private final int secondStationId;
    private final double weight;
    private final int creationOrder;
    private boolean active;

    /**
     * Creates an active route between two station IDs with a stable creation order.
     */
    public Edge(int id, int firstStationId, int secondStationId, double weight, int creationOrder) {
        this.id = id;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.weight = weight;
        this.creationOrder = creationOrder;
        this.active = true;
    }

    /**
     * Returns the unique edge ID used by adjacency entries and undo records.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the first endpoint station ID as originally inserted.
     */
    public int getFirstStationId() {
        return firstStationId;
    }

    /**
     * Returns the second endpoint station ID as originally inserted.
     */
    public int getSecondStationId() {
        return secondStationId;
    }

    /**
     * Returns the cost or distance associated with this route.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the insertion order used as a deterministic tie-breaker.
     */
    public int getCreationOrder() {
        return creationOrder;
    }

    /**
     * Reports whether this edge is still part of the active graph.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Marks the edge as removed while keeping its historical ID stable.
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Returns the smaller endpoint ID for deterministic edge sorting.
     */
    public int minEndpointId() {
        return firstStationId < secondStationId ? firstStationId : secondStationId;
    }

    /**
     * Returns the larger endpoint ID for deterministic edge sorting.
     */
    public int maxEndpointId() {
        return firstStationId > secondStationId ? firstStationId : secondStationId;
    }
}
