package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Cooperation Simulator
 * Well Mixed Population in 1D
 * Created: 12-OCT-2016
 * Updated: 13-OCT-2016
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 * 
 * CONSIDER A BACKLINK TO ORIGIN
 * AND ONE 
 */
public class RingWellMixed_Single {
    
    Random generator;
    FileWriter csvWriter;
    FileWriter dataWriter;
    final int NEIGHBORS = 2;
    private static int updateCounter = 0;
    private int coopCount;
    int lastMainNode;
    Node[] population;
    Node node;
    
    //newStuff
    //int tempLoc; //store last loc here
    int neighbor1 = 0, neighbor2 = 0;
    
    /**
     * Constructs a new data structure
     * containing nodes
     */
    public RingWellMixed_Single(){
        
        try{
            csvWriter = new FileWriter("mixed_data.csv");
        } catch (IOException ex){
            System.out.println("DOES NOT WORK");
        }
        generator = new Random();
    }
    
    
     /**
     * Performs the experiment
     * @throws java.io.IOException
     */
    public void doExperiment() throws IOException{
        
        int sampleSize = ExpConstants.GAME_ROUNDS;
        population = UtilityMethods.setup1DPopulation(population);        //System.out.println(getCooperatorCount());
        
        for(int i = 0; i < sampleSize; i++){
            
            //picks random node in lattice
            int loc = generator.nextInt(population.length);
            lastMainNode = loc;
            //System.out.print("\n" + loc);
            writeToFile();
            computePayOff(loc, true);
            compareAndUpdate(loc, neighbor1, neighbor2);
        }
        writeToFile();
        csvWriter.close();
    }
    
    
    /**
     * Compares with random neighbor
     * Updates coop status if applicable
     * @param loc 
     * @param n1 
     * @param n2 
     */
    public void compareAndUpdate(int loc, int n1, int n2){
        
        double rand = generator.nextDouble();//nextGaussian for normal distribution
        int neighbor;
        
        if(rand < 0.5){
            neighbor = n1;
            
        } else {
            neighbor = n2;
        }
        computePayOff(neighbor, false);
        
        //comparison
        if(population[loc].getFitness() < population[neighbor].getFitness()){
            //update strategy
            population[loc].setCoopStatus(population[neighbor].getCoopStatus());
            //System.out.println("\n changed:" + loc + "to " + population[loc].getCoopStatus());
            ++updateCounter;
        }
    }
    
    private void playPDLocally(){
        
        
        
    }
    /**
     * Computes and stores sum of pay off groups.
     * @param loc location of the node
     * @param originalRunthrough kick this out when not needed anymore
     */
    public void computePayOff(int loc, boolean originalRunthrough){  
        
        double payOff1=0, payOff2 = 0;
        int count = 0;
        
        //picks random neighbors if not original run through
        //remove if backlink is not required
        if(originalRunthrough){
            while(count < NEIGHBORS){
                int tempLoc = generator.nextInt(population.length-1); //pick random neighbor

                if(count == 0 && tempLoc != loc){
                    neighbor1 = tempLoc;
                    ++count;
                }
                if(count == 1 && tempLoc != loc && tempLoc != neighbor1){ //is not first temploc either
                    neighbor2 = tempLoc;
                    ++count;
                }
            }
        } else {
            while(count<NEIGHBORS-1){
                int tempLoc = generator.nextInt(population.length-1);
                
                if(count==0 && tempLoc != loc && tempLoc != lastMainNode){
                    neighbor1 = tempLoc;
                    ++count;
                }
                neighbor2 = lastMainNode;
            }
        }
        payOff1 = payOffFormula(loc, neighbor1);
        payOff2 = payOffFormula(loc, neighbor2);
        //debug
        //System.out.print("\t" + leftNeighbor);
        //System.out.print("\t" + rightNeighbor);
        
//        payOff1 = payOffFormula(loc, neighbor1);
//        payOff2 = payOffFormula(loc, neighbor2);
        //System.out.print("\t" + payOff1);
        //System.out.print("\t" + payOff2);
        
        //population[loc].setPayOff(payOff1 + payOff2);
        double pOff = payOff1+payOff2;
        
        //subtract cost is cooperator is cooperator
//        if(population[loc].getCoopStatus()){           
//            pOff-=ExperimentConstants.COST;
//        }
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
        
        double payOff = (ExpConstants.REWARD * coopNeighbors) / NEIGHBORS;
        
        //subtract cost is cooperator is cooperator
        if(population[loc].getCoopStatus()){           
            payOff-=ExpConstants.COST;
        }
        return payOff;
    }
        
    /**
     * 
     * @throws IOException 
     */
    private void writeToFile() throws IOException{
        
        csvWriter.append(Integer.toString(UtilityMethods.get1DCoopCount(population))+ ",");
    }
        
}
