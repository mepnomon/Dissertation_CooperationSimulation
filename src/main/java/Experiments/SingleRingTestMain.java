/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.SingleRingTest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eeu436
 */
public class SingleRingTestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SingleRingTest experiment = new SingleRingTest();
        try {
            experiment.doExperiment();
        } catch (IOException ex) {
            Logger.getLogger(SingleRingTestMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
