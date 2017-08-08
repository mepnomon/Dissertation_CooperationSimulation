package Game_Models;

import Utility.ExpConstants;
import static Utility.Notifications.ERR_FILENOTCREATED;
import Utility.UtilityMethods;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Utility.ExpConstants.GAME_ROUNDS;

//print sample population
/**
 * Cooperation Simulation of the proposed Group Game Model.
 * Underlying game theoretical model is the Prisoner's Dilemma game.
 * Used to verify findings against other research.
 * @author D.B. Dressler
 */
public class PD_GroupsGame{
    
    
    //globals
    double[] groupTotals;
    Node aPopulation[];
    Random generator;
    //Buffered Writer to print performance
    PrintWriter output;
    
    /**
     * 
     */
    public PD_GroupsGame(){
        
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        generator = new Random();
    }
    
    
    /**
     * Performs the experiment
     * @param numOfGroups
     * @return fraction of cooperation 
     */
    public double[] runExperiment(int numOfGroups){

        double localAvg[] = new double[GAME_ROUNDS];
        int iterations = 1000;
        //Node n;
        //to compute the averages
        for(int i = 0; i < iterations; i++){
            
            //addresses of focal, model and opposing nodes
            // modelGroup stores address of selected group
            int focal, model, modelGroup, opponent = 0;
            boolean modelStrategy,oppStrategy, isValid = false;
            //stores the opponent strategies
            ArrayList<Boolean> oppStrategies, modelStrategies;
            
            //local sums and average
            double modelSum, oppSum, sum = 0;
            //set up the population
            aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
            //set up the strategy groups for each node
            for(Node n : aPopulation){
                n.setupStrategyGroup(numOfGroups);
            }
            
//            for(int k = 0; k < aPopulation.length; k++){
//                for(int l = 0; l < aPopulation.length; l++){
//                    System.out.print("K: " + k + "l " + l +  " ");
//                    System.out.println(Boolean.toString(aPopulation[k].getStrategyForOpponent(l)));
//                }
//            }
            
            if(i == 0){
                //print sample strategy
//                for(int j = 0; j < 128; j++)
//                    System.out.println(Boolean.toString(aPopulation[i].getStrategyForGroup(j)));
               aPopulation[i].getStrategyGroup();
            }
            //run simulation
            for(int j = 0; j < GAME_ROUNDS ;j++){
                //reset values
                modelSum = 0; oppSum = 0; sum = 0;
                //select focal player
                focal = generator.nextInt(aPopulation.length);
                //get model opponent
                model = getValidOpponent(focal);
                //System.out.println("Model: " + model);
                //get group ID for model
                
                modelGroup = aPopulation[focal].getGroupFor(model);
                //get the strategy used against the model
                modelStrategy = aPopulation[focal].getStrategyForGroup(modelGroup);
                //initialize list for strategies from model group
                //this line needs to be changed for mixed
                //modelStrategies = getOpponentStrategies(modelGroup, focal); <= commented out
                //get model strategies for well mixed
                modelStrategies = getOpponentStrategiesWM(model);
                //select an opponent group that isn't the model group
                while(!isValid){
                    opponent = getValidOpponent(focal);//generator.nextInt(groupSize);
                    //System.out.println("Opp selection" + opponent);
                    if(opponent != modelGroup){
                        isValid = true;
                    }
                }
                //System.out.println("Opp" + opponent);
                //reset value
                isValid = false;
                //assign strategy used against opp - not used in WM
                oppStrategy = aPopulation[focal].getStrategyForGroup(opponent);
                //initialize the array list for strategies
                //oppStrategies = getOpponentStrategies(opponent, focal);
                
                oppStrategies = getOpponentStrategiesWM(opponent);

                //get sums from PGG
                modelSum = aPopulation[model].getSumPD(modelStrategy, modelStrategies);
                //System.out.println("Model sum" + modelSum);
                oppSum = aPopulation[opponent].getSumPD(oppStrategy, oppStrategies);
                //System.out.println("Opp sum" + oppSum);

                //if model performs better than opponent
                if(modelSum > oppSum){
                    //replace strategy against opponent group
                    aPopulation[focal].setStrategyForGroup(modelStrategy, opponent);
                }

                //traverse population to get strategies
                for(Node n : aPopulation){
                    sum += n.getCoopAvgFromStrategies();
                }
                //avg cooperation
                sum /= aPopulation.length-1;
                localAvg[j] += sum;
            }
        }
        
        //get averages
        for(int i = 0; i < GAME_ROUNDS; i++){
            localAvg[i] /= iterations;
        }
        return localAvg;
    }
    
    /**
     * 
     * @param cardinal
     * @return 
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
                System.out.println(Boolean.toString(aPopulation[i].getStrategyForOpponent(nodeID)));
            }
        }
        //return the arraylist
        return anArrayList;
    }
    
    /**
     * Gets strategies against a node.
     * @param nodeID
     * @return 
     */
    private ArrayList<Boolean> getOpponentStrategiesWM(int nodeID){
        //array list to contain all opponent strategies used against nodeID
        ArrayList<Boolean> anArrayList = new ArrayList<>();
        for(int i = 0; i < aPopulation.length;i++){
            anArrayList.add(i,aPopulation[i].getStrategyForOpponent(nodeID));
        }
//        for(int i = 0; i < anArrayList.size(); i++){
//            System.out.println(Boolean.toString(anArrayList.get(i)));
//        }
        //return the arraylist
        return anArrayList;
    }
    
    /**
     * Main method to execute program
     * Writes to file
     * @param args 
     */
    public static void main(String[] args){
        
        int numOfGroups = 100;
        double[] means;
        
        try {
            PrintWriter output;
            output = new PrintWriter(new BufferedWriter(
                    new FileWriter("TEST_GroupGame_ " + numOfGroups +"_Means.csv")));
            
            //update weak opponent
            PD_GroupsGame exp= new PD_GroupsGame();
            means = exp.runExperiment(numOfGroups);
            
            for(int i = 0; i < GAME_ROUNDS; i++){
                output.append(Double.toString(means[i]) + ", ");
                output.flush();
            }
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(NGroupsWellMixed.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
