package structures;

/**
 * Manual string-to-integer hash table for station-name lookup.
 */
public class HashTable {
    private HashNode[] buckets;
    private int size;

    /**
     * Creates an empty hash table with initial bucket capacity.
     */
    public HashTable() {
        buckets = new HashNode[16];
    }

    /**
     * Inserts a key-value pair and rejects duplicate station names.
     */
    public boolean put(String key, int value) {
        if (key == null) {
            return false;
        }
        if ((size + 1) * 4 > buckets.length * 3) {
            resize();
        }
        int index = bucketIndex(key, buckets.length);
        HashNode current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return false;
            }
            current = current.next;
        }
        HashNode node = new HashNode(key, value);
        node.next = buckets[index];
        buckets[index] = node;
        size++;
        return true;
    }

    /**
     * Returns the mapped integer value, or -1 when the key is missing.
     */
    public int get(String key) {
        HashNode node = findNode(key);
        return node == null ? -1 : node.value;
    }

    /**
     * Reports whether a key exists in the table.
     */
    public boolean contains(String key) {
        return findNode(key) != null;
    }

    /**
     * Removes a key from its bucket chain if present.
     */
    public boolean remove(String key) {
        if (key == null) {
            return false;
        }
        int index = bucketIndex(key, buckets.length);
        HashNode current = buckets[index];
        HashNode previous = null;
        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                current.next = null;
                current.key = null;
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the number of stored key-value pairs.
     */
    public int size() {
        return size;
    }

    /**
     * Clears every bucket chain and releases node references.
     */
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                HashNode next = current.next;
                current.key = null;
                current.next = null;
                current = next;
            }
            buckets[i] = null;
        }
        size = 0;
    }

    /**
     * Searches the correct bucket chain for a key.
     */
    private HashNode findNode(String key) {
        if (key == null) {
            return null;
        }
        int index = bucketIndex(key, buckets.length);
        HashNode current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Doubles the bucket array and re-links existing nodes into new buckets.
     */
    private void resize() {
        HashNode[] old = buckets;
        HashNode[] next = new HashNode[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            HashNode current = old[i];
            while (current != null) {
                HashNode following = current.next;
                int index = bucketIndex(current.key, next.length);
                current.next = next[index];
                next[index] = current;
                current = following;
            }
        }
        buckets = next;
    }

    /**
     * Computes the bucket index with an FNV-1a style string hash.
     */
    private int bucketIndex(String key, int length) {
        long hash = 1469598103934665603L;
        for (int i = 0; i < key.length(); i++) {
            hash ^= key.charAt(i);
            hash *= 1099511628211L;
        }
        long positive = hash & 0x7fffffffffffffffL;
        return (int) (positive % length);
    }
}
