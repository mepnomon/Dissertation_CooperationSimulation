package GameModels;

import Utility.ExpConstants;
import static Utility.Notifications.ERR_FILENOTCREATED;
import static Utility.UtilityMethods.setup1DPopulation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.ExpConstants.POPULATION_SIZE;

/**
 * Title: Dissertation
 * THIS DATA IS CURRENTLY IN USE
 * This class forms the implementation of the asynchronous algorithm proposed in
 * "Distinguishing the opponents promotes cooperation in well-mixed populations"
 * by Lucas Wardil and Jafferson K. L. da Silva.
 * 
 * Game rules:
 * -----------
 * Select random focal player - Monte Carlo Method
 * Select random link and use as model.
 * Play prisoner's dilemma with all opponents and realize a cumulative payoff.
 * Repeat this for the model player. Compare cumulative payoffs and if 
 * model payoff > focal payoff, adopt strategy model uses in this encounter.
 * Use this strategy against the weakest encounter. If multiple weak encounters
 * present, choose a random encounter.
 * 
 * @author D.B. Dressler
 */
public class PD_WellMixed {
    
    //the population
    Node[] aPopulation;
    //focal node payoffs
    double[] focalPayoffs;
    //random number generated
    Random generator;
    //the update method
    final int UPDATE_METHOD;
    
    /**
     * Constructor
     * @param update
     */
    public PD_WellMixed(int update){
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        generator = new Random();
        UPDATE_METHOD = 1;//update;
    }
    
    /**
     * 
     * @return 
     */
    public boolean debugExperiment(){
        
        boolean allTestsPassed = false;
        
        
        
        return allTestsPassed;
    }
    
    /**
     * Runs the experiment.
     * @return  average cooperation final value
     */
    public double[] runExperiment(){
        
        int samples = 100;
        //store sum
        //array for sums
        double cooperationPerRound[] = new double[GAME_ROUNDS];
        double multiplier = ((double)samples*aPopulation.length);
        final int MAX_POS = aPopulation.length;
        int transitoryGameSteps;
        
        for(int i = 0; i < samples; i++){
            //transitoryGameSteps = 0;
            createNewPopulation();
            
            //plays iterative public goods  game
            for(int j = 0; j < GAME_ROUNDS;j++){
                
                
                //chooses the update strategy
                //direct reciprocal
                //indirect reciprocal
                //animal behavior (with low cognition)
//                switch(UPDATE_METHOD){
                      //tit for tat
//                    case(0): replaceModel(generator.nextInt(aPopulation.length)); break;
//                    case(1): replaceWeakOpponent(generator.nextInt(aPopulation.length)); break;
//                    case(2): replaceRandomOpponent(generator.nextInt(aPopulation.length)); break;
//                }
//                //pick type of replacement strategy
                //select random node
                
                replaceWeakOpponent(generator.nextInt(MAX_POS));
                //replace the weaker opponent
                //transitoryGameSteps++;
                //retrieves averages from all nodes       
                //currentCooperationFraction = getCurrCooperationFraction();
                //averages this value by number of nodes
                //System.out.println(Double.toString(sum));
//                if(transitoryGameSteps == 1000){
                    //add value to list of cooperation fraction per round
                cooperationPerRound[j] += getFractionOfCooperation();
                //}
            }
            // end game rounds
        }
        // end samples
        
                                                                                System.out.println("Total Generated payoff " + getTotalPayoff());
        // calculates and final percentage for each entry
        for(int i = 0; i < GAME_ROUNDS;i++){
            //split math and do once
            cooperationPerRound[i] /= multiplier;
        }
        //System.out.println(Arrays.toString(localAvg));
        return cooperationPerRound;
    }
    
    /**
     * Sums the payoff from each node.
     * @return 
     */
    private double getTotalPayoff(){
        
        // payoffs from cooperation and defection
        double cPayoffs=0, dPayoffs=0, totalPayoff=0;
        
        // traverse the population
        for(int i = 0; i < aPopulation.length; i++){
           totalPayoff += aPopulation[i].getSum(getStrategiesAgainstPlayer(i));
        }
        
        return totalPayoff;
    }
    
    /**
     * Retrieves the current level of cooperation in the population
     * @return the current level of cooperation
     */
    private double getFractionOfCooperation(){
    
        double currentCoopLevel = 0;
        for(Node n : aPopulation){
                    currentCoopLevel += n.getCoopAvgFromStrategies();
                    //System.out.println(Double.toString(sum));
                }
        return currentCoopLevel;
    }
    
    /**
     * Creates a new population.
     */
    private void createNewPopulation(){
        //each sample receives a taken from a random population
            aPopulation = setup1DPopulation(aPopulation); 
            for(Node n : aPopulation){
                //sets up individual strategy tables
                n.setupStrategyArray();
            }
    }
    
