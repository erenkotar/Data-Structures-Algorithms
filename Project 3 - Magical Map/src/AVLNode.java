public class AVLNode<K extends Comparable<K>, V> {
    K key;                 // key for comparison in the AVL tree
    V value;               // value associated with the key
    int height;            // height of the node within the tree
    AVLNode<K, V> left;    // left child
    AVLNode<K, V> right;   // right child

    // constructor initializes key and value, left and rightr initilized as null and height is setted to 1
    public AVLNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.height = 1;
        this.left = null;
        this.right = null;
    }

    // get the value of the node
    public V getValue() {
        return value;
    }
}
