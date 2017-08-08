package Experiments;

import Game_Models.GroupGame_VarGroups;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author D.B. Dressler
 */
public class CBR_Various_Group_Sizes {
    
    public static void main(String[] args) throws IOException{
        int groupSize = 2;
        final int SIZE = 1000;
        final int GROUP_MULTIPLIER = 2;
        double customCost = 0.01;
        final double COST_INCR = 0.01;
        double avg;
        GroupGame_VarGroups sim = new GroupGame_VarGroups(0);
        System.out.println("Simulation of various groups");
        String title = "\nGroups " + groupSize + " \n";
        PrintWriter output;
        output = new PrintWriter(new BufferedWriter(
                new FileWriter("CostBenefit_VariousGroupSizes.csv")));
        output.append(title);
        do{
            output.flush();
            avg = 0;
            for(int i = 0; i < SIZE; i++){
                avg += sim.runExperiment(customCost, groupSize);
            }
            System.out.print(".");
            Math.abs(avg /= SIZE);
            output.append(Double.toString(avg) + ", ");
            //System.out.println("complete.");
            //write to file
            customCost += COST_INCR;
            if(customCost > 1.9 && groupSize != 64){
                System.out.println("Groups: " + groupSize + " completed");
                output.append(title);
                customCost = 0;
                groupSize *= GROUP_MULTIPLIER;
            }
        }while(groupSize <= 64 && customCost <= 1.9);
        output.close();
    }
}
