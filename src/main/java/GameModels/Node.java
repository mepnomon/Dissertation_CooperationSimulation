package GameModels;

import static Utility.ExpConstants.COST;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.POPULATION_SIZE;
import static Utility.ExpConstants.REWARD;
import java.util.ArrayList;
import static Utility.ExpConstants.DEFECTION_P;
import java.util.Arrays;
import java.util.Random;

/**
 * Cooperation Simulator
 * A class for a Node
 * Created:  11-Aug-2016
 * Updated:  20-JAN-2017
 * https://github.com/mepnomon/Dissertation-CooperationSimulator
 * @author D.B. Dressler (eeu436@bangor.ac.uk)
 */
public class Node {
    
    //global variables
    private double fitness, contributionL, contributionR, contributionUp, 
            contributionDown;
    private boolean aStrategy, rightStrategy, upStrategy, downStrategy;
    private final int nodeID, GROUP_SIZE = 2;
    boolean customCostUsed = false;
    double customCost;
    //stores strategies used against opponents
    private boolean strategies[];
    
    private int groups[];
    

    //used in PDWELLMIXED
    //stores the pairwise payoffs
    private double[] pairwisePayoffs;
    
    //stores the payoffs from each group
    private double[] groupPayoffs;
    
    Random generator;
    
    
    
    /**
     * Constructor for a node
     * @param id location in Array
     * @param aStrategy random value assigned at creation
     */
    public Node(int id, boolean aStrategy){
        
        nodeID = id;
        this.aStrategy = aStrategy; //left strategy
        rightStrategy = aStrategy;
        upStrategy = aStrategy;
        downStrategy =aStrategy;
        
        generator = new Random();
        //initialize the pairwise payoffs
        pairwisePayoffs = new double[(int)POPULATION_SIZE];
    }
     
    /**
     * Constructs a node with a custom set cost
     * @param id the location in the array
     * @param aStrategy random boolean assigned at creation
     * @param customCost the user specified cost
     */
    public Node(int id, boolean aStrategy, double customCost){
        
        nodeID = id;
        this.aStrategy = aStrategy; //left strategy
        rightStrategy = aStrategy;
        upStrategy = aStrategy;
        downStrategy = aStrategy;
        customCostUsed = true; //did user specify a cost?
        this.customCost = customCost; //the user specified cost
    }
    
    //--------------------------------------------------
    //*             MUTATOR METHODS
    //--------------------------------------------------
    
