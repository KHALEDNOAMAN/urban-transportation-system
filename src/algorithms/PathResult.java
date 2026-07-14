package algorithms;

/**
 * Immutable result object for route-finding algorithms.
 */
public class PathResult {
    private final boolean found;
    private final int[] stationIds;
    private final int length;
    private final double totalCost;

    /**
     * Stores whether a path was found, the station sequence, and total path cost.
     */
    public PathResult(boolean found, int[] stationIds, int length, double totalCost) {
        this.found = found;
        this.stationIds = stationIds;
        this.length = length;
        this.totalCost = totalCost;
    }

    /**
     * Reports whether the requested route exists.
     */
    public boolean isFound() {
        return found;
    }

    /**
     * Returns the path station IDs in travel order.
     */
    public int[] getStationIds() {
        return stationIds;
    }

    /**
     * Returns how many entries in the station ID array belong to the path.
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the summed cost of the selected route.
     */
    public double getTotalCost() {
        return totalCost;
    }
}
