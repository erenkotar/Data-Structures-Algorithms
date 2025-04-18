public class AVLTree<K extends Comparable<K>, V> {
    private AVLNode<K, V> root;

    public AVLTree() {
        // initialize the AVL tree with a null root (AVLNode)
        root = null;
    }

    public void insert(K key, V value) {
        root = insert(root, key, value);
    }

    private AVLNode<K, V> insert(AVLNode<K, V> node, K key, V value) {
        // if the node is null, create a new node with the key and value (so new node will be the root)
        if (node == null) {
            return new AVLNode<>(key, value);
        }
        
        // compare the key with the node's key
        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            // if the key is less than the node's key, insert the key to the left subtree
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            // if the key is greater than the node's key, insert the key to the right subtree
            node.right = insert(node.right, key, value);
        } else {
            // if the key is equal to the node's key, update the value of the node
            return node;
        }

        // rebalance the tree after insertion
        return rebalance(node);
    }

    // find the node with the given key
    public AVLNode<K, V> find(K key) {
        return findNode(root, key);
    }

    // helper method to find the node with the given key
    private AVLNode<K, V> findNode(AVLNode<K, V> node, K key) {
        // if the node is null, return null
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                // if the key is less than the node's key, search in the left subtree
                node = node.left;
            } else if (cmp > 0) {
                // if the key is greater than the node's key, search in the right subtree
                node = node.right;
            } else {
                // if the key is equal to the node's key, return the node itself
                return node;
            }
        }
        // if the key is not found, return null
        return null; 
    }

    public V delete(K key) {
        // create a temporary node to store the deleted node's value
        AVLNode<K, V> deletedNode = new AVLNode<>(null, null);
        root = delete(root, key, deletedNode);
        return deletedNode.value;
    }

    private AVLNode<K, V> delete(AVLNode<K, V> node, K key, AVLNode<K, V> deletedNode) {
        // if the node is null, return null
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // if the key is less than the node's key, delete the key from the left subtree
            node.left = delete(node.left, key, deletedNode);
        } else if (cmp > 0) {
            // if the key is greater than the node's key, delete the key from the right subtree
            node.right = delete(node.right, key, deletedNode);
        } else {
            // if the key is equal to the node's key, store value of the node to be deleted
            deletedNode.value = node.value;
            if (node.left == null || node.right == null) {
                // if the node has at most one child, return the non-null child
                node = (node.left == null) ? node.right : node.left;
            } else {
                // if the node has two children, replace the node with the most left child of the right subtree
                AVLNode<K, V> mostLeftChild = getMostLeftChild(node.right);
                node.key = mostLeftChild.key;
                node.value = mostLeftChild.value;
                // delete the most left child from the right subtree
                node.right = delete(node.right, node.key, new AVLNode<>(null, null));
            }
        }
        // rebalance the tree after deletion
        return node == null ? null : rebalance(node);
    }

    private AVLNode<K, V> getMostLeftChild(AVLNode<K, V> node) {
        AVLNode<K, V> current = node;
        while (current.left != null) {
            // find the most left child of the given node
            current = current.left;
        }
        return current;
    }

    private AVLNode<K, V> rebalance(AVLNode<K, V> node) {
        updateHeight(node); // update the height of the node
        int balance = getBalance(node); // get the balance factor of the node
        if (balance > 1) {
            // if the left subtree is right-heavy
            if (getBalance(node.left) < 0) {
                // left rotate the left subtree
                node.left = leftRotate(node.left);
            }
            // if the left subtree is left-heavy, right rotate the node
            return rightRotate(node);
        }
        if (balance < -1) {
            // if the right subtree is left-heavy
            if (getBalance(node.right) > 0) {
                // right rotate the right subtree
                node.right = rightRotate(node.right);
            }
            // if the right subtree is right-heavy, left rotate the node
            return leftRotate(node);
        }
        return node;
    }

    // get the balance factor of the node
    private int getBalance(AVLNode<K, V> node) {
        // balance factor = height of left subtree - height of right subtree
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // get the height of the node
    private int height(AVLNode<K, V> node) {
        // height of the node is 0 if the node is null so that the height of the leaf nodes (not null nodes) is 1
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(AVLNode<K, V> node) {
        // update the height of node based on maximum height of its children
        // plus one (1) for the node itself
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // right rotate the subtree rooted with y
    private AVLNode<K, V> rightRotate(AVLNode<K, V> y) {
        AVLNode<K, V> x = y.left; // x is the left child of y
        AVLNode<K, V> T2 = x.right; // T2 (subtree/root) is the right child of x
        x.right = y; // y becomes the right child of x
        y.left = T2; // T2 becomes the left child of y
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // left rotate the subtree rooted with x
    private AVLNode<K, V> leftRotate(AVLNode<K, V> x) {
        AVLNode<K, V> y = x.right; // y is the right child of x
        AVLNode<K, V> T2 = y.left; // T2 (subtree/root) is the left child of y
        y.left = x; // x becomes the left child of y
        x.right = T2; // T2 becomes the right child of x
        updateHeight(x); 
        updateHeight(y);
        return y;
    }

    public Queue<String> traverse() {
        // create a queue to store the keys in-order
        Queue<String> result = new Queue<>();
        traverse(root, result);
        // return the queue of keys
        return result;
    }

    // helper method for in-order traversal of the tree
    private void traverse(AVLNode<K, V> node, Queue<String> list) {
        // in-order traversal: left -> root -> right
        if (node != null) {
            // until the node is null
            traverse(node.left, list);  // visit left subtree
            list.enqueue(node.key.toString()); // visit node itself
            traverse(node.right, list); // visit right subtree
        }
    }
}
