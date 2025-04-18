public class MapEdge {
    private String targetNodeKey; // holds the target node's key
    private double weight;  // travel time to the target node

    public MapEdge(String targetNodeKey, double weight) {
        this.targetNodeKey = targetNodeKey;
        this.weight = weight;
    }

    // getter and setter methods
    public String getTargetNodeKey() {
        return targetNodeKey;
    }

    public void setTargetNode(MapNode targetNode) {
        this.targetNodeKey = targetNode.getKey();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
