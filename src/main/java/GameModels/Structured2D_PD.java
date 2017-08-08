package GameModels;

import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.ExpConstants.POPULATION_SIZE;
import static Utility.Notifications.ERR_FILENOTCREATED;
import Utility.UtilityMethods;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author Mepnomon
 */
public class Structured2D_PD {
    
    Random generator;
    int focalPlayerX, focalPlayerY;
    int modelPlayerX, modelPlayerY;
    Node[][] aPopulation;
    final int POP_SIZE;
    final int MAX, MIN;
    boolean modelStrategy;
    /**
     * Constructor
     */
    public Structured2D_PD(){
        
        POP_SIZE = ((int) POPULATION_SIZE);
        //instantuate RNG
        generator = new Random();
        //instantiate populatin
        aPopulation = new Node[POP_SIZE][POP_SIZE];
        //boundary control variables
        MAX = POP_SIZE-1;
        MIN = 0;
    }
    
    /**
     * Performs the experiment
     * needs to return array[double]
     */
    public double[] runExperiment(){
        
        final int SAMPLES = 100;
        double focalSum, modelSum;
        double[] averages = new double[(int)GAME_ROUNDS];
        //obtain Samples
        for(int i = 0; i < SAMPLES; i++){
            
            //create new population
            aPopulation = UtilityMethods.setup2DPopulation(aPopulation);
            
            //play game
            for(int j = 0; j < GAME_ROUNDS; j++){
                
                double cumulativePayoffFocal, cumulativePayoffModel;
                //pick random player
                focalPlayerX = generator.nextInt(POP_SIZE);
                focalPlayerY = generator.nextInt(POP_SIZE);
                                                                                //System.out.println("Focal: X: "+ focalPlayerX + " Y: "+ focalPlayerY);
                selectModel();
                //test the output
                                                                                //System.out.println("Model: X: "+ modelPlayerX + " Y: "+ modelPlayerY);
                
                cumulativePayoffFocal = playPDInNeighborhood(
                        focalPlayerX, focalPlayerY);
                
                cumulativePayoffModel = playPDInNeighborhood(
                        modelPlayerX, modelPlayerY);
                
                                                                                //System.out.println("Focal payoff: " + cumulativePayoffFocal);
                                                                                //System.out.println("Model payoff: " + cumulativePayoffModel);
                
                //update strategy if outperformed by model
                if(cumulativePayoffModel > cumulativePayoffFocal){
                                                                                //System.out.println("-----\nChange strategy\n-----\n");
                    
//select weakest contributor to replace strategy for
                    int weakest = 
                            aPopulation[focalPlayerX][focalPlayerY].
                                    getWeakestContributor();
                                                                                //System.out.println("Weakest: " + weakest);
                     replaceWeakest(weakest);
                } // end if
                                                                                //System.out.println("\n\nxxxxxxxxxxxxxxxxxxxxxx\n");
            averages[j] += UtilityMethods.fractionOfCooperationMulti2D(aPopulation);
            //UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
            }//end inner loop
        }//end outer loop
        
        //average obtained datasets
        for(int i = 0; i < GAME_ROUNDS; i++){
            averages[i] /= SAMPLES*10;
        }
        
        return averages;
    }//end method
    
    /**
     * Replaces the strategy for the weakest opponent
     * @param weakest 
     */
    public void replaceWeakest(int weakest){
                    
                    //replace left
                    if(weakest == 0){
                        aPopulation[focalPlayerX][focalPlayerY].
                                setStrategyLeft(modelStrategy);
                    }
                    //replace right
                    if(weakest == 1){
                        aPopulation[focalPlayerX][focalPlayerY].
                                setStrategyRight(modelStrategy);
                    }
                    //replace up
                    if(weakest == 2){
                        aPopulation[focalPlayerX][focalPlayerY].
                                setStrategyUp(modelStrategy);
                    }
                    //replace down
                    if(weakest == 3){
                        aPopulation[focalPlayerX][focalPlayerY].
                                setStrategyDown(modelStrategy);
                    }
    }
    
