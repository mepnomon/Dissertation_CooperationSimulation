/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analysis;


import Utility.ExpConstants;
import Utility.Notifications;
import Game_Models.Node;
import static Utility.UtilityMethods.setup1DPopulation;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import static Utility.ExpConstants.GAME_ROUNDS;

/**
 * Simulation:
 * Network: 1 Dimensional Circular Lattice.
 * Node: uses 2 strategies, each linked to a neighbor
 * A node is selected and it plays a Public Goods Game with its neighbors
 * to compute its payoff. It compares its payoff with a random neighbor and 
 * copies this neighbor's strategy if it performs better.
 * It uses the copied strategy against the whose performance is the poorest in
 * the group.
 * 
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class RingStructured_TwoStrategies {
      
    //Global variables
    Random generator;
    
    //file writers
    FileWriter coopRatioWriter, calcOutputWriter, cb_ratioWriter;;
    
    boolean displayData = false; //true if user wants to display data
    
    
    boolean strategy, strategyLeftNeighbor, strategyRightNeighbor;
    double sumRightNeighbor, sumLeftNeighbor;
    static Node aNode;  //a node
    Node[] aPopulation; //a population of nodes
    int[] nodeOrder; // order nodes are being visited
    final int size = 10;
    final int sampleSize = GAME_ROUNDS;
    int leftNeighbor, rightNeighbor; // globals
    
    
    
    public RingStructured_TwoStrategies(){
        
        //aPopulation = new Node[size];
        //nodeOrder = ExperimentConstants.TEST_INDICES;
        generator = new Random();
        aPopulation = setup1DPopulation(aPopulation);
        //for debug population
//        for(int i = 0; i < size; i++){
//            Node n;
//            aPopulation[i] = new Node(i, ExperimentConstants.TEST_STRATEGIES[i]);
//            //aPopulation = UtilityMethods.
//            //aPopulation[i] = new Node(i, ExperimentConstants.TEST_STRATEGIES_TWO[i]);
//        }
        try{
            coopRatioWriter = new FileWriter("two_strategies_ratio.csv");
            //calcOutputWriter = new FileWriter("RingSimultaneousCoopFraction.csv");
            //cb_ratioWriter = new FileWriter("RingSimultaneousCB.csv");
        }catch(IOException ex){        
            System.out.println(Notifications.ERR_FILENOTCREATED);
        }
    }
    
    /**
     * performs the experiment
     * @throws IOException 
     */
    public void doExperiment() throws IOException{
        boolean rightStrat, leftStrat;
        int location, neighborLoc;
        Node cardinalNode;
        printLattice();
        for(int i = 0; i < sampleSize; i++){
            
            
            //cardinalNode = aPopulation[nodeOrder[i]];
            location = generator.nextInt(aPopulation.length-1);
            //location = nodeOrder[i];
            //cardinalNode = aPopulation[location];
            //System.out.println("ite:" + i);
            //System.out.println("Select: " + nodeOrder[i]);
            //System.out.println("Select:" + location);
            //location = nodeOrder[i];
            
            double cardinalSum = getSum(location);
            getNeighborSums(location);
            //pick a random neighbor
            float rand = generator.nextFloat();
            boolean selectedStrategy;
            //right neighbor
            if(rand > 0.5){
                //System.out.println("Right neighbor");
                
                if(sumRightNeighbor > cardinalSum){
                    selectedStrategy = strategyRightNeighbor;
                    swapStrategies(location, selectedStrategy);
                }
                
            //left neighbor
            } else {
                //System.out.println("Left neighbor");
                
                if(sumLeftNeighbor > cardinalSum){
                    selectedStrategy = strategyLeftNeighbor;
                    swapStrategies(location, selectedStrategy);
                }
            }
        }   
        printLattice();
        System.out.println("Coop ratio" + getCoopRatio());
        coopRatioWriter.append(Double.toString(getCoopRatio()));
        coopRatioWriter.close();
//        /System.out.println("Coop ratio" + getCoopRatio());
    }
    
    public void swapStrategies(int location, boolean selectedStrategy){
        
        //this is for the other approach, where the strategy is changed for the loser
        //contributed to the node's own cumulative payoff
        //aPopulation[location].changeSecondaryStrategy(selectedStrategy);
        if(sumLeftNeighbor > sumRightNeighbor){
                aPopulation[location].setStrategyRight(selectedStrategy);
        }
            
        if(sumRightNeighbor > sumLeftNeighbor){
            aPopulation[location].setStrategyLeft(selectedStrategy);
               
        }
    }
    
    /**
     * Picks the appropriate neighbors and returns the sum.
     * @param i
     * @return 
     */
    public double getSum(int i){
        
        boolean rightStrat, leftStrat;
         
        //gets the neighbor's respective strategies
        
        
        //boundary cases
        if(i == aPopulation.length-1){
            rightStrat = aPopulation[0].getStrategyLeft();
        } else {
            rightStrat = aPopulation[i+1].getStrategyLeft();
        }
        //leftNeighbor at 0
        if(i == 0){
            leftStrat = aPopulation[aPopulation.length-1].getStrategyRight();
        } else {
            leftStrat = aPopulation[i-1].getStrategyRight();    
        }
        return aPopulation[i].getSum(leftStrat, rightStrat);
    }
    
    /**
     * Calculates how well each neighbor is doing
     * @param i the location of the cardinal node
     */
    public void getNeighborSums(int i){
     
        int locLeftNeighbor, locRightNeighbor;
       
        //left neighbor
        if(i == 0){
            locLeftNeighbor = aPopulation.length-1;
        } else {
            locLeftNeighbor = i-1;
        }
        strategyLeftNeighbor = aPopulation[locLeftNeighbor].getStrategyRight();
        //calculate cumulative payoff
        sumLeftNeighbor = getSum(locLeftNeighbor);
       //right neighbor
        if(i == aPopulation.length-1){
            locRightNeighbor = 0;
               
        } else {
                    
            locRightNeighbor = i+1;
        }
        strategyRightNeighbor = aPopulation[locRightNeighbor].getStrategyLeft();
        //calculate cumulative payoff
        sumRightNeighbor = getSum(locRightNeighbor);
        
    }
    
    /**
    *  Formats and shows population.
    */
    private void printLattice(){
        
        char status1, status2;
        System.out.print("[ ");
        for(Node n : aPopulation){
            status1 = 'd';
            status2 = 'd';
            
            if(n.getStrategyLeft()){
                status1 = 'c';
            }
            if(n.getStrategyRight()){
                status2 = 'c';
            }
            System.out.print("[" + status1 + status2 +"] " );
        }
        System.out.println(" ]");
    }
    
    /**
     * 
     * @return 
     */
    private double getCoopRatio(){
   
        int offset = 2;
        double cooperators = 0;
        
        //iterates through all nodes and increments counter if cooperator
        for (Node n : aPopulation) {
            if (n.getStrategyLeft()) {
                ++cooperators;
            }
            if (n.getStrategyRight()) {
                ++cooperators;
            }
        }
        //divides the number of cooperators by the size of the population
       cooperators = cooperators/ExpConstants.POPULATION_SIZE;
       //divided by offset, because each node has 2 strategies
       cooperators /= offset;
       
       return cooperators;
    }
}
        
   
