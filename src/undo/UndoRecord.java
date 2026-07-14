package undo;

/**
 * Stores the minimal information needed to reverse one successful graph modification.
 */
public class UndoRecord {
    private final UndoType type;
    private final int stationId;
    private final String stationName;
    private final int edgeId;
    private final int firstStationId;
    private final int secondStationId;
    private final double weight;

    /**
     * Creates a record for either station or edge undo data.
     */
    private UndoRecord(UndoType type, int stationId, String stationName, int edgeId,
            int firstStationId, int secondStationId, double weight) {
        this.type = type;
        this.stationId = stationId;
        this.stationName = stationName;
        this.edgeId = edgeId;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.weight = weight;
    }

    /**
     * Builds an undo record for a successful ADD_STATION command.
     */
    public static UndoRecord stationAdded(int stationId, String stationName) {
        return new UndoRecord(UndoType.ADD_STATION, stationId, stationName, -1, -1, -1, 0.0);
    }

    /**
     * Builds an undo record for a successful ADD_EDGE command.
     */
    public static UndoRecord edgeAdded(int edgeId, int firstStationId, int secondStationId, double weight) {
        return new UndoRecord(UndoType.ADD_EDGE, -1, null, edgeId, firstStationId, secondStationId, weight);
    }

    /**
     * Returns the kind of operation this record reverses.
     */
    public UndoType getType() {
        return type;
    }

    /**
     * Returns the station ID used when undoing an added station.
     */
    public int getStationId() {
        return stationId;
    }

    /**
     * Returns the station name used when undoing an added station.
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Returns the edge ID used when undoing an added route.
     */
    public int getEdgeId() {
        return edgeId;
    }

    /**
     * Returns the first endpoint ID for an edge undo record.
     */
    public int getFirstStationId() {
        return firstStationId;
    }

    /**
     * Returns the second endpoint ID for an edge undo record.
     */
    public int getSecondStationId() {
        return secondStationId;
    }

    /**
     * Returns the edge weight captured for completeness in undo records.
     */
    public double getWeight() {
        return weight;
    }
}
