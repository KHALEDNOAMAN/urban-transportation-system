package structures;

import graph.Edge;

/**
 * Manual merge sort used to order edges before Kruskal's algorithm.
 */
public class MergeSort {
    /**
     * Sorts the first length entries of an edge array in deterministic MST order.
     */
    public void sortEdges(Edge[] edges, int length) {
        if (edges == null || length <= 1) {
            return;
        }
        Edge[] temporary = new Edge[length];
        mergeSort(edges, temporary, 0, length - 1);
    }

    /**
     * Recursively splits the edge range before merging sorted halves.
     */
    private void mergeSort(Edge[] edges, Edge[] temporary, int left, int right) {
        if (left >= right) {
            return;
        }
        int middle = left + (right - left) / 2;
        mergeSort(edges, temporary, left, middle);
        mergeSort(edges, temporary, middle + 1, right);
        merge(edges, temporary, left, middle, right);
    }

    /**
     * Merges two sorted edge ranges back into the main array.
     */
    private void merge(Edge[] edges, Edge[] temporary, int left, int middle, int right) {
        int leftIndex = left;
        int rightIndex = middle + 1;
        int output = left;
        while (leftIndex <= middle && rightIndex <= right) {
            if (compare(edges[leftIndex], edges[rightIndex]) <= 0) {
                temporary[output] = edges[leftIndex];
                leftIndex++;
            } else {
                temporary[output] = edges[rightIndex];
                rightIndex++;
            }
            output++;
        }
        while (leftIndex <= middle) {
            temporary[output] = edges[leftIndex];
            leftIndex++;
            output++;
        }
        while (rightIndex <= right) {
            temporary[output] = edges[rightIndex];
            rightIndex++;
            output++;
        }
        for (int i = left; i <= right; i++) {
            edges[i] = temporary[i];
            temporary[i] = null;
        }
    }

    /**
     * Compares edges by weight, endpoints, and creation order for deterministic output.
     */
    private int compare(Edge first, Edge second) {
        int byWeight = Double.compare(first.getWeight(), second.getWeight());
        if (byWeight != 0) {
            return byWeight;
        }
        int byFirstEndpoint = first.minEndpointId() - second.minEndpointId();
        if (byFirstEndpoint != 0) {
            return byFirstEndpoint;
        }
        int bySecondEndpoint = first.maxEndpointId() - second.maxEndpointId();
        if (bySecondEndpoint != 0) {
            return bySecondEndpoint;
        }
        return first.getCreationOrder() - second.getCreationOrder();
    }
}
