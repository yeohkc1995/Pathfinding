package astar;

import java.util.*;

/**
 * A Star Algorithm
 *
 * @author Marcelo Surriabre
 * @version 2.1, 2017-02-23
 */
public class AStar {
    //take note of the cost
    private static int DEFAULT_HV_COST = 10; // Horizontal - Vertical Cost
    private static int DEFAULT_DIAGONAL_COST = 14;
    //KC added 3D cost
    private static int DEFAULT_Z_COST = 16;
    private int hvCost;
    private int diagonalCost;
    private int zCost;
    private Node[][][] searchArea;
    private PriorityQueue<Node> openList;
    private Set<Node> closedSet;
    private Node initialNode;
    private Node finalNode;

    //constructor 1 to set rows, cols, initial, final, and costs
    public AStar(int rows, int cols, int zs, Node initialNode, Node finalNode, int hvCost, int diagonalCost, int zCost) {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;
        this.zCost = zCost;
        setInitialNode(initialNode);
        setFinalNode(finalNode);
        this.searchArea = new Node[rows][cols][zs];
        this.openList = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node node0, Node node1) {
                return Integer.compare(node0.getF(), node1.getF());
            }
        });
        setNodes();
        this.closedSet = new HashSet<>();
    }

    //constructor 2
    public AStar(int rows, int cols, int zs, Node initialNode, Node finalNode) {
        this(rows, cols, zs, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST, DEFAULT_Z_COST);
    }

    //set all cell within the search area to be nodes
    private void setNodes() {
        for (int i = 0; i < searchArea.length; i++) {
            for (int j = 0; j < searchArea[0].length; j++) {
                for (int k = 0; k < searchArea[0][0].length; k++){
                    Node node = new Node(i, j, k);
                    node.calculateHeuristic(getFinalNode());
                    this.searchArea[i][j][k] = node;
                }
            }
        }
    }

    //takes in array of nodes, set those nodes to be blocks
    public void setBlocks(int[][] blocksArray) {
        for (int i = 0; i < blocksArray.length; i++) {
            int row = blocksArray[i][0];
            int col = blocksArray[i][1];
            int z = blocksArray[i][2];
            setBlock(row, col, z);
        }
    }

    //while open list is not empty,
    //get the first node in the openList queue to be current node
    //add the current node to the closed set
    //if current node is final node, get the path
    //else add adjacent nodes of current node
    public List<Node> findPath() {
        int count = 0;
        openList.add(initialNode);
        while (!isEmpty(openList)) {
            Node currentNode = openList.poll();
            closedSet.add(currentNode);
            if (isFinalNode(currentNode)) {
                return getPath(currentNode);
            } else {
                if ((count%1000) == 0){
                    System.out.print("Processing Count: ");
                    System.out.print(count);
                    System.out.println("\n");
                }
                addAdjacentNodes(currentNode);
                addTopNodes(currentNode);
                addBottomNodes(currentNode);
                count++;
            }
        }
        return new ArrayList<Node>();
    }

    //calls a recurring function to return the path leading to the input node
    private List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<Node>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    //add all adjacent nodes
    private void addAdjacentNodes(Node currentNode) {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    //add all top nodes
    private void addTopNodes(Node currentNode){
        addTopUpperRow(currentNode);
        addTopMiddleRow(currentNode);
        addTopLowerRow(currentNode);
    }

    //add all bottom nodes
    private void addBottomNodes(Node currentNode){
        addBottomUpperRow(currentNode);
        addBottomMiddleRow(currentNode);
        addBottomLowerRow(currentNode);
    }

    private void addAdjacentLowerRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int lowerRow = row + 1;
        if (lowerRow < getSearchArea().length) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, lowerRow, z, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, lowerRow, z, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }
            checkNode(currentNode, col, lowerRow, z, getHvCost());
        }
    }

    private void addAdjacentMiddleRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int middleRow = row;
        if (col - 1 >= 0) {
            checkNode(currentNode, col - 1, middleRow, z, getHvCost());
        }
        if (col + 1 < getSearchArea()[0].length) {
            checkNode(currentNode, col + 1, middleRow, z, getHvCost());
        }
    }

    private void addAdjacentUpperRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int upperRow = row - 1;
        if (upperRow >= 0) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, upperRow, z, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, upperRow, z, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            checkNode(currentNode, col, upperRow, z, getHvCost());
        }
    }



    private void addTopUpperRow(Node currentNode){
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int topLayer = z + 1;
        int upperRow = row - 1;

        if (topLayer < getSearchArea()[0][0].length){
            if (upperRow >= 0){
                if (col - 1 >= 0) {
                    checkNode(currentNode, col - 1, upperRow, topLayer, getZCost()); // Comment this if diagonal movements are not allowed
                }
                if (col + 1 < getSearchArea()[0].length) {
                    checkNode(currentNode, col + 1, upperRow, topLayer, getZCost()); // Comment this if diagonal movements are not allowed
                }

                checkNode(currentNode, col, upperRow, topLayer, getDiagonalCost());
            }
        }
    }

    private void addTopMiddleRow(Node currentNode){
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int topLayer = z + 1;
        int middleRow = row;

        if (topLayer < getSearchArea()[0][0].length){
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, middleRow, topLayer, getDiagonalCost());
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, middleRow, topLayer, getDiagonalCost());
            }

            checkNode(currentNode, col, middleRow, topLayer, getHvCost());
        }
    }

    private void addTopLowerRow(Node currentNode){
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int topLayer = z + 1;
        int lowerRow = row + 1;

        if (topLayer < getSearchArea()[0][0].length){
            if (lowerRow < getSearchArea().length) {
                if (col - 1 >= 0) {
                    checkNode(currentNode, col - 1, lowerRow, topLayer, getZCost()); // Comment this line if diagonal movements are not allowed
                }
                if (col + 1 < getSearchArea()[0].length) {
                    checkNode(currentNode, col + 1, lowerRow, topLayer, getZCost()); // Comment this line if diagonal movements are not allowed
                }
                checkNode(currentNode, col, lowerRow, topLayer, getDiagonalCost());
            }
        }
    }




    private void addBottomUpperRow(Node currentNode){
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int bottomLayer = z - 1;
        int upperRow = row - 1;

        if (bottomLayer >= 0){
            if (upperRow >= 0){
                if (col - 1 >= 0) {
                    checkNode(currentNode, col - 1, upperRow, bottomLayer, getZCost()); // Comment this if diagonal movements are not allowed
                }
                if (col + 1 < getSearchArea()[0].length) {
                    checkNode(currentNode, col + 1, upperRow, bottomLayer, getZCost()); // Comment this if diagonal movements are not allowed
                }

                checkNode(currentNode, col, upperRow, bottomLayer, getDiagonalCost());
            }
        }
    }

    private void addBottomMiddleRow(Node currentNode){
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int bottomLayer = z - 1;
        int middleRow = row;

        if (bottomLayer >= 0){
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, middleRow, bottomLayer, getDiagonalCost());
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, middleRow, bottomLayer, getDiagonalCost());
            }

            checkNode(currentNode, col, middleRow, bottomLayer, getHvCost());
        }
    }

    private void addBottomLowerRow(Node currentNode){
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int z = currentNode.getZ();
        int bottomLayer = z - 1;
        int lowerRow = row + 1;

        if (bottomLayer >= 0){
            if (lowerRow < getSearchArea().length) {
                if (col - 1 >= 0) {
                    checkNode(currentNode, col - 1, lowerRow, bottomLayer, getZCost()); // Comment this line if diagonal movements are not allowed
                }
                if (col + 1 < getSearchArea()[0].length) {
                    checkNode(currentNode, col + 1, lowerRow, bottomLayer, getZCost()); // Comment this line if diagonal movements are not allowed
                }
                checkNode(currentNode, col, lowerRow, bottomLayer, getDiagonalCost());
            }
        }
    }




    //input a current node, and an adjacent node (using col and row)
    //if adjacent node is not a block, and not in a closed set, 
    //and not in the open list, then set adjacent Node data and add node to the open list
    //if in openList, check path from current node to adjacent node. If there is a better path, refresh the postion of the adjacent node in the openList
    private void checkNode(Node currentNode, int col, int row, int z, int cost) {
        Node adjacentNode = getSearchArea()[row][col][z];
        if (!adjacentNode.isBlock() && !getClosedSet().contains(adjacentNode)) {
            if (!getOpenList().contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            } else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    private boolean isFinalNode(Node currentNode) {
        return currentNode.equals(finalNode);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    private void setBlock(int row, int col, int z) {
        this.searchArea[row][col][z].setBlock(true);
    }

    public Node getInitialNode() {
        return initialNode;
    }

    public void setInitialNode(Node initialNode) {
        this.initialNode = initialNode;
    }

    public Node getFinalNode() {
        return finalNode;
    }

    public void setFinalNode(Node finalNode) {
        this.finalNode = finalNode;
    }

    public Node[][][] getSearchArea() {
        return searchArea;
    }

    public void setSearchArea(Node[][][] searchArea) {
        this.searchArea = searchArea;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public Set<Node> getClosedSet() {
        return closedSet;
    }

    public void setClosedSet(Set<Node> closedSet) {
        this.closedSet = closedSet;
    }

    public int getHvCost() {
        return hvCost;
    }

    public void setHvCost(int hvCost) {
        this.hvCost = hvCost;
    }

    private int getDiagonalCost() {
        return diagonalCost;
    }

    private void setDiagonalCost(int diagonalCost) {
        this.diagonalCost = diagonalCost;
    }

    private int getZCost(){
        return zCost;
    }

    private void setZCost(int zCost){
        this.zCost = zCost;
    }
}
