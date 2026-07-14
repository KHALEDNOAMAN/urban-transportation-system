package structures;

/**
 * Node used internally by the custom LIFO stack.
 */
public class StackNode<T> {
    T value;
    StackNode<T> next;

    /**
     * Creates a stack node storing one pushed value.
     */
    StackNode(T value) {
        this.value = value;
    }
}
