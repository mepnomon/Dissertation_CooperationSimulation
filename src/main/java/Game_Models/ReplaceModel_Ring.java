package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.Random;


/**
 * A class to investigate the performance 
 * of nodes with multiple strategies in a 1D population that 
 * copies a model's strategy and uses it with the model.
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class ReplaceModel_Ring {
    
    Node[] aPopulation;
    Random rand;
    
    /**
     * Constructor
     */
    public ReplaceModel_Ring(){
        rand = new Random();
        
    }
    
    /**
     * Performs the experiment.
     */
    public void performExperiment(){
        int interactions = ExpConstants.GAME_ROUNDS;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
        int selectedNode;
        
        for(int i = 0; i < interactions; i++){
            selectedNode = rand.nextInt(aPopulation.length);
            computePayoff(selectedNode);
            compareAndUpdate(selectedNode);
            UtilityMethods.print1DMultiStrategyPopulation(aPopulation);
            System.out.println(UtilityMethods.fractionOfCooperationMulti1D(aPopulation));
        }
    }
    
    /**
     * Performs the experiment.
     * @param customCost user set cost
     * @return fraction of cooperation
     */
    public double performExperiment(double customCost){
        int interactions = ExpConstants.GAME_ROUNDS;
        int selectedNode;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);
         for(int i = 0; i < interactions; i++){
            selectedNode = rand.nextInt(aPopulation.length);
            computePayoff(selectedNode);
            compareAndUpdate(selectedNode);
            //UtilityMethods.print1DMultiStrategyPopulation(aPopulation);
            //System.out.println(UtilityMethods.fractionOfCooperationMulti1D(aPopulation));
        }
         
        return UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
    }//end of constructor
    
    /**
     * 
     * @param selectedNode 
     */
    private void computePayoff(int selectedNode){
        int leftNeighbor, rightNeighbor;
        
        //selects neighbors, takes into account boundaries
        if(selectedNode == aPopulation.length-1){
            leftNeighbor = selectedNode-1; rightNeighbor = 0;  
        } else if (selectedNode == 0){
            leftNeighbor = aPopulation.length-1; rightNeighbor = 1;
        } else {
            leftNeighbor = selectedNode-1; rightNeighbor = selectedNode+1;
        }
        
        aPopulation[selectedNode].getSum(aPopulation[leftNeighbor].getStrategyRight()
                ,aPopulation[rightNeighbor].getStrategyLeft());
    }//end of computePayoff
    
    /**
     * 
     * @param selectedNode 
     */
    private void compareAndUpdate(int selectedNode){
        float pick = rand.nextFloat();
        int neighbor;
        boolean newStrategy, isNeighborLeft;
        
         //pick a neighbor to compare payoffs with
        if(pick > 0.5){ //pick right neighbor
            if(selectedNode == aPopulation.length-1){
                neighbor = 0;
            } else {
                neighbor = selectedNode+1;
            }
            isNeighborLeft = false;
        } else { // pick left neighbor
            if(selectedNode == 0){
                neighbor = aPopulation.length-1;
            } else {
                neighbor = selectedNode-1;
            }
            isNeighborLeft = true;
        }
        computePayoff(neighbor);
        
        if(aPopulation[selectedNode].getFitness() < aPopulation[neighbor].getFitness()){
            
            if(!isNeighborLeft){ //if right neighbor
                aPopulation[selectedNode].setStrategyRight(aPopulation[neighbor].getStrategyLeft());
            } else {
                aPopulation[selectedNode].setStrategyLeft(aPopulation[neighbor].getStrategyRight());
                
            }
        }
    }//end of compare and update
}//end of class
