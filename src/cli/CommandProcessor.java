package cli;

import algorithms.BreadthFirstSearch;
import algorithms.DeadEndSearch;
import algorithms.DijkstraShortestPath;
import algorithms.FloydWarshallDiameter;
import algorithms.KruskalMST;
import algorithms.PathResult;
import graph.Edge;
import graph.Graph;
import graph.Station;
import structures.RedBlackNode;
import undo.UndoManager;

/**
 * Parses input lines, dispatches commands, and formats command results.
 */
public class CommandProcessor {
    private final Graph graph;
    private final UndoManager undoManager;
    private boolean exitRequested;

    /**
     * Connects the command processor to the graph and undo manager it controls.
     */
    public CommandProcessor(Graph graph, UndoManager undoManager) {
        this.graph = graph;
        this.undoManager = undoManager;
    }

    /**
     * Processes one input line and returns the output text, or null for no output.
     */
    public String processLine(String line) {
        if (line == null) {
            cleanup();
            exitRequested = true;
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.length() == 0) {
            return null;
        }
        String[] parts = trimmed.split("\\s+");
        String command = parts[0];
        if ("EXIT".equals(command)) {
            if (parts.length != 1) {
                return OutputMessages.INVALID_ARGUMENTS;
            }
            cleanup();
            exitRequested = true;
            return null;
        }
        if ("ADD_STATION".equals(command)) {
            return addStation(parts);
        }
        if ("ADD_EDGE".equals(command)) {
            return addEdge(parts);
        }
        if ("BFS".equals(command)) {
            return bfs(parts);
        }
        if ("SHORTEST_PATH".equals(command)) {
            return shortestPath(parts);
        }
        if ("DEADENDS".equals(command)) {
            return deadEnds(parts);
        }
        if ("MST".equals(command)) {
            return parts.length == 1 ? new KruskalMST().build(graph) : OutputMessages.INVALID_ARGUMENTS;
        }
        if ("DIAMETER".equals(command)) {
            return parts.length == 1 ? new FloydWarshallDiameter().compute(graph) : OutputMessages.INVALID_ARGUMENTS;
        }
        if ("SEARCH".equals(command)) {
            return search(parts);
        }
        if ("UNDO".equals(command)) {
            return parts.length == 1 ? undoManager.undo(graph) : OutputMessages.INVALID_ARGUMENTS;
        }
        return OutputMessages.INVALID_COMMAND;
    }

    /**
     * Reports whether EXIT or EOF requested termination.
     */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /**
     * Clears graph and undo state for EXIT and EOF.
     */
    public void cleanup() {
        graph.clear();
        undoManager.clear();
    }

    /**
     * Handles ADD_STATION validation, graph insertion, and undo recording.
     */
    private String addStation(String[] parts) {
        if (parts.length != 2) {
            return OutputMessages.INVALID_ARGUMENTS;
        }
        if (graph.findStationId(parts[1]) >= 0) {
            return OutputMessages.STATION_EXISTS;
        }
        Station station = graph.addStation(parts[1]);
        if (station == null) {
            return OutputMessages.STATION_EXISTS;
        }
        undoManager.pushStationAdded(station.getId(), station.getName());
        return OutputMessages.STATION_ADDED;
    }

