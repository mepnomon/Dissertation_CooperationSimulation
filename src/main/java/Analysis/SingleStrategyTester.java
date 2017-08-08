package Analysis;

import Utility.UtilityMethods;
import Utility.ExpConstants;
import Game_Models.Node;
import java.util.Random;

/**
 *
 * @author Mepnomon
 */
public class SingleStrategyTester {
    
    Random rand;
    final double GROUP_SIZE = 2;        //Size of a game group
    final double INTERACTION_REWARD; //Reward constant
    final double INTERACTION_COST;   //Cost constant
    
    static Node aNode;  //a node
    Node[] aPopulation; //a population of nodes
    int[] nodeOrder; // order nodes are being visited
    final int size = 10;
    final int sampleSize = 100;
    
    /**
     * 
     */
    public SingleStrategyTester(){
        
        INTERACTION_REWARD = ExpConstants.REWARD;
        INTERACTION_COST = ExpConstants.COST;
        aPopulation = new Node[size];
        nodeOrder = ExpConstants.TEST_INDICES;
        rand = new Random();
        for(int i = 0; i < size; i++){
            Node n;
            aPopulation[i] = new Node(i, ExpConstants.TEST_STRATEGIES[i]);
        }
    }
    
    /**
     * 
     */
    public void doExperiment(){
        
        UtilityMethods.print1DPopulation(aPopulation);
        for(int i = 0; i < sampleSize; i++){
            
            
            int selectedNode = nodeOrder[i];
            System.out.println("\nIte: " + i);
            System.out.println("Select: " + nodeOrder[i]);
            computePayOff(selectedNode);
            compareAndUpdate(selectedNode);
            System.out.println("");
            UtilityMethods.print1DPopulation(aPopulation);
        }
    }
    
    /**
     * Computes and stores sum of pay off groups.
     * @param loc location of the node
     */
    public void computePayOff(int loc){  
        double p_offLeft, p_offRight;
        int leftNeighbor, rightNeighbor;
        
        
        //System.out.println("plength"+population.length);
        if(loc == aPopulation.length-1){
            leftNeighbor = loc-1; rightNeighbor = 0;  
        } else if (loc == 0){
            leftNeighbor = aPopulation.length-1; rightNeighbor = 1;
        } else {
            leftNeighbor = loc-1; rightNeighbor = loc+1;
        }
        
        //debug
        //System.out.print("\t" + leftNeighbor);
        //System.out.print("\t" + rightNeighbor);
        
        p_offLeft = payOffFormula(loc, leftNeighbor);
        System.out.println("Left: " + p_offLeft);
        p_offRight = payOffFormula(loc, rightNeighbor);
        System.out.println("Right: " + p_offRight);
        
        //population[loc].setPayOff(payOff1 + payOff2);
        double p_offTotal = p_offLeft + p_offRight;
        System.out.println("Total " + p_offTotal);
        aPopulation[loc].setFitness(p_offTotal);
    }
   
    /**
     * The formula to compute the payoff
     * @param loc
     * @param neighbor
     * @return 
     */
    private double payOffFormula(int loc, int neighbor){ 
        int coopNeighbors = 0;
        if(aPopulation[loc].getCoopStatus()){
            ++coopNeighbors;
        }
        if(aPopulation[neighbor].getCoopStatus()){
            ++coopNeighbors;
        }
        //so(coopNeighbors);
        //System.out.println(coopNeighbors);
        double payOff = (double)(ExpConstants.REWARD * coopNeighbors) / GROUP_SIZE;
        //System.out.println(payOff);
        
        if(aPopulation[loc].getCoopStatus()){
            payOff -= ExpConstants.COST;
        }
        //System.out.println(payOff);
        return payOff;
    }
    
    /**
     * Compares with random neighbor
     * Updates coop status if applicable
     * @param loc 
     */
    public void compareAndUpdate(int loc){
        
        //double rng = rand.nextDouble();
        double rng = 0.7; //always choose right
        //System.out.println(rand);
        int neighbor = 0;
        final int leftmostNode = 0;
        final int rightmostNode = aPopulation.length-1;
        
        //could lock this to one neighbor
        if(rng < 0.5){
            
            System.out.println("Left");
            neighbor = loc-1;
            if(neighbor < leftmostNode){
                neighbor = rightmostNode;
            }
            
        } else {
            
            System.out.print("right: ");
            if(loc == rightmostNode){
               neighbor = leftmostNode;
            } else {
                neighbor = loc+1;
            }
        } 
        computePayOff(neighbor);
        
        System.out.println("utility: " + aPopulation[neighbor].getFitness());
        if(aPopulation[loc].getFitness() < aPopulation[neighbor].getFitness()){
            aPopulation[loc].setCoopStatus(aPopulation[neighbor].getCoopStatus());
            System.out.println("Changing strategy.");
        }
    }
    
}
