/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.LowCognition_Ring;
import Game_Models.StructuredRing_TwoStrategies;
import Game_Models.StructuredRingSingle;
import java.io.IOException;
import static Utility.Notifications.ERR_FILENOTCREATED;

/**
 *
 * @author eeu436
 */
public class ComparisonOf3In1D {
    
    public static void main(String[] args) throws IOException{
        
        LowCognition_Ring experiment = new LowCognition_Ring();
        StructuredRingSingle experiment2 = new StructuredRingSingle();
        StructuredRing_TwoStrategies experiment3 = new StructuredRing_TwoStrategies();
        
        try{
            experiment.performExperiment();
            experiment2.runExperiment();
            experiment3.runExperiment();
            
        }catch(IOException ex){
            System.out.println(ERR_FILENOTCREATED);
        }
        
    }
}