    /**
     * Sets up the strategy list to be divided 
     * 
     * STILL NEEDS TO BE SHUFFLED
     * 
     * 
     * into n evenly sized groups. 
     * @param nodesInGroup
     */
    public void setupStrategyGroup(int nodesInGroup){
                                                                                //System.out.println("Setup Strategy Group - nodes in group" + nodesInGroup);
        //System.out.println("Node ID: " + nodeID);
        strategies = new boolean[(int)POPULATION_SIZE];
        groups = new int[(int)POPULATION_SIZE];
	int groupOffset = nodesInGroup; //actual numbers of nodes in array
        //1 IS A SPECIAL CASE
        
	int nodeCount = 0; //count nodes in group
	int groupDesignator = 0; //used to assign group
	
	//setup strategy group values
	for(int i = 0; i < strategies.length;i++){
		
            //ignore the node itself
            if(i != nodeID){
                //is group full?
		if(nodeCount < nodesInGroup){
                    groups[i] = groupDesignator;
                    ++nodeCount;
                } 
                
                if(nodeCount == groupOffset){ //reset values
                    ++groupDesignator;
                    nodeCount = 0;
                }
            }
	}
	
	//set up 50/50 true-false
	boolean groupStrategies[] = new boolean[(int)POPULATION_SIZE];
	
	for(int i = 0; i < groupStrategies.length; i++){
            if(i < ((groupStrategies.length/2))){
		groupStrategies[i] = false;
            } else {
		groupStrategies[i] = true;
            }
	}

	//fisher yates shuffle
	int n = groupStrategies.length;
	boolean randVal;
	for(int i = 0; i < groupStrategies.length; i++){
            int rand = i + (int)(Math.random()*(n-i));
            randVal = groupStrategies[rand];
            groupStrategies[rand] = groupStrategies[i];
            groupStrategies[i] = randVal;
	}
        
        strategies = groupStrategies;
        Arrays.toString(groups);
//        for(int i = 0; i < strategies.length; i++){
//            
//            System.out.println("Player " + i + " is in group " + groups[i] + " uses strategy " 
//            +Boolean.toString(strategies[i]));
//        }
        
        //lastly change offset, so half group left, half group right
    }
    
    
    /**
     * Sets up the strategy list to be 
     * divided into 2 groups, with node index being the center point.
     * c < index < d 
     */
    public void setupStrategyGroup(){
        //nodeIndex
        strategies = new boolean[(int)POPULATION_SIZE];
        groups = new int[(int)POPULATION_SIZE];
        //System.out.println("NodeID" + nodeID);
        
        //get group boundary from offset position
        int coopLowBound = nodeID - strategies.length/2;
        
        if(coopLowBound < 0){
            coopLowBound = strategies.length + coopLowBound;
        }
        
        double direction = Math.random();
        //System.out.println("C Low" + coopLowBound); 
        for(int i = 0; i < strategies.length; i++){
            
            if(direction < 0.5){
                if(i < strategies.length/2){
                    strategies[i] = true;
                } else {
                    strategies[i] = false;
                }
            } else {
                if(i < strategies.length/2){
                    strategies[i] = false;
                } else {
                    strategies[i] = true;
                }
            }
        }
        
        int shift;
        if(coopLowBound < nodeID){
            //shift left
            //System.out.println("shifting left");
            shift = strategies.length/2 - coopLowBound;
            //System.out.println("shift by" + shift);
            boolean tempArr[] = new boolean[shift];
            for(int i = 0; i < shift; i++){
                tempArr[i] = strategies[i];
            }
            System.arraycopy(strategies, shift, strategies, 0, 
                    strategies.length - shift);
            for(int i = strategies.length - shift; i < strategies.length; i++){
                strategies[i] = tempArr[shift+i - strategies.length];
            }
        } else {
            //shift right
            //System.out.println("shifting right");
            shift = coopLowBound-strategies.length/2; //this isnt right either
            //System.out.println("shift r" + shift);
            boolean tempArr[] = new boolean[shift];
            for(int i = 0; i < shift; i++){
                tempArr[i] = strategies[strategies.length - shift + i];
                //System.out.println(Arrays.toString(tempArr));
            }
            System.arraycopy(strategies, 0, strategies, shift, 
                    strategies.length - shift);
            for(int i = 0; i < shift; i++){
                strategies[i] = tempArr[i];
            }
        }
        
        //randomize groups
        int gr1 = 0, gr2 = 1;
        if(Math.random() < 0.5){
            gr1 = 1;
            gr2 = 0;
        }
        //populate group array
        for(int i = 0; i < strategies.length; i++){
            if(strategies[i]){
                groups[i] = gr1; 
           } else {
                groups[i] = gr2;
            }
        }
    }
    
    /**
     * Resets the group pay offs.
     * @param numberOfGroups
     */
    public void resetGroupPayoffs(int numberOfGroups){
        groupPayoffs = new double[numberOfGroups];
    }
    
    //new stuff
    /**
     * Stores the group contributions to a player's cumulative payoff.
     * @param groupID the selected group
     * @param payoff the payoff added to it
     */
    public void setGroupPayoffs(int groupID, double payoff){
       
       //accumulates payoffs in appropriate group
       groupPayoffs[groupID] += payoff; 
        
    }
    
    /**
     * Finds the weakest groups contributing to the caller's
     * cumulative payoff
     * @param nodesInGroup
     * @return the group id of one of the weakest groups
     */
    public int getWeakestGroup(int nodesInGroup){
        
                                                                                //System.out.println("--------------------\nWEAKEST GROUP CALLED\n----------------");
        //define the maximum payoff possible from a group
        final double MAX = nodesInGroup * DEFECTION_P;
        double min = MAX;
        ArrayList<Integer> lowestPayoffs = new ArrayList<>();
        //find lowest payoff
        for(int i = 0; i < groupPayoffs.length; i++){
            
            //if group payoff is < min
            if(groupPayoffs[i] < min){
                //set new min
                min = groupPayoffs[i];
                
                //if group payoff is the lowest possible
                if(groupPayoffs[i] == 0){
                    //end loop
                    i = groupPayoffs.length;
                }
            }
        }
        
        //add lowest to list
        for(int i = 0; i < groupPayoffs.length; i++){
            
            //if pay off is lowest
            if(groupPayoffs[i] == min){
                
                //store group number
                lowestPayoffs.add(i);
            }
        }
        
                                                                                //System.out.println("Lowest group contains:");
                                                                                //        int count = 0;
                                                                                //        for(int i : lowestPayoffs){
                                                                                //            System.out.println(count + ": " + " Group: " + i );
                                                                                //            count++;
                                                                                //        }
        //return a group id, a random one will be chosen if 
        //more than one group is a weak contributor
        int returnGroup =  lowestPayoffs.get(generator.nextInt(lowestPayoffs.size()));
                                                                                //System.out.println("returning group:" + returnGroup);
        return returnGroup;
    }
    
