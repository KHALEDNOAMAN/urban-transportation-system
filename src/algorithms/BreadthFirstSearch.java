package algorithms;

import graph.AdjacencyNode;
import graph.Edge;
import graph.Graph;
import graph.Station;
import structures.CustomQueue;
import structures.DoublyLinkedListNode;

/**
 * Implements BFS to find a route with the fewest edges between two stations.
 */
public class BreadthFirstSearch {
    /**
     * Finds a minimum-stop route and sums the weights of that discovered route.
     */
    public PathResult find(Graph graph, int sourceId, int destinationId) {
        int capacity = graph.stationSlotCount();
        if (sourceId == destinationId) {
            int[] single = new int[1];
            single[0] = sourceId;
            return new PathResult(true, single, 1, 0.0);
        }
        boolean[] visited = new boolean[capacity];
        int[] parent = new int[capacity];
        double[] parentWeight = new double[capacity];
        for (int i = 0; i < capacity; i++) {
            parent[i] = -1;
        }
        CustomQueue<Integer> queue = new CustomQueue<Integer>();
        visited[sourceId] = true;
        queue.enqueue(Integer.valueOf(sourceId));
        while (!queue.isEmpty()) {
            int currentId = queue.dequeue().intValue();
            Station current = graph.getActiveStation(currentId);
            if (current == null) {
                continue;
            }
            DoublyLinkedListNode<AdjacencyNode> node = current.getAdjacency().getHead();
            while (node != null) {
                AdjacencyNode adjacency = node.getValue();
                int neighborId = adjacency.getNeighborStationId();
                Edge edge = graph.getEdge(adjacency.getEdgeId());
                if (edge != null && edge.isActive() && graph.getActiveStation(neighborId) != null && !visited[neighborId]) {
                    visited[neighborId] = true;
                    parent[neighborId] = currentId;
                    parentWeight[neighborId] = adjacency.getWeight();
                    if (neighborId == destinationId) {
                        queue.clear();
                        break;
                    }
                    queue.enqueue(Integer.valueOf(neighborId));
                }
                node = node.getNext();
            }
        }
        if (!visited[destinationId]) {
            return new PathResult(false, null, 0, 0.0);
        }
        return buildPath(parent, parentWeight, sourceId, destinationId, capacity);
    }

    /**
     * Reconstructs a BFS path by following parent links from destination to source.
     */
    private PathResult buildPath(int[] parent, double[] parentWeight, int sourceId, int destinationId, int capacity) {
        int[] reverse = new int[capacity];
        int length = 0;
        double cost = 0.0;
        int current = destinationId;
        while (current != -1) {
            reverse[length] = current;
            length++;
            if (current == sourceId) {
                break;
            }
            cost += parentWeight[current];
            current = parent[current];
        }
        int[] path = new int[length];
        for (int i = 0; i < length; i++) {
            path[i] = reverse[length - 1 - i];
        }
        return new PathResult(true, path, length, cost);
    }
}
