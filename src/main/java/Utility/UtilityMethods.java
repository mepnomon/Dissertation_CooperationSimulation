package Utility;

import Game_Models.Node;
import static Utility.ExpConstants.POPULATION_SIZE;
import java.util.Arrays;
import java.util.Random;

/**
 * This is a library of utility methods.
 * It contains the following types of methods for 1D and 2D Arrays,
 * containing nodes with single and multiple strategies.
 * Contents:
 * - Population setup.
 * - Population printing.
 * - get count of total cooperators.
 * - get fraction of cooperation.
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class UtilityMethods {
    
    
    private static int MAX_X = 0;
    private static int MAX_Y = 0;
    private static Random generator; 
    /**
     * Creates a 1D population with a 50/50 C-D ratio
     * @param aPopulation local copy of population
     * @return populated data structure
    */
    public static Node[] setup1DPopulation(Node[] aPopulation){
        Node node;
        aPopulation = new Node[(int)POPULATION_SIZE];
        
        double nodesHalf = POPULATION_SIZE/2;
        for(int i = 0; i < aPopulation.length; i++){
            if(i < nodesHalf){ 
                node = new Node(i, true);
            } else {
                node = new Node(i, false);
            }
            aPopulation[i] = node;
        }
        
        //Fisher Yates Shuffle algorithm
        int n = aPopulation.length;
        Node randNode;
        for(int i = 0; i < aPopulation.length; i++){
            int rand = i + (int)(Math.random()*(n-i));
            randNode = aPopulation[rand];
            aPopulation[rand] = aPopulation[i];
            aPopulation[i] = randNode;
        }
        return aPopulation;
    }
    
    /**
     * Creates a 1D population with a 50/50 C-D ratio
     * @param aPopulation the array to be populated
     * @param customCost the user set cost, overrides ExperimentConstant class cost
     * @return a populated 1D array
     */
    public static Node[] setup1DPopulation(Node[] aPopulation, double customCost){
        Node node;
        aPopulation = new Node[(int)POPULATION_SIZE];
        
        double nodesHalf = POPULATION_SIZE/2;
        for(int i = 0; i < aPopulation.length; i++){
            if(i < nodesHalf){ 
                node = new Node(i, true, customCost);
            } else {
                node = new Node(i, false, customCost);
            }
            aPopulation[i] = node;
        }
        
        //Fisher Yates Shuffle algorithm
        int n = aPopulation.length;
        Node randNode;
        for(int i = 0; i < aPopulation.length; i++){
            int rand = i + (int)(Math.random()*(n-i));
            randNode = aPopulation[rand];
            aPopulation[rand] = aPopulation[i];
            aPopulation[i] = randNode;
        }
        return aPopulation;
        
    }
   
    /**
     * get max X coordinate for 2D array
     * @return the maximum x value
     */
    public static int getMAX_X(){
        return MAX_X;
    }
    
    /**
     * get max Y coordinate for 2d array
     * @return the maximum y value
     */
    public static int getMAX_Y(){
        return MAX_Y;
    }
    
    private static void setMAX_X(int aValue){
        MAX_X = aValue;
    }
    
    private static void setMAX_Y(int aValue){
         MAX_Y = aValue;
    }
     
    /**
     * Sets up 2 Dimensional population 
     * @param size Linear Size of population
     * @return the population
     */
    public static Node[][] setup2DPopulation(int size){
        
        Node[][] aPopulation;
        //get square root of size
        double sizeRoot = Math.sqrt(size);
        boolean strategies[] = new boolean[size];
        
        //create 50/50 (C/D) configuration
        for(int i = 0; i < size; i++){
            if(i < (size/2)){
                strategies[i] = false;
            } else {
                strategies[i] = true;
            }
        }
        
        //fisher yates shuffle
        //shuffles the 1d array
        int n = strategies.length;
        boolean temp;
        for(int i = 0; i < size; i++){
            int rand = i + (int)(Math.random()*(n-i));
            temp = strategies[rand];
            strategies[rand] = strategies[i];
            strategies[i] = temp;
        }

        //if square root of array size is int
        if(sizeRoot % 1 == 0){ //array is square
            
            aPopulation = new Node[(int)sizeRoot][(int)sizeRoot];
            setMAX_X((int)sizeRoot);
            setMAX_Y((int)sizeRoot);
            
            //coordinates
            int x=0, y=0;
            //System.out.println("size: " + size);
            
            //convert 1D coordinates into 2D
            for(int i = 0; i < size; i++){
                //System.out.println("x " + x + "\ny " + y);
                aPopulation[x][y]= new Node(i,(strategies[i]));
                //if y max
                if(y == sizeRoot-1){
                    //incr x
                    x = x+1;
                    //reset y
                    y = 0;
                } else {
                    y++;
                }
            }
            return aPopulation;
        
        } else {//array is rectangle
            
            //prime factorize
            int tempX = size/2;
            double tempY  = Math.sqrt((double)tempX);
            //System.out.println("tempY " + tempY);
            boolean pass = false;
            
            final int MAX_PASS = 5;
            int passCount = 0;
            //make sure number is int
            if(tempY % 1 != 0){
                while(!pass){
                    //if not
                    //make int
                    tempY = tempX/2;
                    if(tempY % 1 != 0){
                        pass = true;
                    }
                    //count pass thru's
                    ++passCount;
                    
                    
                    //if program is stuck
                    if(passCount >= MAX_PASS){
                        //brute force array
                        aPopulation = bruteForceArray(size, strategies);
                        //return a brute forced array
                        return aPopulation;
                    }
                }
            }
            int x=0, y=0;
            boolean test = false;
            //System.out.println("size:" + size);
            while(!test){
                //pass
                if((int)tempX*(int)tempY == size){
                    
                    if((int)(tempX/2)*(int)(tempY*2) == size){
                        x = (int)tempX/2;
                        y = (int)tempY*2;
                        test = true;
                    }
                } else {
                    tempX /= 2;
                    //System.out.println("not yet");
                }
            }
            setMAX_X(x);
            setMAX_Y(y);
            //System.out.println("x " + x  + "y " + y);
            aPopulation = new Node[(int)x][(int)y];
            
            int counter = 0;
            for(int i = 0; i < x; i++){
                for(int j = 0; j < y; j++){
                    aPopulation[i][j] = new Node(counter,strategies[counter]);
                    counter++;
                }
            }
            return aPopulation;
        }
    }
    
    /**
     * Fallback brute force method
     */
    private static Node[][] bruteForceArray(int size, boolean[] strategies){
        Node[][] aBruteForcedPopulation;
        int x=0, y=0;

        //try and divide by 5 first
        int divisor = 5;
        if(size%divisor == 0){
            x = 5;
            y = size/x;
            //System.out.println("y" + y);
        }
        aBruteForcedPopulation = new Node[x][y];
        int counter = 0;
        UtilityMethods.setMAX_X(x);
        UtilityMethods.setMAX_Y(y);
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                aBruteForcedPopulation[i][j] = new Node(counter, 
                        strategies[counter]);
                counter++;
            }
        }
        
        return aBruteForcedPopulation;
    }
    
    /**
     * Populates and shuffles a 2D Lattice with C-D ratio: 50/50
     * @param aPopulation the empty to be populated
     * @return the populated data structure
     */
    public static Node[][] setup2DPopulation(Node[][] aPopulation){
        //Node node;
        int size = (int)POPULATION_SIZE;
        Node[] tempPopulation = new Node[size*size];
        aPopulation = new Node[size][size];
        int count = 0;
        int nodesHalf = (size*size)/2; //half the nodes if evenly sized
        
        for(int i = 0; i < tempPopulation.length; i++){
            if(count < nodesHalf){
                tempPopulation[i]= new Node(count++, false);
            } else {
                tempPopulation[i] = new Node(count++, true);
            }
        }
        
        //Fisher Yates Shuffle algorithm
        int n = tempPopulation.length;
        Node randNode;
        for(int i = 0; i < tempPopulation.length; i++){
            int rand = i + (int)(Math.random()*(n-i));
            randNode = tempPopulation[rand];
            tempPopulation[rand] = tempPopulation[i];
            tempPopulation[i] = randNode;
        }
        
        //populate 2D array
        count = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                aPopulation[i][j] = tempPopulation[count++];
            }
        }
        return aPopulation;
    }
    
    /**
     * Populates and shuffles a 2D Lattice with C-D ratio: 50/50
     * @param aPopulation the empty to be populated
     * @param customCost user specified cost
     * @return the populated data structure
     */
    public static Node[][] setup2DPopulation(Node[][] aPopulation, 
            double customCost){
        //Node node;
        int size =(int)POPULATION_SIZE;
        Node[] tempPopulation = new Node[size*size];
        aPopulation = new Node[size][size];
        int count = 0;
        int nodesHalf = (size*size)/2; //half the nodes if evenly sized
        
        for(int i = 0; i < tempPopulation.length; i++){
            if(count < nodesHalf){
                tempPopulation[i]= new Node(count++, false, customCost);
            } else {
                tempPopulation[i] = new Node(count++, true, customCost);
            }
        }
        
        //Fisher Yates Shuffle algorithm
        int n = tempPopulation.length;
        Node randNode;
        for(int i = 0; i < tempPopulation.length; i++){
            int rand = i + (int)(Math.random()*(n-i));
            randNode = tempPopulation[rand];
            tempPopulation[rand] = tempPopulation[i];
            tempPopulation[i] = randNode;
        }
        
        //populate 2D array
        count = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                aPopulation[i][j] = tempPopulation[count++];
            }
        }
        return aPopulation;
    }
    
     /**
     * Prints 1-dimensional population of nodes with
     * a single strategy.
     * @param aPopulation The population
     */
    public static void print1DPopulation(Node[] aPopulation){
        char status;
        
        System.out.print("[");
        for(int i = 0; i < aPopulation.length; i++){
            
            if(!aPopulation[i].getCoopStatus()){
               status = 'd';
            } else {
                status = 'c';
            }
            
            if(i == aPopulation.length-1){
                System.out.print(status);
            } else {
                System.out.print(status + ", ");
            }
        }
        System.out.print("]");
    }
    
    /**
     * Prints a 2-dimensional array of nodes with a single strategy.
     * @param aPopulation the array of nodes
     */
    public static void print2DPopulation(Node[][] aPopulation){
        char status;
        int count = 0;
        System.out.print("[");
        
        for(Node[] nArray : aPopulation){
            for(Node n : nArray){
                count++;
                if(!n.getCoopStatus()){
                    status = 'd';
                } else {
                    status = 'c';
                }
                
                if(nArray.length == count){
                    System.out.println(status);
                    count = 0;
                } else {
                    System.out.print(status + ", ");
                }
            }
        }
        System.out.print("]");
    }
    
    /**
     * Prints a 1-dimensional population
     * of nodes with two strategies.
     * @param aPopulation the population to be printed
     */
    public static void print1DMultiStrategyPopulation(Node[] aPopulation){
 
        
        char status1, status2;
        System.out.print("[ ");
        for(Node n : aPopulation){
            status1 = 'd';
            status2 = 'd';
            
            if(n.getStrategyLeft()){
                status1 = 'c';
            }
            if(n.getStrategyRight()){
                status2 = 'c';
            }
            System.out.print("[" + status1 + status2 +"] " );
        }
        System.out.print(" ]");  
        System.out.println("");
    }
    
    /**
     * Prints the 2-dimensional array containing 
     * Nodes that have 1 strategy linked to each neighbor.
     * @param aPopulation the population
     */
    public static void print2DMultiStrategyPopulation(Node[][] aPopulation){
        
        Node n;
        char status, status2;
        //populate the print list
        for(Node[] nArr : aPopulation){
            int count = 0;
            while(count < nArr.length){
                if(nArr[count].getStrategyUp()){
                    status = 'c';
                } else {
                    status = 'd';
                }
                System.out.print("   " + status + "   ");
                ++count;
            }
            System.out.println("");
            count = 0;
            while(count < nArr.length){
                if(nArr[count].getStrategyLeft()){
                    status = 'c';
                } else {
                    status = 'd';
                }
                
                if(nArr[count].getStrategyRight()){
                    status2 = 'c';
                } else {
                    status2 = 'd';
                }
                System.out.print(" " + status + " o " + status2 + " ");
                ++count;
            }
            System.out.println("");
            count = 0;
            while(count < nArr.length){
                if(nArr[count].getStrategyDown()){
                    status = 'c';
                } else {
                    status = 'd';
                }
                System.out.print("   " + status + "   ");
                ++count;
            }
            System.out.println("");
        }
    }
    
    /**
     * Gets the number of cooperators in a 1D data structure
     * @param aPopulation
     * @return the total count of cooperators.
     */
    public static int get1DCoopCount(Node[] aPopulation){
        int coopCount = 0;
        for (Node n : aPopulation) {
            if (n.getCoopStatus()) {
                ++coopCount;
            }
        }
        return coopCount;
    }
        
    /**
     * Counts cooperators in a 1D array where nodes use multiple
     * simultaneous strategies.
     * @param aPopulation
     * @return the number of cooperators
     */
    public static int get1DCoopCountMulti(Node[] aPopulation){
        int coopCount = 0;
        
        for(Node n : aPopulation){
            if(n.getStrategyLeft()){
                ++coopCount;
            }
            if(n.getStrategyRight()){
                ++coopCount;
            }
        }
        return coopCount;
    }
    
    /**
     * Counts the number of cooperators in a 2D lattice
     * @param aPopulation the data structure
     * @return the number of cooperators
     */
    public static int get2DCoopCount(Node[][] aPopulation){
        int coopCount = 0;
        for(Node[] nArray : aPopulation){
            for(Node n : nArray){
                if(n.getCoopStatus()){
                    ++coopCount;
                }
            }
        }
        return coopCount;
    }
        
    /**
     * Counts cooperators in a 2D where Nodes use multiple simultaneous
     * strategies.
     * @param aPopulation
     * @return the total count of cooperators.
     */
    public static int get2DCoopCountMulti(Node[][] aPopulation){
        int coopCount = 0;
        
       //coutns cooperators for each node
        for(Node[] nArray : aPopulation){
            for(Node n : nArray){
                if(n.getStrategyLeft()){
                    ++coopCount;
                }
                if(n.getStrategyRight()){
                    ++coopCount;
                }
                if(n.getStrategyDown()){
                    ++coopCount;
                }
                if(n.getStrategyUp()){
                    ++coopCount;
                }
            }
        }
        
        return coopCount;
    }
    
    /**
     * Computes fraction of cooperation in a 1D Array
     * with Nodes using multiple simultaneous strategies
     * @param aPopulation the population
     * @return the fraction of cooperation in the array
     */
    public static double fractionOfCooperationMulti1D(Node[] aPopulation){
        double cFraction;
        int offset = 2;
        cFraction = (double) get1DCoopCountMulti(aPopulation)
                / (double)POPULATION_SIZE;
        //System.out.println(Double.toString(cFraction));
        cFraction /= (double)offset;
        return cFraction;
    }
    
    /**
     * Computes the fraction of cooperators in a 2D array
     * with Nodes using multiple simultaneous strategies.
     * @param aPopulation the population
     * @return the fraction of cooperation in the array.
     */
    public static double fractionOfCooperationMulti2D(Node[][] aPopulation){
        double cFraction;
        int offset = 4;
        
        //*10 for linear size
        cFraction = (double) get2DCoopCountMulti(aPopulation)
                /((double)POPULATION_SIZE*10);
        cFraction /= (double)offset;
        
        return cFraction;
    }
    
    /**
     * Computes the fraction of cooperation in a 2D Array
     * with nodes using a single strategy.
     * @param aPopulation the population.
     * @return the fraction of cooperation.
     */
    public static double fractionOfCooperationSingle2D(Node[][] aPopulation){
        double cFraction;
        cFraction = (double) get2DCoopCount(aPopulation)
                /(double)(POPULATION_SIZE*10);
        return cFraction;
    }
    
    /**
     * Computes the fraction of cooperation in a 1D Array 
     * with ndoes using a single strategy.
     * @param aPopulation
     * @return the fraction of cooperation.
     */
    public static double fractionOfCooperationSingle1D(Node[] aPopulation){
        double cFraction;
        
        cFraction = (double) get1DCoopCount(aPopulation)
                /(double)POPULATION_SIZE;
        return cFraction;
    }
}
