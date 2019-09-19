// Java Program to illustrate reading from Text File 
// using Scanner Class 
package astar;

import java.io.File; 
import java.util.Scanner; 

public class CSV{
    
    //function to get length of the "occupancyGrids.csv"
    public static int getLength() throws Exception{
        //code to count number of datasets in csv file
        String thrash = "";
        int numOfSet = 0;
        File file0 = new File("TelokBlangahGrids(10m).csv"); 
        Scanner sc0 = new Scanner(file0); 

        while (sc0.hasNextLine()){
            thrash = sc0.nextLine();
            numOfSet++;
        }
        sc0.close();

        System.out.print("Num of Dataset:");
        System.out.print(numOfSet);
        System.out.println("\n");

        return numOfSet;
    }


    public static int[][] getGrids() throws Exception 
    {
        
        //code to count number of datasets in csv file
        int numOfSet = getLength();


        //create 2D integer array to store occupancy grid
        //create tempString to store data for each line from the csv
        int[][] occupancyGrid = new int[numOfSet][];
        int count = 0;  
        String tempString = new String(""); 

        // pass the path to the file as a parameter 
        File file = new File("TelokBlangahGrids(10m).csv"); 
        Scanner sc = new Scanner(file); 
        
        //code to append each line of data to occupancyGrid
        while (sc.hasNextLine()){
            tempString = sc.nextLine();
            int[] tempIntArr = new int[3];
            String intString = "";
            String intString2 = "";
            String intString3 = "";
            int pos = 0;
            int pos2 = 0;
            int pos3 = 0;
            //System.out.println(tempString);

            for (pos = 0; pos < tempString.length(); pos++){
                if (tempString.charAt(pos) != ','){
                    intString = intString + tempString.charAt(pos);
                }
                else{
                    //System.out.println(intString);
                    pos2 = pos;
                    break;
                }
            }

            for (pos2 = pos + 1; pos2 < tempString.length(); pos2++){
                if (tempString.charAt(pos2) != ',')
                    intString2 = intString2 + tempString.charAt(pos2);
                else{
                    //System.out.println(intString2);
                    pos3 = pos2;
                    break;
                }
            }

            for (pos3 = pos2 + 1; pos3 < tempString.length(); pos3++){
                intString3 = intString3 + tempString.charAt(pos3);
            }
            //System.out.println(intString3);

            //System.out.print("Count: ");
            //System.out.print(count);
            tempIntArr[0] = Math.round(Float.parseFloat(intString));
            tempIntArr[1] = Math.round(Float.parseFloat(intString2));
            tempIntArr[2] = Math.round(Float.parseFloat(intString3));

            //System.out.println(tempIntArr[0]);
            //System.out.println(tempIntArr[1]);
            //System.out.println(tempIntArr[2]);
            
            occupancyGrid[count] = tempIntArr;
            count++;
        }

        //code to print out the data.... be overwhlemed...
        /*
        for (int x = 0; x < numOfSet; x++)
        {
            for (int y = 0; y < 3; y++){
                System.out.print(occupancyGrid[x][y]);
                System.out.print(", ");
            }

            System.out.println("\n");
        }
        */
        return (occupancyGrid);
    }

    

    
}