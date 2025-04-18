import java.util.NoSuchElementException;

public class Queue<K> {
    private LinkedList<K> list;

    public Queue() {
        // queue is basically a linked list with some restrictions
        // has FIFO property
        this.list = new LinkedList<>();
    }

    // add an element to the end of the queue
    public void enqueue(K element) {
        list.addLast(element);
    }

    // remove the first element of the queue
    public K dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        K element = list.get(0);
        list.removeFirst();
        return element;
    }

    // returns the first element of the queue without removing it
    public K peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return list.get(0);
    }

    // check if the queue is empty
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // return the size of the queue
    public int getSize() {
        return list.getSize();
    }
}
