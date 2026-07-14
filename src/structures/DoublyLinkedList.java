package structures;

/**
 * Manual doubly linked list used for graph adjacency storage and simple iteration.
 */
public class DoublyLinkedList<T> {
    /**
     * Predicate interface used to find or remove values without Java collections.
     */
    public interface Matcher<T> {
        /**
         * Returns true when the supplied value is the target item.
         */
        boolean matches(T value);
    }

    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    /**
     * Appends a value to the tail and returns the newly created node.
     */
    public DoublyLinkedListNode<T> addLast(T value) {
        DoublyLinkedListNode<T> node = new DoublyLinkedListNode<T>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.setNext(node);
            node.setPrevious(tail);
            tail = node;
        }
        size++;
        return node;
    }

    /**
     * Inserts a value at the head and returns the newly created node.
     */
    public DoublyLinkedListNode<T> addFirst(T value) {
        DoublyLinkedListNode<T> node = new DoublyLinkedListNode<T>(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.setNext(head);
            head.setPrevious(node);
            head = node;
        }
        size++;
        return node;
    }

    /**
     * Removes the first value accepted by the matcher.
     */
    public boolean removeFirstMatch(Matcher<T> matcher) {
        DoublyLinkedListNode<T> node = findNode(matcher);
        if (node == null) {
            return false;
        }
        removeNode(node);
        return true;
    }

    /**
     * Unlinks a known node from the list and clears its neighbor pointers.
     */
    public void removeNode(DoublyLinkedListNode<T> node) {
        if (node == null) {
            return;
        }
        DoublyLinkedListNode<T> previous = node.getPrevious();
        DoublyLinkedListNode<T> next = node.getNext();
        if (previous == null) {
            head = next;
        } else {
            previous.setNext(next);
        }
        if (next == null) {
            tail = previous;
        } else {
            next.setPrevious(previous);
        }
        node.setPrevious(null);
        node.setNext(null);
        size--;
    }

    /**
     * Returns the first value accepted by the matcher, or null when missing.
     */
    public T find(Matcher<T> matcher) {
        DoublyLinkedListNode<T> node = findNode(matcher);
        return node == null ? null : node.getValue();
    }

    /**
     * Returns the first node accepted by the matcher, or null when missing.
     */
    public DoublyLinkedListNode<T> findNode(Matcher<T> matcher) {
        DoublyLinkedListNode<T> current = head;
        while (current != null) {
            if (matcher.matches(current.getValue())) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Returns the head node for manual traversal.
     */
    public DoublyLinkedListNode<T> getHead() {
        return head;
    }

    /**
     * Returns the tail node for reverse traversal or quick tail checks.
     */
    public DoublyLinkedListNode<T> getTail() {
        return tail;
    }

    /**
     * Returns the current number of list nodes.
     */
    public int size() {
        return size;
    }

    /**
     * Reports whether the list currently has no nodes.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all nodes and releases their stored values and links.
     */
    public void clear() {
        DoublyLinkedListNode<T> current = head;
        while (current != null) {
            DoublyLinkedListNode<T> next = current.getNext();
            current.setValue(null);
            current.setPrevious(null);
            current.setNext(null);
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }
}
