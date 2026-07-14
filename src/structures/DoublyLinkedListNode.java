package structures;

/**
 * Node used by the manual doubly linked list implementation.
 */
public class DoublyLinkedListNode<T> {
    private T value;
    private DoublyLinkedListNode<T> previous;
    private DoublyLinkedListNode<T> next;

    /**
     * Creates a node holding one value.
     */
    public DoublyLinkedListNode(T value) {
        this.value = value;
    }

    /**
     * Returns the value currently stored in this node.
     */
    public T getValue() {
        return value;
    }

    /**
     * Replaces the node value, mainly used while clearing references.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns the previous node in the list.
     */
    public DoublyLinkedListNode<T> getPrevious() {
        return previous;
    }

    /**
     * Updates the previous pointer during insertions and removals.
     */
    void setPrevious(DoublyLinkedListNode<T> previous) {
        this.previous = previous;
    }

    /**
     * Returns the next node in the list.
     */
    public DoublyLinkedListNode<T> getNext() {
        return next;
    }

    /**
     * Updates the next pointer during insertions and removals.
     */
    void setNext(DoublyLinkedListNode<T> next) {
        this.next = next;
    }
}
