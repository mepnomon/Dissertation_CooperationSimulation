package GameModels;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.POPULATION_SIZE;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**--THIS DATA IS CURRENTLY IN USE--
 *  Nodes with multiple strategies
 * High cognition, aware of neighbor performance
 * in a 1 dimensional structured population.
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class StructuredRing_TwoStrategies {
    
    Random generator;
    boolean displayData = true;     //true if user wants to display data
    final int MIN_BOUND, MAX_BOUND;
    Node[] aPopulation; //a population of nodes   
    double leftPay, rightPay;
    int focal;
    
    
    //Constructors
    /**
     * Zero parameter Constructs a Structured Population Lattice
     */
    public StructuredRing_TwoStrategies(){
        
        generator = new Random();
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        MIN_BOUND = 0;
        MAX_BOUND = aPopulation.length-1;   
    }
    
    //methods
    /**
     * carries out the experiment
     * @return 
     * @throws IOException 
     */
    public double[] runExperiment() throws IOException{
        
        int rounds = ExpConstants.GAME_ROUNDS;
        int samples = 1000;
        double averages[] = new double[rounds];    
        if(displayData){
            System.out.println("Nodes with two strategies.");
            System.out.println("**************************");
        }
        
        //creates a shuffled population where 50% of nodes cooperate
        for(int i = 0; i < samples; i++){     
            //begin simulation
            aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
            for(int j = 0; j < rounds; j++){
               // UtilityMethods.print1DMultiStrategyPopulation(aPopulation);
//                double fc = UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
//                System.out.println(Double.toString(fc));
                if(i == 0){
                    averages[j] = UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
                } else {
                averages[j] += 
                        UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
                }
                focal = generator.nextInt(aPopulation.length);
                updateStrategy(focal);
                
            }
            //System.out.println("\n\n new population \n");
        }
        
        //caculate averages
        for(int i = 0; i < averages.length; i++){
            averages[i]/= samples;
        }
        //return array of averages
        return averages;
    }
    
    
    public double runExperiment(double customCost){
        
        int selectedNode;
        int interactions = ExpConstants.GAME_ROUNDS;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);
        
        for(int j = 0; j < interactions; j++){
            selectedNode = generator.nextInt(aPopulation.length);
            //calculatePayoff(selectedNode);
            compareAndUpdate(selectedNode);
        }
        return UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
    }
    
    /**
     * 
     * @param customB
     * @return 
     */
    public double runExperimentVarB (double customB){
        int rounds = ExpConstants.GAME_ROUNDS;
        int samples = 1000;
        int focal;
        double average = 0;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
        
//        if(displayData){
//            System.out.println("Nodes with two strategies.");
//            System.out.println("**************************");
//        }
        
        //creates a shuffled population where 50% of nodes cooperate
        for(int i = 0; i < samples; i++){     
            //begin simulation
            for(int j = 0; j < rounds; j++){
                focal = generator.nextInt(aPopulation.length);
                updateStrategy(focal, customB);
            }//end inner loop
            average += UtilityMethods.fractionOfCooperationMulti1D(aPopulation);
        }//end outer loop
        
        //caculate averages
        
        //return array of averages
        return average/(double)samples;
    }
      
    /**
     *
     */
    private void updateStrategy(int focal){
        int leftNeighbor, rightNeighbor;
        
         if(focal == MAX_BOUND){
            leftNeighbor = focal-1; rightNeighbor = MIN_BOUND;  
        } else if (focal == MIN_BOUND){
            leftNeighbor = MAX_BOUND; rightNeighbor = focal+1;
        } else {
            leftNeighbor = focal-1; rightNeighbor = focal+1;
        }
         
       double focalSum = calculatePayoff(focal);
        //select a model
        int model, neighbor;
        double selectModel = generator.nextDouble();
        boolean copyStrategy;
        if(selectModel < 0.5){
            //model is right neighbor
            model = rightNeighbor;
            copyStrategy = aPopulation[model].getStrategyLeft();
            neighbor = leftNeighbor;
        } else {
            //model is left neighbor
            model = leftNeighbor;
            copyStrategy = aPopulation[model].getStrategyRight();
            neighbor = rightNeighbor;
        }
        
        double modelSum = calculatePayoff(model);
        double neighborSum = calculatePayoff(neighbor);
        
//        //compare and update strategy against  player
          if(modelSum > focalSum){
              
            //replace weaker contributor
            if(leftPay > rightPay){
                aPopulation[focal].setStrategyRight(copyStrategy);
            } else if(rightPay > leftPay){
                aPopulation[focal].setStrategyLeft(copyStrategy);
            
            } else{ //if both are equal

                if(generator.nextDouble() < 0.5){
                    aPopulation[focal].setStrategyRight(copyStrategy);
                } else {
                    aPopulation[focal].setStrategyLeft(copyStrategy);
                }
            }
            
            //replace model
            //cluster-bildung
        }
    }
    
    /**
     * Calculates the cumulative payoff of a node.
     * @param player the node's location
     */
    private double calculatePayoff(int player){
        
        int leftNeighbor, 
            rightNeighbor;
        boolean recordLeftPay=false, 
                recordRightPay = false;
        //selects direct neighbors, prev population.length - 1
        if(player == MAX_BOUND){
            leftNeighbor = player-1; rightNeighbor = MIN_BOUND;  
        } else if (player == MIN_BOUND){
            leftNeighbor = MAX_BOUND; rightNeighbor = player+1;
        } else {
            leftNeighbor = player-1; rightNeighbor = player+1;
        }
//    use this code stub with public good's game, else use PD stub    
//        return aPopulation[player].getSum(
//                aPopulation[leftNeighbor].getStrategyRight(), 
//                aPopulation[rightNeighbor].getStrategyLeft());
        //this is the block for the Prisoner's Dilemma game
        boolean playerLeft = aPopulation[player].getStrategyLeft(), 
                playerRight = aPopulation[player].getStrategyRight(),
                rightNStrategy = aPopulation[rightNeighbor].getStrategyLeft(), 
                leftNStrategy = aPopulation[leftNeighbor].getStrategyRight();
        
        //check if player is focal
        if(player == focal){
            //record the left payoff
            recordLeftPay = true;
        }
        
        
        double leftSum = playPDLocally(leftNStrategy, playerLeft, 
                recordLeftPay, recordRightPay);
        
        //check if player is focal
        if(player == focal){
            //reset
            recordLeftPay = false;
            //record right pay
            recordRightPay = true;
        }
        
        double rightSum = playPDLocally(rightNStrategy, playerRight, 
                recordLeftPay, recordRightPay);
        return leftSum+rightSum;
    }
    
    
    /**
     * Plays the prisoner's dilemma game in a local scope
     * @param neighbor the neighbor's strategy
     * @param focal the focal player's strategy
     * @return the payoff from an interaction
     */
    public double playPDLocally(boolean neighbor, boolean focal, 
            boolean recordLeftPay, boolean recordRightPay){
        //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!focal && neighbor){
            payoff = DEFECTION_P;// - cognitiveCost;
            //return customB;
        }
        // check C-C encounter
        if(focal && neighbor){
            payoff = 1.0;// - cognitiveCost;
        }
        // check D-D encounter
        if(!focal && !neighbor){
            payoff =  EPSILON;// - cognitiveCost;
        }
        
        if(recordRightPay){
            rightPay = payoff;
        }
        if(recordLeftPay){
            leftPay = payoff;
        }
        //return the payoff
        return payoff;// - cognitiveCost;
    }
    
    
    
    
    
    
    
    
    /**
     *
     */
    private void updateStrategy(int focal, double customB){
        int leftNeighbor, rightNeighbor;
        
         if(focal == MAX_BOUND){
            leftNeighbor = focal-1; rightNeighbor = MIN_BOUND;  
        } else if (focal == MIN_BOUND){
            leftNeighbor = MAX_BOUND; rightNeighbor = focal+1;
        } else {
            leftNeighbor = focal-1; rightNeighbor = focal+1;
        }
         
       double focalSum = calculatePayoff(focal, customB);
        //select a model
        int model, neighbor;
        double selectModel = generator.nextDouble();
        boolean copyStrategy;
        if(selectModel < 0.5){
            //model is right neighbor
            model = rightNeighbor;
            copyStrategy = aPopulation[model].getStrategyLeft();
            neighbor = leftNeighbor;
        } else {
            //model is left neighbor
            model = leftNeighbor;
            copyStrategy = aPopulation[model].getStrategyRight();
            neighbor = rightNeighbor;
        }
        
        double modelSum = calculatePayoff(model, customB);
        double neighborSum = calculatePayoff(neighbor, customB);
        
        //compare and update strategy against  player
        if(modelSum > focalSum){
            //replace model
//            if(model == rightNeighbor){
//                aPopulation[focal].setStrategyRight(copyStrategy);
//            } else {
//                aPopulation[focal].setStrategyLeft(copyStrategy);
//            }
              //replace weaker opponent block (encapsulate)
            if(modelSum > neighborSum){
                //replace strategy for this neighbor
                if(neighbor == leftNeighbor){
                    aPopulation[focal].setStrategyLeft(copyStrategy);
                } else {
                    aPopulation[focal].setStrategyRight(copyStrategy);
                }
            //in case both neighbors perform same
            } else if(modelSum == neighborSum) {
                //choose a random one
                double selectPlayerToUpdate = generator.nextDouble();
                //select random
                if(selectPlayerToUpdate < 0.5){
                    aPopulation[focal].setStrategyLeft(copyStrategy);
                } else {
                    aPopulation[focal].setStrategyRight(copyStrategy);
                }
               
            } else {
                //do nothing or replace strategy with model
//                if(model == leftNeighbor){
//                    aPopulation[focal].setStrategyLeft(copyStrategy);
//                } else {
//                    aPopulation[focal].setStrategyRight(copyStrategy);
//                }
            }
        }   
    }
    
    /**
     * Calculates the cumulative payoff of a node.
     * @param player the node's location
     */
    private double calculatePayoff(int player, double customB){
        
        int leftNeighbor, rightNeighbor;
        
        
        //selects direct neighbors, prev population.length - 1
        if(player == MAX_BOUND){
            leftNeighbor = player-1; rightNeighbor = MIN_BOUND;  
        } else if (player == MIN_BOUND){
            leftNeighbor = MAX_BOUND; rightNeighbor = player+1;
        } else {
            leftNeighbor = player-1; rightNeighbor = player+1;
        }
//    use this code stub with public good's game, else use PD stub    
//        return aPopulation[player].getSum(
//                aPopulation[leftNeighbor].getStrategyRight(), 
//                aPopulation[rightNeighbor].getStrategyLeft());
        //this is the block for the Prisoner's Dilemma game
        boolean playerLeft = aPopulation[player].getStrategyLeft(), 
                playerRight = aPopulation[player].getStrategyRight(),
                rightNStrategy = aPopulation[rightNeighbor].getStrategyLeft(), 
                leftNStrategy = aPopulation[leftNeighbor].getStrategyRight();
        
        double leftSum = playPDLocally(leftNStrategy, playerLeft, customB);
        double rightSum = playPDLocally(rightNStrategy, playerRight, customB);
        return leftSum+rightSum;
    }
    
    
    /**
     * Compares to a neighbor. 
     * If neighbor is a model, gets its strategy and uses
     * it against the neighbor that contributed least to 
     * cardinal node's payoff.
     * @param selectedNode cardinal node's locations
     */
    private void compareAndUpdate(int selectedNode){
        
        float pick = generator.nextFloat();
        int neighbor;
        boolean newStrategy, isNeighborLeft;
        
        //pick a neighbor to compare payoffs with
        if(pick > 0.5){ //pick right neighbor
            if(selectedNode == MAX_BOUND){
                neighbor = MIN_BOUND;
            } else {
                neighbor = selectedNode+1;
            }
            isNeighborLeft = false;
        } else { // pick left neighbor
            if(selectedNode == MIN_BOUND){
                neighbor = MAX_BOUND;
            } else {
                neighbor = selectedNode-1;
            }
            isNeighborLeft = true;
        }
        //calculatePayoff(neighbor); ADD after removing custom B
            
        if(aPopulation[selectedNode].getUtility() < aPopulation[neighbor].getUtility()){
            if(!isNeighborLeft){ // right neighbor
                newStrategy = aPopulation[neighbor].getStrategyLeft();
            } else { //left neighor
                newStrategy = aPopulation[neighbor].getStrategyRight();
            }
            aPopulation[selectedNode].changeSecondaryStrategy(newStrategy);
        } 
    }
    
    
    
    
    /**
     * Plays the prisoner's dilemma game in a local scope
     * @param neighbor the neighbor's strategy
     * @param focal the focal player's strategy
     * @return the payoff from an interaction
     */
    public double playPDLocally(boolean neighbor, boolean focal, double customB){
        //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!focal && neighbor){
            //return DEFECTION_P;// - cognitiveCost;
            return customB;
        }
        // check C-C encounter
        if(focal && neighbor){
            return 1.0;// - cognitiveCost;
        }
        // check D-D encounter
        if(!focal && !neighbor){
            return EPSILON;// - cognitiveCost;
        }
        //return the payoff
        return payoff;// - cognitiveCost;
    }
    
    
    /**
     * Main entry point for the program.
     * @param args 
     */
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        double[] averages;    
        System.out.println("Enter:\n1 to compute averages\n2 to compute CBR");
        System.out.print("Select: ");
        int modeSelect = sc.nextInt();
        System.out.println(Integer.toString(modeSelect));
        if(modeSelect != 1 || modeSelect != 2){
            System.out.println("Please enter 1 or 2");
        }
        sc.close();
        if(modeSelect == 1){
            
            PrintWriter output = new PrintWriter(new BufferedWriter(
                        new FileWriter("1D Structured - Two Strategies with" + 
                                Double.toString(POPULATION_SIZE) +"_Nodes.csv")));
            StructuredRing_TwoStrategies exp = new StructuredRing_TwoStrategies();
            averages = exp.runExperiment();

            for(double d : averages){
                output.append(Double.toString(d)+", ");
                output.flush();
            }
            output.close();
        } 
        
        if(modeSelect == 2){
            System.out.println("\nCBR computation running...");
            ArrayList<Double> averagesPerB = new ArrayList<>();
            //offset b value
            double customB = 0;
            final double B_MAX = 5;
            //increase b by 0.25
            final double B_INCR = 0.25;
            //declare a writer
            PrintWriter output = new PrintWriter(new BufferedWriter(
                        new FileWriter("CBR "+ Double.toString(DEFECTION_P) + "for 1D Structured - Two Strategies with" + 
                                Double.toString(POPULATION_SIZE) +"_Nodes.csv")));
            while(customB <= B_MAX){
                StructuredRing_TwoStrategies exp = 
                        new StructuredRing_TwoStrategies();
                averagesPerB.add(exp.runExperimentVarB(customB));
                customB += B_INCR;
            }
            
            for(double d : averagesPerB){
                output.append(Double.toString(d) + ", ");
                output.flush();
            }
            output.close();
        }
    }//end main

}
