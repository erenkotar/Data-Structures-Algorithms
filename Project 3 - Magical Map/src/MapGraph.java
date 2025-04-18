public class MapGraph {

    private HashTable nodes; // stores all nodes in hash table
    private Queue<MapNode> modifiedNodes; // caches modified nodes for resetting the distances after each search
    private HashTable typeStatus; // stores all types of nodes to quickly check if a type is unlocked O(1)

    public MapGraph() {
        this.nodes = new HashTable(160000);
        this.modifiedNodes = new Queue<>();
        this.typeStatus =  new HashTable(30);
    }

    // add node to hash table
    public void addNode(MapNode node) {
        nodes.put(node.getKey(), node); 
    }

    // get node by using key
    public MapNode getNodeKey(String key) {
        return (MapNode) nodes.get(key);
    }

    // get node by using x and y 
    public MapNode getNodeXY(int x, int y) {
        return (MapNode) nodes.get(XYToKey(x, y));
    }

    // convertors functions
    public String XYToKey(int x, int y) {
        return x + "-" + y;
    }

    public int[] keyToXY(String key) {
        String[] parts = key.split("-");
        return new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    // get typeStatus hash table
    public HashTable getTypeStatus() {
        return typeStatus;
    }

    // set a type to be unlocked or locked (it overrides the previous status)
    public void setType(int type, boolean status) {
        if (typeStatus.get(String.valueOf(type)) != null) {
            typeStatus.remove(String.valueOf(type));
        } 
        typeStatus.put(String.valueOf(type), status);
    }

    // add edge's key between two nodes
    public void addEdge(String sourceKey, String destinationKey, double weight) {
        MapNode sourceNode = (MapNode) nodes.get(sourceKey); // first got the source node
        MapNode destinationNode = (MapNode) nodes.get(destinationKey); // then got the destination node
        if (sourceNode != null && destinationNode != null && sourceNode != destinationNode) { // if both nodes are not null and not the same
            sourceNode.addEdge(new MapEdge(destinationNode.getKey(), weight)); // add edge from source to destination
            destinationNode.addEdge(new MapEdge(sourceNode.getKey(), weight)); // add edge from destination to source since it is undirected graph
        }
    }

    // find shortest path between two nodes (DIJKSTRA is used)
    public MapPath findShortestPath(String startKey, String endKey, int option) {
        
        resetNodes(); // reset only previously modified nodes
        modifiedNodes = new Queue<>(); // intialize modified nodes queue (it will be filled during the search)
    
        // get start and end nodes
        MapNode startNode = getNodeKey(startKey);
        MapNode endNode = getNodeKey(endKey);
    
        if (startNode == null || endNode == null) {
            // EXCEPTION
            return null;
        }
        
        // create MinHeap to select the node with minimum distance
        MinHeap<MapNode> pq = new MinHeap<>(nodes.getSize()); 
        
        // initialize start node
        startNode.minDistance = 0;
        modifiedNodes.enqueue(startNode); // track modified node
        pq.insert(startNode); // add the starting node to priority queue
        
        // we have to track precious nodes to reconstruct the final path
        HashTable previousNodes = new HashTable(nodes.getSize());
    
        while (!pq.isEmpty()) {
            MapNode current = pq.extractMin(); // get the node with minimum distance (greedy component)
            
            // if reached the end node, reconstruct and return the path
            if (current == endNode) {
                return reconstructPath(startNode, endNode, previousNodes, option);
            }
                        
            // explore neighbors
            for (MapEdge edgeCurrent : current.getEdges()) {
                String neighborKey = edgeCurrent.getTargetNodeKey();
                MapNode neighbor = getNodeKey(neighborKey);
                double newDistance = current.minDistance + edgeCurrent.getWeight();
                
                // check if the neighbor is passable
                if (isPassable(neighbor, option)) {
                    // if we find a shorter path
                    if (newDistance < neighbor.minDistance) {
                        
                        neighbor.minDistance = newDistance; // update distance
                        modifiedNodes.enqueue(neighbor); // track modified node
                        pq.insert(neighbor); // add to heap
                        
                        previousNodes.remove(neighbor.getKey()); // remove previous node
                        previousNodes.put(neighbor.getKey(), current); // update previous node with current
                    }
                }
            }
        }
        return null;
    }

    private boolean isTypeUnlocked(int option) {
        // check if the type is unlocked
        // it will get the status of the type from typeStatus hash table
        return (boolean) typeStatus.get(String.valueOf(option));
    }

    public boolean isPassable(MapNode node, int option) {
        if (node.getRevealed()) {
            // if the node is revealed, check if the option is the same as the node type (will be used in wizard option) or the type is already unlocked
            return option==node.getType() || isTypeUnlocked(node.getType());
        } else {
            // if the node is not revealed, no need to check anything, default cases (0 and 1) is already revealed initailly
            return true;
        }
    }

    private MapPath reconstructPath(MapNode start, MapNode end, HashTable previousNodes, int option) {
        // after the iteration is finished, reconstruct the path from end to start to get the path as a linked list

        LinkedList<MapNode> path = new LinkedList<>();
        double totalDistance = end.minDistance; // total distance is the minDistance of the end node
        MapNode current = end; // we will start from end node and go back to start node by following previous nodes from previousNodes hash table
    
        // end node will be the last node in the path after reconstruction is finished
        path.addFirst(current);
        
        // use a reference to the previous node to avoid repeated lookups
        MapNode prevNode = (MapNode) previousNodes.get(current.getKey());
        
        // trace back from end to start
        while (prevNode != null && current != start) {    
            // move to previous node
            current = prevNode;
            path.addFirst(current);
            prevNode = (MapNode) previousNodes.get(current.getKey());
        }
    
        // add start node if not already added
        if (current != start) {
            path.addFirst(start);
        }
        
        // return the path as MapPath object
        return new MapPath(path, totalDistance, option);
    }

    private void resetNodes() {
        // reset only previously modified nodes minDistance which will be used in next search
        while (!modifiedNodes.isEmpty()) {
            MapNode node = (MapNode) modifiedNodes.dequeue(); 
            node.minDistance = Double.POSITIVE_INFINITY; // reset distance to inf
        }
    }
}
