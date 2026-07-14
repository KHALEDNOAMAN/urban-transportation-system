package structures;

/**
 * Manual red-black tree that stores station names in sorted order for prefix search.
 */
public class RedBlackTree {
    private final RedBlackNode nil;
    private RedBlackNode root;
    private int size;

    /**
     * Creates an empty tree with a shared black NIL sentinel.
     */
    public RedBlackTree() {
        nil = new RedBlackNode(null, -1, false);
        nil.left = nil;
        nil.right = nil;
        nil.parent = nil;
        root = nil;
    }

    /**
     * Inserts a unique station name and restores red-black balance.
     */
    public boolean insert(String key, int stationId) {
        if (key == null) {
            return false;
        }
        RedBlackNode parent = nil;
        RedBlackNode current = root;
        while (current != nil) {
            parent = current;
            int compare = key.compareTo(current.key);
            if (compare == 0) {
                return false;
            }
            current = compare < 0 ? current.left : current.right;
        }
        RedBlackNode node = new RedBlackNode(key, stationId, true);
        node.left = nil;
        node.right = nil;
        node.parent = parent;
        if (parent == nil) {
            root = node;
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        insertFixup(node);
        size++;
        return true;
    }

    /**
     * Searches for a station name and returns its ID, or -1 when missing.
     */
    public int search(String key) {
        RedBlackNode node = searchNode(key);
        return node == null ? -1 : node.stationId;
    }

    /**
     * Searches for a station name and returns the tree node when found.
     */
    public RedBlackNode searchNode(String key) {
        RedBlackNode current = root;
        while (current != nil) {
            int compare = key.compareTo(current.key);
            if (compare == 0) {
                return current;
            }
            current = compare < 0 ? current.left : current.right;
        }
        return null;
    }

    /**
     * Deletes a station name and restores red-black balance.
     */
    public boolean delete(String key) {
        RedBlackNode node = searchNode(key);
        if (node == null) {
            return false;
        }
        deleteNode(node);
        size--;
        return true;
    }

    /**
     * Finds the first node whose key is greater than or equal to the given key.
     */
    public RedBlackNode lowerBound(String key) {
        RedBlackNode current = root;
        RedBlackNode candidate = nil;
        while (current != nil) {
            if (current.key.compareTo(key) >= 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return candidate == nil ? null : candidate;
    }

    /**
     * Returns the next node in sorted order after the supplied node.
     */
    public RedBlackNode successor(RedBlackNode node) {
        if (node == null || node == nil) {
            return null;
        }
        if (node.right != nil) {
            return minimum(node.right);
        }
        RedBlackNode parent = node.parent;
        RedBlackNode current = node;
        while (parent != nil && current == parent.right) {
            current = parent;
            parent = parent.parent;
        }
        return parent == nil ? null : parent;
    }

    /**
     * Returns the number of station names stored in the tree.
     */
    public int size() {
        return size;
    }

    /**
     * Clears all tree nodes and resets the root to NIL.
     */
    public void clear() {
        clearNode(root);
        root = nil;
        size = 0;
    }

    /**
     * Checks core red-black properties for development and tests.
     */
    public boolean validate() {
        if (root == nil) {
            return true;
        }
        if (root.red) {
            return false;
        }
        return validateNode(root) >= 0;
    }

    /**
     * Recursively validates ordering, red-node rules, and black height.
     */
    private int validateNode(RedBlackNode node) {
        if (node == nil) {
            return 1;
        }
        if (node.red && (node.left.red || node.right.red)) {
            return -1;
        }
        if (node.left != nil && node.left.key.compareTo(node.key) >= 0) {
            return -1;
        }
        if (node.right != nil && node.right.key.compareTo(node.key) <= 0) {
            return -1;
        }
        int leftHeight = validateNode(node.left);
        int rightHeight = validateNode(node.right);
        if (leftHeight < 0 || rightHeight < 0 || leftHeight != rightHeight) {
            return -1;
        }
        return leftHeight + (node.red ? 0 : 1);
    }

    /**
     * Repairs red-black properties after inserting a red node.
     */
    private void insertFixup(RedBlackNode node) {
        RedBlackNode current = node;
        while (current.parent.red) {
            if (current.parent == current.parent.parent.left) {
                RedBlackNode uncle = current.parent.parent.right;
                if (uncle.red) {
                    current.parent.red = false;
                    uncle.red = false;
                    current.parent.parent.red = true;
                    current = current.parent.parent;
                } else {
                    if (current == current.parent.right) {
                        current = current.parent;
                        leftRotate(current);
                    }
                    current.parent.red = false;
                    current.parent.parent.red = true;
                    rightRotate(current.parent.parent);
                }
            } else {
                RedBlackNode uncle = current.parent.parent.left;
                if (uncle.red) {
                    current.parent.red = false;
                    uncle.red = false;
                    current.parent.parent.red = true;
                    current = current.parent.parent;
                } else {
                    if (current == current.parent.left) {
                        current = current.parent;
                        rightRotate(current);
                    }
                    current.parent.red = false;
                    current.parent.parent.red = true;
                    leftRotate(current.parent.parent);
                }
            }
        }
        root.red = false;
    }

    /**
     * Removes a node using binary-search-tree deletion plus red-black repair.
     */
    private void deleteNode(RedBlackNode node) {
        RedBlackNode y = node;
        boolean yWasRed = y.red;
        RedBlackNode x;
        if (node.left == nil) {
            x = node.right;
            transplant(node, node.right);
        } else if (node.right == nil) {
            x = node.left;
            transplant(node, node.left);
        } else {
            y = minimum(node.right);
            yWasRed = y.red;
            x = y.right;
            if (y.parent == node) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.red = node.red;
        }
        node.left = nil;
        node.right = nil;
        node.parent = nil;
        node.key = null;
        if (!yWasRed) {
            deleteFixup(x);
        }
    }

    /**
     * Repairs red-black properties after deleting a black node.
     */
    private void deleteFixup(RedBlackNode node) {
        RedBlackNode current = node;
        while (current != root && !current.red) {
            if (current == current.parent.left) {
                RedBlackNode sibling = current.parent.right;
                if (sibling.red) {
                    sibling.red = false;
                    current.parent.red = true;
                    leftRotate(current.parent);
                    sibling = current.parent.right;
                }
                if (!sibling.left.red && !sibling.right.red) {
                    sibling.red = true;
                    current = current.parent;
                } else {
                    if (!sibling.right.red) {
                        sibling.left.red = false;
                        sibling.red = true;
                        rightRotate(sibling);
                        sibling = current.parent.right;
                    }
                    sibling.red = current.parent.red;
                    current.parent.red = false;
                    sibling.right.red = false;
                    leftRotate(current.parent);
                    current = root;
                }
            } else {
                RedBlackNode sibling = current.parent.left;
                if (sibling.red) {
                    sibling.red = false;
                    current.parent.red = true;
                    rightRotate(current.parent);
                    sibling = current.parent.left;
                }
                if (!sibling.right.red && !sibling.left.red) {
                    sibling.red = true;
                    current = current.parent;
                } else {
                    if (!sibling.left.red) {
                        sibling.right.red = false;
                        sibling.red = true;
                        leftRotate(sibling);
                        sibling = current.parent.left;
                    }
                    sibling.red = current.parent.red;
                    current.parent.red = false;
                    sibling.left.red = false;
                    rightRotate(current.parent);
                    current = root;
                }
            }
        }
        current.red = false;
    }

    /**
     * Rotates a subtree left around the supplied node.
     */
    private void leftRotate(RedBlackNode node) {
        RedBlackNode right = node.right;
        node.right = right.left;
        if (right.left != nil) {
            right.left.parent = node;
        }
        right.parent = node.parent;
        if (node.parent == nil) {
            root = right;
        } else if (node == node.parent.left) {
            node.parent.left = right;
        } else {
            node.parent.right = right;
        }
        right.left = node;
        node.parent = right;
    }

    /**
     * Rotates a subtree right around the supplied node.
     */
    private void rightRotate(RedBlackNode node) {
        RedBlackNode left = node.left;
        node.left = left.right;
        if (left.right != nil) {
            left.right.parent = node;
        }
        left.parent = node.parent;
        if (node.parent == nil) {
            root = left;
        } else if (node == node.parent.right) {
            node.parent.right = left;
        } else {
            node.parent.left = left;
        }
        left.right = node;
        node.parent = left;
    }

    /**
     * Replaces one subtree root with another during deletion.
     */
    private void transplant(RedBlackNode oldNode, RedBlackNode newNode) {
        if (oldNode.parent == nil) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }
        newNode.parent = oldNode.parent;
    }

    /**
     * Returns the smallest node in a subtree.
     */
    private RedBlackNode minimum(RedBlackNode node) {
        RedBlackNode current = node;
        while (current.left != nil) {
            current = current.left;
        }
        return current;
    }

    /**
     * Recursively clears node references below the supplied node.
     */
    private void clearNode(RedBlackNode node) {
        if (node == nil) {
            return;
        }
        clearNode(node.left);
        clearNode(node.right);
        node.left = nil;
        node.right = nil;
        node.parent = nil;
        node.key = null;
    }
}
