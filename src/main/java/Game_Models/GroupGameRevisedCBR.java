package Game_Models;

import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.ExpConstants.POPULATION_SIZE;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Dissertation
 * Program to simulate effect of changes in defection potential b.
 * @author D. Dressler
 */
public class GroupGameRevisedCBR {
    
    Node[] aPopulation; //the population
    Random generator; //random generator
    int nodesInGroup; //number of nodes in groip
    final int SAMPLE_SIZE = 100; //sample size
    
    /**
     * Constructor
     */
    public GroupGameRevisedCBR(){
        
        //initialize population
        aPopulation = new Node[(int)POPULATION_SIZE];
        generator = new Random();
    }
    
    /**
     * Performs the experiment
     * @param nodesInGroup
     * @param bValue
     * @return 
     */
     public double runExperiment(int nodesInGroup, double bValue){
        double average = 0;
        
        //run for number of samples
        for(int i = 0; i < SAMPLE_SIZE; i++){
            //declare and initialize sum accumulators
            double oppSum, focalSum;
            
            //fill population with 50% cooperators
            for(int j = 0; j < aPopulation.length; j++){
                aPopulation[j] = new Node(j, false);
            }

            //set up strategy groups    
            for(int j = 0; j < aPopulation.length; j++){
                aPopulation[j].setupStrategyGroup(nodesInGroup);
            }
            
            int focal; //the focal player
            int opponent; //the opponent
            
            //play specified number of rounds
            for(int j = 0; j < GAME_ROUNDS; j++){
          
                //pick focal
                focal = generator.nextInt(aPopulation.length);
                //pick opponent
                opponent = getValidOpponent(focal);
                
                //play the game with population
                focalSum = playGameWithAll(focal, bValue);
                oppSum = playGameWithAll(opponent, bValue);
                
                //compare performance
                if(focalSum < oppSum){
                    
                    //update strategy
                    boolean newStrategy = aPopulation[opponent].getStrategyForOpponent(focal);
                    int groupToSwitch = aPopulation[focal].getGroupFor(opponent);
                    aPopulation[focal].setStrategyForGroup(newStrategy, groupToSwitch);

                }
               oppSum = 0; focalSum = 0;
               //System.out.println(Double.toString(getTimeStepAvgCoop()));
            }//end of single experiment loop
            average += getTimeStepAvgCoop();
        }//end of outer for loop
        
       //return average at final step
       double output = average/SAMPLE_SIZE;
       return output;
    }
     
     /**
     * Picks an opponent
     * @param focal the focal player
     * @return an opponent that is not the focal player 
     */
    private int getValidOpponent(int focal){
        int aValidOpp = 0;
        boolean isValid = false; 
       
        while(!isValid){
            aValidOpp = generator.nextInt(aPopulation.length);
            if(aValidOpp != focal){
                isValid = true;
            }
        }
        return aValidOpp;
    }
    
     /**
     * Local version of playPD w/ different b values
     * @param neighbor
     * @param cNode
     * @return 
     */
    private double playPDLocally(boolean neighbor, boolean cNode, double bValue){
        //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!cNode && neighbor){
            return bValue;
        }
        // check C-C encounter
        if(cNode && neighbor){
            return 1.0;
        }
        // check D-D encounter
        if(!cNode && !neighbor){
            return EPSILON;
        }
        //return the payoff
        return payoff;
    }
    
        
    /**
     * Accumulates strategies of all opponents in population.
     * @param selectedPlayer 
     * @return  
     */
    private ArrayList<Boolean> getOppStrategies(int selectedPlayer){
        ArrayList<Boolean> oppStrategies = new ArrayList<>();
        int groups;
        
        //retrieve strategies opponents use against selected player
        for (Node n : aPopulation) {
            oppStrategies.add(n.getStrategyForOpponent(selectedPlayer));
        }
       //return list of opponent strategies
       return oppStrategies;
    }
    
    /**
     * Plays game with all nodes
     * @param selectedPlayer 
     * @return  
     */
    private float playGameWithAll(int selectedPlayer, double bValue){
        //get opponent strategies
        ArrayList<Boolean> oppStrategies = getOppStrategies(selectedPlayer);
        int group = -1;
        boolean playerStrategy = false;
        float sum = 0;
        //System.out.println("Player:" + selectedPlayer);
        for(int i = 0; i < aPopulation.length;i++){
            
            if(i != selectedPlayer){
                //get strategy to be used with group
                group = aPopulation[selectedPlayer].getGroupFor(i);
                //System.out.println(i + " is in group " + group);
                playerStrategy = aPopulation[selectedPlayer].getStrategyForGroup(group);
                //System.out.println("Strategy for group " + Boolean.toString(playerStrategy));
                sum += playPDLocally(oppStrategies.get(i), playerStrategy, bValue);
            }
        }
        return sum;
    }
    
     /**
     * Retrieves population-wide level of cooperation at the given time.
     * @return average cooperation.
     */
    public double getTimeStepAvgCoop(){
        float tempAvg=0;
        
        for(Node n : aPopulation){
            tempAvg += n.getCoopAvgFromStrategies();
            //System.out.println("Temp Avg:" + tempAvg);
        }
        
        return tempAvg/(aPopulation.length);
    }
    
}
