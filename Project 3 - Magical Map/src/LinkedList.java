import java.util.NoSuchElementException;

public class LinkedList<K> {
    private Node<K> head;  // head node of the linked list
    private Node<K> tail;  // tail node of the linked list
    private int size;      // size of the linked list for quick checks

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // adding node at the beginning of the list
    public void addFirst(K element) {
        Node<K> newNode = new Node<>(element);
        // if head is null, then the list is empty
        if (head == null) {
            head = tail = newNode;
        } else {
            // add the new node at the beginning of the list
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
    }

    // adding node at the end of the list (implemented because of the queue)
    public void addLast(K element) {
        Node<K> newNode = new Node<>(element);  
        // if head is null, then the list is empty
        if (head == null) {
            head = tail = newNode;
        } else {
            // add the new node at the end of the list
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        size++;
    }

    // remove element from beginning of list
    public void removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("Cannot remove from an empty list");
        }
        // if head and tail are same, then the list has only one element
        if (head == tail) {
            head = tail = null;
        } else {
            // remove the first element from the list
            head = head.getNext();
            head.setPrev(null);
        }
        size--;
    }

    // remove element from end of list
    public void removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("Cannot remove from an empty list");
        }
        // if head and tail are same, then the list has only one element
        if (head == tail) {
            head = tail = null;
        } else {
            // remove the last element from the list
            tail = tail.getPrev();
            tail.setNext(null);
        }
        size--;
    }

    // get element at a specific index
    public K get(int index) {
        // check if the index is valid and within the size of the list
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<K> current = head;
        for (int i = 0; i < index; i++) {
            // traverse the list to find the element at the given index
            current = current.getNext();
        }
        return current.getData();
    }

    // return the head of the list
    public Node<K> getHead() {
        return head;
    }

    // return the tail of the list
    public Node<K> getTail() {
        return tail;
    }

    // return the size of the list
    public int getSize() {
        return size;
    }

    // check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }
    
    // check if the list contains a specific element
    public boolean contains(K element) {
        Node<K> current = head;
        while (current != null) {
            // traverse the list to find the element
            if (current.getData().equals(element)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // remove the first occurrence of a specific element
    public boolean remove(K element) {
        Node<K> current = head;
        while (current != null) {
            // traverse the list to find the element
            if (current.getData().equals(element)) {
                // remove the element from the list
                if (current == head) {
                    removeFirst();
                } else if (current == tail) {
                    // remove the last element from the list
                    removeLast();
                } else {
                    // remove the element from the list
                    current.getPrev().setNext(current.getNext());
                    current.getNext().setPrev(current.getPrev());
                    size--;
                }
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // print list
    public void printList() {
        Node<K> current = head;
        while (current != null) {
            // traverse the list and print each element
            System.out.print(current.getData() + " ");
            current = current.getNext();
        }
        System.out.println();
    }    
}