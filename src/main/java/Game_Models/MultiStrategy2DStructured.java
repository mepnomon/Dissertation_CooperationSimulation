package Game_Models;

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
 * @author D. Dressler
 */
public class MultiStrategy2DStructured {
    
    Random generator; //random number generator
    int focalPlayerX, focalPlayerY; //focal player location
    int modelPlayerX, modelPlayerY; //model player location
    Node[][] aPopulation; //the population
    final int POP_SIZE;   //populatio nsize
    final int MIN;   //store boundaries
    int max_x; int max_y;
    boolean modelStrategy; //model's strategy
    double sumA, sumB, sumC, sumD; //individual pairwise payofs
    
    /**
     * Constructor
     */
    public MultiStrategy2DStructured(){
        
        //instantiate population
        POP_SIZE = ((int) POPULATION_SIZE);
        //instantuate RNG
        generator = new Random();
        //instantiate populatin
        aPopulation = new Node[POP_SIZE][POP_SIZE];
        //boundary control variables
        //MAX = POP_SIZE-1;
        MIN = 0;
    }
    
    /**
     * Performs the experiment
     * @return averaged fraction of cooperation/time
     */
    public double[] runExperiment(){
        
        final int SAMPLES = 100;
        double focalSum, modelSum;
        double[] averages = new double[(int)GAME_ROUNDS];
        //obtain Samples
        for(int i = 0; i < SAMPLES; i++){
            
            int updates = 0; //count updates
            //create new population for each sample
            
            aPopulation = UtilityMethods.setup2DPopulation((int)POPULATION_SIZE);
            max_x = UtilityMethods.getMAX_X()-1;
            max_y = UtilityMethods.getMAX_Y()-1;
            //play game
            for(int j = 0; j < GAME_ROUNDS; j++){
                
                double cumulativePayoffFocal, cumulativePayoffModel;
                //pick random player
                focalPlayerX = generator.nextInt(max_x);
                focalPlayerY = generator.nextInt(max_y);
                                                                                //System.out.println("Focal: X: "+ focalPlayerX + " Y: "+ focalPlayerY);
                selectModel();
                //test the output
                                                                                //System.out.println("Model: X: "+ modelPlayerX + " Y: "+ modelPlayerY);
                
                cumulativePayoffFocal = getCumulativePayoff(
                        focalPlayerX, focalPlayerY);
                
                cumulativePayoffModel = getCumulativePayoff(
                        modelPlayerX, modelPlayerY);
                
                                                                                //System.out.println("Focal payoff: " + cumulativePayoffFocal);
                                                                                //System.out.println("Model payoff: " + cumulativePayoffModel);
                
                //update strategy if outperformed by model
                if(cumulativePayoffModel > cumulativePayoffFocal){
                                                                                //System.out.println("-----\nChange strategy\n-----\n");
                    ++updates;
                    //select weakest contributor to replace strategy for
                    int weakest = 
                            aPopulation[focalPlayerX][focalPlayerY].
                                    getWeakestContributor();
                                                                                //System.out.println("Weakest: " + weakest);
                   //this introduces misjudgment each 1000 steps
                   if(updates % GAME_ROUNDS*0.1 == 0){
                       weakest = generator.nextInt(4);
                    }
                   replaceWeakest(weakest);
                } // end if
                                                                                //System.out.println("\n\nxxxxxxxxxxxxxxxxxxxxxx\n");
            averages[j] += UtilityMethods.fractionOfCooperationMulti2D(aPopulation)*10;
            //UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
            }//end inner loop
        }//end outer loop
        
        
        //average all values
        for(int i = 0; i < GAME_ROUNDS; i++){
            averages[i] /= SAMPLES;
        }
        
        //print statistics
        displayPerformanceStatistics(averages);
        return averages;
    }//end method
    
