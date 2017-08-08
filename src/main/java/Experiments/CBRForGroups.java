/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.GroupGameRevisedCBR;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Run this next and plot the data
 * @author Dressler, D.B.
 */
public class CBRForGroups {
    
        
         /**
     * Main method to run the program.
     * @param args 
     */
    public static void main(String[] args) throws IOException{
        
        //the type of game
        GroupGameRevisedCBR exp;
        int nodesInGroup = 1; //offset value
        double bValue = 1; //offset value
        final double B_INCR = 0.25; //offset
        double avg; //average cooperation
        PrintWriter output = new PrintWriter("varying_b_Groups_"+ nodesInGroup +"_.csv");
        do{
            if(bValue <= 3.0){
                exp = new GroupGameRevisedCBR();
                //get final value
                avg = exp.runExperiment(nodesInGroup, bValue);
                //append to file
                output.append(Double.toString(Math.abs(avg)) + ", ");
                //flush writer
                output.flush();
                //increment bVal
                 bValue += B_INCR;
            }
                    
            //if bValue is max
            if(bValue == 3.0){
                //reset value
                bValue = 1;
                //print notification
                System.out.println("Exp: " + nodesInGroup + "done");
                nodesInGroup *= 2;
                //close writer
                //output.close();
                 //output.append(title + nodesInGroup);
             }
        } while (nodesInGroup <= 128);
        output.close();
    }
}
