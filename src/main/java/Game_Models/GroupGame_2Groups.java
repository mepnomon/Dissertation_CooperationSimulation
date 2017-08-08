package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.Random;

/**
 * In this Simulation, the population is divided into
 * a set of groups to the left and right of the neighbor.
 * 
 * @author D.B. Dressler(eeu436@bangor.ac.uk)
 */
public class GroupGame_2Groups {
    
    //class variables
    Node aPopulation[];
    Random generator;
    final int UPDATE_METHOD;
    //0 for weakest, 1 for same group, 2 for random
    
    
    /**
     * Constructor
     * @param update
     */
    public GroupGame_2Groups(int update){
     
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        generator = new Random();
        UPDATE_METHOD = update;
    }
    
    
    /**
     * Performs the experiment.
     * @param customCost    a custom set cost value for the game
     * @return  an averaged percentage of cooperation in the population at
     * at a final state
     */
    public double runExperiment(double customCost){
        
        int cardinal, modelGroup, oppGroup; //centered node and chosen group and the other group
        boolean s1, s2, s3; //2 strategies
        double runningTotal, runningTotal_0, avgCooperation=0; //running totals for each group coop average
        //fill the array with random configuration 50/50
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);
        
        //setup strategy groups for each node
        for(Node n : aPopulation) 
            n.setupStrategyGroup();
        
        //------------run experiment for sample size---------------------
        for(int i = 0; i < ExpConstants.GAME_ROUNDS; i++){
            
            //set running total to 0
            runningTotal = 0; runningTotal_0 = 0;
           
            //select random player
            cardinal = generator.nextInt(aPopulation.length);
           
            //select group to model
            modelGroup = 1;
            oppGroup = 0;
            if(generator.nextInt()<0.5){ //randomize choice
                modelGroup = 0;
                oppGroup = 1;
            }
            
            //get player strategy
            s2 = aPopulation[cardinal].getStrategyForGroup(modelGroup);
            s3 = aPopulation[cardinal].getStrategyForGroup(oppGroup);
           
            //select all encounters in group and sum payoffs
            for(int next = 0; next < aPopulation.length; next++){
                if(next != cardinal){ //exclude cardinal
                    //get next strategy used against cardinal
                    s1 = aPopulation[next].getStrategyForOpponent(cardinal);
                    //if next is in model group
                    if(aPopulation[cardinal].getGroupFor(next) == modelGroup){
                        runningTotal += aPopulation[cardinal].playPGG(s1, s2);
                    } else {
                        runningTotal_0 += aPopulation[cardinal].playPGG(s1, s3);
                    }
                }
            }//end for loop
            
            
            //select update method
            switch(UPDATE_METHOD){
                case(0): updateWeakOpponent(runningTotal, runningTotal_0, 
                        cardinal, oppGroup, s2); break;
                case(1): updateRandomGroup(cardinal, s2, runningTotal, 
                        runningTotal_0); break;
            }
        }//end main loop

        //get average cooperation for each node group
        for(Node n : aPopulation)
            avgCooperation += n.getCoopAvgFromStrategies();
    
        //return an average
        return avgCooperation/aPopulation.length;
    }
    
    /**
     * Updates the 
     * @param runningTotal
     * @param runningTotal_0
     * @param cardinal
     * @param oppGroup
     * @param strategy 
     */
    public void updateWeakOpponent(double runningTotal, double runningTotal_0,
            int cardinal, int oppGroup, boolean strategy){
            
            //if model group outperforms opposing group
            if(runningTotal > runningTotal_0){
                //change strategy to the same used for model
                aPopulation[cardinal].setStrategyForGroup(strategy, oppGroup);
            }
    }
    
    public void updateForSameGroup(){
        
        
    }
    
    /**
     * Updates the strategy for a random encounter
     * @param cardinal the cardinal node
     * @param strategy  the replacing strategy
     * @param runningTotal
     * @param runningTotal_0
     */
    public void updateRandomGroup(int cardinal, boolean strategy, double runningTotal, double runningTotal_0){
        
        //if 
        if(runningTotal > runningTotal_0)
            //choose random group to update
            if(generator.nextFloat() < 0.5){
                //update
                aPopulation[cardinal].setStrategyForGroup(strategy, 1);
            } else {
                //update
                aPopulation[cardinal].setStrategyForGroup(strategy, 0);
            }
    }
}
