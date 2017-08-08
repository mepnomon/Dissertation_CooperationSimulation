package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.ExpConstants.POPULATION_SIZE;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * --THIS DATA IS CURRENTLY IN USE--
 * Cooperation Simulator
 * A population set out on a ring.
 * Each node employs a single strategy.
 * The population is structured, meaning that nodes can only interact with
 * their neighbors.
 * Created:  12-Aug-2016
 * Updated:  12-OCT-2016
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class StructuredRingSingle {
   
    private final Random GENERATOR;
    //file writers for total cooperation, fraction of cooperation and ratio
    FileWriter totalCoopWriter, fractionWriter, cb_ratioWriter;
    //writes data
    FileWriter dataWriter;
    //number of neighbors each node has
    final double NEIGHBORS = 2;
    //data structure stores population
    Node[] aPopulation;
    //ring boundaires
    final int MAX, MIN;
    
    
    /**
     * Constructs a new data structure
     * containing nodes.
     * @throws java.io.IOException
     */
    public StructuredRingSingle() throws IOException{
        
        //total of cooperators
        //fractionWriter = new FileWriter("Ring_SingleStrategy_CDRatio_PD.csv");
        //initialize rng
        GENERATOR = new Random();
        //initialize population
        aPopulation = new Node[(int)ExpConstants.POPULATION_SIZE];
        //maximum node
        MAX = aPopulation.length-1;
        //minimum node
        MIN = 0;
    }
    
    /**
     * Performs the experiment
     * @return 
     * @throws java.io.IOException
     */
    public double[] runExperiment() throws IOException{
        
        double averages[] = new double[(int)GAME_ROUNDS];
        //get game rounds from constants
        final int ROUNDS = GAME_ROUNDS;
        final int SAMPLES = 100;   
        
        for(int i = 0; i < SAMPLES; i++){
            
            //create new population
            aPopulation = UtilityMethods.setup1DPopulation(aPopulation);
            for(int j = 0; j < ROUNDS; j++){
                //select a random player
                int focal = GENERATOR.nextInt(aPopulation.length);
                //writeCDRatioToFile();
                //playPGG(focal);
                //playPD(focal);
                //compareAndUpdate(focal);
                updateStrategyPD(focal);

                //Debug Dialog
                //UtilityMethods.print1DPopulation(aPopulation);
                //System.out.println("");
                averages[j] += 
                        UtilityMethods.fractionOfCooperationSingle1D(aPopulation);
            }
        }
        
        for(int i = 0; i < averages.length; i++){
            averages[i] /= SAMPLES;
        }
        //writeCDRatioToFile();
        
        //just there to get the values
        double cooperators = UtilityMethods.get1DCoopCount(aPopulation);
        double coopRatio = cooperators / ExpConstants.POPULATION_SIZE;
        System.out.println("Ratio:" + coopRatio);
        return averages;
    }
    
    /**
     * Run Experiment method used when a custom cost has been
     * specified by the user.
     * @param customCost the interaction cost
     * @return the fraction of cooperation in the population
     */
    public double runExperiment(double customCost){
        int rounds = ExpConstants.GAME_ROUNDS;
        int focal;
        aPopulation = UtilityMethods.setup1DPopulation(aPopulation, customCost);
        
        for(int i = 0; i < rounds; i++){
            focal = GENERATOR.nextInt(aPopulation.length);
            playPGG(focal);
            updateStrategyPGG(focal);
        }
        return UtilityMethods.fractionOfCooperationSingle1D(aPopulation);
    }
    
    /**
     * Writes the number of cooperators to a file
     * @throws IOException 
     */
    private void writeCDRatioToFile() throws IOException{
        double cooperators = UtilityMethods.get1DCoopCount(aPopulation);
        double coopRatio = cooperators / ExpConstants.POPULATION_SIZE;
        fractionWriter.append(Double.toString(coopRatio)+ ",");
    }
           
    /**
     * Computes and stores sum of pay off groups.
     * @param focal location of the node
     */
    private void playPGG(int focal){  
        int left, right;
        boolean leftNeighbor, rightNeighbor;
        
        
        //System.out.println("plength"+population.length);
        if(focal == MAX){
            left = focal-1; right = MIN;  
        } else if (focal == MIN){
            left = MAX; right = focal+1;
        } else {
            left = focal-1; right = focal+1;
        }
        
        leftNeighbor = aPopulation[left].getCoopStatus();
        rightNeighbor = aPopulation[right].getCoopStatus();
        aPopulation[focal].getSum(leftNeighbor, rightNeighbor);
    }
    
    /**
     * Plays the prisoner's dilemma game.
     * @param aPlayer the player
     * @return the sum of the payoff
     */
    private double getCumulativePayoff(int aPlayer){
        
        //local variables for left and right neighbor locations
        int left, right;
        //if  player is at last position
        if(aPlayer == MAX){
            left = aPlayer-1; right = MIN;
        } else if (aPlayer == MIN){
            left = MAX; right = aPlayer+1;
        } else {
            left = aPlayer-1; right = aPlayer+1;
        }
       
        //get strategies
        boolean focalStrategy = aPopulation[aPlayer].getCoopStatus();
        boolean rightStrategy = aPopulation[right].getCoopStatus();
        boolean leftStrategy = aPopulation[left].getCoopStatus();
        double sumLeft = playPD(leftStrategy, focalStrategy);
        double sumRight = playPD(rightStrategy, focalStrategy);
        
        return sumLeft + sumRight;
    }
    
    /**
     * Plays the prisoner's Dilemma game locally.
     * @param aNeighbor
     * @param aFocalPlayer
     * @return 
     */
    private double playPD(boolean aNeighbor, boolean aFocalPlayer){
        //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!aFocalPlayer && aNeighbor){
            return DEFECTION_P;// - cognitiveCost;
        }
        // check C-C encounter
        if(aFocalPlayer && aNeighbor){
            return 1.0;// - cognitiveCost;
        }
        // check D-D encounter
        if(!aFocalPlayer && !aNeighbor){
            return EPSILON;// - cognitiveCost;
        }
        //return the payoff
        return payoff;// - cognitiveCost;
    }
    
    /**
     * Compares with random neighbor
     * Updates coop status if applicable
     * @param focal 
     */
    private void updateStrategyPGG(int focal){
        
        float pick = this.GENERATOR.nextFloat();
        //System.out.println(rand);
        int neighbor = 0;
        //picks the focal player's 
        if(pick < 0.5){ //left neighbor selected
            neighbor = focal-1;
            if(focal ==  MIN){
                neighbor = MAX;
            }
            
        } else { // right neighbor selected
            if(focal == MAX){
               neighbor = MIN;
            } else {
                neighbor = focal+1;
            }
        } 
        //playPGG(neighbor);
        getCumulativePayoff(neighbor);
        
        //replaces model
        if(aPopulation[focal].getFitness() < aPopulation[neighbor].getFitness()){
            aPopulation[focal].setCoopStatus(aPopulation[neighbor].getCoopStatus());
        }
    }
    
    //replace weaker opponent here/
    /**
     * Replaces the weaker opponent
     * @param focal the focal player
     */
    private void updateStrategyPD(int focal){
        
        //establish who the neighbors are
        int model, neighbor;
        double selectModel = GENERATOR.nextDouble();
        //select a model to mimic
        if(selectModel < 0.5){;
            //select left as model
            if(focal == MIN){
                model = MAX;
                neighbor = focal +1;
            } else {
                //normal left neighbor as model
                model = focal-1;
                //if focal is on the right boundary
                if(focal == MAX){
                    //right neighbor is 0
                    neighbor = MIN;
                } else { //if not
                    //select normal right neighbor
                    neighbor = focal+1;
                }
            } // end if
            
        } else { //select right as model
            //if focal player is on the right boundary
            if(focal == MAX){
                //select 0 as right neighbor
                model = MIN;
                neighbor = focal-1;
            } else {
                //select normal right neighbor
                model = focal +1;
                
                //if focal is on the left boundary
                if(focal == MIN){
                    //select MAX as left neighbor
                    neighbor = MAX;
                } else {
                    //normal left neighbor
                    neighbor = focal-1;
                }
            }
               
        }
        
        boolean modelStrategy = aPopulation[model].getCoopStatus();
        boolean focalStrategy = aPopulation[focal].getCoopStatus();
        boolean neighborStrategy = aPopulation[neighbor].getCoopStatus();
        
        //get pay offs from each group
        double focalSum = getCumulativePayoff(focal);
        double modelSum = getCumulativePayoff(model);
        double neighborSum = getCumulativePayoff(neighbor);
        
        //if model outperforms focal player
        if(modelSum > focalSum){
            
            aPopulation[focal].setCoopStatus(modelStrategy);
        }
        //try replace weaker
        
    }
        
    /**
     * Calculates the benefit / cost ratio
     * and writes it to a file
     */
    private void writeCBR(int loc) throws IOException{
        
        double benefitCostRatio;
        double cost = 0;
        if(aPopulation[loc].getCoopStatus()){
            benefitCostRatio = (aPopulation[loc].getFitness() / ExpConstants.COST);
        } else {
            benefitCostRatio = aPopulation[loc].getFitness();
        }
        cb_ratioWriter.append(Double.toString(benefitCostRatio) + ", ");
        //System.out.println("BCR " + benefitCostRatio);
    }
    
    /**
     * Main entry point
     * @param args 
     */
    public static void main(String[] args) throws IOException{
        
            double[] averages;
            //declare and instantiate filewriter
            PrintWriter output = new PrintWriter(new BufferedWriter(
                    new FileWriter("SingleStrategy_Ring_Avgs"+  Double.toString(DEFECTION_P)+ "_" + 
                            Double.toString(POPULATION_SIZE) +"PD.csv")));
            
            StructuredRingSingle exp = new StructuredRingSingle();
            averages = exp.runExperiment();
           
            //write averages to file
            for(double d : averages){
                output.append(Double.toString(d) + ", ");
                output.flush();
            }
            output.close();
    }
}
