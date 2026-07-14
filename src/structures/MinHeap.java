package structures;

/**
 * Manual binary min heap used as Dijkstra's priority queue.
 */
public class MinHeap {
    private HeapNode[] heap;
    private int[] position;
    private int size;

    /**
     * Creates a heap and a station-position array for efficient decreaseKey.
     */
    public MinHeap(int capacity, int maxStationIdExclusive) {
        int heapCapacity = capacity < 1 ? 1 : capacity;
        heap = new HeapNode[heapCapacity];
        position = new int[maxStationIdExclusive < 1 ? 1 : maxStationIdExclusive];
        clearPositions();
    }

    /**
     * Reports whether the heap has no entries.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Reports whether a station currently has an entry in the heap.
     */
    public boolean contains(int stationId) {
        return stationId >= 0 && stationId < position.length && position[stationId] >= 0;
    }

    /**
     * Inserts a station with its current tentative distance.
     */
    public void insert(int stationId, double distance) {
        ensurePositionCapacity(stationId + 1);
        ensureHeapCapacity(size + 1);
        HeapNode node = new HeapNode(stationId, distance);
        heap[size] = node;
        position[stationId] = size;
        siftUp(size);
        size++;
    }

    /**
     * Removes and returns the station with the smallest tentative distance.
     */
    public HeapNode extractMin() {
        if (size == 0) {
            return null;
        }
        HeapNode min = heap[0];
        size--;
        HeapNode replacement = heap[size];
        heap[size] = null;
        position[min.stationId] = -1;
        if (size > 0) {
            heap[0] = replacement;
            position[replacement.stationId] = 0;
            siftDown(0);
        }
        return min;
    }

    /**
     * Lowers a station's distance and restores heap order.
     */
    public void decreaseKey(int stationId, double newDistance) {
        if (!contains(stationId)) {
            return;
        }
        int index = position[stationId];
        if (newDistance > heap[index].distance) {
            return;
        }
        heap[index].distance = newDistance;
        siftUp(index);
    }

    /**
     * Clears heap contents and station position tracking.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
        clearPositions();
    }

    /**
     * Moves a node upward until the parent is no longer larger.
     */
    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (!less(heap[current], heap[parent])) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
    }

    /**
     * Moves a node downward until both children are no smaller.
     */
    private void siftDown(int index) {
        int current = index;
        while (true) {
            int left = current * 2 + 1;
            int right = left + 1;
            int smallest = current;
            if (left < size && less(heap[left], heap[smallest])) {
                smallest = left;
            }
            if (right < size && less(heap[right], heap[smallest])) {
                smallest = right;
            }
            if (smallest == current) {
                break;
            }
            swap(current, smallest);
            current = smallest;
        }
    }

    /**
     * Compares heap nodes by distance and station ID for deterministic ties.
     */
    private boolean less(HeapNode first, HeapNode second) {
        int byDistance = Double.compare(first.distance, second.distance);
        if (byDistance != 0) {
            return byDistance < 0;
        }
        return first.stationId < second.stationId;
    }

    /**
     * Swaps heap entries and updates their station positions.
     */
    private void swap(int firstIndex, int secondIndex) {
        HeapNode temp = heap[firstIndex];
        heap[firstIndex] = heap[secondIndex];
        heap[secondIndex] = temp;
        position[heap[firstIndex].stationId] = firstIndex;
        position[heap[secondIndex].stationId] = secondIndex;
    }

    /**
     * Grows the heap array when another insert would exceed capacity.
     */
    private void ensureHeapCapacity(int required) {
        if (required <= heap.length) {
            return;
        }
        HeapNode[] next = new HeapNode[heap.length * 2];
        for (int i = 0; i < heap.length; i++) {
            next[i] = heap[i];
        }
        heap = next;
    }

    /**
     * Grows the station-position array so any station ID can be tracked.
     */
    private void ensurePositionCapacity(int required) {
        if (required <= position.length) {
            return;
        }
        int newCapacity = position.length * 2;
        while (newCapacity < required) {
            newCapacity *= 2;
        }
        int[] next = new int[newCapacity];
        for (int i = 0; i < next.length; i++) {
            next[i] = -1;
        }
        for (int i = 0; i < position.length; i++) {
            next[i] = position[i];
        }
        position = next;
    }

    /**
     * Marks every station as absent from the heap.
     */
    private void clearPositions() {
        for (int i = 0; i < position.length; i++) {
            position[i] = -1;
        }
    }
}
