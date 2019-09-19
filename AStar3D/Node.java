package astar;

/**
 * Node Class
 *
 * @author Marcelo Surriabre
 * @version 2.0, 2018-02-23
 */
public class Node {

    private int g;
    private int f;
    private int h;
    private int row;
    private int col;
    private int z;
    private boolean isBlock;
    private Node parent;

    //constructor to set row and col
    public Node(int row, int col, int z) {
        this.row = row;
        this.col = col;
        this.z = z;
    }

    //heuristics calculated base on heuristicCost = (horizontal distance from end) + (vertical distance from end)
    public void calculateHeuristic(Node finalNode) {
        this.h = (int)Math.pow(Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol()) + Math.abs(finalNode.getZ() - getZ()), 3);
    }

    //initialize a new node?
    //input a node and a cost
    //set the new gCost to be sum of old cost up till that point, and new cost. See AStar.java
    //Horizontal vertical cost will be set to 10, diagonal cost will be 14
    //set new parent node to be current node
    // set final cost to be gCost + hCost (cost of getting the the node + cost of heuristics)
    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }

    //check the path from the "current node" to the instance node.
    //If the cost from "current node" is less than the existing path to the instance node,
    //then update the instance node to set it's parent node to the "current node" 
    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    //update fCost of instance
    private void calculateFinalCost() {
        int finalCost = getG() + getH();
        setF(finalCost);
    }

    // check if 2 nodes are equal?
    @Override
    public boolean equals(Object arg0) {
        Node other = (Node) arg0;
        return (this.getRow() == other.getRow() && this.getCol() == other.getCol()) && this.getZ() == other.getZ();
    }

    // print out col and row of Node
    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + ", Z =" + z + "]";
    }

    //return hCost
    public int getH() {
        return h;
    }

    //set hCost of instance
    public void setH(int h) {
        this.h = h;
    }

    //return gCost
    public int getG() {
        return g;
    }

    // set gCost of instance
    public void setG(int g) {
        this.g = g;
    }

    //return f
    public int getF() {
        return f;
    }

    // set fCost of instance
    public void setF(int f) {
        this.f = f;
    }

    //return parent node of Instance
    public Node getParent() {
        return parent;
    }

    //set Parent node of instance node to input
    public void setParent(Node parent) {
        this.parent = parent;
    }

    //return boolean True or Flase
    public boolean isBlock() {
        return isBlock;
    }

    //set variable isblock to Boolean True or False
    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    //return row of instance Node
    public int getRow() {
        return row;
    }

    //set row of instance
    public void setRow(int row) {
        this.row = row;
    }

    //return col of Instance node
    public int getCol() {
        return col;
    }

    //set col of Instance node
    public void setCol(int col) {
        this.col = col;
    }

    //return z of instance node
    public int getZ(){
        return z;
    }

    //set z of Instance Node
    public void setZ(int z){
        this.z = z;
    }
}