    /**
     * Changes strategies in a group
     * @param strategy the group is changed to
     * @param groupID the group id
     */
    public void setStrategyForGroup(boolean strategy, int groupID){
        
        for(int i = 0; i < strategies.length; i++){
            if(groups[i] == groupID){//if member of selected group
                strategies[i] = strategy; //change strategy used for it
            }
        }
    }
    
    /**
     * Sets up the Array with 50/50 strategies
     * initializing left of cardinal to C and right 
     * of cardinal to D. 
     */
    public void setupStrategyArray(){
            
        strategies = new boolean[(int)POPULATION_SIZE];
        
        double halfOfPopulation = (double)strategies.length/2;
        
        //populate array with 50/50 c,d ratio
        for(int i = 0; i < strategies.length; i++){
            
            if(i < halfOfPopulation){
                strategies[i] = false;
            } else {
                strategies[i] = true;
            }
        }
        
        //Fisher Yates shuffle
        int n = strategies.length;
        boolean randBool;
        for(int i = 0; i < strategies.length; i++){
            int rand = i + (int)(Math.random()*(n-i));
            randBool = strategies[rand];
            strategies[rand] = strategies[i];
            strategies[i] = randBool;
        }
        
//        System.out.println(Arrays.toString(strategies));
    }
    
    /**
     * CURRENTLY IN USE FOR 2D Game
     * Sets the individual contributions
     * @param left
     * @param right
     * @param up
     * @param down 
     */
    public void setContributions(double left, double right, double up, double down){
        contributionL = left;
        contributionR = right;
        contributionUp = up;
        contributionDown = down;
    }
    
    /**
     * Returns the weakest the location of the node
     * @return 
     */
    public int getWeakestContributor(){
        int weakest;
        //stores the minimum value encountered
        double MAX = DEFECTION_P;
        double min = MAX;
        double contributions[] = {contributionL, contributionR, contributionUp, contributionDown};
        for(int i = 0; i < contributions.length;i++){
            //test if contributions is less than min
            if(contributions[i] < min){
                min = contributions[i];
            }
            
            //if min is minimum
            if(min == 0){
                //end loop
                i = contributions.length;
            }
        }
        //store weakest contributor addresses
        ArrayList<Integer> lowestContributors = new ArrayList<>();
        for(int i = 0; i < contributions.length; i++){
            if(contributions[i] == min){
                lowestContributors.add(i); 
            }
        }
        
        //selects a random player if more than one are weakeast
        weakest = generator.nextInt(lowestContributors.size());
        //return the location of the weakest player
        return lowestContributors.get(weakest);
    }
    
    /**
     * Mutates strategy in strategy list.
     * @param nID   the ID of the node to be changed
     * @param strategy  the value to change the node for
     */
    public void setStrategyListEntry(int nID, boolean strategy){
        strategies[nID] = strategy;
    }
    
    /**
     * Mutates strategy in single strategy node
     * @param aStrategy
     */
    public void setCoopStatus(Boolean aStrategy){
        this.aStrategy = aStrategy;
    }
    
    /**
     * Stores the pay off, of an individual node.
     * Used if payoff calculated outside of Node
     * @param fitness
     */
    public void setFitness(double fitness){
        this.fitness = fitness;
    }
    
    /**
     * Sets a node's strategy for left neighbor
     * @param aStrategy 
     */
    public void setStrategyLeft(boolean aStrategy){ 
        this.aStrategy = aStrategy;
    }
    
    /**
     * Sets a node's strategy for right neighbor
     * @param aStrategy 
     */
    public void setStrategyRight(boolean aStrategy){
        rightStrategy = aStrategy;
    }
    
