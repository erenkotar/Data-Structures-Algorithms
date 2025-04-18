public class MinHeap<T extends Comparable<T>> { // T must be a comparable type
    private T[] heap; // array to store the heap elements
    private int size; // number of elements in the heap
    private int capacity; // maximum number of elements the heap can store

    @SuppressWarnings("unchecked")
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity + 1];  // +1 because index starts at 1
        this.size = 0;
    }

    // Insert a new element into the heap
    public void insert(T element) {
        if (size >= capacity) {
            throw new RuntimeException("Heap is full");
        }

        // temporary store the new element at the start of the heap array
        heap[0] = element; 

        // percolate up process
        size++; // increase the size of the heap which will be the current index of the new element
        int currentIndex = size;

        // compare the new element with its parent
        while (element.compareTo(heap[currentIndex / 2]) < 0) {
            // and move the parent down if the new element is greater
            heap[currentIndex] = heap[currentIndex / 2];
            currentIndex /= 2;
        }
        // insert the new element at the correct position
        heap[currentIndex] = element;
    }

    // get the min element from the heap
    public T getMin() {
        if (size == 0) {
            throw new RuntimeException("Heap is empty");
        }
        // the min element is always at the root of the heap which is index 1
        return heap[1];
    }

    // remove and return the maximum element from the heap
    public T extractMin() {
        if (size == 0) {
            throw new RuntimeException("Heap is empty");
        }
        // the min element is always at the root of the heap which is index 1
        T min = heap[1];
        heap[1] = heap[size]; // move the last element to the root
        size--; // decrease the size of the heap
        minHeapify(1); // maintain the min-heap property
        return min; // return the min element
    }

    // check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // maintain the min-heap property
    private void minHeapify(int index) {
        // get the left and right child of the current index
        int smallest = index;
        int left = 2 * index;
        int right = 2 * index + 1;

        // compare the left child with the parent
        if (left <= size && heap[left].compareTo(heap[smallest]) < 0) {
            smallest = left;
        }

        // compare the right child with the lowest so far
        if (right <= size && heap[right].compareTo(heap[smallest]) < 0) {
            smallest = right;
        }

        // if the smallest is not the parent, swap the parent with the largest child
        if (smallest != index) {
            swap(index, smallest); // swap the parent with the lowest child
            minHeapify(smallest); // recursively maintain the max-heap property
        }
    }

    // swap elements at two indexes in the heap
    private void swap(int i, int j) {
        T temp = heap[i]; // temporary variable to store the element at index i
        heap[i] = heap[j]; // move the element at index j to index i
        heap[j] = temp; // move the element at index i to index j
    }
}
