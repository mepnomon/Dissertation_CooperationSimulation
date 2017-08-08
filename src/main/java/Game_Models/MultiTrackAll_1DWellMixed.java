
package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.Random;

/**
 *
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class MultiTrackAll_1DWellMixed {

    Node[] aPopulation;
    Random generator;
    final int UPDATE_METHOD;
    
    /**
     * Constructor
     * @param update
     */
    public MultiTrackAll_1DWellMixed(int update){
       
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        generator = new Random();
        UPDATE_METHOD = update;
    }
    
    /**
     * Runs the experiment.
     * @param customCost a custom cost
     * @return  average cooperation final value
     */
    public double runExperiment(double customCost){
        
        double avgCooperation = 0;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);
        for(Node n : aPopulation)//setup local strategy arrays
            n.setupStrategyArray();
        
        //runs the experiment--------------------------------
        for(int i = 0; i < ExpConstants.GAME_ROUNDS; i++){
            
            
            switch(UPDATE_METHOD){
                case(0): replaceModel(generator.nextInt(aPopulation.length)); break;
                case(1): replaceWeakOpponent(generator.nextInt(aPopulation.length)); break;
                case(2): replaceRandomOpponent(generator.nextInt(aPopulation.length)); break;
            }
            //pick type of replacement strategy
            //select random node    
        }
        
        //retrieves averages from all nodes
        for(Node n : aPopulation)
            avgCooperation += n.getCoopAvgFromStrategies();
        //averages this value by number of nodes
        return avgCooperation /aPopulation.length;
    }
    
    /**
     * Compares nodes and updates strategy
     * @param focal node's location
     */
    public void replaceModel (int focal){
        
        int model = getValidOpponent(focal);
        //if model performs better
        //may not be correct
        if(aPopulation[focal].getSum(getStrategies(focal)) 
                < aPopulation[model].getSum(getStrategies(model))){
            
            //copy model's strategy for next encounter
            aPopulation[focal].setStrategyListEntry(model, 
                    aPopulation[model].getStrategyForOpponent(focal));
        }
    }
    
    
    /**
     * 
     * @param focal 
     */
    public void replaceWeakOpponent(int focal){
       boolean isValid = false;
       int model = getValidOpponent(focal);
       int opponent = -1;
       while(!isValid){
           opponent = getValidOpponent(focal);
           if(opponent != model){
               isValid = true;
           }
       }
       
       //replace for weaker opponent
       if(aPopulation[model].getSum(getStrategies(focal)) >
               aPopulation[opponent].getSum(getStrategies(focal))){
           

           //change strategy
           aPopulation[focal].setStrategyListEntry(opponent, 
                   aPopulation[focal].getStrategyForOpponent(model));
       }
    }
    
    /**
     * Updates strategy for a random opponent
     * @param focal
     */
    public void replaceRandomOpponent(int focal){
        
        int model = getValidOpponent(focal);
        boolean isValid = false;
        int opponent = -1;
        while(!isValid){
            opponent = getValidOpponent(focal);
            if(opponent != model){
                isValid = true;
            }
        }
        
        if(aPopulation[model].getSum(getStrategies(focal)) > 
                aPopulation[model].getSum(getStrategies(model))){
            
            aPopulation[model].setStrategyListEntry(
                    opponent, aPopulation[model].getStrategyForOpponent(focal));
        }
    }
    
     /**
     * Retrieves and locally saves the list of strategy employed by a node
     * @param nodeID identifies the node
     * @return an array of strategies
     */
    public boolean[] getStrategies(int nodeID){
        
        boolean strategies[] = new boolean[aPopulation.length];
        for(int i = 0; i < aPopulation.length; i++){
            strategies[i] = aPopulation[i].getStrategyForOpponent(nodeID);
        }
        return strategies;
    }
    
    /**
     * Gets a valid opponent locations
     * @param cardinal the location to exclude
     * @return a valid location
     */
    private int getValidOpponent(int cardinal){
        
        int opponent = -1;
        boolean isValid = false; //is the selected node valid
        while(!isValid){ //ensures that node doesn't select itself
            opponent = generator.nextInt(aPopulation.length);
            if(opponent != cardinal){
               isValid = true;
            }
        }
       return opponent; 
    }
    
}
