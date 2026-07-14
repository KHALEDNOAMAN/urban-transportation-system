package tests;

import cli.CommandProcessor;
import graph.Edge;
import graph.Graph;
import structures.CustomQueue;
import structures.CustomStack;
import structures.DisjointSet;
import structures.DoublyLinkedList;
import structures.HashTable;
import structures.HeapNode;
import structures.MergeSort;
import structures.MinHeap;
import structures.RedBlackNode;
import structures.RedBlackTree;
import undo.UndoManager;

/**
 * Lightweight no-JUnit test runner for custom structures and command integration.
 */
public class ProjectTestRunner {
    private static int assertions;

    /**
     * Runs every focused unit-style test and reports the assertion count.
     */
    public static void main(String[] args) {
        testLinkedList();
        testHashTable();
        testQueue();
        testStack();
        testMinHeap();
        testRedBlackTree();
        testDisjointSet();
        testMergeSort();
        testGraphAndCommands();
        System.out.println("All tests passed: " + assertions);
    }

    /**
     * Verifies insertion, removal, traversal endpoints, and clearing for the linked list.
     */
    private static void testLinkedList() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals(3, list.size(), "linked list size after inserts");
        assertEquals("A", list.getHead().getValue(), "linked list head");
        assertEquals("C", list.getTail().getValue(), "linked list tail");
        boolean removedMiddle = list.removeFirstMatch(value -> "B".equals(value));
        assertTrue(removedMiddle, "linked list removes middle");
        assertEquals(2, list.size(), "linked list size after middle removal");
        list.removeNode(list.getHead());
        list.removeNode(list.getTail());
        assertTrue(list.isEmpty(), "linked list empty after removals");
        list.addFirst("X");
        list.clear();
        assertTrue(list.isEmpty(), "linked list clear");
    }

    /**
     * Verifies lookup, duplicate handling, resizing, removal, and clearing for the hash table.
     */
    private static void testHashTable() {
        HashTable table = new HashTable();
        assertTrue(table.put("A", 1), "hash insert A");
        assertTrue(table.put("B", 2), "hash insert B");
        assertFalse(table.put("A", 3), "hash rejects duplicate");
        assertEquals(1, table.get("A"), "hash lookup A");
        for (int i = 0; i < 80; i++) {
            table.put("K" + i, i);
        }
        assertEquals(79, table.get("K79"), "hash lookup after resize");
        assertTrue(table.remove("B"), "hash remove B");
        assertEquals(-1, table.get("B"), "hash missing after removal");
        table.clear();
        assertEquals(0, table.size(), "hash clear size");
    }

    /**
     * Verifies FIFO behavior and clearing for the custom queue.
     */
    private static void testQueue() {
        CustomQueue<Integer> queue = new CustomQueue<>();
        assertTrue(queue.isEmpty(), "queue starts empty");
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.peek(), "queue peek");
        assertEquals(1, queue.dequeue(), "queue first out");
        assertEquals(2, queue.dequeue(), "queue second out");
        assertTrue(queue.isEmpty(), "queue empty after reads");
        queue.enqueue(3);
        queue.clear();
        assertTrue(queue.isEmpty(), "queue clear");
    }

    /**
     * Verifies LIFO behavior and clearing for the custom stack.
     */
    private static void testStack() {
        CustomStack<Integer> stack = new CustomStack<>();
        assertTrue(stack.isEmpty(), "stack starts empty");
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.peek(), "stack peek");
        assertEquals(2, stack.pop(), "stack first pop");
        assertEquals(1, stack.pop(), "stack second pop");
        assertTrue(stack.isEmpty(), "stack empty after pops");
        stack.push(3);
        stack.clear();
        assertTrue(stack.isEmpty(), "stack clear");
    }

    /**
     * Verifies heap ordering, decreaseKey, and empty-state behavior.
     */
    private static void testMinHeap() {
        MinHeap heap = new MinHeap(2, 8);
        heap.insert(3, 30.0);
        heap.insert(1, 10.0);
        heap.insert(2, 20.0);
        heap.decreaseKey(3, 5.0);
        HeapNode first = heap.extractMin();
        assertEquals(3, first.stationId, "heap decrease key result");
        assertEquals(1, heap.extractMin().stationId, "heap next min");
        assertEquals(2, heap.extractMin().stationId, "heap final min");
        assertTrue(heap.isEmpty(), "heap empty");
    }

    /**
     * Verifies red-black insertion, search, lowerBound, successor, deletion, and validation.
     */
    private static void testRedBlackTree() {
        RedBlackTree tree = new RedBlackTree();
        assertTrue(tree.insert("C", 3), "tree insert C");
        assertTrue(tree.insert("A", 1), "tree insert A");
        assertTrue(tree.insert("B", 2), "tree insert B");
        assertTrue(tree.insert("E", 5), "tree insert E");
        assertTrue(tree.insert("D", 4), "tree insert D");
        assertFalse(tree.insert("D", 9), "tree duplicate");
        assertTrue(tree.validate(), "tree validates after inserts");
        assertEquals(2, tree.search("B"), "tree search B");
        RedBlackNode lower = tree.lowerBound("B");
        assertEquals("B", lower.key, "tree lower bound exact");
        assertEquals("C", tree.successor(lower).key, "tree successor");
        assertTrue(tree.delete("A"), "tree delete leaf");
        assertTrue(tree.validate(), "tree validates after leaf delete");
        assertTrue(tree.delete("C"), "tree delete two-child/root-ish");
        assertTrue(tree.validate(), "tree validates after delete");
        tree.clear();
        assertEquals(0, tree.size(), "tree clear");
    }

    /**
     * Verifies union, repeated union, find, and path compression behavior.
     */
    private static void testDisjointSet() {
        DisjointSet set = new DisjointSet(6);
        assertTrue(set.union(1, 2), "disjoint union 1-2");
        assertTrue(set.union(2, 3), "disjoint union 2-3");
        assertFalse(set.union(1, 3), "disjoint repeated union");
        assertEquals(set.find(1), set.find(3), "disjoint path");
    }

    /**
     * Verifies edge sorting by weight, endpoints, and creation order.
     */
    private static void testMergeSort() {
        Edge[] edges = new Edge[5];
        edges[0] = new Edge(0, 3, 4, 5.0, 0);
        edges[1] = new Edge(1, 1, 2, 1.0, 1);
        edges[2] = new Edge(2, 0, 2, 1.0, 2);
        edges[3] = new Edge(3, 1, 3, 1.0, 3);
        edges[4] = new Edge(4, 2, 4, 8.0, 4);
        new MergeSort().sortEdges(edges, edges.length);
        assertEquals(2, edges[0].getId(), "merge sort tie first endpoint");
        assertEquals(1, edges[1].getId(), "merge sort tie second endpoint");
        assertEquals(3, edges[2].getId(), "merge sort tie creation");
        assertEquals(0, edges[3].getId(), "merge sort weight order");
        assertEquals(4, edges[4].getId(), "merge sort largest");
    }

    /**
     * Exercises the public command flow across graph updates, algorithms, and undo.
     */
    private static void testGraphAndCommands() {
        Graph graph = new Graph();
        UndoManager undo = new UndoManager();
        CommandProcessor processor = new CommandProcessor(graph, undo);
        assertEquals("Station added successfully", processor.processLine("ADD_STATION Tajrish"), "add Tajrish");
        assertEquals("Station added successfully", processor.processLine("ADD_STATION Mirdamad"), "add Mirdamad");
        assertEquals("Station added successfully", processor.processLine("ADD_STATION ValiAsr"), "add ValiAsr");
        assertEquals("Station added successfully", processor.processLine("ADD_STATION RahAhan"), "add RahAhan");
        assertEquals("Station added successfully", processor.processLine("ADD_STATION Azadi"), "add Azadi");
        assertEquals("Station already exists", processor.processLine("ADD_STATION Tajrish"), "duplicate station");
        assertEquals("Edge added between 'Tajrish' and 'Mirdamad' with weight 5.5",
                processor.processLine("ADD_EDGE Tajrish Mirdamad 5.5"), "add edge 1");
        assertEquals("Edge added between 'Mirdamad' and 'ValiAsr' with weight 10.0",
                processor.processLine("ADD_EDGE Mirdamad ValiAsr 10"), "add edge 2");
        assertEquals("Edge added between 'ValiAsr' and 'RahAhan' with weight 30.0",
                processor.processLine("ADD_EDGE ValiAsr RahAhan 30"), "add edge 3");
        assertEquals("Edge added between 'Tajrish' and 'Azadi' with weight 100.0",
                processor.processLine("ADD_EDGE Tajrish Azadi 100"), "add edge 4");
        assertEquals("Station not found", processor.processLine("ADD_EDGE Unknown Tajrish 4"), "missing edge station");
        assertEquals("Tajrish -> Mirdamad -> ValiAsr -> RahAhan Total Cost: 45.5",
                processor.processLine("BFS Tajrish RahAhan"), "bfs route");
        assertEquals("Tajrish -> Mirdamad -> ValiAsr -> RahAhan Total Cost: 45.5",
                processor.processLine("SHORTEST_PATH Tajrish RahAhan"), "dijkstra route");
        assertEquals("Tajrish Total Cost: 0.0", processor.processLine("BFS Tajrish Tajrish"), "same source route");
        assertEquals("Dead-ends reachable from Tajrish: RahAhan, Azadi",
                processor.processLine("DEADENDS Tajrish"), "dead ends");
        assertEquals("Tajrish", processor.processLine("SEARCH T"), "prefix search");
        String mst = processor.processLine("MST");
        assertContains(mst, "MST Total Weight: 145.5", "mst total");
        assertContains(mst, "- (Tajrish, Azadi): 100.0", "mst route includes Azadi weight");
        assertEquals("Edge between 'Tajrish' and 'Azadi' removed successfully.", processor.processLine("UNDO"), "undo edge");
        assertTrue(graph.validateUndirectedEdges(), "graph validates after undo edge");
        assertEquals("Station not found", processor.processLine("SHORTEST_PATH Tajrish Unknown"), "missing shortest station");

        Graph graph2 = new Graph();
        CommandProcessor processor2 = new CommandProcessor(graph2, new UndoManager());
        processor2.processLine("ADD_STATION A");
        processor2.processLine("ADD_STATION B");
        processor2.processLine("ADD_STATION C");
        processor2.processLine("ADD_EDGE A B 1");
        processor2.processLine("ADD_EDGE B C 2");
        processor2.processLine("ADD_EDGE A C 5");
        assertEquals("Network Diameter: 3.0\nLongest-Shortest Path: A -> C",
                processor2.processLine("DIAMETER"), "diameter");
        assertEquals("Edge between 'A' and 'C' removed successfully.", processor2.processLine("UNDO"), "undo edge graph2");
        assertEquals("Edge between 'B' and 'C' removed successfully.", processor2.processLine("UNDO"), "undo edge graph2 second");
        assertEquals("Edge between 'A' and 'B' removed successfully.", processor2.processLine("UNDO"), "undo edge graph2 third");
        assertEquals("Station 'C' removed successfully.", processor2.processLine("UNDO"), "undo station C");
    }

    /**
     * Fails the test run if a boolean condition is false.
     */
    private static void assertTrue(boolean value, String message) {
        assertions++;
        if (!value) {
            throw new AssertionError(message);
        }
    }

    /**
     * Fails the test run if a boolean condition is true.
     */
    private static void assertFalse(boolean value, String message) {
        assertTrue(!value, message);
    }

    /**
     * Compares two integers and fails with context if they differ.
     */
    private static void assertEquals(int expected, int actual, String message) {
        assertions++;
        if (expected != actual) {
            throw new AssertionError(message + " expected " + expected + " but got " + actual);
        }
    }

    /**
     * Compares two strings and fails with context if they differ.
     */
    private static void assertEquals(String expected, String actual, String message) {
        assertions++;
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null || !expected.equals(actual)) {
            throw new AssertionError(message + " expected [" + expected + "] but got [" + actual + "]");
        }
    }

    /**
     * Fails if a larger output string does not contain the expected text.
     */
    private static void assertContains(String text, String expectedPart, String message) {
        assertions++;
        if (text == null || !text.contains(expectedPart)) {
            throw new AssertionError(message + " expected to contain [" + expectedPart + "] but got [" + text + "]");
        }
    }
}
