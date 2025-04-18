import java.util.ArrayList;

public class MapManager {

    private MapGraph graph; // stores all nodes and edges
    private int sightRadius; 
    private String startingNodeKey;
    private int[] gridSize = new int[2];
    private Queue<MapObj> objectives; // stores all objectives in queue
    private HashTable pathNodes; // stores temporary path nodes
    private boolean terminatePath; // flag to terminate the path if impassable node is found during reveal
    private MapPath precomputedPath; // cache the precomputed path to be used after wizard options (so we wont need to recompute during next objective)
    private StringBuilder output = new StringBuilder(); 
    
    public MapManager() {
        this.graph = new MapGraph();
        this.objectives = new Queue<>();
        this.pathNodes = new HashTable(7);
    }

    public void setStartingNode(int x, int y) {
        // sets the starting node in reading files
        this.startingNodeKey = graph.XYToKey(x, y);
    }

    public void setGridSize(int width, int height) {
        // sets the grid size in reading files
        this.gridSize[0] = width;
        this.gridSize[1] = height;
    }

    public void setSightRadius(int radius) {
        this.sightRadius = radius;
    }

    public MapGraph getGraph() {
        return graph;
    }

    public String getStartingNodeKey() {
        return startingNodeKey;
    }

    public int[] getGridSize() {
        return gridSize;
    }

    public int getSightRadius() {
        return sightRadius;
    }

    public void addObjective(MapObj objective) {
        this.objectives.enqueue(objective);
    }

    public Queue<MapObj> getObjectives() {
        return objectives;
    }

    public StringBuilder getOutput() {
        return output;
    }

    public void setType(int type, boolean status) {
        // during reading process of nodes, we will set the type status
        // if the type is already set into hash table, no need to set again
        if (graph.getTypeStatus().get(String.valueOf(type)) != null) {
            return;
        }
        graph.setType(type, status);
    }

    // simply adds node to the graph
    public void addNode(int x, int y, int type) {
        MapNode node = new MapNode(x, y, type);
        if (type == 0 || type == 1) { // if type is 0 or 1, it is already revealed
            node.setRevealed(true); // so set them to revealed
        }
        graph.addNode(node); // add node to the graph
    }

    public void addEdge(String sourceKey, String destinationKey, double weight) {
        // add edge to the graph (will add edge to both nodes since it is undirected graph)
        graph.addEdge(sourceKey, destinationKey, weight);
    }
    
    public void startingVisibility() {
        // at start we will reveal the visible nodes of the starting node
        MapNode startingNode = graph.getNodeKey(startingNodeKey);
        revealVisibleNodes(startingNode);
    }

    public void revealVisibleNodes(MapNode currentNode) {
        // we will reveal the visible nodes of the current node (newly moved node) that we have not seen before
        ArrayList<String> revealedNodes = currentNode.revealNodes(graph, sightRadius, gridSize); // get the visible nodes we have not seen before
        for (String nodeKey : revealedNodes) { // iterate through the visible nodes
            MapNode node = graph.getNodeKey(nodeKey);
            node.setRevealed(true); // reveal it
            if (pathNodes.get(node.getKey()) != null && !graph.isPassable(node, 0)) { 
                // if the node is on the current path and not passable
                terminatePath = true; // terminate the path
            }
        }
    }

    public MapPath findBestOption(LinkedList<MapPath> optionPaths) {
        // find the best option by comparing the option path ()wizard options lengths
        int shortestPathIndex = -1;
        double shortestPathLength = Double.MAX_VALUE;
        for (int i = 0; i < optionPaths.getSize(); i++) {
            MapPath currentPath = optionPaths.get(i);
            if (currentPath != null && currentPath.pathLength < shortestPathLength) { // if the path length is shorter
                shortestPathLength = currentPath.pathLength; // update the shortest path length
                shortestPathIndex = i; // update the shortest path index
            }
        }
        return optionPaths.get(shortestPathIndex); // return the best option
    }