    /**
     * Displays performance statistics
     * @param averages data structure with averages
     */
    private void displayPerformanceStatistics(double averages[]){
        
        //local variables
        double totalFitnessAll = 0,
               totalFitnessDefectors = 0,
               totalFitnessCooperators,
               averageFitnessDefectors,
               averageFitnessCooperators;
        
        //calculate total fitness
        for(int x = 0; x  < UtilityMethods.getMAX_X(); x++){
            for(int y = 0; y < UtilityMethods.getMAX_Y(); y++){
                
                //ssum cumulative payoffs
                totalFitnessAll += getCumulativePayoff(x,y);
                //check if defector payoffs
                if(sumA == EPSILON || sumA == DEFECTION_P){
                    totalFitnessDefectors += sumA;
                }
                
                if(sumB == EPSILON || sumB == DEFECTION_P){
                    totalFitnessDefectors += sumB;
                }
                
                if(sumC == EPSILON || sumC == DEFECTION_P){
                    totalFitnessDefectors += sumC;
                }
                
                if(sumD == EPSILON || sumD == DEFECTION_P){
                    totalFitnessDefectors += sumD;
                }
                //if 
            }//end y
        }//end x
        
        //print values to console
        System.out.println("Prisoner's Dilemma Game with Multiple Strategies in 2D Strucured Lattice:\n");
        System.out.println("Linear Size : " + POPULATION_SIZE + ", b = " + DEFECTION_P + ".  ");
        System.out.println("Total fitness all : " + totalFitnessAll);
        System.out.println("Average fitness per node : " + totalFitnessAll/POPULATION_SIZE);
        //get fraction of defection
        double fractionOfDefection = 1 - (averages[GAME_ROUNDS-1]*10);
        totalFitnessDefectors = totalFitnessAll * fractionOfDefection;
        System.out.println("Total fitness defectors : " + totalFitnessDefectors);
        //divide the Total Fitness for defectors by it
        System.out.println("Fraction of defection : " + fractionOfDefection);
        //averageFitnessDefectors = totalFitnessDefectors/(fractionOfDefection*100);
        averageFitnessDefectors = totalFitnessDefectors/((POPULATION_SIZE)*fractionOfDefection);
        System.out.println("Average fitness p. defectors : " + averageFitnessDefectors);
        totalFitnessCooperators = totalFitnessAll - totalFitnessDefectors;
        System.out.println("Total Fitness Cooperators : " + totalFitnessCooperators);
        averageFitnessCooperators = (totalFitnessCooperators/(averages[GAME_ROUNDS-1]*100));
        System.out.println("Average Fitness p. Cooperator : " + averageFitnessCooperators);
        System.out.println("Pop. fraction of cooperation : " + averages[GAME_ROUNDS-1]*10 );
        System.out.println("\n\nPopulation view:\n****************\n");
        UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
        System.out.println("\n\n");
        System.out.println("count:" + UtilityMethods.get2DCoopCountMulti(aPopulation));
    }
    
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
        
        //select a neighbor
        int neighbor = generator.nextInt(4);
        
        //select appropriate neighbor in 2D structure
        //0=left, 1 right, 2 above, 3, below
        switch(neighbor){
            
            case 0: //left opponent
                                                                                //System.out.println("Select left neighbor as model");
                //check if left player on opposite side
                if(focalPlayerX == MIN){
                    modelPlayerX = max_x;
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
                if(focalPlayerX == max_x){
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
                    modelPlayerY = max_y;
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
                if(focalPlayerY == max_y){
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
     * Plays Prisoner's Dilemma in a von Neumann neighborhood
     * @param x x coordinate of player
     * @param y y coordinate of player
     * @return cumulative payoff
     */
    public double getCumulativePayoff(int x, int y){
        
        double sum;
        int below, above, left, right;
        
        //check boundary cases
        if(x == max_x){
            right = MIN;
        } else{
            right = x+1;
        }
        
        if(x == MIN){
            left = max_x;
        } else {
            left = x-1;
        }
        
        if(y == MIN){
            above = max_y;
        } else {
            above = y-1;
        }
        
        if(y == max_y){
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
     * Plays the Prisoner's Dilemma game between two opponents
     * @param neighbor the neighbor's strategy
     * @param selectedPlayer the player's strategy
     * @return a pairwise payoff
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
        MultiStrategy2DStructured exp = new MultiStrategy2DStructured();
        
        try{
            averages = exp.runExperiment();
            String popSize = Double.toString(POPULATION_SIZE);
            String bVal = Double.toString(DEFECTION_P);
            output = new PrintWriter(new BufferedWriter(
                    new FileWriter("CBR_ " + bVal + "_Structured_2D_Multi_PopulationSize_ "
                            + popSize +".csv")));
            
            output.append("Linear Size : " + popSize + ", b = " + Double.toString(DEFECTION_P));
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
