package Game_Models;

import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.ExpConstants.POPULATION_SIZE;
import static Utility.Notifications.ERR_FILENOTCREATED;
import Utility.UtilityMethods;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for comparison with groups
 * A Node is selected and picks a random node
 * in the well mixed population. These play the Prisoner's Dilemma
 * game, followed by an update of the focal node's strategy, if the opponent
 * outperforms this node. The game is played for N Rounds (defined in Game Rounds)
 * 
 * @author Dressler, D.B.
 */
public class WellMixedRingSingle {
    //global variables
    Random generator;
    Node[] aPopulation;
    double[] averages; //averages
    final int SAMPLES;
    
    /**
     * Class constructor
     */
    public WellMixedRingSingle(){
        aPopulation = new Node[(int)POPULATION_SIZE];
        generator = new Random();
        SAMPLES = 1000;
        averages = new double[GAME_ROUNDS];
    }
    
    /**
     * 
     * @return 
     */
    public double[] runExperiment(){
        int focal, opponent;
        double focalSum, opponentSum;
        //number of samples to realize average
        for(int i = 0; i < SAMPLES; i++){
            aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
            //plays game for these rounds
            for(int j = 0; j < GAME_ROUNDS; j++){
                focal = generator.nextInt(aPopulation.length);
                
                focalSum = playGameWithAll(focal);
                opponent = selectValidOpponent(focal);
                opponentSum = playGameWithAll(opponent);
                
                //if oppnent outperforms focal player
                if(focalSum < opponentSum){
                    aPopulation[focal].setStrategyLeft(
                            aPopulation[opponent].getStrategyLeft());
                }
                averages[j] += UtilityMethods.fractionOfCooperationSingle1D(aPopulation);
            }
        }     
        return averages;
    }
    
   /**
    * 
    * @param player
    * @return 
    */
    private double playGameWithAll(int player){
        double payoff = 0;
        boolean playerStrategy = aPopulation[player].getStrategyLeft();
       
        for(int i = 0; i < aPopulation.length; i++){
            
            if(i != player){
                payoff +=
                playPDLocally(playerStrategy,aPopulation[i].getStrategyLeft());
            }
        }
        return payoff;
    }
    
    /**
     * Local instance of the prisoner's dilemma game.
     * @param focal
     * @param neighbor
     * @return 
     */
    private double playPDLocally(boolean fNode, boolean neighbor){
        double sum = 0;
        if(!fNode && neighbor){
            return DEFECTION_P;
        }
        // check C-C encounter
        if(fNode && neighbor){
            return 1.0;
        }
        // check D-D encounter
        if(!fNode && !neighbor){
            return EPSILON;
        }
        
        return sum;
    }
    
    /**
     * Selects a random opponent that is not the focal player.
     * @return 
     */
    private int selectValidOpponent(int focal){
        int validOpponent = 0;
        boolean isValid = false;
        
        while(!isValid){
            //get an opponent from population
            validOpponent = generator.nextInt(aPopulation.length);
            //if opponent is not focal player
            if(validOpponent != focal){
                //end loop
                isValid = true;
            }
        }
        //return a valid opponent
        return validOpponent;
    }

    /**
     * Main entry point for program.
     * @param args 
     */
    public static void main(String[] args){
        double[] averages;
        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(
                    new FileWriter("SingleStrategyWellMixed"+ Double.toString(POPULATION_SIZE) +".csv")));
        
            final int SAMPLES = 1000;
            WellMixedRingSingle exp = new WellMixedRingSingle();
            averages = exp.runExperiment();
        
            for(int i = 0; i < averages.length; i++){
                averages[i] /= SAMPLES;
                output.append(Double.toString(averages[i])+ ", ");
            }
        } catch (IOException ex) {
            Logger.getLogger(WellMixedRingSingle.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
