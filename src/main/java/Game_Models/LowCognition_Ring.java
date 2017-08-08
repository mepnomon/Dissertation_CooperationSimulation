package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * 10/12/2016
 * Simulation of a population of
 * animals with low cognition on a 1 dimensional lattice.
 * Each node has a strategy associated with one of its neighbors.
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class LowCognition_Ring{
    
    Random rand;
    Node node;
    Node[] aPopulation;
    FileWriter writeCoopRatio;
    
    /**
     * Constructor
     * @throws java.io.IOException
     */
    public LowCognition_Ring() throws IOException{
        
        rand = new Random(); //initialize rng
        //initialize population, see constant class for size
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
        writeCoopRatio = new FileWriter("1DLowCognition_RingData.csv"); //initialize writer
    }
    
    /**
     * Call this method to begin the experiment.
     * @throws java.io.IOException
     */
    public void performExperiment() throws IOException{
        
        int selectedNode; // cardinal node's location
        int interactions = ExpConstants.GAME_ROUNDS;
        //runs the simulation 
        UtilityMethods.print1DMultiStrategyPopulation(aPopulation);
        for(int i = 0; i < interactions; i++){
            selectedNode = rand.nextInt(aPopulation.length);
            System.out.println("Ite:" + i);
            System.out.println("Selected"  + selectedNode);
            computePayoff(selectedNode);
            compareAndUpdate(selectedNode);
            UtilityMethods.print1DMultiStrategyPopulation(aPopulation);
            writeCoopRatio.append(Double.toString(
                    UtilityMethods.fractionOfCooperationMulti1D(aPopulation)) 
                    + ", ");
        }
        writeCoopRatio.close(); // close writer
    }
    
    public double performExperiment(double customCost){
        int selectedNode;
        int interactions = ExpConstants.GAME_ROUNDS;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);
        for(int i = 0; i < interactions; i++){
            selectedNode = rand.nextInt(aPopulation.length);
            computePayoff(selectedNode);
            compareAndUpdate(selectedNode);
        }
        return UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
    }
    
    /**
     * Computes the payoff of a node
     * @param loc the location of the node
     */
    private void computePayoff(int loc){
        
        int leftNeighbor, rightNeighbor;
        
        //selects direct neighbors, takes boundaries into account
        if(loc == aPopulation.length-1){
            leftNeighbor = loc-1; rightNeighbor = 0;  
        } else if (loc == 0){
            leftNeighbor = aPopulation.length-1; rightNeighbor = 1;
        } else {
            leftNeighbor = loc-1; rightNeighbor = loc+1;
        }
        
        //gets the cumulative payoff of the cardinal node
        aPopulation[loc].getSum(aPopulation[leftNeighbor].getStrategyRight(), 
                aPopulation[rightNeighbor].getStrategyLeft());
    }
    
    /**
     * Picks a random neighbor, copies its model strategy
     * if payoff_i < payoff n
     * then uses strategy against a random neighbor.
     * 
     * @param loc location of the cardinal node
     */
    private void compareAndUpdate(int loc){
        int neighborLoc;
        float pick = rand.nextFloat(); //pick random neighbor to compare with
        boolean isNeighborLeft, neighborStrategy; //is neighbor to the left
        
        if(pick > 0.5){ // compare with right neighbor
            if(loc == aPopulation.length-1){
                neighborLoc = 0;
            } else {
                neighborLoc = loc+1;
            }
            isNeighborLeft = false;
            //System.out.println("comparing to right neigbor");
        } else { // compare to left neighbor
            if(loc == 0){
                neighborLoc = aPopulation.length-1;
            } else {
                neighborLoc = loc-1;
            }
            isNeighborLeft = true;
            //System.out.println("comparing to left neighbor");
        }
        computePayoff(neighborLoc); // neighbor's cumulative payoff
        
        //comparison
        if(aPopulation[loc].getFitness() < aPopulation[neighborLoc].getFitness()){
            //randomly replaces one of the cardinal's strategies.
            if(!isNeighborLeft){ //right neighbor
                neighborStrategy = aPopulation[neighborLoc].getStrategyLeft();
            } else { //left neighbor
                neighborStrategy = aPopulation[neighborLoc].getStrategyRight();
            }
            pick = rand.nextFloat();
            if(pick > 0.5){ //replaces right strategy
                //System.out.println("Replace: right");
                aPopulation[loc].setStrategyRight(neighborStrategy);
                
            } else { //replaces left strategy
                aPopulation[loc].setStrategyLeft(neighborStrategy);
                //System.out.println("replace left");
            }
        }
    }
} // end of class
