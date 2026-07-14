package graph;

import structures.DoublyLinkedList;
import structures.DoublyLinkedListNode;
import structures.HashTable;
import structures.RedBlackTree;

/**
 * Owns the transportation network and coordinates all station, edge, and lookup structures.
 */
public class Graph {
    private Station[] stations;
    private int stationSlots;
    private int activeStationCount;
    private int nextCreationOrder;
    private final EdgeStore edgeStore;
    private final HashTable stationIds;
    private final RedBlackTree stationTree;

    /**
     * Creates an empty graph with custom lookup structures and edge storage.
     */
    public Graph() {
        stations = new Station[16];
        edgeStore = new EdgeStore();
        stationIds = new HashTable();
        stationTree = new RedBlackTree();
    }

    /**
     * Adds a station if its name is unique and indexes it in the hash table and tree.
     */
    public Station addStation(String name) {
        if (stationIds.contains(name)) {
            return null;
        }
        ensureStationCapacity(stationSlots + 1);
        Station station = new Station(stationSlots, name);
        stations[stationSlots] = station;
        stationSlots++;
        activeStationCount++;
        stationIds.put(name, station.getId());
        stationTree.insert(name, station.getId());
        return station;
    }

    /**
     * Adds a simple undirected weighted edge and creates both adjacency entries.
     */
    public Edge addEdge(int firstStationId, int secondStationId, double weight) {
        Station first = getActiveStation(firstStationId);
        Station second = getActiveStation(secondStationId);
        if (first == null || second == null || firstStationId == secondStationId) {
            return null;
        }
        if (hasActiveEdge(firstStationId, secondStationId)) {
            return null;
        }
        Edge edge = edgeStore.add(firstStationId, secondStationId, weight, nextCreationOrder);
        nextCreationOrder++;
        first.getAdjacency().addLast(new AdjacencyNode(secondStationId, edge.getId(), weight));
        second.getAdjacency().addLast(new AdjacencyNode(firstStationId, edge.getId(), weight));
        return edge;
    }

    /**
     * Removes an edge by deleting both adjacency entries and deactivating the store record.
     */
    public boolean removeEdgeById(int edgeId) {
        Edge edge = edgeStore.get(edgeId);
        if (edge == null || !edge.isActive()) {
            return false;
        }
        Station first = getStation(edge.getFirstStationId());
        Station second = getStation(edge.getSecondStationId());
        if (first != null) {
            removeAdjacencyEntry(first, edgeId);
        }
        if (second != null) {
            removeAdjacencyEntry(second, edgeId);
        }
        return edgeStore.deactivate(edgeId);
    }

    /**
     * Removes an active station from lookup structures and clears its adjacency list.
     */
    public boolean removeStationById(int stationId) {
        Station station = getActiveStation(stationId);
        if (station == null) {
            return false;
        }
        stationIds.remove(station.getName());
        stationTree.delete(station.getName());
        station.clearAdjacency();
        station.deactivate();
        activeStationCount--;
        return true;
    }

    /**
     * Looks up a station name and returns its ID, or -1 when missing.
     */
    public int findStationId(String name) {
        return stationIds.get(name);
    }

    /**
     * Returns a station by ID regardless of whether it has been deactivated.
     */
    public Station getStation(int stationId) {
        if (stationId < 0 || stationId >= stationSlots) {
            return null;
        }
        return stations[stationId];
    }

    /**
     * Returns a station only if it exists and is still active.
     */
    public Station getActiveStation(int stationId) {
        Station station = getStation(stationId);
        return station != null && station.isActive() ? station : null;
    }

    /**
     * Returns a stored edge by its global ID.
     */
    public Edge getEdge(int edgeId) {
        return edgeStore.get(edgeId);
    }

    /**
     * Exposes the edge store for graph algorithms that need all routes.
     */
    public EdgeStore getEdgeStore() {
        return edgeStore;
    }

    /**
     * Exposes the station tree used by prefix search.
     */
    public RedBlackTree getStationTree() {
        return stationTree;
    }

    /**
     * Returns the number of station slots assigned, including deactivated stations.
     */
    public int stationSlotCount() {
        return stationSlots;
    }

    /**
     * Returns the number of stations currently active in the graph.
     */
    public int activeStationCount() {
        return activeStationCount;
    }

    /**
     * Checks whether an active edge already connects two station IDs.
     */
    public boolean hasActiveEdge(int firstStationId, int secondStationId) {
        Station first = getActiveStation(firstStationId);
        if (first == null || getActiveStation(secondStationId) == null) {
            return false;
        }
        DoublyLinkedListNode<AdjacencyNode> current = first.getAdjacency().getHead();
        while (current != null) {
            AdjacencyNode adjacency = current.getValue();
            Edge edge = edgeStore.get(adjacency.getEdgeId());
            if (edge != null && edge.isActive() && adjacency.getNeighborStationId() == secondStationId) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Clears all graph state and releases references for normal shutdown.
     */
    public void clear() {
        for (int i = 0; i < stationSlots; i++) {
            if (stations[i] != null) {
                stations[i].clearAdjacency();
                stations[i].deactivate();
                stations[i] = null;
            }
        }
        stationSlots = 0;
        activeStationCount = 0;
        nextCreationOrder = 0;
        edgeStore.clear();
        stationIds.clear();
        stationTree.clear();
    }

    /**
     * Validates that every active undirected edge has both adjacency entries.
     */
    public boolean validateUndirectedEdges() {
        for (int i = 0; i < edgeStore.size(); i++) {
            Edge edge = edgeStore.getAtIndex(i);
            if (edge == null || !edge.isActive()) {
                continue;
            }
            if (!hasAdjacency(edge.getFirstStationId(), edge.getSecondStationId(), edge.getId())) {
                return false;
            }
            if (!hasAdjacency(edge.getSecondStationId(), edge.getFirstStationId(), edge.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks one station's adjacency list for a specific neighbor and edge ID.
     */
    private boolean hasAdjacency(int stationId, int neighborId, int edgeId) {
        Station station = getActiveStation(stationId);
        if (station == null) {
            return false;
        }
        DoublyLinkedListNode<AdjacencyNode> current = station.getAdjacency().getHead();
        while (current != null) {
            AdjacencyNode adjacency = current.getValue();
            if (adjacency.getNeighborStationId() == neighborId && adjacency.getEdgeId() == edgeId) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Removes one adjacency entry from a station for the supplied edge ID.
     */
    private void removeAdjacencyEntry(Station station, final int edgeId) {
        DoublyLinkedList<AdjacencyNode> adjacency = station.getAdjacency();
        adjacency.removeFirstMatch(new DoublyLinkedList.Matcher<AdjacencyNode>() {
            public boolean matches(AdjacencyNode value) {
                return value.getEdgeId() == edgeId;
            }
        });
    }

    /**
     * Expands the station array when a new station would exceed capacity.
     */
    private void ensureStationCapacity(int required) {
        if (required <= stations.length) {
            return;
        }
        Station[] next = new Station[stations.length * 2];
        for (int i = 0; i < stations.length; i++) {
            next[i] = stations[i];
        }
        stations = next;
    }
}
