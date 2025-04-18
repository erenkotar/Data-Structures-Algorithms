import java.util.ArrayList;

public class MapNode implements Comparable<MapNode> {
    private int x;
    private int y;
    private String key; // actaully it is x + "-" + y
    private int type; // type of the node
    private boolean revealed = false; // whether the node is revealed
    private ArrayList<MapEdge> edges;  // adjacent nodes with travel times
    private boolean visibilityComputed; // whether the visible nodes are computed for this node
    public double minDistance = Double.POSITIVE_INFINITY; // for Dijkstras algorithm

    public MapNode(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.key = x + "-" + y;
        this.type = type;
        this.revealed = false; // default is false (all nodes are not revealed at first (except for type 0 and 1, they will be revealed at start))
        this.edges = new ArrayList<>();
        this.visibilityComputed = false;
    }

    public ArrayList<MapEdge> getEdges() {
        return edges;
    }

    public void addEdge(MapEdge edge) {
        edges.add(edge);
    }

    // Getter and setter methods
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public boolean getRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        // we may want to reveal some nodes as we progress in map
        this.revealed = revealed;
    }

    public ArrayList<String> revealNodes(MapGraph graph, int sightRadius, int[] gridSize) {
        if (visibilityComputed) {
            return new ArrayList<>();    
        }
        ArrayList<String> revealedNodes = computeVisibleNodes(graph, sightRadius, gridSize);
        visibilityComputed = true;
        return revealedNodes;
    }

    private ArrayList<String> computeVisibleNodes(MapGraph graph, int sightRadius, int[] gridSize) {
        // we will define a square area around the node to narrow down the visible nodes
        int minX = Math.max(0, x - sightRadius);
        int maxX = Math.min(gridSize[0] - 1, x + sightRadius);
        int minY = Math.max(0, y - sightRadius);
        int maxY = Math.min(gridSize[1] - 1, y + sightRadius);

        ArrayList<String> revealedNodes = new ArrayList<>(); // this array will contain new visible nodes
        for (int i = minX; i <= maxX; i++) { // iterate through the square area
            for (int j = minY; j <= maxY; j++) {
                MapNode node = graph.getNodeXY(i, j);
                if (node != null) {
                    if (node.getRevealed()) {
                        continue; // if the node is already revealed, skip it
                    } else {
                        double distance = Math.sqrt((x - i) * (x - i) + (y - j) * (y - j)); // calculate eucledian distance
                        if (distance <= sightRadius) { // if the distance is within the sight radius
                            node.setRevealed(true);; // add the node to the visible nodes
                            revealedNodes.add(node.getKey()); // add the node key to the revealed nodes (we will check if any of the newly revealed nodes are on out path)
                        }
                    }
                }
            }
        }
        return revealedNodes;
    }

    @Override
    public int compareTo(MapNode other) {
        // compare nodes based on their minimum distance
        return Double.compare(this.minDistance, other.minDistance);
    }
}
