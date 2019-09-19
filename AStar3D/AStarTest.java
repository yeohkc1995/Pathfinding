package astar;

import java.io.PrintWriter;
import java.util.List;



public class AStarTest {
    public static void main(String[] args) throws Exception{

        //code to settle the csv import
        int length = CSV.getLength();
        int[][] grids = new int [length][];

        grids = CSV.getGrids();

        //code to find max x,y,z
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;

        //VERY IMPORTANT!!! DATASET ONLY START FROM GRIDS[1] NOT GRIDS[0]
        for (int i = 1; i < length; i++){
            if (grids[i][0] > maxX){
                maxX = grids[i][0];
            }
            
            if (grids[i][1] > maxY){
                maxY = grids[i][1];
            }

            if (grids[i][2] > maxZ){
                maxZ = grids[i][2];
            }
        }

        //print out the borders of the grids
        System.out.print("Max X = ");
        System.out.print(maxX);
        System.out.println("\n");
        System.out.print("Max Y = ");
        System.out.print(maxY);
        System.out.println("\n");
        System.out.print("Max Z = ");
        System.out.print(maxZ);
        System.out.println("\n");




        Node initialNode = new Node(17, 100, 70);
        Node finalNode = new Node(66, 328, 50);
        int rows = maxX + 10;
        int cols = maxY + 10;
        int zs = maxZ + 10;
        AStar aStar = new AStar(rows, cols, zs, initialNode, finalNode);
        int[][] blocksArray = new int[][]{{1, 3, 0}, {2, 3, 0}, {3, 3, 0}, {4,3,0}, {2,3,1}, {3,3,1}};
        aStar.setBlocks(blocksArray);

        //save the offset data in the first line of the CSV file
        int zeroX = grids[0][0];
        int zeroY = grids[0][1];
        int scale = grids[0][2];
        //change the first line of csv to (0,0,0)
        grids[0][0] = 0;
        grids[0][1] = 0;
        grids[0][2] = 0;

        /*
        //code to print grids to troubleshoot
        for (int i = 0; i < length; i++){
            System.out.println(grids[i][0]);
            System.out.println(grids[i][1]);
            System.out.println(grids[i][2]);
        }
        */
        //set grids to be obtacle
        aStar.setBlocks(grids);
        
        //opens text file to write answers to
        PrintWriter writer = new PrintWriter("shortestPath.txt", "UTF-8");

        List<Node> path = aStar.findPath();
        for (Node node : path) {
            String xcoord = Float.toString((float)(node.getRow()+zeroX) / (float)scale);
            String ycoord = Float.toString((float)(node.getCol()+zeroY) / (float)scale);
            String zcoord = Integer.toString(node.getZ());
            writer.println(xcoord + "," + ycoord + "," + zcoord);
            System.out.println(node);
        }

        //close the text file
        writer.close();

        //Search Area
        //      0   1   2   3   4   5   6
        // 0    -   -   -   -   -   -   -
        // 1    -   -   -   B   -   -   -
        // 2    -   I   -   B   -   F   -
        // 3    -   -   -   B   -   -   -
        // 4    -   -   -   -   -   -   -
        // 5    -   -   -   -   -   -   -

        //Expected output with diagonals
        //Node [row=2, col=1]
        //Node [row=1, col=2]
        //Node [row=0, col=3]
        //Node [row=1, col=4]
        //Node [row=2, col=5]

        //Search Path with diagonals
        //      0   1   2   3   4   5   6
        // 0    -   -   -   *   -   -   -
        // 1    -   -   *   B   *   -   -
        // 2    -   I*  -   B   -  *F   -
        // 3    -   -   -   B   -   -   -
        // 4    -   -   -   -   -   -   -
        // 5    -   -   -   -   -   -   -

        //Expected output without diagonals
        //Node [row=2, col=1]
        //Node [row=2, col=2]
        //Node [row=1, col=2]
        //Node [row=0, col=2]
        //Node [row=0, col=3]
        //Node [row=0, col=4]
        //Node [row=1, col=4]
        //Node [row=2, col=4]
        //Node [row=2, col=5]

        //Search Path without diagonals
        //      0   1   2   3   4   5   6
        // 0    -   -   *   *   *   -   -
        // 1    -   -   *   B   *   -   -
        // 2    -   I*  *   B   *  *F   -
        // 3    -   -   -   B   -   -   -
        // 4    -   -   -   -   -   -   -
        // 5    -   -   -   -   -   -   -
    }
}
