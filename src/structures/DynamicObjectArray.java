package structures;

/**
 * Simple manually resized object array for indexed dynamic storage.
 */
public class DynamicObjectArray<T> {
    private Object[] values;
    private int size;

    /**
     * Creates the array with a default initial capacity.
     */
    public DynamicObjectArray() {
        this(8);
    }

    /**
     * Creates the array with a caller-selected initial capacity.
     */
    public DynamicObjectArray(int initialCapacity) {
        int capacity = initialCapacity < 1 ? 1 : initialCapacity;
        values = new Object[capacity];
    }

    /**
     * Appends a value at the next used index.
     */
    public void add(T value) {
        ensureCapacity(size + 1);
        values[size] = value;
        size++;
    }

    /**
     * Stores a value at an index, growing the array when needed.
     */
    public void set(int index, T value) {
        ensureCapacity(index + 1);
        values[index] = value;
        if (index >= size) {
            size = index + 1;
        }
    }

    /**
     * Returns the value at an index, or null if it is outside the used range.
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) values[index];
    }

    /**
     * Returns the number of used slots.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the current backing-array capacity.
     */
    public int capacity() {
        return values.length;
    }

    /**
     * Clears used references while preserving allocated capacity.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            values[i] = null;
        }
        size = 0;
    }

    /**
     * Grows the backing array until it can hold the required slot.
     */
    private void ensureCapacity(int required) {
        if (required <= values.length) {
            return;
        }
        int newCapacity = values.length * 2;
        while (newCapacity < required) {
            newCapacity *= 2;
        }
        Object[] next = new Object[newCapacity];
        for (int i = 0; i < values.length; i++) {
            next[i] = values[i];
        }
        values = next;
    }
}
