class MapObj {
    int x;
    int y;
    String key; // key of the objective node
    LinkedList<Integer> options = new LinkedList<>(); // wizard options
    int objIdx; // inex of the objective

    public MapObj(int x, int y) {
        this.x = x;
        this.y = y;
        this.key = x + "-" + y;
    }

    // some getters and setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getKey() {
        return key;
    }

    public int getObjIdx() {
        return objIdx;
    }

    public void setObjIdx(int objIdx) {
        this.objIdx = objIdx;
    }

    public LinkedList<Integer> getOptions() {
        return options;
    }

    public void addOption(int option) {
        // add option to the list
        options.addLast(option);
    }

    public boolean hasAnyOption() {
        return options.getSize() > 0;
    }
}