public class MapPath {
    LinkedList<MapNode> path; // path nodes
    double pathLength; // lenght of the path
    int option = 0; // option of the path (default is 0)

    public MapPath(LinkedList<MapNode> path, double pathLength, int option) {
        this.path = path;
        this.pathLength = pathLength;
        this.option = option;
    }

    public LinkedList<MapNode> getPath() {
        return path;
    }

    // print the path
    public void printPath() {
        if (path == null) {
            System.out.println("NO PATH");
            return;
        }
        for (int i = 0; i < path.getSize() - 1; i++) {
            MapNode node = path.get(i);
            System.out.print(node.getKey() + " -> ");
        }
        System.out.println(path.getTail().getData().getKey());
        System.out.println("Length: " + pathLength);
    }

}
