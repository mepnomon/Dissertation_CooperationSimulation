package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.ArrayList;
import java.util.Random;

/**
 * Simulates
 * @author D.B. Dressler
 */
public class GroupGame_VarGroups {
    
    //globals
    double[] groupTotals;
    Node aPopulation[];
    Random generator;
    final int UPDATE_METHOD;
    
    /**
     * Constructor 
     * @param updateMethod 
     */
    public GroupGame_VarGroups(int updateMethod){
        
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        generator = new Random();
        UPDATE_METHOD = updateMethod;
    }
    
    /**
     * 
     * @param customCost
     * @param groupSize
     * @return 
     */
    public double runExperiment(double customCost, int groupSize){
       
        //Node n;
        int cardinal, model, modelGroup, opponent = 0; // the cardinal node
        boolean modelStrategy,oppStrategy, isValid = false;
        ArrayList<Boolean> oppStrategies, modelStrategies;
        double modelSum, oppSum, avg = 0;
        //groupTotals = new double[groupSize];
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);

        for(Node n : aPopulation){
            n.setupStrategyGroup(groupSize);
        }
        
        //run simulation
        for(int i = 0; i < ExpConstants.GAME_ROUNDS ;i++){
            modelSum = 0; oppSum = 0;
            //select cardinal
            cardinal = generator.nextInt(aPopulation.length);
            //get model opponent
            model = getValidOpponent(cardinal);
            //System.out.println("Model: " + model);
            //get group ID for model
            modelGroup = aPopulation[cardinal].getGroupFor(model);
            modelStrategy = aPopulation[cardinal].getStrategyForGroup(modelGroup);
            //initialize list for strategies from model group
            modelStrategies = getOpponentStrategies(modelGroup, cardinal);
            //select an opponent group that isn't the model group
            while(!isValid){
                opponent = generator.nextInt(groupSize);
                //System.out.println("Opp selection" + opponent);
                if(opponent != modelGroup){
                    isValid = true;
                }
            }
            //System.out.println("Opp" + opponent);
            //reset value
            isValid = false;
            //assign strategy used against opp
            oppStrategy = aPopulation[cardinal].getStrategyForGroup(opponent);
            //initialize the array list for strategies
            oppStrategies = getOpponentStrategies(opponent, cardinal);
            
            //get sums from PGG
            modelSum = aPopulation[cardinal].getSum(modelStrategy, modelStrategies);
            //System.out.println("Model sum" + modelSum);
            oppSum = aPopulation[cardinal].getSum(oppStrategy, oppStrategies);
            //System.out.println("Opp sum" + oppSum);
            
            //if model performs better than opponent
            if(modelSum > oppSum){
                //replace strategy against opponent group
                aPopulation[cardinal].setStrategyForGroup(modelStrategy, opponent);
            }
        }
        
        //calculate avg cooperation
        for(Node n : aPopulation)
            avg += n.getCoopAvgFromStrategies();
        return Math.abs(avg/aPopulation.length);
    }
    
    /**
     * 
     */
    private void replaceWeakOpponent(){
        
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
    
    /**
     * Creates a data structure containing the strategies
     * members of a group are using against the node.
     * @param group the specified group to retrieve the data for
     * @param nodeID the node that requests the list 
     * @return a collection of booleans from the opposing group
     */
    private ArrayList<Boolean> getOpponentStrategies(int group, int nodeID){        
        ArrayList<Boolean> anArrayList = new ArrayList<>();
        //iterate population
        for(int i = 0; i < aPopulation.length; i++){
            //if node is member of the group and is not main node
            if(aPopulation[nodeID].getGroupFor(i) == group && i != nodeID){
                //append the strategy
                anArrayList.add(aPopulation[i].getStrategyForOpponent(nodeID));
            }
        }
        //return the arraylist
        return anArrayList;
    }
}
