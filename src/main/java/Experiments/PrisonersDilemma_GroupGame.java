
package Experiments;

import Game_Models.PD_GroupsGame;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static Utility.ExpConstants.GAME_ROUNDS;

/**
 * Experiment comparing various group sizes
 * using the Prisoner's Dilemma game model.
 * @author D.B. Dressler
 */
public class PrisonersDilemma_GroupGame {
    
    public static void main(String[] args) throws IOException{
        
        //local variables
        PrintWriter output;
        int groupSize = 2;
        //MAX Group size
        final int MAX_GSIZE = 64;
        //initialize the experiment
        PD_GroupsGame exp;
        //get the mean mean data
        double[] means;
        do{
            exp = new PD_GroupsGame();
            //initialize file writer
            output = new PrintWriter(new BufferedWriter(
                    new FileWriter("Groups_PD_Mean_" + groupSize+ ".csv")));
                    
            means = exp.runExperiment(groupSize);
            for(int i = 0; i < GAME_ROUNDS; i++){
                output.append(Double.toString(means[i]) + ", ");
                output.flush();
            }
            output.close();
            //increment gSize
            groupSize *= 2;
        }while(groupSize <= MAX_GSIZE);
    }//end of main
}
