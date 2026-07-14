package structures;

/**
 * Node used in a hash-table bucket chain for collision handling.
 */
public class HashNode {
    String key;
    int value;
    HashNode next;

    /**
     * Creates a chained hash entry for one string key and station ID value.
     */
    HashNode(String key, int value) {
        this.key = key;
        this.value = value;
    }
}
