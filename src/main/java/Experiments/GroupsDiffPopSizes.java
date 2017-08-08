package Experiments;

import Game_Models.Node;
import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.GAME_ROUNDS;
import Utility.UtilityMethods;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generates data for groups over varying pop sizes.
 * b = 2.0
 * Group sizes: 128-1024 in *2 increments.
 * Uses the Prisoner's Dilemma Game
 * @author Dressler, D.B.
 */
public class GroupsDiffPopSizes {
    
    final int SAMPLES = 1000;
    Random generator;
    Node[] aPopulation;
   
    /**
     * Constructor.
     */
    public GroupsDiffPopSizes(){
        generator = new Random();
    }
    
    /**
     * 
     * @param popSize
     * @param nodesInGroup
     * @return 
     */
    public double runExperiment(int popSize, double nodesInGroup){
        int focal, opponent;
        aPopulation = new Node[popSize];
        
        double average = 0;
        for(int i = 0; i < SAMPLES; i++){
            //set up a new population 50/50
            aPopulation = UtilityMethods.setup1DPopulation(aPopulation);   
            //populate strategy tables for each constiutent
            for(int j = 0; j < aPopulation.length; j++){
                aPopulation[j].setupStrategyGroup((int)nodesInGroup);
            }
                //play game    
            for(int j = 0; j < GAME_ROUNDS; j++){
                float oppSum, focalSum;
                //run experiment in here 
                focal = generator.nextInt(aPopulation.length);
                opponent = getValidOpponent(focal);
                focalSum = playGameWithAll(focal);
                oppSum = playGameWithAll(opponent);
                
                if(focalSum < oppSum){
                    boolean newStrategy = aPopulation[opponent].getStrategyForOpponent(focal);
                    int groupToSwitch = aPopulation[focal].getGroupFor(opponent);
                       
                    aPopulation[focal].setStrategyForGroup(newStrategy, groupToSwitch);
                }
            }
            average += getTimeStepAvgCoop();
        }
        //return the average
        return average / SAMPLES;
    }
    
     /**
     * 
     * @param selectedPlayer 
     * @return  
     */
    public float playGameWithAll(int selectedPlayer){
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
                //sum += aPopulation[selectedPlayer].playPD(oppStrategies.get(i), playerStrategy);
                sum += playPDLocally(oppStrategies.get(i), playerStrategy);
            }
        }
        return sum;
    }
    /**
     * 
     * @param selectedPlayer 
     * @return  
     */
    public ArrayList<Boolean> getOppStrategies(int selectedPlayer){
       ArrayList<Boolean> oppStrategies = new ArrayList<>();
       for(int i = 0; i < aPopulation.length;i++){
           
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
    private double playPDLocally(boolean neighbor, boolean cNode){
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
     * Main entry point
     * @param args 
     */
    public static void main(String[] args) throws IOException{
        GroupsDiffPopSizes exp = new GroupsDiffPopSizes();
        String title = "Line contains:N Groups, N/2 Groups, N/4 Groups, N/8 Groups, 2 Groups";
        int minPopSize = 128, maxPopSize = 1024;
        double nodesInGroup = 1;
        //PrintWriter output = new PrintWriter("differentpopulationsizes.csv");
        double avg = 0;
        PrintWriter output = new PrintWriter("PDGame_Groups_Diff_Pop_sizes.csv");
        output.append(title);
        output.flush();
        //from 128 to 1024
        for(int pop = minPopSize; pop <= maxPopSize; pop*=2){
            output.append("\n//"+ Integer.toString(pop) + ":\n");
            //System.out.println("p:" + pop);
            //get 5 key values
            for(int i = 0; i < 5; i++){
                    
                switch(i){
                    //supply population size and number of nodes in a group
                    case 0: avg = exp.runExperiment(pop, 1); break; //N strategies
                    
                    case 1: nodesInGroup = (double)pop/((double)pop/2); //N2 groups
                    avg = exp.runExperiment(pop, nodesInGroup); break;
                            
                    case 2: nodesInGroup = (double)pop/4; 
                            avg = exp.runExperiment(pop, nodesInGroup); break;
                    
                    case 3: nodesInGroup = (double)pop/8; 
                            avg = exp.runExperiment(pop, nodesInGroup); break;
                    
                    case 4: nodesInGroup = (double)pop/2; 
                            avg = exp.runExperiment(pop, nodesInGroup); break; //2 groups
                }
                output.append(Double.toString(avg) + ", "); output.flush();
                //System.out.println(Double.toString(avg));
            }//inner for loop
        }//outer for loop
        output.close();
    }
}
