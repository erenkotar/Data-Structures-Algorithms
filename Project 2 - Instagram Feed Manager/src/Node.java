public class Node<K> {
    K data;          // holds data of any type
    Node<K> next;    // reference to next node
    Node<K> prev;    // reference to previous node

    public Node(K data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    // Getters and setters for data, next, and prev
    public K getData() {
        return data;
    }

    // sets the data of the node
    public void setData(K data) {
        this.data = data;
    }

    // gets the next node
    public Node<K> getNext() {
        return next;
    }

    // sets the next node
    public void setNext(Node<K> next) {
        this.next = next;
    }

    // gets the previous node
    public Node<K> getPrev() {
        return prev;
    }

    // sets the previous node
    public void setPrev(Node<K> prev) {
        this.prev = prev;
    }
}
