package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.io.IOException;
import java.util.Random;

/**
 * Cooperation Simulator
 * A class to investigate the performance of
 * nodes with low cognitive abilities, 
 * using multiple simultaneous strategies in 2D.
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class LowCognition_2DStructure {
    Random rng;
    Node aPopulation[][];
    Node n;
    final int MAX, MIN;
    //FileWriter coopRatioWriter;
    
    /**
     * Constructor
     * @throws java.io.IOException
     */
    public LowCognition_2DStructure() throws IOException{
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation);
        rng = new Random();
        MAX = aPopulation.length-1;
        MIN = 0;
        //coopRatioWriter = new FileWriter("LowCognition2D_CoopRatio.csv");
    }
    
    /**
     * Call this method to perform the experiment
     * @throws java.io.IOException
     */
    public void performExperiment() throws IOException{
        int interactions = ExpConstants.GAME_ROUNDS;
        int randX, randY;
        UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
        for(int i = 0; i < interactions; i++){
            System.out.println("Ite:" + i);
            randX = rng.nextInt(aPopulation.length);
            randY = rng.nextInt(aPopulation.length);
            System.out.println("Select:" + randX + " " +  randY);
            computePayoff(randX, randY);
            compareAndUpdate(randX, randY);
//            UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
            System.out.println("CoopRatio:" 
                    + UtilityMethods.fractionOfCooperationMulti2D(aPopulation));
            //coopRatioWriter.append(Double.toString(UtilityMethods.fractionOfCooperationMulti2D(aPopulation)) + ", ");
            UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
        }
        System.out.println("CoopRatio:" 
                    + UtilityMethods.fractionOfCooperationMulti2D(aPopulation));
        //UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
        //coopRatioWriter.close();//close file writer
    }
    
    /**
     * 
     * @param customCost 
     * @return  
     */
    public double performExperiment(double customCost){
        int interactions = ExpConstants.GAME_ROUNDS;
        int randX, randY;
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation, customCost);
        
        for(int i = 0; i < interactions; i++){
            randX = rng.nextInt(aPopulation.length);
            randY = rng.nextInt(aPopulation.length);
            computePayoff(randX, randY);
            compareAndUpdate(randX, randY);
        }
        return UtilityMethods.fractionOfCooperationMulti2D(aPopulation);
    }
    
    /**
     * Computes cumulative payoff of a node.
     * @param x node's row
     * @param y node's column
     */
    private void computePayoff(int x, int y){
        boolean leftNeighbor, rightNeighbor, upNeighbor, downNeighbor;
        
        if(x == MAX){ 
            upNeighbor = aPopulation[MIN][y].getStrategyDown();    
        }else{
            upNeighbor = aPopulation[x+1][y].getStrategyDown();
        }
        
        if(x == MIN){
            downNeighbor = aPopulation[MAX][y].getStrategyUp();
        } else {
            downNeighbor = aPopulation[x-1][y].getStrategyUp();
        }
        
        if(y == MAX){
            rightNeighbor = aPopulation[x][MIN].getStrategyLeft();
        } else {
            rightNeighbor = aPopulation[x][y+1].getStrategyLeft();
        }
        
        if(y == MIN){
            leftNeighbor = aPopulation[x][MAX].getStrategyRight();
        } else {
            leftNeighbor = aPopulation[x][y-1].getStrategyRight();
        }
        
        aPopulation[x][y].getSum(leftNeighbor, rightNeighbor, upNeighbor, 
                downNeighbor);
    }
    
    /**
     * compares and updates strategy
     * @param x node's row
     * @param y node's column
     */
    private void compareAndUpdate(int x, int y){
        int pick = rng.nextInt(4);
        int next = 0;
        boolean newStrategy;
        
        
        //selects a neighbor
        switch(pick){
            case 0: if(y==MIN)next=MAX; else next=y-1; break;
            case 1: if(y==MAX)next=MIN; else next=y+1; break;
            case 2: if(x==MIN)next=MAX; else next=x-1; break;
            case 4: if(x==MAX)next=MIN; else next=x+1; break;
        }
        
        //select neighboring strategy
        if(pick < 2){
            computePayoff(x, next);
            if(pick == 0){  //left neighbor
                newStrategy = aPopulation[x][next].getStrategyRight();
            } else {        //right neighbor
                newStrategy = aPopulation[x][next].getStrategyLeft();    
            }
            if(aPopulation[x][y].getFitness() < aPopulation[x][next].getFitness()){
                pickNodeToReplace(x, y, newStrategy);
            }
        } else {
            computePayoff(next, y);
            if(pick == 2){  //Down
                newStrategy = aPopulation[next][y].getStrategyUp();
            } else {        // Up
                newStrategy = aPopulation[next][y].getStrategyDown();
            }
            if(aPopulation[x][y].getFitness() < aPopulation[next][y].getFitness()){
                pickNodeToReplace(x,y,newStrategy);
            }
        }
    }
    
    /**
     * Updates the strategy for a random opponent.
     * @param x
     * @param y
     * @param newStrategy 
     */
    private void pickNodeToReplace(int x, int y, boolean newStrategy){
        int pick = rng.nextInt(4);
        switch(pick){
            case 0: aPopulation[x][y].setStrategyLeft(newStrategy); break;
            case 1: aPopulation[x][y].setStrategyRight(newStrategy); break;
            case 2: aPopulation[x][y].setStrategyDown(newStrategy); break;
            case 3: aPopulation[x][y].setStrategyUp(newStrategy); break;
        }
    }
}
