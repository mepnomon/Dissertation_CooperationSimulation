package Experiments;

import Game_Models.SingleStrategy_2DStructured;

/**
 * Cooperation Simulator
 * 2D Main
 * Created:  30-SEP-2016
 * Updated:
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class SimulatorMain2D {
    public static void main(String[] args){
       
       SingleStrategy_2DStructured experiment = new SingleStrategy_2DStructured();
       experiment.runExperiment();

    }    
}
