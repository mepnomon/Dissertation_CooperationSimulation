/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.SingleStrategy_2DStructured;

/**
 * This class is to verify output of the above imported class.
 * @author eeu436
 */
public class SingleStrategy2DMain {
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args){
       
        final int SAMPLES = 100;
        double avg[] = new double[SAMPLES];
        double cost = 1;
        
        for(int i = 0; i < SAMPLES; i++){
            
            SingleStrategy_2DStructured test = new SingleStrategy_2DStructured();
            avg[i] = test.runExperiment(cost);
        }
        
        double avgVal = 0;
        for(double val : avg){
            avgVal += val;
        }
        avgVal /= SAMPLES;
        
        System.out.println("Average: " + avgVal);
    }
}
