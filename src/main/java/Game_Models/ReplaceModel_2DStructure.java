package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.Random;

/**
 * A class to investigate the performance 
 * of nodes with multiple strategies in a 2D population that 
 * copies a model's strategy and uses it with the model.
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class ReplaceModel_2DStructure {
    
    Node[][] aPopulation;
    boolean customCostUsed = false;
    double customCost;
    Random rand;
    final int MIN, MAX;
    
    /**
     * 
     */
    public ReplaceModel_2DStructure(){
        int popSize = (int)ExpConstants.POPULATION_SIZE;
        rand = new Random();
        Node[][] aPopulation = new Node[popSize][popSize];
        MAX = aPopulation.length-1;
        MIN = 0;
        
    }
    
    /**
     * Performs the experiment.
     */
    public void performExperiment(){
        int randX, randY;
        int interactions = ExpConstants.GAME_ROUNDS;
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation);
        for(int i = 0; i < interactions; i++){
            randX = rand.nextInt(aPopulation.length);
            randY = rand.nextInt(aPopulation.length);
            computePayoff(randX, randY);
            System.out.println("%C" + UtilityMethods.fractionOfCooperationMulti2D(aPopulation));
            UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
            //compareAndUpdate(randX, randY);
        }
    }
    
    /**
     * Performs experiment with custom cost set.
     * @param customCost user specified cost.
     * @return fraction of cooperation at given CBR.
     */
    public double performExperiment(double customCost){
        
        int randX, randY;
        int interactions = ExpConstants.GAME_ROUNDS;
        aPopulation  = UtilityMethods.setup2DPopulation(aPopulation, customCost);
        
        for(int i = 0; i < interactions; i++){
            randX = rand.nextInt(aPopulation.length);
            randY = rand.nextInt(aPopulation.length);
            computePayoff(randX, randY);
            compareAndUpdate(randX, randY);
            //System.out.println("%C" + UtilityMethods.fractionOfCooperationMulti2D(aPopulation));
            //UtilityMethods.print2DPopulation(aPopulation);
            //compareAndUpdate(randX, randY);
        }
        
        //returns fraction of cooperation at given cost benefit ratio
        return UtilityMethods.fractionOfCooperationMulti2D(aPopulation);
    }
    
    /**
     * 
     * @param randX
     * @param randY 
     */
    private void computePayoff(int randX, int randY){
         boolean leftNeighbor, rightNeighbor, upNeighbor, downNeighbor;
        
         //gets strategies of neighbors, taking boundaries into account
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
        
        //compute the pay off from each encounter
        aPopulation[randX][randY].getSum(leftNeighbor, rightNeighbor, upNeighbor,
                downNeighbor);
    }
    
    /**
     * Compares node's total utility against a random neighbor.
     * If random neighbor's performance is better, copies model's strategy
     * and uses it against the model.
     * @param randX
     * @param randY 
     */
    private void compareAndUpdate(int randX, int randY){
        
        int pick = rand.nextInt(4);
        int next = 0;
        boolean newStrategy;
        
        //pick rnadom neighbor ot compare to
        switch(pick){
            case(0): if(randY==MIN)next=MAX; else next=randY-1; break;
            case(1): if(randY==MAX)next=MIN; else next=randY+1; break;
            case(2): if(randX==MIN)next=MAX; else next=randX-1; break;
            case(3): if(randX==MAX)next=MIN; else next=randX+1;break;
        }
        
         if(pick < 2){ //horizontal axis neighbors
            computePayoff(randX, next); //compute payoff for node
            if(aPopulation[randX][randY].getFitness() < aPopulation[randX][next].getFitness()){
                
                if(pick == 0){ // pick left neighbor
                    newStrategy = aPopulation[randX][next].getStrategyRight();
                    aPopulation[randX][randY].setStrategyLeft(newStrategy);
                }
                if(pick == 1){ // pick right neighbor
                    newStrategy = aPopulation[randX][next].getStrategyLeft();
                    aPopulation[randX][randY].setStrategyRight(newStrategy);
                }
            }
        } else { //vertical neighbors
            computePayoff(next,randY);
            if(aPopulation[randX][randY].getFitness() < aPopulation[next][randY].getFitness()){
                
                if(pick == 2){ //copy neighbor below
                    newStrategy = aPopulation[randX][next].getStrategyUp();
                    aPopulation[randX][randY].setStrategyDown(newStrategy);
                }
                
                if(pick == 3){ // copy neighbor above
                    newStrategy = aPopulation[randX][next].getStrategyDown();
                    aPopulation[randX][randY].setStrategyUp(newStrategy);
                }
            }
        }
    }
}
