package Experiments;

import Game_Models.StructuredRingSingle;
import java.io.IOException;
import static Utility.Notifications.ERR_FILENOTCREATED;

/**
 * Cooperation Simulator
 * Main to compare the performance 
 * Nodes with a single strategy and Nodes with two simultaneous strategies
 * in a 1 dimensional Lattice.
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class Comparison1D {
    
    public static void main(String[] args) throws IOException{
        StructuredRingSingle sample1 = new StructuredRingSingle();
        //RingStructured_Simultaneous sample2 = new RingStructured_Simultaneous(true, 1, 1.9);
        
        try{
            sample1.runExperiment();
            //sample2.doExperiment();
        }catch(IOException ex){
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
