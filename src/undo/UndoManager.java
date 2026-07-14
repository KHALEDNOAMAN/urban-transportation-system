package undo;

import cli.OutputMessages;
import graph.Graph;
import graph.Station;
import structures.CustomStack;

/**
 * Manages undo history using the custom stack.
 */
public class UndoManager {
    private final CustomStack<UndoRecord> records;

    /**
     * Creates an empty undo stack.
     */
    public UndoManager() {
        records = new CustomStack<UndoRecord>();
    }

    /**
     * Stores the inverse information for a successful station addition.
     */
    public void pushStationAdded(int stationId, String stationName) {
        records.push(UndoRecord.stationAdded(stationId, stationName));
    }

    /**
     * Stores the inverse information for a successful edge addition.
     */
    public void pushEdgeAdded(int edgeId, int firstStationId, int secondStationId, double weight) {
        records.push(UndoRecord.edgeAdded(edgeId, firstStationId, secondStationId, weight));
    }

    /**
     * Reverses the most recent successful modifying command and returns its message.
     */
    public String undo(Graph graph) {
        UndoRecord record = records.pop();
        if (record == null) {
            return OutputMessages.NOTHING_TO_UNDO;
        }
        if (record.getType() == UndoType.ADD_EDGE) {
            Station first = graph.getStation(record.getFirstStationId());
            Station second = graph.getStation(record.getSecondStationId());
            String firstName = first == null ? "Unknown" : first.getName();
            String secondName = second == null ? "Unknown" : second.getName();
            graph.removeEdgeById(record.getEdgeId());
            return "Edge between '" + firstName + "' and '" + secondName + "' removed successfully.";
        }
        graph.removeStationById(record.getStationId());
        return "Station '" + record.getStationName() + "' removed successfully.";
    }

    /**
     * Clears all pending undo records during shutdown.
     */
    public void clear() {
        records.clear();
    }
}
