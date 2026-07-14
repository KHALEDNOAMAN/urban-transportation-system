package structures;

/**
 * Stores one station and tentative distance inside the custom min heap.
 */
public class HeapNode {
    public int stationId;
    public double distance;

    /**
     * Creates a heap entry for a station-distance pair.
     */
    public HeapNode(int stationId, double distance) {
        this.stationId = stationId;
        this.distance = distance;
    }
}
