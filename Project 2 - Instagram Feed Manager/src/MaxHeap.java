public class MaxHeap<T extends Comparable<T>> { // T must be a comparable type
    private T[] heap; // array to store the heap elements
    private int size; // number of elements in the heap
    private int capacity; // maximum number of elements the heap can store

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity + 1];  // +1 because index starts at 1
        this.size = 0;
    }

    // insert a new element into the heap
    public void insert(T element) {
        // check if the heap is full
        if (size >= capacity) {
            throw new RuntimeException("Heap is full");
        }

        // temporary store the new element at the start of the heap array
        heap[0] = element;

        // percolate up process
        size++; // increase the size of the heap which will be the current index of the new element
        int currentIndex = size;
        // compare the new element with its parent
        while (element.compareTo(heap[currentIndex / 2]) > 0) {
            // and move the parent down if the new element is greater
            heap[currentIndex] = heap[currentIndex / 2];
            currentIndex /= 2; // move up to the parent's index
        }
        // insert the new element at the correct position
        heap[currentIndex] = element;
    }

    // get the maximum element from the heap
    public T getMax() {
        if (size == 0) {
            throw new RuntimeException("Heap is empty");
        }
        // the maximum element is always at the root of the heap which is index 1
        return heap[1];
    }

    // remove and return the maximum element from the heap
    public T extractMax() {
        if (size == 0) {
            throw new RuntimeException("Heap is empty");
        }
        // the maximum element is always at the root of the heap which is index 1
        T max = heap[1];
        heap[1] = heap[size]; // move the last element to the root
        size--; // decrease the size of the heap
        maxHeapify(1); // maintain the max-heap property
        return max; // return the maximum element
    }

    // check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // maintain the max-heap property
    private void maxHeapify(int index) {
        // get the left and right child of the current index
        int largest = index; // since the parent is the largest
        int left = 2 * index; // left child (2i)
        int right = 2 * index + 1; // right child (2i + 1)

        // compare the left child with the parent
        if (left <= size && heap[left].compareTo(heap[largest]) > 0) {
            largest = left;
        }

        // compare the right child with the largest so far
        if (right <= size && heap[right].compareTo(heap[largest]) > 0) {
            largest = right;
        }

        // if the largest is not the parent, swap the parent with the largest child
        if (largest != index) { 
            swap(index, largest); // swap the parent with the largest child
            maxHeapify(largest); // recursively maintain the max-heap property
        }
    }

    // swap elements at two indexes in the heap
    private void swap(int i, int j) {
        T temp = heap[i]; // temporary variable to store the element at index i
        heap[i] = heap[j]; // move the element at index j to index i
        heap[j] = temp; // move the element at index i to index j
    }
}
