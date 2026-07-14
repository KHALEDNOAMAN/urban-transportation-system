package cli;

/**
 * Centralizes user-facing output strings so expected text is easy to adjust.
 */
public class OutputMessages {
    public static final String STATION_ADDED = "Station added successfully";
    public static final String INVALID_COMMAND = "Invalid command";
    public static final String INVALID_ARGUMENTS = "Invalid arguments";
    public static final String STATION_EXISTS = "Station already exists";
    public static final String STATION_NOT_FOUND = "Station not found";
    public static final String INVALID_WEIGHT = "Invalid weight";
    public static final String NEGATIVE_WEIGHT = "Negative weight not allowed";
    public static final String SELF_LOOP = "Self-loops are not allowed";
    public static final String EDGE_EXISTS = "Edge already exists";
    public static final String NO_ROUTE = "No route found";
    public static final String NO_DEAD_ENDS = "No dead-ends found";
    public static final String NO_MST = "No MST Found";
    public static final String GRAPH_DISCONNECTED = "Graph Disconnected";
    public static final String NO_STATIONS_FOUND = "No stations found";
    public static final String NOTHING_TO_UNDO = "No operations to undo";

    /**
     * Prevents construction because this class only stores constants.
     */
    private OutputMessages() {
    }
}
