package Game_Models;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import java.util.Random;

/**
 * Nodes with multiple strategies
 * High cognition, aware of neighbor performance
 * in a 2 dimensional structured population
 * @author D.B. Dresslre(eeu436@bangor.ac.uk)
 */
public class MultiStrategy_2DStructured {
    
    Random rand;
    Node[][] aPopulation;
    Node n;
    int randX, randY;
    final int MAX, MIN; 
    
    
    public MultiStrategy_2DStructured(){
        
        rand = new Random();
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation);
        MAX = aPopulation.length-1; 
        MIN = 0;    
    }
   
    /**
     * Runs the experiment with constant values
     */
    public void doExperiment(){
        int sampleSize = ExpConstants.GAME_ROUNDS;
        for(int i = 0; i < sampleSize; i++){
            System.out.println("Ite:" + i);
            randX = rand.nextInt(aPopulation.length);
            randY = rand.nextInt(aPopulation.length);
            System.out.println("Selected:" + randX + " " + randY);
            computePayoff(randX, randY);
            compareAndUpdate(randX, randY);
            UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
            System.out.println("Coops:" + UtilityMethods.get2DCoopCountMulti(aPopulation));
            System.out.println("pC" + 
                    UtilityMethods.fractionOfCooperationMulti2D(aPopulation));
        }  
    }
    
    /**
     * Runs the experiment with custom values 
     * @param customCost cost supplied
     * @return final ratio of cooperation
     */
    public double doExperiment(double customCost){
        int sampleSize = ExpConstants.GAME_ROUNDS;
        aPopulation = UtilityMethods.setup2DPopulation(aPopulation, customCost);
        
        for(int i = 0; i < sampleSize; i++){
            randX = rand.nextInt(aPopulation.length);
            randY = rand.nextInt(aPopulation.length);
            computePayoff(randX, randY);
            compareAndUpdate(randX, randY);
        }
        //System.out.println(UtilityMethods.fractionOfCooperationMulti2D(aPopulation));
        return UtilityMethods.fractionOfCooperationMulti2D(aPopulation);
    }
    
    /**
     * 
     * @param i
     * @param j
     * @return the payoff
     */
    private double computePayoff(int i, int j){
        
        boolean leftNeighbor, rightNeighbor, upNeighbor, downNeighbor;
        
        if(i == MAX){ 
            upNeighbor = aPopulation[MIN][j].getStrategyDown();    
        }else{
            upNeighbor = aPopulation[i+1][j].getStrategyDown();
        }
        
        if(i == MIN){
            downNeighbor = aPopulation[MAX][j].getStrategyUp();
        } else {
            downNeighbor = aPopulation[i-1][j].getStrategyUp();
        }
        
        if(j == MAX){
            rightNeighbor = aPopulation[i][MIN].getStrategyLeft();
        } else {
            rightNeighbor = aPopulation[i][j+1].getStrategyLeft();
        }
        
        if(j == MIN){
            leftNeighbor = aPopulation[i][MAX].getStrategyRight();
        } else {
            leftNeighbor = aPopulation[i][j-1].getStrategyRight();
        }
        double sum = aPopulation[i][j].getSum(leftNeighbor, rightNeighbor, upNeighbor,
                downNeighbor);
        return sum;
    }
    
    /**
     * Compares nodes total utility to that of a random neighbor.
     * Uses neighbor as model, if neighbor's utility is greater than the cardinal
     * node's. 
     * @param i
     * @param j 
     */
    private void compareAndUpdate(int i, int j){
     
        int pick = rand.nextInt(4);
        int next = 0;
        boolean neighborStrategy = false;
        
        //pick a random neighbor
        //takes 2D boundaries into account.
        switch(pick){
            case(0): if(j==MIN)next=MAX; else next=j-1; break; //left
            case(1): if(j==MAX)next=MIN; else next=j+1; break; //right
            case(2): if(i==MIN)next=MAX; else next=i-1; break; //down
            case(3): if(i==MAX)next=MIN; else next=i+1; break; //up
        }
        //System.out.println("Selected neighbor " + neighbor);
        if(pick < 2){ //compare - update
            computePayoff(i, next); //compute payoff for node
            if(aPopulation[i][j].getFitness() < aPopulation[i][next].getFitness()){
                
                if(pick == 0){
                    neighborStrategy = aPopulation[i][next].getStrategyRight();
                }
                if(pick == 1){
                    neighborStrategy = aPopulation[i][next].getStrategyLeft();
                }
                //System.out.println("changing strategy for neighbor");
                //changeStrategy(i,j,neighborStrategy); 
                aPopulation[i][j].changeLowestStrategy(neighborStrategy);
                
            }
        
        } else {
            computePayoff(next,j); //the get utility method isn't doing its job
            if(aPopulation[i][j].getFitness() < aPopulation[next][j].getFitness()){
                
                if(pick == 2){
                    neighborStrategy = aPopulation[i][next].getStrategyUp();
                }
                
                if(pick == 3){
                    neighborStrategy = aPopulation[i][next].getStrategyDown();
                }
                //System.out.println("changing strategy for neighbor");
                //changeStrategy(i,j, neighborStrategy);
                aPopulation[i][j].changeLowestStrategy(neighborStrategy);
            }
        }
    }
     
    
    /**
     * Comments would be nice
     * @param i
     * @param j
     * @param newStrategy 
     */
    private void changeStrategy(int i,int j,boolean newStrategy){
     
        int leftJ, rightJ, downI, upI;
        double left, right, bottom, top;
       
        //assign appropriate neighbor locations
        if(i == MAX){ upI = MIN; } else { upI = ++i; }
        if(i == MIN){ downI = MAX; } else { downI = --i; } 
        if(j == MAX){ rightJ = MIN; } else { rightJ = ++j; }
        if(j == MIN){ leftJ = MAX; } else { leftJ = --j; }
       
        //compute individual payoffs
        left = computePayoff(i, leftJ); //left
        right = computePayoff(i, rightJ); //right
        bottom = computePayoff(downI, j); //down
        top = computePayoff(upI, j); //up
       
      
        //get location of lowest value and  change strategy for this player
        //check for repeats
        boolean repeats = false;
        int locationOfLowest = 0;
        double values[] = {left, right, bottom, top};
        double lowest = values[0];
        for(int ite = 0; ite < values.length; ite++){
           
            if(values[ite] < lowest){
                locationOfLowest = ite;
            }
        }
        System.out.println("Lowest neighbor" + locationOfLowest);
        switch(locationOfLowest){
            case(0): aPopulation[i][j].setStrategyLeft(newStrategy); break;
            case(1): aPopulation[i][j].setStrategyRight(newStrategy); break;
            case(2): aPopulation[i][j].setStrategyDown(newStrategy); break;
            case(3): aPopulation[i][j].setStrategyUp(newStrategy); break;
        }
    }
} //End of class
