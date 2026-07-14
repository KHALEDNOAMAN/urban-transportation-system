package structures;

/**
 * Manual LIFO stack used by DFS and undo records.
 */
public class CustomStack<T> {
    private StackNode<T> top;
    private int size;

    /**
     * Pushes a value onto the top of the stack.
     */
    public void push(T value) {
        StackNode<T> node = new StackNode<>(value);
        node.next = top;
        top = node;
        size++;
    }

    /**
     * Removes and returns the top value, or null if empty.
     */
    public T pop() {
        if (top == null) {
            return null;
        }
        StackNode<T> node = top;
        top = top.next;
        node.next = null;
        T value = node.value;
        node.value = null;
        size--;
        return value;
    }

    /**
     * Returns the top value without removing it.
     */
    public T peek() {
        return top == null ? null : top.value;
    }

    /**
     * Reports whether the stack currently has no values.
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns the number of values in the stack.
     */
    public int size() {
        return size;
    }

    /**
     * Clears all stack nodes and releases references.
     */
    public void clear() {
        while (top != null) {
            StackNode<T> next = top.next;
            top.value = null;
            top.next = null;
            top = next;
        }
        size = 0;
    }
}
