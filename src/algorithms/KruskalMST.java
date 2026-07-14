package algorithms;

import cli.NumberFormatter;
import graph.Edge;
import graph.EdgeStore;
import graph.Graph;
import graph.Station;
import structures.DisjointSet;
import structures.MergeSort;

/**
 * Builds a minimum spanning tree using Kruskal's algorithm.
 */
public class KruskalMST {
    /**
     * Selects MST edges with custom merge sort and disjoint sets, then formats output.
     */
    public String build(Graph graph) {
        int activeStations = graph.activeStationCount();
        if (activeStations == 0) {
            return "No MST Found";
        }
        EdgeStore store = graph.getEdgeStore();
        Edge[] edges = new Edge[store.activeCount()];
        int edgeCount = 0;
        for (int i = 0; i < store.size(); i++) {
            Edge edge = store.getAtIndex(i);
            if (edge != null && edge.isActive()
                    && graph.getActiveStation(edge.getFirstStationId()) != null
                    && graph.getActiveStation(edge.getSecondStationId()) != null) {
                edges[edgeCount] = edge;
                edgeCount++;
            }
        }
        new MergeSort().sortEdges(edges, edgeCount);
        DisjointSet sets = new DisjointSet(graph.stationSlotCount() + 1);
        sets.reset(graph.stationSlotCount() + 1);
        Edge[] selected = new Edge[activeStations > 1 ? activeStations - 1 : 0];
        int selectedCount = 0;
        double total = 0.0;
        for (int i = 0; i < edgeCount && selectedCount < activeStations - 1; i++) {
            Edge edge = edges[i];
            if (sets.union(edge.getFirstStationId(), edge.getSecondStationId())) {
                selected[selectedCount] = edge;
                selectedCount++;
                total += edge.getWeight();
            }
        }
        sets.clear();
        if (selectedCount < activeStations - 1) {
            return "No MST Found";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("MST Total Weight: ").append(NumberFormatter.format(total)).append('\n');
        builder.append("Routes to build:");
        for (int i = 0; i < selectedCount; i++) {
            Edge edge = selected[i];
            Station first = graph.getActiveStation(edge.getFirstStationId());
            Station second = graph.getActiveStation(edge.getSecondStationId());
            builder.append('\n')
                    .append("- (").append(first.getName()).append(", ").append(second.getName()).append("): ")
                    .append(NumberFormatter.format(edge.getWeight()));
        }
        return builder.toString();
    }
}
