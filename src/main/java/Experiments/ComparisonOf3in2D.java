/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.LowCognition_2DStructure;
import Game_Models.MultiStrategy_2DStructured;
import Game_Models.SingleStrategy_2DStructured;
import java.io.IOException;

/**
 *
 * @author eeu436
 */
public class ComparisonOf3in2D {
    
    public static void main(String[] args) throws IOException{
        
        SingleStrategy_2DStructured experiment = new SingleStrategy_2DStructured();
        MultiStrategy_2DStructured experiment2 = new MultiStrategy_2DStructured();
        LowCognition_2DStructure experiment3 = new LowCognition_2DStructure();
        
        //experiment.doExperiment();
        //experiment2.doExperiment();
        experiment3.performExperiment();
    }
}
