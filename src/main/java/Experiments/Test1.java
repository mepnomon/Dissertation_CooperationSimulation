/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.PD_GroupsGame;
import java.util.Arrays;

/**
 *
 * @author Mepnomon
 */
public class Test1 {
    
    public static void main(String[] args){
        PD_GroupsGame exp = new PD_GroupsGame();
        System.out.println(Arrays.toString(exp.runExperiment(2)));
    }
}
