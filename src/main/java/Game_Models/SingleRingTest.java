package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import Utility.Notifications;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Cooperation Simulator
 * A Ring, Structured, Single Strategy
 * Created:  12-Aug-2016
 * Updated:  12-OCT-2016
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class SingleRingTest {
   
    Random generator;
    FileWriter totalCoopWriter, fractionWriter, cb_ratioWriter;
    FileWriter dataWriter;
    final double NEIGHBORS = 2;
    Node[] population;
    Node node;
    
    
    /**
     * Constructs a new data structure
     * containing nodes.
     */
    public SingleRingTest(){
        
        try{
            //total of cooperators
            totalCoopWriter = new FileWriter("ring_structured_cooperation.csv");
            //fractionWriter = new FileWriter("RingStructuredCoopFraction.csv");
            //cb_ratioWriter = new FileWriter("RingStructuredCB.csv");
            //this.dataWriter = dataWriter;//new FileWriter("coopData.csv");
        }catch(IOException ex){
            System.out.println(Notifications.ERR_FILENOTCREATED);
        }
        generator = new Random();
    }
    
    /**
     * Performs the experiment
     * @throws java.io.IOException
     */
    public void doExperiment() throws IOException{
        
        int sampleSize = ExpConstants.GAME_ROUNDS;
        population = UtilityMethods.setup1DPopulation(population);
        for(int i = 0; i < sampleSize; i++){
            System.out.println("\nInteraction: " + i);
            UtilityMethods.print1DPopulation(population);
            int loc = generator.nextInt(population.length);
            System.out.println("\nSelect Node:" + loc);
            //UtilityMethods.print1DPopulation(population);
            //computeFractionOfCooperation();
            printCDRatioToFile();
            computePayOff(loc);
            //computeBenefitCostRatio(loc);
            compareAndUpdate(loc);
            //UtilityMethods.print1DPopulation(population);
        }
        printCDRatioToFile();
        System.out.println("");
        UtilityMethods.print1DPopulation(population);
        totalCoopWriter.close();
        //fractionWriter.close();
        //cb_ratioWriter.close();
    }
    
    /**
     * Writes the number of cooperators to a file
     * @throws IOException 
     */
    private void printCDRatioToFile() throws IOException{
        double cooperators = UtilityMethods.get1DCoopCount(population);
        double coopRatio = cooperators / ExpConstants.POPULATION_SIZE ;
        totalCoopWriter.append(Double.toString(coopRatio)+ ",");
    }
           
    /**
     * Computes and stores sum of pay off groups.
     * @param loc location of the node
     */
    public void computePayOff(int loc){  
        double payOff1, payOff2;
        int leftNeighbor, rightNeighbor;
        
        
        //System.out.println("plength"+population.length);
        if(loc == population.length-1){
            leftNeighbor = loc-1; rightNeighbor = 0;  
        } else if (loc == 0){
            leftNeighbor = population.length-1; rightNeighbor = 1;
        } else {
            leftNeighbor = loc-1; rightNeighbor = loc+1;
        }
        
        //debug
        //System.out.print("\t" + leftNeighbor);
        //System.out.print("\t" + rightNeighbor);
        
        payOff1 = payOffFormula(loc, leftNeighbor);
        //System.out.println("left" + payOff1);
        payOff2 = payOffFormula(loc, rightNeighbor);
        //System.out.println("right" + payOff2);
        
        //population[loc].setPayOff(payOff1 + payOff2);
        double pOff = payOff1 + payOff2;
        System.out.println("Total payoff " + pOff);
        //System.out.println("total " + pOff);
        population[loc].setFitness(pOff);
    }
   
    /**
     * The formula to compute the payoff
     * @param loc
     * @param neighbor
     * @return 
     */
    private double payOffFormula(int loc, int neighbor){ 
        int coopNeighbors = 0;
        if(population[loc].getCoopStatus()){
            ++coopNeighbors;
        }
        if(population[neighbor].getCoopStatus()){
            ++coopNeighbors;
        }
        //so(coopNeighbors);
        //System.out.println(coopNeighbors);
        double payOff = (double)(ExpConstants.REWARD * coopNeighbors)/NEIGHBORS;
        //System.out.println(payOff);
        
        if(population[loc].getCoopStatus()){
            payOff -= ExpConstants.COST;
        }
        System.out.println("Interaction payoff: " + payOff);
        return payOff;
    }
    
    /**
     * Compares with random neighbor
     * Updates coop status if applicable
     * @param loc 
     */
    public void compareAndUpdate(int loc){
        
        double rand = generator.nextDouble();
        //System.out.println(rand);
        int neighbor = 0;
        final int leftmostNode = 0;
        final int rightmostNode = population.length-1;
        
        if(rand < 0.5){
            System.out.println("Choose left neighbor");
            neighbor = loc-1;
            if(neighbor < leftmostNode){
                neighbor = rightmostNode;
            }
            
        } else {
            System.out.println("Choose left neighbor");
            if(loc == rightmostNode){
               neighbor = leftmostNode;
            } else {
                neighbor = loc+1;
            }
        } 
        computePayOff(neighbor);
        //System.out.println("neighbor:" +population[neighbor].getUtility());
        if(population[loc].getFitness() <= population[neighbor].getFitness()){
            System.out.println("Copying neighbor's strategy");
            population[loc].setCoopStatus(population[neighbor].getCoopStatus());
        }
    }
        
    /**
     * 
     */
    private void computeBenefitCostRatio(int loc) throws IOException{
        
        double benefitCostRatio;
        double cost = 0;
        if(population[loc].getCoopStatus()){
            benefitCostRatio = (population[loc].getFitness() / ExpConstants.COST);
        } else {
            benefitCostRatio = population[loc].getFitness();
        }
        cb_ratioWriter.append(Double.toString(benefitCostRatio) + ", ");
        //System.out.println("BCR " + benefitCostRatio);
    }
}
