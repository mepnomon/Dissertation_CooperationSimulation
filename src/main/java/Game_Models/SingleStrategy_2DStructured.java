
package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.Random;

/**
 * Cooperation Simulator
 * PopulationIn2D
 * Created:  29-SEP-2016
 * Updated:  30-SEP-2016
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class SingleStrategy_2DStructured {
    
    Random generator;
    
    final int NEIGHBORS = 2;
    Node[][] aPopulation;
    Node node;
    double customCost;
    final int MAX, MIN;
    
    
    /**
     * Constructs a 2D Population
     */
    public SingleStrategy_2DStructured(){
        
        int size = (int)ExpConstants.POPULATION_SIZE;
        aPopulation = new Node[size][size];
        generator = new Random();
        MAX = aPopulation.length-1;
        MIN = 0;
    }
    
    public void runExperiment(){
        
        int sampleSize = ExpConstants.GAME_ROUNDS;
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation);
        UtilityMethods.print2DPopulation(aPopulation);
        for(int i = 0; i < sampleSize; i++){
//            System.out.println("Poplength" + aPopulation.length);
            int iRand = generator.nextInt(aPopulation.length);
            int jRand = generator.nextInt(aPopulation.length);
            System.out.println("Getting node:" + iRand+ ", "+ jRand);
            //System.out.println(UtilityMethods.get2DCoopCount(aPopulation));
            computePayoff(iRand,jRand); //center case
            compareAndUpdate(iRand, jRand);
            //UtilityMethods.print2DPopulation(aPopulation);
            System.out.println("Ratio: " + UtilityMethods.fractionOfCooperationSingle2D(aPopulation));
        }
        UtilityMethods.print2DPopulation(aPopulation);
        System.out.println("Coop count: " + UtilityMethods.get2DCoopCount(aPopulation));
    }
    
    /**
     * Performs experiment with custom set cost parameter
     * @param customCost the custom cost
     * @return the final fraction of cooperation in the population
     */
    public double runExperiment(double customCost){
        int interactions = ExpConstants.GAME_ROUNDS;
        int randX, randY;
        this.customCost = customCost;
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation, customCost);
        for(int i = 0; i < interactions; i++){
            randX = generator.nextInt(aPopulation.length);
            randY = generator.nextInt(aPopulation.length);
            computePayoff(randX, randY);
            compareAndUpdate(randX, randY);
        }
        UtilityMethods.print2DPopulation(aPopulation);
        return UtilityMethods.fractionOfCooperationSingle2D(aPopulation);
    }
    
    
    /**
     * Computes and stores sum of pay off groups.
     * @param randX
     * @param randY
     */
    public void computePayoff(int randX, int randY){  
        double payOffLeft, payOffRight, payOffTop, payOffBottom;
       
        boolean leftNeighbor, rightNeighbor, upNeighbor, downNeighbor;
        //int nodeID = population[i][j].getId();  
        
        //cases for neighbors
        if(randX == MAX){ 
            upNeighbor = aPopulation[MIN][randY].getStrategyDown();    
        }else{
            upNeighbor = aPopulation[randX+1][randY].getStrategyDown();
        }
        if(randX == MIN){
            downNeighbor = aPopulation[MAX][randY].getStrategyUp();
        } else {
            downNeighbor = aPopulation[randX-1][randY].getStrategyUp();
        }
        
        if(randY == MAX){
            rightNeighbor = aPopulation[randX][MIN].getStrategyLeft();
        } else {
            rightNeighbor = aPopulation[randX][randY+1].getStrategyLeft();
        }
        
        if(randY == MIN){
            leftNeighbor = aPopulation[randX][MAX].getStrategyRight();
        } else {
            leftNeighbor = aPopulation[randX][randY-1].getStrategyRight();
        }
        
        aPopulation[randX][randY].getSum(leftNeighbor, rightNeighbor, upNeighbor,
                downNeighbor);
    }
    
    /**
     * Compares with random neighbor
     * Updates coop status if applicable
     * @param randX row
     * @param randY column
     */
    public void compareAndUpdate(int randX, int randY){
        int next = 0;
        boolean modifyRow = false;
        int pick = generator.nextInt(4);
        String neighbor = null;
        switch(pick){
            case(0): if(randY==MIN)next=MAX; else next=randY-1; neighbor = "left"; break;
            case(1): if(randY==MAX)next=MIN; else next=randY+1; neighbor = "right"; break;
            case(2): if(randX==MIN)next=MAX; else next=randX-1; neighbor = "down"; modifyRow = true; break;
            case(3): if(randX==MAX)next=MIN; else next=randX+1; neighbor = "up"; modifyRow = true; break;
        }
        
        //System.out.println(neighbor);
        
        if(!modifyRow){ //compare - update
            computePayoff(randX, next);
            if(aPopulation[randX][randY].getFitness() < aPopulation[randX][next].getFitness()){
                aPopulation[randX][randY].setCoopStatus(aPopulation[randX][next].getCoopStatus());
            }
        
        } else {
            computePayoff(next,randY);
            if(aPopulation[randX][randY].getFitness() < aPopulation[next][randY].getFitness()){
                aPopulation[randX][randY].setCoopStatus(aPopulation[next][randY].getCoopStatus());
            }
        }
    }
}