    /**
     * Sets a node's strategy for neighbor above
     * @param aStrategy 
     */
    public void setStrategyUp(boolean aStrategy){
        upStrategy = aStrategy;
    }
    
    /**
     * Sets a node's strategy for neighbor below
     * @param aStrategy
     */
    public void setStrategyDown(boolean aStrategy){
        downStrategy = aStrategy;
    }
    
    /**
     * MOVE TO RING BECAUSE ONLY USED THERE?
     * determines the lowest contributor and changes strategy for it
     * @param newStrategy the more successful neighbor's strategy
     */
    public void changeSecondaryStrategy(boolean newStrategy){
        
        //System.out.println("Contr L" + contributionL);
        //System.out.println("Contr R" + contributionR);
        if(contributionL < contributionR){
            this.aStrategy = newStrategy;
            //System.out.println("Changing left strategy");
            
        }
        
        if(contributionR < contributionL){
            rightStrategy = newStrategy;
            //System.out.println("changing right strategy");
        }
    }
    
    /**
     * Changes strategy for the lowest contributor to the node.
     * Used in 2D
     * @param newStrategy
     */
    public void changeLowestStrategy(boolean newStrategy){
        
        //store contributions in data structure
        double values[] = {contributionL, contributionR, contributionUp, 
            contributionDown};
        //assign minimum value
        double minValue = values[0];
        double maxValue = 0;
        boolean noLowestFound = false;
        int minLocation = 0;;
        int maxLocation;
        //this needs to be improved
        //get the strategy that gave you the highest payoff
     
        //find lowest contribution for replacement
        for(int i = 0; i < values.length; i++){
            
            if(values[i] < minValue){
                //store location of minimum value
                minLocation = i;
            }
        }
        
        //check that not all values are the same
        if(values[0] == values[1] && values[1] == values[2] 
                && values[2] == values[3]){
            //if all the same, no lowest value exists
            noLowestFound=true;
        }
        //System.out.println("Lowest contr:" + minLocation);
        
        //if a lowest value exists
        if(!noLowestFound){
            //change strategy w.r.t. this value.
            switch(minLocation){
                case(0): setStrategyLeft(newStrategy); break;
                case(1): setStrategyRight(newStrategy); break;
                case(2): setStrategyUp(newStrategy); break;
                case(3): setStrategyDown(newStrategy); break;
            }
        }
    }
    
    //--------------------------------------------------
    //*             ACCESSOR METHODS
    //--------------------------------------------------
    
    
    /**
     * Finds the strategy used for a group.
     * By convention left of n is 0
     * Right of n is 1
     * @param groupID
     * @return 
     */
    public boolean getStrategyForGroup(int groupID){
        
        int i = 0;
        boolean strategy = false;
        while(i < strategies.length){
            
            ///i != nodeID added
            if(groups[i] == groupID && i != nodeID){
                strategy = getStrategyForOpponent(i);
                break;
            }
            i++;
        }
        return strategy;
    }
    
    /**
     * Gets the group a node belongs to.
     * @param opponent the node's position
     * @return the group id
     */
    public int getGroupFor(int opponent){
        return groups[opponent];
    }
    
    /**
     * Prints the strategy group values for 
     * a node.
     */
    public void getStrategyGroup(){
       
        System.out.print("[");
        for(int i = 0; i < strategies.length; i++){
            
            if(i < strategies.length){
                if(strategies[i] == true){
                    System.out.print(" c,");
                } else {
                    System.out.print(" d,");
                }
            }
        }
         System.out.print("]");
    }
    
    /**
     * Accesses the strategy for an opponent.
     * @param opponent   the opponent's id
     * @return      the strategy
     */
    public boolean getStrategyForOpponent(int opponent){
 
        return strategies[opponent];

    }
    
    /**
     * Accesses a node's utility.
     * @return the node's utility
     */
    public double getFitness(){
        return fitness;
    }
    
    /**
     * Access main cooperation strategy
     * @return a strategy
     */
    public boolean getCoopStatus(){
        return this.aStrategy;
    }
    
    /**
     * Access Strategy for left neighbor.
     * @return a strategy
     */
    public boolean getStrategyLeft(){
        return this.aStrategy;
    }
    
    /**
     * Access strategy for right neighbor
     * @return a strategy
     */
    public boolean getStrategyRight(){
        return rightStrategy;
    }
    
    /**
     * Access strategy associated with neighbor above
     * @return a strategy
     */
    public boolean getStrategyUp(){
        return upStrategy;
    }
    