    public void move(MapPath path, MapObj obj) {
        // move along the path to reach the objective

        // create and store the path nodes
        pathNodes = new HashTable(path.getPath().getSize());
        for (int i = 0; i < path.getPath().getSize(); i++) { 
            MapNode node = path.getPath().get(i);
            pathNodes.put(node.getKey(), node);
        }
        terminatePath = false; // set the flag to false initailly

        if (path == null || path.getPath() == null || path.getPath().isEmpty()) {
            // EXCEPTION
            output.append("NO VALID PATH!\n");
            return;
        }
    
        LinkedList<MapNode> nodes = path.getPath(); // get the path nodes
        
        // start from index 1 since index 0 is current position
        for (int i = 1; i < nodes.getSize(); i++) {
            MapNode currentNode = nodes.get(i);
            
            // check if remaining path is still valid (initailly set to false)
            if (terminatePath) { 
                output.append("Path is impassable!\n");
                
                // try to find alternative path
                MapNode prevNode = nodes.get(i-1); // it should find a way from previous node
                MapPath newPath = graph.findShortestPath(prevNode.getKey(), obj.getKey(), 0);
                
                if (newPath == null || newPath.getPath() == null) {
                    // EXCEPTION
                    output.append("NO VALID PATH!\n");
                    return;
                }
                
                
                // recursively move to new path
                move(newPath, obj);
                return;
            }
            
            // if no need to terminate, move to node and update visibility
            output.append("Moving to " + currentNode.getX() + "-" + currentNode.getY() + "\n");
            revealVisibleNodes(currentNode);
        }
        
        // if no nodes left, objective is reached
        output.append("Objective " + obj.getObjIdx() + " reached!\n");
    }
       
    private MapPath processWizardOptions(MapObj currentObj, MapObj nextObj) {
        LinkedList<MapPath> optionPaths = new LinkedList<>(); // store path object for each option
        LinkedList<Integer> options = currentObj.getOptions(); // extract urrent objective's options
        MapPath bestPath = null; // store the best path along the options
    
        // iteraet through the options and calculate path for each option
        for (int i = 0; i < options.getSize(); i++) {
            int option = options.get(i); // get curent option
            
            // if there is no next objective, option cannot be evaluated w.r.t. next objective
            // if there is, then find the shortest path to next objective with the option
            // option parameter will enable the passable check for the revealed nodes
            MapPath path = nextObj != null ?
                graph.findShortestPath(currentObj.getKey(), nextObj.getKey(), option) :
                new MapPath(null, 0, option);
            
            optionPaths.addFirst(path); // add path to the option paths
        }
    
        // find best option across all options
        bestPath = findBestOption(optionPaths);
        
        // apply the chosen option
        if (bestPath != null) {
            graph.setType(bestPath.option, true); // unlock the type (add to hash table)
            output.append("Number " + bestPath.option + " is chosen!\n");
        }

        return bestPath; 
    }
    
    private void iterateObj(MapObj currentObj, MapObj nextObj) {
        MapPath path;

        // check if there is a precomputed path (precomputedPath is calculated during wizard options)
        if (precomputedPath != null) {
            path = precomputedPath;
            precomputedPath = null;  // clear the cached path after use
        } else {
            // otherwise calculate new path
            path = graph.findShortestPath(startingNodeKey, currentObj.getKey(), 0);
        }
        
        // reach to objective
        move(path, currentObj);
        
        // if wizard has options, process them
        if (currentObj.hasAnyOption()) {
            // Caching the precomputed path for wizard options
            precomputedPath = processWizardOptions(currentObj, nextObj);
        }
        
        // update starting point for next iteration
        startingNodeKey = currentObj.getKey();
    }

    public StringBuilder iterateObjs() {
        // initially all types are set to false status (locked) but type 0 should set to true
        graph.setType(0, true);
        MapObj currentObj = null;
        
        // get first objective
        if (!objectives.isEmpty()) {
            currentObj = objectives.dequeue();
        }
        
        // process each objective
        while (currentObj != null) {
            // next objective are also given due to wizard options
            MapObj nextObj = objectives.isEmpty() ? null : objectives.peek();
            iterateObj(currentObj, nextObj);
            // move to next objective
            currentObj = objectives.isEmpty() ? null : objectives.dequeue();
        }

        return output;
    }
}
