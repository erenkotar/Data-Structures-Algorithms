public class SimpleList<K> {
    private Object[] elements;  // array to store the elements of object
    private int size = 0;       // current number of elements (initially 0)
    private int capacity = 100;  // initial capacity of the list

    public SimpleList() {
        elements = new Object[capacity];
    }

    // add an element to the end of the list
    public void add(K element) {
        ensureCapacity();  // check if resizing is needed
        elements[size++] = element;
    }

    // ensure there is enough space in list, resize if needed
    private void ensureCapacity() {
        // if the size is equal to the capacity-1, list will be full after adding one more element, so resize
        if (size == capacity-1) {
            capacity = capacity * 2;
            Object[] newElements = new Object[capacity]; // new array with capacity doubled
            System.arraycopy(elements, 0, newElements, 0, size); // copy the elements from old array and paste it to new one
            elements = newElements;
        }
    }

    // get an element at a specific index
    public K get(int index) {
        // check if the index is valid and within the size of the list
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (K) elements[index];
    }

    // get the current size of the list
    public int size() {
        return size;
    }

    // remove an element at a specific index
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;  // clear
    }

    // check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }
}
