import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cli.CommandProcessor;
import graph.Graph;
import undo.UndoManager;

/**
 * Program entry point that reads commands from standard input until EXIT or EOF.
 */
public class Main {
    /**
     * Initializes the graph application, dispatches input lines, and prints results.
     */
    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();
        UndoManager undoManager = new UndoManager();
        CommandProcessor processor = new CommandProcessor(graph, undoManager);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String output = processor.processLine(line);
            if (output != null && output.length() > 0) {
                System.out.println(output);
            }
            if (processor.isExitRequested()) {
                return;
            }
        }
        processor.processLine(null);
    }
}
