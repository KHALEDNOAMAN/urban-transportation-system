package structures;

/**
 * Manual union-find structure used by Kruskal's MST algorithm.
 */
public class DisjointSet {
    private int[] parent;
    private int[] rank;

    /**
     * Creates singleton sets for the requested capacity.
     */
    public DisjointSet(int capacity) {
        int actual = capacity < 1 ? 1 : capacity;
        parent = new int[actual];
        rank = new int[actual];
        initializeSets();
    }

    /**
     * Reinitializes the structure so each index is its own set.
     */
    public void reset(int capacity) {
        ensureCapacity(capacity);
        initializeSets();
    }

    /**
     * Assigns every index to itself with zero rank.
     */
    private void initializeSets() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Finds the representative of a set while applying path compression.
     */
    public int find(int value) {
        if (parent[value] != value) {
            parent[value] = find(parent[value]);
        }
        return parent[value];
    }

    /**
     * Merges two sets with union by rank and reports whether a merge occurred.
     */
    public boolean union(int first, int second) {
        int firstRoot = find(first);
        int secondRoot = find(second);
        if (firstRoot == secondRoot) {
            return false;
        }
        if (rank[firstRoot] < rank[secondRoot]) {
            parent[firstRoot] = secondRoot;
        } else if (rank[firstRoot] > rank[secondRoot]) {
            parent[secondRoot] = firstRoot;
        } else {
            parent[secondRoot] = firstRoot;
            rank[firstRoot]++;
        }
        return true;
    }

    /**
     * Clears state by restoring every index to a singleton set.
     */
    public void clear() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Grows parent and rank arrays when more set slots are needed.
     */
    private void ensureCapacity(int required) {
        if (required <= parent.length) {
            return;
        }
        int newCapacity = parent.length * 2;
        while (newCapacity < required) {
            newCapacity *= 2;
        }
        int[] nextParent = new int[newCapacity];
        int[] nextRank = new int[newCapacity];
        for (int i = 0; i < parent.length; i++) {
            nextParent[i] = parent[i];
            nextRank[i] = rank[i];
        }
        parent = nextParent;
        rank = nextRank;
    }
}