    /**
     * Selects a model for comparison
     */
    public void selectModel(){
        
        int neighbor = generator.nextInt(4);
        //0=left, 1 right, 2 above, 3, below
        switch(neighbor){
            
            case 0: //left opponent
                                                                                //System.out.println("Select left neighbor as model");
                //check if left player on opposite side
                if(focalPlayerX == MIN){
                    modelPlayerX = MAX;
                } else {
                    modelPlayerX = focalPlayerX-1;
                } 
                modelPlayerY = focalPlayerY;
                modelStrategy = 
                        aPopulation[modelPlayerX][modelPlayerY].getStrategyRight();
                break;
                
            case 1: //right opponent
                                                                                //System.out.println("Select right neighbor as model");
                //check boundary
                if(focalPlayerX == MAX){
                    modelPlayerX = MIN;
                } else {
                    modelPlayerX = focalPlayerX+1;
                }; 
                modelPlayerY = focalPlayerY;
                modelStrategy = 
                        aPopulation[modelPlayerX][modelPlayerY].getStrategyLeft();
                break;
            
            //opponent above
            case 2: 
                //check boundary
                                                                                //System.out.println("Select neighbor above as model");
                if(focalPlayerY == MIN){
                    modelPlayerY = MAX;
                } else {
                    modelPlayerY = focalPlayerY-1;
                }; 
                modelPlayerX = focalPlayerX;
                modelStrategy = 
                        aPopulation[modelPlayerX][modelPlayerY].getStrategyDown();
                break;
                
                
            //opponent below
            case 3:
                //check boundary
                                                                                //System.out.println("Select neighbor below as model");
                if(focalPlayerY == MAX){
                    modelPlayerY = MIN;
                } else {
                    modelPlayerY = focalPlayerY+1;
                };
                modelPlayerX = focalPlayerX;
                modelStrategy = 
                        aPopulation[modelPlayerX][modelPlayerY].getStrategyUp();
                break;
        }
                                                                                //System.out.println("Model Strategy: " + modelStrategy);
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public double playPDInNeighborhood(int x, int y){
        
        double sum;
        int below, above, left, right;
        double sumA, sumB, sumC, sumD;
        
        //check boundary cases
        if(x == MAX){
            right = MIN;
        } else{
            right = x+1;
        }
        
        if(x == MIN){
            left = MAX;
        } else {
            left = x-1;
        }
        
        if(y == MIN){
            above = MAX;
        } else {
            above = y-1;
        }
        
        if(y == MAX){
            below = MIN;
        } else {
            below = y+1;
        }
        
        //store theese invidiaul values in variables
        //get strategies from players
        
        sumA = playPD(aPopulation[left][y].getStrategyRight(),
                aPopulation[x][y].getStrategyLeft());
        
        sumB = playPD(aPopulation[right][y].getStrategyLeft(),
                aPopulation[x][y].getStrategyRight());
       
        sumC = playPD(aPopulation[x][above].getStrategyDown(),
                aPopulation[x][y].getStrategyUp());
       
        sumD = playPD(aPopulation[x][below].getStrategyUp(),
                aPopulation[x][y].getStrategyDown());
       
        //store individual contributions in node's memory
        aPopulation[x][y].setContributions(sumA, sumB, sumC, sumD);
        sum = sumA + sumB + sumC + sumD;
                                                                                //System.out.println("Sum from game:" + sum);
        //return the sum
        return sum;
    }
    
    /**
     * 
     * @param neighbor
     * @param selectedPlayer
     * @return 
     */
    public double playPD(boolean neighbor, boolean selectedPlayer){
        
       double payoff = 0.0;
        // check D-C encounter
        if(!selectedPlayer && neighbor){
            return DEFECTION_P;
        }
        // check C-C encounter
        if(selectedPlayer && neighbor){
            return 1.0;
        }
        // check D-D encounter
        if(!selectedPlayer && !neighbor){
            return EPSILON;
        }
        //return the payoff
        return payoff;
    }
    
    /**
     * Main entry point.
     * @param args 
     */
    public static void main(String[] args){
        
        PrintWriter output;
        double[] averages;
        Structured2D_PD exp = new Structured2D_PD();
        
        try{
            averages = exp.runExperiment();
            String popSize = Double.toString(POPULATION_SIZE);
            output = new PrintWriter(new BufferedWriter(new FileWriter("CBR_"+
                    Double.toString(DEFECTION_P)+ "Structured_2D_Multi_PopulationSize_ "
                    + popSize +".csv")));
            for(int i = 0; i < averages.length; i++){
                output.append(Double.toString(averages[i])+ ", ");
                output.flush();
            }
            output.close();
        }catch(IOException ex){
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