    /**
     * Compares nodes and updates strategy
     * @param aFocalPlayer node's location
     */
    public void replaceModel (int aFocalPlayer){
        
        int model = getValidOpponent(aFocalPlayer);
        //if model performs better
        if(aPopulation[aFocalPlayer].getSumPD(getStrategiesAgainstPlayer(aFocalPlayer)) 
                < aPopulation[model].getSumPD(getStrategiesAgainstPlayer(model))){
            
            //copy model's strategy for next encounter
            aPopulation[aFocalPlayer].setStrategyListEntry(model, 
                    aPopulation[model].getStrategyForOpponent(aFocalPlayer));
        }
    }
    
    /**
     * Replaces the weakest link opponent.
     * This is the opponent that provides the list benefit.
     * @param aFocalPlayer the focal player
     */
    public void replaceWeakOpponent(int aFocalPlayer){
       
       //selects opponent != focal
       int aModelPlayer = getValidOpponent(aFocalPlayer);
       boolean misjudge = false;
       //this block contains:
       //call to Node to play public goods game
       if(aPopulation[aFocalPlayer].getSumPD(getStrategiesAgainstPlayer(aFocalPlayer)) <
               aPopulation[aModelPlayer].getSumPD(getStrategiesAgainstPlayer(aModelPlayer))){
                                                                                //System.out.println("Model " + modelP + " has higher fitness!");
                                                                                //System.out.println("Focal player is " + focalP);
           //get the strategy model uses against focal player
           boolean aNewStrategy = 
                   aPopulation[aModelPlayer].getStrategyForOpponent(aFocalPlayer);
           
                                                                                //System.out.println("Model uses strategy " + Boolean.toString(aPopulation[modelP].getStrategyForOpponent(focalP)));
                                                                                //System.out.println("Adopted strategy " + Boolean.toString(newStrategy));
           //0float m = generator.nextFloat();
           
//           if(m <= 0.1){
//                                                                              //System.out.println(Float.toString(m));
//               misjudge = true;
//           }
           //get the interaction that provided the weakest pairwise payoff
           int aWeakOpponent = aPopulation[aFocalPlayer].getWeakestLink(misjudge);
                                                                                //System.out.println("Weak opponent ID: " + aWeakOpponent);
                                                                                //System.out.println("Current strategy against opponent " + Boolean.toString(aPopulation[focalP].getStrategyForOpponent(aWeakOpponent)));
                                                                                //boolean oppStrategy = aPopulation[aWeakOpponent].getStrategyForOpponent(modelP);
                                                                                //System.out.println("Opponent uses strategy: " + Boolean.toString(oppStrategy));
           //change focal's strategy against weakest contributor
           aPopulation[aFocalPlayer].setStrategyListEntry(aWeakOpponent, aNewStrategy);
                                                                                //System.out.println("Verifying new strategy is: " + aPopulation[focalP].getStrategyForOpponent(aWeakOpponent));
                                                                                //System.out.println("Selecting Next:\n\n");
       }
    }
    
    /**
     * Updates strategy for a random opponent
     * Low cognition - choose random
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
        
        //compare cumulative payoffs of focal and model
        if(aPopulation[model].getSumPD(getStrategiesAgainstPlayer(model)) > 
                aPopulation[model].getSumPD(getStrategiesAgainstPlayer(opponent))){
            //set the strategy
            aPopulation[model].setStrategyListEntry(
                    opponent, aPopulation[model].getStrategyForOpponent(focal));
        }
    }
    
     /**
     * Accumulates a list of opponent strategies.
     * @param aPlayer - the player's location
     * @return an array of strategies
     */
    public boolean[] getStrategiesAgainstPlayer(int aPlayer){
        
        //method's local strategies
        boolean strategies[] = new boolean[aPopulation.length];
        
        //accumulate strategies against aPlayer
        for(int i = 0; i < aPopulation.length; i++){
            strategies[i] = aPopulation[i].getStrategyForOpponent(aPlayer);                             //System.out.println(Boolean.toString(strategies[i]));
        }
        //return array to caller
        return strategies;
    }
    
    /**
     * Gets a valid opponent
     * @param aFocalPlayer the location to exclude
     * @return a valid location
     */
    private int getValidOpponent(int aFocalPlayer){
        
        //set opponent outside of array
        int opponent = -1;
        //initialise condition
        boolean isValid = false; //is the selected node valid
        
        while(!isValid){
            //get a random node
            opponent = generator.nextInt(aPopulation.length);
            
            //must not match focal
            if(opponent != aFocalPlayer){
               
                isValid = true;
            }
        }
        
        //return a valid location
       return opponent; 
    }
    
    
    /**
     * Main method to execute program
     * Writes to file
     * @param args 
     */
    public static void main(String[] args){
        
        double[] means;
        
        try {
            PrintWriter output;
            output = new PrintWriter(new BufferedWriter(
                    new FileWriter("PD_Well_Mixed_Means_"
                            +Double.toString(POPULATION_SIZE)+".csv")));
            
            //update weak opponent
            PD_WellMixed exp = new PD_WellMixed(1); //update for weaker
            means = exp.runExperiment();
            
            for(int i = 0; i < GAME_ROUNDS; i++){
                output.append(Double.toString(means[i]) + ", ");
                output.flush();
            }
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(PD_WellMixed.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ERR_FILENOTCREATED);
        }
    }
    
}
