class Bucket {
    // bucket is the class that holds the data structure for each bucket in the hash table
    AVLTree<String, Object> tree; // bucket will have an AVL tree to store key-value pairs

    public Bucket() {
        // initializes the AVL tree (root is null)
        tree = new AVLTree<>();
    }

    // add method to insert a key-value pair to the AVL tree
    public void add(String key, Object value) {
        // if the key is already present, pair will return same node (so tree will not be updated)
        tree.insert(key, value);
    }

    // get the value associated with a key (value will also hold the key)
    public Object get(String key) {
        AVLNode<String, Object> node = tree.find(key);
        return node != null ? node.getValue() : null;
    }

    // remove a key-value pair from the AVL tree
    public Object remove(String key) {
        return tree.delete(key);
    }
}
