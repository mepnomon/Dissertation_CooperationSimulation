package Experiments;

import Game_Models.GroupGame_2Groups;
import Game_Models.MultiTrackAll_1DWellMixed;
import Game_Models.StructuredRingSingle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Analyzes performance of game using groups,
 * well mixed, where each node is connected to all
 * and single strategy.
 * @author D.B Dressler(eeu436@bangor.ac.uk)
 */
public class CostBenefit_MixedGroups {
    
    public static void main(String[] args) throws IOException{
        
        PrintWriter output;
        //variables
        double avg = 0, cost = 0; //average  and cost
        final int SIZE = 1000;          //times a simulation is sampled
        final double MAX_COST = 1.9, COST_INCR = 0.01;  //max cost and icnrementor
        double averages[]; //contains averages for sampled size        
        
        //Experiments used in comparison
        GroupGame_2Groups group_repl_weak = new GroupGame_2Groups(0);
        GroupGame_2Groups group_repl_rand = new GroupGame_2Groups(1);
        MultiTrackAll_1DWellMixed multi_repl_model = new MultiTrackAll_1DWellMixed(0);
        MultiTrackAll_1DWellMixed multi_repl_weak = new MultiTrackAll_1DWellMixed(1);
        MultiTrackAll_1DWellMixed multi_repl_rand  = new MultiTrackAll_1DWellMixed(2);
        StructuredRingSingle singleExp = new  StructuredRingSingle();
        
        //initialize file writer
        output = new PrintWriter(new BufferedWriter(
                new FileWriter("CostBenefit_MixedGroups.csv")));
        
        System.out.println("Simulation running...");
        //--------Groups----------
        output.append("\n1D Groups repl weak:\n");
        //as long as cost is not max
        while(cost < MAX_COST){
            averages = new double[SIZE];
            for(int i = 0; i < SIZE; i++)
                averages[i] = Math.abs(group_repl_weak.runExperiment(cost));
            
            //add all values
            for(double d : averages)
                avg += d;
            
            avg /= SIZE;//divide by size
            output.append(Double.toString(Math.abs(avg)) + ", "); //write result
            cost += COST_INCR;//increment cost
            avg = 0; //reset average
        }
        //reset cost, flush writer
        cost = 0;
        output.flush();
        
        output.append("\n1D Groups repl rand:\n");
        //as long as cost is not max
        while(cost < MAX_COST){
            averages = new double[SIZE];
            for(int i = 0; i < SIZE; i++)
                averages[i] = group_repl_rand.runExperiment(cost);
            
            //add all values
            for(double d : averages)
                avg += d;
            
            avg /= SIZE;//divide by size
            output.append(Double.toString(Math.abs(avg)) + ", "); //write result
            cost += COST_INCR;//increment cost
            avg = 0; //reset average
        }
        //reset cost, flush writer
        cost = 0;
        output.flush();
        
        
        System.out.println("Groups complete.");
        
        
        
        //--------Well Mixed----------
        output.append("\nWell Mixed: Repl Model");
        while(cost < MAX_COST){
            averages = new double[SIZE]; //reinitialize array
            for(int i = 0; i < SIZE; i++)
                averages[i] = Math.abs(multi_repl_model.runExperiment(cost));
            
            for(double d : averages)
                avg += d;
            
            avg /= SIZE;
            output.append(Double.toString(Math.abs(avg)) + ", "); //write result
            cost += COST_INCR;//increment cost
            avg = 0; //reset average
        }
        cost = 0;
        output.flush();
        
        
        output.append("\nWell Mixed: Repl Weak");
        while(cost <= MAX_COST){
            averages = new double[SIZE]; //initialize array
            for(int i = 0; i < SIZE; i++)
                averages[i] = Math.abs(multi_repl_weak.runExperiment(cost)); 
            
            for(double d : averages)
                avg += d;
            
            avg/=SIZE;
            output.append(Double.toString(Math.abs(avg)) + ", ");
            cost += COST_INCR;
            avg = 0;
        }
        cost = 0;
        output.flush();
        
        output.append("\nWell Mixed: Repl rand");
        while(cost <= MAX_COST){
            averages = new double[SIZE]; //initialize array
            for(int i = 0; i < SIZE; i++)
                averages[i] = Math.abs(multi_repl_rand.runExperiment(cost)); 
            
            for(double d : averages)
                avg += d;
            
            avg/=SIZE;
            output.append(Double.toString(Math.abs(avg)) + ", ");
            cost += COST_INCR;
            avg = 0;
        }
        cost = 0;
        output.flush();
        System.out.println("Well Mixed complete.");
        //--------Single Strategy----------
        output.append("\n1D Single Strategy:\n");
        while(cost < MAX_COST){
            averages = new double[SIZE]; //reinitialize array
            for(int i = 0; i < SIZE; i++)
                averages[i] = Math.abs(singleExp.runExperiment(cost));
            
            for(double d : averages)
                avg += d;
            avg /= SIZE;
            output.append(Double.toString(Math.abs(avg)) + ", "); //write result
            cost += COST_INCR;//increment cost
            avg = 0; //reset average
        }
        System.out.println("Single Strategy complete.");
        output.close(); //close writer
    }
}
