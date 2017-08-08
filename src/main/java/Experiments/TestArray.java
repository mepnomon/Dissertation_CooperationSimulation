/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.GroupGame_VarGroups;

/**
 *
 * @author Mepnomon
 */
public class TestArray {
    
    public static void main(String[] args){
        
        
        double customCost = 0.1;
        //GroupGame_2Groups exp = new GroupGame_2Groups(1);
        ///System.out.println("cP: " + Double.toString(exp.runExperiment(customCost))); 
        //pop has to be big enough to accommodate the groups
        for(int i = 0; i < 1000; i++){
            GroupGame_VarGroups exp = new GroupGame_VarGroups(0);
            double avg = exp.runExperiment(customCost, 32);
            System.out.println("Average Cooperation: " + avg);
        }
//         Node n;
//         for(int i  = 0; i < 101; i++){
//             n = new Node(i, false);
//             n.setupStrategyGroup(2);
//         }
         
         
    }
}
