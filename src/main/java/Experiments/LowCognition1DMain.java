package Experiments;

import Game_Models.LowCognition_Ring;
import java.io.IOException;
import static Utility.Notifications.ERR_FILENOTCREATED;

/**
 *
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class LowCognition1DMain {
    
    public static void main(String[] args) throws IOException{
        LowCognition_Ring experiment = new LowCognition_Ring();
        //RingStructured_Single experiment2 = new RingStructured_Single();
        //experiment.performExperiment();
        
        try{
            experiment.performExperiment();
            //experiment2.doExperiment();
        } catch(IOException ex){
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
