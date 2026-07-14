package structures;

/**
 * Node used by the red-black tree for ordered station-name search.
 */
public class RedBlackNode {
    public String key;
    public int stationId;
    public boolean red;
    public RedBlackNode left;
    public RedBlackNode right;
    public RedBlackNode parent;

    /**
     * Creates a red-black node with key, station ID, and initial color.
     */
    public RedBlackNode(String key, int stationId, boolean red) {
        this.key = key;
        this.stationId = stationId;
        this.red = red;
    }
}
