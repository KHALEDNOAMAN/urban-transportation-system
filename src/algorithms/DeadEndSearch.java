package algorithms;

import graph.AdjacencyNode;
import graph.Edge;
import graph.Graph;
import graph.Station;
import structures.CustomStack;
import structures.DoublyLinkedListNode;

/**
 * Uses iterative DFS to find reachable dead-end stations.
 */
public class DeadEndSearch {
    /**
     * Traverses from a starting station and returns all reachable degree-one stations.
     */
    public int[] findDeadEnds(Graph graph, int startingStationId) {
        int capacity = graph.stationSlotCount();
        boolean[] visited = new boolean[capacity];
        int[] result = new int[capacity];
        int resultCount = 0;
        CustomStack<Integer> stack = new CustomStack<Integer>();
        stack.push(Integer.valueOf(startingStationId));
        while (!stack.isEmpty()) {
            int stationId = stack.pop().intValue();
            if (stationId < 0 || stationId >= capacity || visited[stationId]) {
                continue;
            }
            Station station = graph.getActiveStation(stationId);
            if (station == null) {
                continue;
            }
            visited[stationId] = true;
            if (isDeadEnd(station)) {
                result[resultCount] = stationId;
                resultCount++;
            }
            int[] neighbors = new int[station.degree()];
            int neighborCount = 0;
            DoublyLinkedListNode<AdjacencyNode> node = station.getAdjacency().getHead();
            while (node != null) {
                AdjacencyNode adjacency = node.getValue();
                Edge edge = graph.getEdge(adjacency.getEdgeId());
                int neighborId = adjacency.getNeighborStationId();
                if (edge != null && edge.isActive() && graph.getActiveStation(neighborId) != null && !visited[neighborId]) {
                    neighbors[neighborCount] = neighborId;
                    neighborCount++;
                }
                node = node.getNext();
            }
            for (int i = neighborCount - 1; i >= 0; i--) {
                stack.push(Integer.valueOf(neighbors[i]));
            }
        }
        stack.clear();
        int[] trimmed = new int[resultCount];
        for (int i = 0; i < resultCount; i++) {
            trimmed[i] = result[i];
        }
        return trimmed;
    }

    /**
     * Defines a dead end as a station with exactly one adjacent route.
     */
    public boolean isDeadEnd(Station station) {
        return station != null && station.degree() == 1;
    }
}
