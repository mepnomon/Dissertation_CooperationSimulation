/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Game_Models.LowCognition_2DStructure;
import java.io.IOException;
import static Utility.Notifications.ERR_FILENOTCREATED;

/**
 *
 * @author D.B.Dressler(eeu436@bangor.ac.uk)
 */
public class LowCognition2DMain {
    
    public static void main(String[] args) throws IOException{
        LowCognition_2DStructure experiment = new LowCognition_2DStructure();
        try{
            experiment.performExperiment();
       }catch(IOException ex){
            System.out.println(ERR_FILENOTCREATED);
       }
    }
}
