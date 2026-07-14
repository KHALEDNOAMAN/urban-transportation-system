package algorithms;

import cli.NumberFormatter;
import graph.Edge;
import graph.EdgeStore;
import graph.Graph;
import graph.Station;

/**
 * Computes network diameter using Floyd-Warshall all-pairs shortest paths.
 */
public class FloydWarshallDiameter {
    /**
     * Builds a distance matrix, runs Floyd-Warshall, and formats the diameter result.
     */
    public String compute(Graph graph) {
        int stationCount = graph.activeStationCount();
        if (stationCount <= 1 || graph.getEdgeStore().activeCount() == 0) {
            return "Graph Disconnected";
        }
        int[] stationIds = new int[stationCount];
        int[] idToIndex = new int[graph.stationSlotCount() + 1];
        for (int i = 0; i < idToIndex.length; i++) {
            idToIndex[i] = -1;
        }
        int index = 0;
        for (int stationId = 0; stationId < graph.stationSlotCount(); stationId++) {
            Station station = graph.getActiveStation(stationId);
            if (station != null) {
                stationIds[index] = stationId;
                idToIndex[stationId] = index;
                index++;
            }
        }
        double[][] distance = new double[stationCount][stationCount];
        for (int i = 0; i < stationCount; i++) {
            for (int j = 0; j < stationCount; j++) {
                distance[i][j] = i == j ? 0.0 : Double.POSITIVE_INFINITY;
            }
        }
        EdgeStore store = graph.getEdgeStore();
        for (int i = 0; i < store.size(); i++) {
            Edge edge = store.getAtIndex(i);
            if (edge == null || !edge.isActive()) {
                continue;
            }
            int firstIndex = idToIndex[edge.getFirstStationId()];
            int secondIndex = idToIndex[edge.getSecondStationId()];
            if (firstIndex >= 0 && secondIndex >= 0 && edge.getWeight() < distance[firstIndex][secondIndex]) {
                distance[firstIndex][secondIndex] = edge.getWeight();
                distance[secondIndex][firstIndex] = edge.getWeight();
            }
        }
        for (int k = 0; k < stationCount; k++) {
            for (int i = 0; i < stationCount; i++) {
                for (int j = 0; j < stationCount; j++) {
                    double through = distance[i][k] + distance[k][j];
                    if (through < distance[i][j]) {
                        distance[i][j] = through;
                    }
                }
            }
        }
        double diameter = -1.0;
        String firstName = null;
        String secondName = null;
        for (int i = 0; i < stationCount; i++) {
            for (int j = i + 1; j < stationCount; j++) {
                if (Double.isInfinite(distance[i][j])) {
                    return "Graph Disconnected";
                }
                Station first = graph.getActiveStation(stationIds[i]);
                Station second = graph.getActiveStation(stationIds[j]);
                String orderedFirst = first.getName().compareTo(second.getName()) <= 0 ? first.getName() : second.getName();
                String orderedSecond = first.getName().compareTo(second.getName()) <= 0 ? second.getName() : first.getName();
                if (distance[i][j] > diameter || (Double.compare(distance[i][j], diameter) == 0
                        && pairComesFirst(orderedFirst, orderedSecond, firstName, secondName))) {
                    diameter = distance[i][j];
                    firstName = orderedFirst;
                    secondName = orderedSecond;
                }
            }
        }
        return "Network Diameter: " + NumberFormatter.format(diameter)
                + "\nLongest-Shortest Path: " + firstName + " -> " + secondName;
    }

    /**
     * Chooses the lexicographically smaller station pair when diameter values tie.
     */
    private boolean pairComesFirst(String first, String second, String currentFirst, String currentSecond) {
        if (currentFirst == null) {
            return true;
        }
        int byFirst = first.compareTo(currentFirst);
        if (byFirst != 0) {
            return byFirst < 0;
        }
        return second.compareTo(currentSecond) < 0;
    }
}
