import java.util.NoSuchElementException;

public class Stack<K> {
    private LinkedList<K> list;

    public Stack() {
        // stack is basically a linked list with some restrictions
        // has LIFO property
        this.list = new LinkedList<>();
    }

    // add an element to the top of the stack
    public void push(K element) {
        list.addFirst(element);
    }

    // remove the top element of the stack
    public K pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        K element = list.get(0);
        list.removeFirst();
        return element;
    }

    // returns the top element of the stack without removing it
    public K peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return list.get(0);
    }

    // check if the stack is empty
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // return the size of the stack
    public int size() {
        return list.getSize();
    }
}
