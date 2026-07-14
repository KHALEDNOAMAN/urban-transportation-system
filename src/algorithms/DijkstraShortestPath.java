package algorithms;

import graph.AdjacencyNode;
import graph.Edge;
import graph.Graph;
import graph.Station;
import structures.DoublyLinkedListNode;
import structures.HeapNode;
import structures.MinHeap;

/**
 * Implements Dijkstra's algorithm with the custom min heap.
 */
public class DijkstraShortestPath {
    /**
     * Finds the cheapest weighted path between two active stations.
     */
    public PathResult find(Graph graph, int sourceId, int destinationId) {
        int capacity = graph.stationSlotCount();
        if (sourceId == destinationId) {
            int[] single = new int[1];
            single[0] = sourceId;
            return new PathResult(true, single, 1, 0.0);
        }
        double[] distance = new double[capacity];
        int[] parent = new int[capacity];
        boolean[] finalized = new boolean[capacity];
        for (int i = 0; i < capacity; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        MinHeap heap = new MinHeap(graph.activeStationCount() + 1, capacity);
        distance[sourceId] = 0.0;
        heap.insert(sourceId, 0.0);
        while (!heap.isEmpty()) {
            HeapNode heapNode = heap.extractMin();
            int currentId = heapNode.stationId;
            if (finalized[currentId]) {
                continue;
            }
            finalized[currentId] = true;
            if (currentId == destinationId) {
                break;
            }
            Station current = graph.getActiveStation(currentId);
            if (current == null) {
                continue;
            }
            DoublyLinkedListNode<AdjacencyNode> node = current.getAdjacency().getHead();
            while (node != null) {
                AdjacencyNode adjacency = node.getValue();
                Edge edge = graph.getEdge(adjacency.getEdgeId());
                int neighborId = adjacency.getNeighborStationId();
                if (edge != null && edge.isActive() && graph.getActiveStation(neighborId) != null && !finalized[neighborId]) {
                    double candidate = distance[currentId] + adjacency.getWeight();
                    if (candidate < distance[neighborId]) {
                        distance[neighborId] = candidate;
                        parent[neighborId] = currentId;
                        if (heap.contains(neighborId)) {
                            heap.decreaseKey(neighborId, candidate);
                        } else {
                            heap.insert(neighborId, candidate);
                        }
                    }
                }
                node = node.getNext();
            }
        }
        heap.clear();
        if (Double.isInfinite(distance[destinationId])) {
            return new PathResult(false, null, 0, 0.0);
        }
        return buildPath(parent, sourceId, destinationId, capacity, distance[destinationId]);
    }

    /**
     * Reconstructs a shortest path from parent links after Dijkstra finishes.
     */
    private PathResult buildPath(int[] parent, int sourceId, int destinationId, int capacity, double cost) {
        int[] reverse = new int[capacity];
        int length = 0;
        int current = destinationId;
        while (current != -1) {
            reverse[length] = current;
            length++;
            if (current == sourceId) {
                break;
            }
            current = parent[current];
        }
        int[] path = new int[length];
        for (int i = 0; i < length; i++) {
            path[i] = reverse[length - 1 - i];
        }
        return new PathResult(true, path, length, cost);
    }
}
