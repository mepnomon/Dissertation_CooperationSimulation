package Experiments;

import Game_Models.StructuredRing_TwoStrategies;
import java.io.IOException;
import static Utility.Notifications.ERR_FILENOTCREATED;

/**
 *
 * @author eeu436
 */
public class RingStructured_SimultaneousMain {
 
    public static void main(String[] args){
        
        StructuredRing_TwoStrategies test = new StructuredRing_TwoStrategies();
        //WellMixedPopSimultaneousStrategy1D test = new WellMixedPopSimultaneousStrategy1D();
        //StructuredPopSimultaneousStrategy2D test = new StructuredPopSimultaneousStrategy2D();
        try{
            test.runExperiment();
        }catch(IOException ex){
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