    /**
     * Handles ADD_EDGE validation, graph insertion, and undo recording.
     */
    private String addEdge(String[] parts) {
        if (parts.length != 4) {
            return OutputMessages.INVALID_ARGUMENTS;
        }
        double weight;
        try {
            weight = Double.parseDouble(parts[3]);
        } catch (NumberFormatException ex) {
            return OutputMessages.INVALID_WEIGHT;
        }
        if (Double.isNaN(weight) || Double.isInfinite(weight)) {
            return OutputMessages.INVALID_WEIGHT;
        }
        if (weight < 0.0) {
            return OutputMessages.NEGATIVE_WEIGHT;
        }
        int firstId = graph.findStationId(parts[1]);
        int secondId = graph.findStationId(parts[2]);
        if (firstId < 0 || secondId < 0) {
            return OutputMessages.STATION_NOT_FOUND;
        }
        if (firstId == secondId) {
            return OutputMessages.SELF_LOOP;
        }
        if (graph.hasActiveEdge(firstId, secondId)) {
            return OutputMessages.EDGE_EXISTS;
        }
        Edge edge = graph.addEdge(firstId, secondId, weight);
        if (edge == null) {
            return OutputMessages.EDGE_EXISTS;
        }
        undoManager.pushEdgeAdded(edge.getId(), firstId, secondId, weight);
        return "Edge added between '" + parts[1] + "' and '" + parts[2] + "' with weight " + NumberFormatter.format(weight);
    }

    /**
     * Runs BFS for a minimum-stop route command.
     */
    private String bfs(String[] parts) {
        if (parts.length != 3) {
            return OutputMessages.INVALID_ARGUMENTS;
        }
        int source = graph.findStationId(parts[1]);
        int destination = graph.findStationId(parts[2]);
        if (source < 0 || destination < 0) {
            return OutputMessages.STATION_NOT_FOUND;
        }
        PathResult result = new BreadthFirstSearch().find(graph, source, destination);
        return result.isFound() ? formatPath(result) : OutputMessages.NO_ROUTE;
    }

    /**
     * Runs Dijkstra for a minimum-cost route command.
     */
    private String shortestPath(String[] parts) {
        if (parts.length != 3) {
            return OutputMessages.INVALID_ARGUMENTS;
        }
        int source = graph.findStationId(parts[1]);
        int destination = graph.findStationId(parts[2]);
        if (source < 0 || destination < 0) {
            return OutputMessages.STATION_NOT_FOUND;
        }
        PathResult result = new DijkstraShortestPath().find(graph, source, destination);
        return result.isFound() ? formatPath(result) : OutputMessages.NO_ROUTE;
    }

    /**
     * Runs iterative DFS and formats reachable dead-end stations.
     */
    private String deadEnds(String[] parts) {
        if (parts.length != 2) {
            return OutputMessages.INVALID_ARGUMENTS;
        }
        int source = graph.findStationId(parts[1]);
        if (source < 0) {
            return OutputMessages.STATION_NOT_FOUND;
        }
        int[] stationIds = new DeadEndSearch().findDeadEnds(graph, source);
        if (stationIds.length == 0) {
            return OutputMessages.NO_DEAD_ENDS;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Dead-ends reachable from ").append(parts[1]).append(": ");
        for (int i = 0; i < stationIds.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(graph.getActiveStation(stationIds[i]).getName());
        }
        return builder.toString();
    }

    /**
     * Runs red-black-tree prefix search and formats alphabetical station names.
     */
    private String search(String[] parts) {
        if (parts.length != 2) {
            return OutputMessages.INVALID_ARGUMENTS;
        }
        String prefix = parts[1];
        RedBlackNode node = graph.getStationTree().lowerBound(prefix);
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while (node != null && node.key.startsWith(prefix)) {
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(node.key);
            count++;
            node = graph.getStationTree().successor(node);
        }
        return count == 0 ? OutputMessages.NO_STATIONS_FOUND : builder.toString();
    }

    /**
     * Converts a path result into the required route and total-cost output.
     */
    private String formatPath(PathResult result) {
        StringBuilder builder = new StringBuilder();
        int[] path = result.getStationIds();
        for (int i = 0; i < result.getLength(); i++) {
            if (i > 0) {
                builder.append(" -> ");
            }
            Station station = graph.getActiveStation(path[i]);
            builder.append(station == null ? "Unknown" : station.getName());
        }
        builder.append(" Total Cost: ").append(NumberFormatter.format(result.getTotalCost()));
        return builder.toString();
    }
}
