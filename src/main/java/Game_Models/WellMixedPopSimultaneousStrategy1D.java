package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import Utility.Notifications;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author D.B. Dressler
 */
public class WellMixedPopSimultaneousStrategy1D {
    
    Random generator;
    FileWriter csvWriter;
    final int NEIGHBORS = 2;
    Node[] population;
    Node node;
    
    /**
     * Constructor
     */
    public WellMixedPopSimultaneousStrategy1D(){
        
        try{
            csvWriter = new FileWriter("wellmixed_simultaneous_data.csv");
        }catch(IOException ex){
            System.out.println(Notifications.ERR_FILENOTCREATED);
        }
        generator = new Random();
        population = UtilityMethods.setup1DPopulation(population);
    }
    
    /**
     * Initiates the experiment
     * @throws java.io.IOException
     */
    public void doExperiment() throws IOException{
        int sampleSize = ExpConstants.GAME_ROUNDS;
        int loc;
        for(int i = 0; i < sampleSize; i ++){
            
            loc = generator.nextInt(population.length);
            printCDRatioToFile();
            computeNodeUtility(loc);
            
        }
        printCDRatioToFile();
        csvWriter.close();
    }
    
    /**
     * 
     */
    private void computeNodeUtility(int loc){
        double utilityLeft, utilityRight;
        int leftNeighbor=0, rightNeighbor=0;
        int count = 0;
        while(count < NEIGHBORS){
            int tempLoc = generator.nextInt(population.length);
            
            if(count == 0 && tempLoc != loc){
                leftNeighbor = tempLoc;
                ++count;
            }
            if(count == 1 && tempLoc != loc && tempLoc != leftNeighbor){
                rightNeighbor = tempLoc;
                ++count;
            }
        }
        
        utilityLeft = payOffFormula(loc, leftNeighbor);
        utilityRight = payOffFormula(loc, rightNeighbor);
        if(utilityLeft < utilityRight){
            //population[loc].changeStrategy();
            utilityLeft = payOffFormula(loc, leftNeighbor);
            //System.out.println("new left" + utilityLeft);
        }
        if(utilityRight < utilityLeft){
            //population[loc].changeStrategy();
            utilityRight = payOffFormula(loc,rightNeighbor);
            //System.out.println("new right" + utilityRight);
        }
        
        population[loc].setFitness(utilityLeft+utilityRight);
    }
     
    
    /**
     * 
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
        
        if(population[loc].getCoopStatus()){
            payOff -= ExpConstants.COST;
        }
        return payOff;
    }
    
    /**
     * 
     * @throws IOException 
     */
    private void printCDRatioToFile() throws IOException{
        
        csvWriter.append(Integer.toString(
                UtilityMethods.get1DCoopCount(population))+ ",");
   }
}
