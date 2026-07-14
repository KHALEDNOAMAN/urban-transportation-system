package structures;

/**
 * Manual FIFO queue used by breadth-first search.
 */
public class CustomQueue<T> {
    private QueueNode<T> head;
    private QueueNode<T> tail;

    /**
     * Adds a value to the back of the queue.
     */
    public void enqueue(T value) {
        QueueNode<T> node = new QueueNode<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
    }

    /**
     * Removes and returns the oldest queued value, or null if empty.
     */
    public T dequeue() {
        if (head == null) {
            return null;
        }
        QueueNode<T> node = head;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        node.next = null;
        T value = node.value;
        node.value = null;
        return value;
    }

    /**
     * Returns the next value without removing it.
     */
    public T peek() {
        return head == null ? null : head.value;
    }

    /**
     * Reports whether the queue contains no values.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Removes all queued nodes and clears references.
     */
    public void clear() {
        while (head != null) {
            QueueNode<T> next = head.next;
            head.value = null;
            head.next = null;
            head = next;
        }
        tail = null;
    }
}
