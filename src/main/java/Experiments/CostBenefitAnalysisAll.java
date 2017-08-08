package Experiments;

import Game_Models.LowCognition_2DStructure;
import Game_Models.LowCognition_Ring;
import Game_Models.MultiStrategy_2DStructured;
import Game_Models.ReplaceModel_2DStructure;
import Game_Models.ReplaceModel_Ring;
import Game_Models.StructuredRing_TwoStrategies;
import Game_Models.StructuredRingSingle;
import Game_Models.SingleStrategy_2DStructured;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class CostBenefitAnalysisAll {
    
    public static void main(String[] args) throws IOException{
        
        FileWriter avgWriter2D = new FileWriter("MethodAverages_2D.csv");
        FileWriter avgWriter1D = new FileWriter("MethodAverages_1D.csv");
        //run n times
        double avg = 0;
        double cost = 0;
        final double MAX_COST = 1.9;
        final double SIZE = 1000;
        final double COST_INCREASE = 0.01;
        double experimentVals[];
        
        //2D Simulations
        MultiStrategy_2DStructured experiment = new MultiStrategy_2DStructured();
        ReplaceModel_2DStructure experiment1 = new ReplaceModel_2DStructure();
        LowCognition_2DStructure experiment2 = new LowCognition_2DStructure();
        SingleStrategy_2DStructured experiment3 = new SingleStrategy_2DStructured();
       
        //1D Simulations
        StructuredRingSingle experiment4 = new StructuredRingSingle();
        StructuredRing_TwoStrategies experiment5 = new StructuredRing_TwoStrategies();
        LowCognition_Ring experiment6 = new LowCognition_Ring();
        ReplaceModel_Ring experiment7 = new ReplaceModel_Ring();
        
        System.out.println("2D Replace strategy for model");
        avgWriter2D.append("//2D Replace Strategy For Model:\n"); 
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment.doExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter2D.append((Double.toString(avg) + ", "));
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
            avg = 0;
        }
        avgWriter2D.flush();
        cost = 0;
        System.out.println("2D: Multiple Strategies");
        avgWriter2D.append("\n//2D Multiple Strategies:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment1.performExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter2D.append((Double.toString(avg) + ", "));
            avg = 0;
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
        }
        avgWriter2D.flush();
        cost = 0;
        avg = 0;
        System.out.println("2D Low Cognition");
        avgWriter2D.append("\n//2D Low Cognition:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment2.performExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter2D.append((Double.toString(avg) + ", "));
            avg = 0;
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
        }
        avgWriter2D.flush();
        
        cost = 0;
        avg = 0;
        System.out.println("2D Single Strategy");
        avgWriter2D.append("\n//2D Single Strategy:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment3.runExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter2D.append((Double.toString(Math.abs(avg)) + ", "));
            avg = 0;
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
        }
        avgWriter2D.close();
        
        //1D classes
        cost = 0;
        avg = 0;
        System.out.println("1D Single Strategy");
        avgWriter1D.append("\n//1D Single Strategy:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment4.runExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter1D.append((Double.toString(Math.abs(avg)) + ", "));
            avg = 0;
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
        }
        avgWriter1D.flush();
        
        avg = 0;
        cost = 0;
        System.out.println("1D Two Strategies");
        avgWriter1D.append("\n//1D Two Strategies:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment5.runExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter1D.append((Double.toString(Math.abs(avg)) + ", "));
            avg = 0;
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
        }
        avgWriter1D.flush();
        
        avg = 0;
        cost = 0;
        System.out.println("1D Low Cognition");
        avgWriter1D.append("\n//1D Low Cognition:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment6.performExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter1D.append((Double.toString(Math.abs(avg)) + ", "));
            avg = 0;
            System.out.println("Data added for " + Math.abs(cost));
            cost += COST_INCREASE;
        }
        avgWriter1D.flush();
        
        cost = 0;
        System.out.println("1D Replace for model");
        avgWriter1D.append("\n//Replace for model:\n");
        while(cost<=MAX_COST){
            experimentVals = new double[(int)SIZE];
            for(int i = 0; i < experimentVals.length; i++){
                experimentVals[i] = experiment7.performExperiment(cost);
            }
        
            for(double d : experimentVals){
                avg += d;
            }
            avg /= SIZE; 
            avgWriter1D.append((Double.toString(Math.abs(avg)) + ", "));
            System.out.println("Data added for " + Math.abs(cost));
            avg = 0;
            cost += COST_INCREASE;
        }
        avgWriter1D.close();
    }
}