    /**
     * Access strategy associated with neighbor below
     * @return a strategy
     */
    public boolean getStrategyDown(){
        return downStrategy;
    }

    //--------------------------------------------------
    //*             NODE METHODS
    //--------------------------------------------------
    
    
    /**
     * Use for game in 1D - Multi strategy!
     * Calculates the sum of a node in a 1D array
     * @param leftNeighbor
     * @param rightNeighbor
     * @return the cumulative payoff for this node
     */
    public double getSum(boolean leftNeighbor, boolean rightNeighbor){
        
        double sum;
        //play game with each neighbor
        contributionL = playPGG(leftNeighbor ,getStrategyLeft());
        contributionR = playPGG(rightNeighbor, getStrategyRight());
        //calculate cumulative payoff
        sum = contributionL + contributionR;
        //store in utilty
        fitness = sum;
        //return cumulative payoff
        return sum;
    }
    
    /**
     * Use for 2D multi strategy!
     * Calculates the sum for a node in 2D Array
     * Parameters are cooperation statuses of respective neighbors.
     * @param cStatusLeft
     * @param cStatusRight 
     * @param cStatusUp
     * @param cStrategyDown
     * @return the cumulative payoff for this node
     */
    public double getSum(boolean cStatusLeft, boolean cStatusRight, 
            boolean cStatusUp, boolean cStrategyDown){
        
        double sum;
        
        //play a game with each neighbor
        contributionL = playPGG(cStatusLeft, getStrategyLeft());
        contributionR = playPGG(cStatusRight, getStrategyRight());
        contributionUp = playPGG(cStatusUp, getStrategyUp());
        contributionDown = playPGG(cStrategyDown, getStrategyDown());
        
        //sum all contributions
        sum = contributionL + contributionR + contributionUp + contributionDown;
        //assign sum to utility for later retrieval
        fitness = sum;
        //return the sum of all contributions
        return sum;
    }
    
    /**
     * Use for well mixed population!
     * Gets the sum of all interactions if
     * well mixed, connected to all.
     * @param opponentStrategies the opponent's strategies
     * @return the sum
     */
    public double getSum(boolean[] opponentStrategies){
        
    //initialize sum
        double sum = 0;
        //traverse strategies
        for(int i = 0; i < opponentStrategies.length; i++){
            //play game with all but self
            if(i != nodeID){
                //sum from public goods game
                sum += playPGG(opponentStrategies[i], strategies[i]);
            }
        }
        return sum;
    }
    
    /**
     * Use for Groups!
     * Calculates the sum of public good games played between cardinal
     * and constituents constituents of the opponent group
     * @param cStrategy the cardinal's strategy
     * @param oppStrategies strategies used by opponents in the group
     * @return the sum of the interactions
     */
    public double getSum(boolean cStrategy, ArrayList<Boolean> oppStrategies){
        double sum = 0;
        int i = 0;
        //iterate over list
        while(i < oppStrategies.size()){
            //play pgg for each encounter
            sum += playPGG(oppStrategies.get(i), cStrategy);
            //increment control var
            i++;
        }
        return sum;
    }
    

    /**
     * Use for groups playing Prisoner's Dilemma!
     * Calculates the sum for nodes playing the
     * Prisoner's Dilemma game.
     * @param cStrategy the focal player's strategy.
     * @param oppStrategies list of opponent strategies in a group.
     * @return the sum of all interactions
     */
    public double getSumPD(boolean cStrategy, ArrayList<Boolean> oppStrategies){
        double sum = 0;
        int i = 0;
        //iterate over list
        while(i < oppStrategies.size()){
            if(i != nodeID){
            //play pd for each encounter
                sum += playPD(oppStrategies.get(i), strategies[i]);
                //incr control var
            }
            i++;
        }
        return sum;
    }
    //------you are here
    /**
     * Plays the Prisoner's Dilemma game.
     * @param opponentStrategies
     * @return 
     */
    public double getCumulativePayoff(boolean[] opponentStrategies){
        
        double pairwisePayoff = 0;
        //System.out.println("called");
        //initialize sum
        double sum = 0;
        //traverse strategies
        for(int i = 0; i < opponentStrategies.length; i++){
            //play game with all but self
            if(i != nodeID){
                //sum from public goods game
                pairwisePayoff = playPD(opponentStrategies[i], strategies[i]);
                sum += pairwisePayoff;
                //add to node's pairwise payoffs
                pairwisePayoffs[i] = pairwisePayoff;
            }
        }
        return sum;
    }
    
