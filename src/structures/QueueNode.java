package structures;

/**
 * Node used internally by the custom FIFO queue.
 */
public class QueueNode<T> {
    T value;
    QueueNode<T> next;

    /**
     * Creates a queue node storing one queued value.
     */
    QueueNode(T value) {
        this.value = value;
    }
}
