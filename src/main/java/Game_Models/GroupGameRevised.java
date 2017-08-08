
package Game_Models;

import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.POPULATION_SIZE;
import java.util.ArrayList;
import java.util.Random;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.Notifications.ERR_FILENOTCREATED;
import static Utility.UtilityMethods.setup1DPopulation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *A class for debugging the group game
 * PLAYS TIT FOR TAT AT THE MOMENT
 * @author 
 */
public class GroupGameRevised {
    
    Node[] aPopulation;
    Random generator;
    int nodesInGroup;
    final int SAMPLE_SIZE = 100;
    float averages[];
    
    /**
     * Constructor for the Group Game
     */
    public GroupGameRevised(){
        
        //initialize population
        aPopulation = new Node[(int)POPULATION_SIZE];
        generator = new Random();
    }
    
    /**
     * Runs an experiment
     * @param nodesInGroup
     * @return 
     */
    public float[] runExperiment(int nodesInGroup, double cCost){
        
        //declare array that contains the averages
        averages = new float[GAME_ROUNDS];
        // obtain samples
        for(int i = 0; i < SAMPLE_SIZE; i++){
           
            float modelSum, focalSum, average = 0;
            //fill population with 50% cooperators
            aPopulation = setup1DPopulation(aPopulation);//don't do this anymore
//            for(int j = 0; j < aPopulation.length; j++){
//                aPopulation[j] = new Node(j, false);
//            }

            //set up strategy groups    
            for(int j = 0; j < aPopulation.length; j++){
                aPopulation[j].setupStrategyGroup(nodesInGroup);
            }

            int aFocalPlayer;
            int aModelPlayer;
            
            //plays the game for the specified number of rounds
            for(int j = 0; j < GAME_ROUNDS; j++){
          
                average = 0;
                //pick focal player
                aFocalPlayer = generator.nextInt(aPopulation.length);
                //pick a valid model player
                aModelPlayer = getValidOpponent(aFocalPlayer);

                //get their cumulative payoffs from the PD game
                focalSum = playGameWithAll(aFocalPlayer, cCost);
                modelSum = playGameWithAll(aModelPlayer, cCost);
                
                //if model outperforms focal player
                if(focalSum < modelSum){
                    
                    //mimic the model's strategy
                    boolean aNewStrategy = aPopulation[aModelPlayer].getStrategyForOpponent(aFocalPlayer);
                    //select the group to switch the strategy for, this group should be the weakest,
                    //otherwise this is just tit for tat
                    int aGroupToReplace = aPopulation[aFocalPlayer].getGroupFor(aModelPlayer);
    //                System.out.println("Adopting opponent staretgy " + Boolean.toString(newStrategy) 
    //                        +" for group " + groupToSwitch);
                    aPopulation[aFocalPlayer].setStrategyForGroup(aNewStrategy, aGroupToReplace);

                    //System.out.println("Verifying new strategy: " + aPopulation[focal].getStrategyForGroup(groupToSwitch));
                }
               modelSum = 0; focalSum = 0;
               average = getTimeStepAvgCoop();
               //System.out.println("Timestep Avg:" + average);
               averages[j] += average;
            }//end of single experiment loop
        }//end of outer for loop
       
        //average the values
       for(int i = 0; i < averages.length; i++){
           //f /= (float)SAMPLE_SIZE;
           averages[i] /= (float)SAMPLE_SIZE;   
       }
       return averages;
    }
    
    /**
     * Plays a game with all opponents and assigns
     * the cumulative payoff from each group
     * @param aSelectedPlayer 
     * @return  
     */
    public float playGameWithAll(int aSelectedPlayer, double cCost){
        //get opponent strategies
        ArrayList<Boolean> oppStrategies = getOppStrategies(aSelectedPlayer);
        int group = -1;
        boolean playerStrategy = false;
        float sum = 0;
        
        //this may need ot keep track of the groups a player belongs to
        //System.out.println("Player:" + selectedPlayer);
        for(int i = 0; i < aPopulation.length; i++){
            
            //play with all except self
            if(i != aSelectedPlayer){
                
                //get the group the opponent is in
                group = aPopulation[aSelectedPlayer].getGroupFor(i);
                                                                                //System.out.println(i + " is in group " + group);
                //get the strategy usde against that group
                playerStrategy = aPopulation[aSelectedPlayer].getStrategyForGroup(group);
                                                                                 //System.out.println("Strategy for group " + Boolean.toString(playerStrategy));
                                                                                   //sum += aPopulation[selectedPlayer].playPD(oppStrategies.get(i), playerStrategy);
                                                                                   
                //supply opponent strategy and player strategy to 
                sum += playPDLocally(oppStrategies.get(i), playerStrategy, cCost);
            }
        }
        return sum;
    }
    
    /**
     * Gets all strategies used against a player
     * @param selectedPlayer 
     * @return  
     */
    public ArrayList<Boolean> getOppStrategies(int selectedPlayer){
       
        ArrayList<Boolean> oppStrategies = new ArrayList<>();
       
        for(int i = 0; i < aPopulation.length; i++){
           
            oppStrategies.add(aPopulation[i].
                getStrategyForOpponent(selectedPlayer));
               
            int num = aPopulation[selectedPlayer].getGroupFor(i);
            // System.out.print("Player " + i + " is in group: " + num + "\nStategy used against is: ");
               
            boolean b = aPopulation[selectedPlayer].getStrategyForGroup(num);

       }
       return oppStrategies;
    }
    
    /**
     * 
     * @param focal
     * @return 
     */
    public int getValidOpponent(int focal){
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
     * 
     * @return 
     */
    public float getTimeStepAvgCoop(){
        float tempAvg=0;
        
        for(Node n : aPopulation){
            tempAvg += n.getCoopAvgFromStrategies();
            //System.out.println("Temp Avg:" + tempAvg);
        }
        
        return tempAvg/(aPopulation.length);
    }
    
    /**
     * Local version of playPD w/ different b values
     * @param neighbor
     * @param cNode
     * @param cognitiveCost
     * @return 
     */
    private double playPDLocally(boolean neighbor, boolean cNode,
            double cognitiveCost){
        //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!cNode && neighbor){
            return DEFECTION_P;// - cognitiveCost;
        }
        // check C-C encounter
        if(cNode && neighbor){
            return 1.0;// - cognitiveCost;
        }
        // check D-D encounter
        if(!cNode && !neighbor){
            return EPSILON;// - cognitiveCost;
        }
        //return the payoff
        return payoff;// - cognitiveCost;
    }
    
    
     /**
     * main method
     * @param args 
     */
    public static void main(String[] args) {
        float averages[];
        GroupGameRevised exp = new GroupGameRevised();
        PrintWriter output;
        int nodesInGroup = 1;
        double customDefectioNP = 2;
        try{
            while(nodesInGroup <= 128){
                output = new PrintWriter(new BufferedWriter(
                        new FileWriter("Groups_PD_" + nodesInGroup + "_Nodes_p_Gr_cost.csv")));

                averages = exp.runExperiment(nodesInGroup, customDefectioNP);
                for(float f : averages){
                    output.append(Float.toString(f) + ", ");
                    output.flush();   
                }
                output.close();
                System.out.println("Exp: " + nodesInGroup + "done");
                customDefectioNP -= 0.1;
                nodesInGroup *= 2;
            }
        } catch (IOException ex){
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
