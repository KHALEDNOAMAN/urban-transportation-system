package graph;

import structures.DoublyLinkedList;

/**
 * Represents a station vertex and owns its manually implemented adjacency list.
 */
public class Station {
    private final int id;
    private final String name;
    private final DoublyLinkedList<AdjacencyNode> adjacency;
    private boolean active;

    /**
     * Creates an active station with a unique ID and empty adjacency list.
     */
    public Station(int id, String name) {
        this.id = id;
        this.name = name;
        this.adjacency = new DoublyLinkedList<>();
        this.active = true;
    }

    /**
     * Returns the unique integer ID assigned when the station was added.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the station name used in command input and search output.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the adjacency list containing all currently connected route entries.
     */
    public DoublyLinkedList<AdjacencyNode> getAdjacency() {
        return adjacency;
    }

    /**
     * Reports whether this station is still present in the active graph.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Marks the station as removed while preserving old IDs for undo/history safety.
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Removes all adjacency entries owned by this station.
     */
    public void clearAdjacency() {
        adjacency.clear();
    }

    /**
     * Returns the number of adjacent entries, used for dead-end detection.
     */
    public int degree() {
        return adjacency.size();
    }
}
