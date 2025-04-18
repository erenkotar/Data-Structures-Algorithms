public class HashTable {
    private Bucket[] table; // array of buckets
    private int capacity; // capacity of the table (initialized with parameter)
    private int size; // number of elements in the table

    public HashTable(int capacity) {
        // initial capacity given as parameter
        size = 0;
        this.capacity = capacity;
        table = new Bucket[capacity]; // array of buckets initialized with the capacity
        for (int i = 0; i < capacity; i++) {
            // initialize each bucket (with null rooted AVL tree)
            table[i] = new Bucket();
        }
    }

    // calculate the hash value of a key
    private int getHash(String key) {
        // modulo to ensure hash value is within the capacity
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    // calculate load factor of the table
    public double get_lambda() {
        return (double) size / capacity;
    }

    // add a key-value pair to the table
    public void put(String key, Object value) {
        int hash = getHash(key);
        // check if key is already present to prevent incrementing size incorrectly (SILINEBILIR)
        if (table[hash].get(key) == null) { 
            // if not presenet add the pair to the bucket
            table[hash].add(key, value);
            size++;
        } else {
            // if key is already present do nothing
            return;
        }
        // if load factor is greater than 2, resize the table
        if (get_lambda() > 2) {
            resize();
        }
    }

    public void hardPut(String key, Object value) {
        int hash = getHash(key);
        // overwrite the value in any condition
        table[hash].add(key, value);
    }

    public void overwrite(String key, Object value) {
        int hash = getHash(key);
        // if key is already present overwrite the value
        if (table[hash].get(key) != null) {
            table[hash].add(key, value);
        }
    }

    public int getSize() {
        return size;
    }

    // retrieve a value from the table using a key
    public Object get(String key) {
        int hash = getHash(key);
        // get will invoke the get method of the bucket which will invoke the find method of the AVL tree
        return table[hash].get(key);
    }

    public void remove(String key) {
        int hash = getHash(key);
        // if deleted node value is not null (meaning it was deleted) decrement the size
        if (table[hash].remove(key) != null) {
            size--;
        }
    }

    private void resize() {
        int oldCapacity = capacity; // store the old capacity
        capacity *= 2; // double the capacity as new capacity
        Bucket[] newTable = new Bucket[capacity]; // create a new table with the new capacity
        for (int i = 0; i < capacity; i++) {
            newTable[i] = new Bucket(); // Initialize buckets of new table
        }
    
        // traverse each bucket's AVL tree and collect all keys into a queue
        for (int i = 0; i < oldCapacity; i++) {
            Queue<String> keys = table[i].tree.traverse(); // collect keys using the traverse method
            // traverse the queue and rehash the keys into the new table
            while (!keys.isEmpty()) {
                String key = keys.dequeue(); // dequeue a key from queue
                Object value = table[i].get(key); // get the value associated with the key from the old table
                int hash = getHash(key); // rehash the key
                newTable[hash].add(key, value); // add the key-value pair to the new bucket
            }
        }
    
        table = newTable; // replace the old table with the new table
    }
    
}