    /**
     * 
     * This method is called when an update is to occur.
     * Finds and returns the weakest, or a random 
     * weakest contributor
     * @param misjudge                                                          -- may have to remove this later
     * @return the location of a weakest interaction
     */
    public int getWeakestLink(boolean misjudge){
        
        if(misjudge){
            //misjudgment error, make a mistake if rand <0.1;
            if(generator.nextGaussian() <= 0.1){
                return generator.nextInt(pairwisePayoffs.length);
            }
        }
        final double maxPayoff = DEFECTION_P;
        double minPayoff = maxPayoff;
        //store location of weak contributors
        ArrayList<Integer> weakContributors = new ArrayList<>();
        //traverse array, updating location of the weakest
                                                                                //System.out.println("start search for lowest");
        for(int i = 0; i < pairwisePayoffs.length; i++ ){
            
            //find the lowest contributors                                      //System.out.println(Double.toString(minPayoff));
            if(pairwisePayoffs[i] < minPayoff){
                minPayoff = pairwisePayoffs[i];
                
                //if lowest value found
                if(minPayoff == 0){
                    //end search for lowest
                    i = pairwisePayoffs.length;
                }
            }
        }
                                                                                //System.out.println("min:" + Double.toString(minPayoff));
                                                                                //System.out.println("stop search for lowest");
        //store locations of weakest interaction
        for(int i = 0; i < pairwisePayoffs.length; i++){
            if(pairwisePayoffs[i] == minPayoff){
                weakContributors.add(i);
            }
        }
                                                                                //System.out.println("Number exists");
                                                                                //System.out.println("weak links count:" + weakContributors.size());
        //return a random weak contributor
        int weakestLinkPosition = weakContributors.get(
                generator.nextInt(weakContributors.size()));
        
        return weakestLinkPosition;
    }
    
    /**
     * 
     */
    public int getWeakestLink(){
        int weakestLinkPosition = 0;
        //initialize maximum possible payoff
        final double maxPayoff = DEFECTION_P;
        double minPayoff = maxPayoff;
       
        for(int i = 0; i < pairwisePayoffs.length; i++){
            
        }
        
        return weakestLinkPosition;
    }
    
    
    /**
     * Plays the prisoner's dilemma game.
     * @param neighbor the neighbor's strategy
     * @param cNode the focal node's strategy
     * @return the payoff earned from an interaction.
     */
    public double playPD(boolean neighbor, boolean cNode){
        //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!cNode && neighbor){
            return DEFECTION_P;
        }
        // check C-C encounter
        if(cNode && neighbor){
            return 1.0;
        }
        // check D-D encounter
        if(!cNode && !neighbor){
            return EPSILON;
        }
        //return the payoff
        return payoff;
    }
    
    
    /**
     * Node plays public goods game with each neighbor.
     * @param neighbor the neighbor's status
     * @param cNode the node's status.
     * @return the game payoff
     */
    public double playPGG(boolean neighbor, boolean cNode){
        
        int cooperators = 0; //how many nodes are cooperators (max 2)
        double cost = 0;    //cost is initialized to 0
        
        if(neighbor){ //check if neighbor is cooperator
            ++cooperators; //add a cooperator
        }
        if(cNode){ //check if node is cooperator
            ++cooperators; //add a cooperator
            
            //is a custom cost value used
            if(!customCostUsed){ 
                cost = COST; //use constant value
            } else {
                cost = customCost;//use supplied custom value
            }
        }
        
        //calculate payoff
        double payoff = ((double)cooperators * REWARD) / 
                (double)GROUP_SIZE;
        //subtract cost for node's cooperation
        payoff -= cost;
        //return the payoff
        return payoff;
    }
    
    /**
     * Calculates the average cooperation ratio 
     * in the local strategyList
     * @return an averaged cooperation value
     */
    public double getCoopAvgFromStrategies(){
        
        int coopCount = 0;
        for(int i = 0; i < strategies.length; i++){
            if(i != nodeID && strategies[i]){
                ++coopCount;
            }
        }
        double aValue = Math.abs((double)coopCount/(strategies.length-1));
        //System.out.println(Double.toString(aValue));
        return aValue;
    }
}