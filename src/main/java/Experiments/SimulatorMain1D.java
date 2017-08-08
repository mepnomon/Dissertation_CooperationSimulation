package Experiments;

import Utility.Notifications;
import Game_Models.StructuredRingSingle;
import Game_Models.RingWellMixed_Single;
import java.io.IOException;
import java.util.Scanner;

/**
 * Cooperation Simulator
 * Main
 * Created:  11-Aug-2016
 * Updated:  27-SEP-2016
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class SimulatorMain1D {
    
    static Scanner sc = new Scanner(System.in);

    /**
     *  Main Method
     *  Created: 11-Aug-2016
     *  @param args 
     */
    public static void main (String[] args){
        
        //FileWriter dataWriter = new FileWriter("coopdata.csv");
        int choice = 0;
        System.out.println(Notifications.TITLE_1D);
        try{
            while(choice !=3){
            int ratio = 0;
                System.out.print(Notifications.MENU);
                choice = sc.nextInt();
                switch(choice){
                    case 1:StructuredRingSingle test = new StructuredRingSingle();
                          test.runExperiment(); break;
                    case 2:
                        RingWellMixed_Single test2 = new RingWellMixed_Single();
                        System.out.println(Notifications.PROGRAM_RUNNING);
                            test2.doExperiment(); break;
                }
                System.out.println(Notifications.PROGRAM_FINISHED);
                //System.out.println("ratio " + ratio);
            }
        }catch(IOException ex){
            System.out.println(Notifications.ERR_FILENOTCREATED);
        }
    }
}
