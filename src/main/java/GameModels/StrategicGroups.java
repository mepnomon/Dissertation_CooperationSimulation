package GameModels;

//constants:
import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;     //epsilon
import static Utility.ExpConstants.GAME_ROUNDS; //game rounds
import static Utility.ExpConstants.POPULATION_SIZE; //population size
//Simulation Utilitiy:
import static Utility.Notifications.ERR_FILENOTCREATED; //file not created error
import static Utility.UtilityMethods.setup1DPopulation; 
//java io and util library:
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * --THIS IS NOW IN USE--
 * Test class to play group game with all opponents
 * Plays an iterative Prisoner's Dilemma game in a population
 * comprised of groups.
 * 
 * Test if results match PD results.
 * If group size N matches, it's all good and you can proceed
 * @author D.B. Dressler
 */
public class StrategicGroups {
    //globals
    private final int SAMPLES = 100;
    private Node[] aPopulation;
    private final Random GENERATOR;
    private int aFocalPlayer;
    
    /**
     * The constructor
     */
    public StrategicGroups(){
        GENERATOR  = new Random();
    }
    
    /**
     * Runs the experiment
     * @param numberOfGroups
     * @return returns the fraction of cooperation realized in experiment
     */
    public double[] runExperiment(int numberOfGroups){
        
        int  aModelPlayer;
        double aFocalPlayerSum, aModelPlayerSum;
        aPopulation = new Node[(int)POPULATION_SIZE];
        //declare and initialize the average for cooperation
        double[] averages = new double[GAME_ROUNDS];
        //declare and initialize the population structure
        
        //work out the nodes per group from the number of groups
        int numberOfNodesInGroup = (int)(POPULATION_SIZE/(double)numberOfGroups);
                                                                                //System.out.println("Numbers of nodes in group " + numberOfNodesInGroup);
        
        //for the number of smaples
        for(int i = 0; i < SAMPLES; i++){
            //invoke a new population
            aPopulation = setup1DPopulation(aPopulation);
            
            //set up the strategy groups
            for(int j = 0; j < aPopulation.length; j++){
                //this is nodes in group for original
                aPopulation[j].setupStrategyGroup(numberOfNodesInGroup);
            }
            
            //play iterative prisoner's dilemma for rounds of game
            for(int j = 0; j < GAME_ROUNDS; j++){
                
                //pick a player and a model
                aFocalPlayer = GENERATOR.nextInt(aPopulation.length);
                                                                                //System.out.println("Focal selected:" + aFocalPlayer);
                aModelPlayer = getValidOpponent(aFocalPlayer);
                                                                                //System.out.println("Model selected: " + aModelPlayer);
                //reset the group thing
                aPopulation[aFocalPlayer].resetGroupPayoffs(numberOfGroups);
                
                
                //now both need to play the Prisoner's Dilemma with all of the groups
                aFocalPlayerSum = getCumulativePayoff(aFocalPlayer);
                                                                                //System.out.println("Focal player sum:" + aFocalPlayerSum);
                aModelPlayerSum = getCumulativePayoff(aModelPlayer);
                                                                                //System.out.println("Model Player sum:"  + aModelPlayerSum);
                
                //check conditions for strategy update
                if(aFocalPlayerSum < aModelPlayerSum){
                                                                                //System.out.println("Changing strategy"); //works
                   //get the model's strategy used against focal
                   int relativeGroupOfFocal = aPopulation[aModelPlayer].
                           getGroupFor(aFocalPlayer);
                   boolean newStrategy = aPopulation[aModelPlayer].
                           getStrategyForGroup(relativeGroupOfFocal);
                                                                                //System.out.println("Strategy used by opponent: " + newStrategy);
                    //model adopts this strategy against weakest group
                   int replacementGroup = aPopulation[aFocalPlayer].
                           getWeakestGroup(numberOfNodesInGroup);
                                                                                //System.out.println("Focal will replace group: " + replacementGroup);
                   
                   //introduce error for 10% of interactions                                                             
                   if(j % GAME_ROUNDS*0.1==0){
                       replacementGroup = GENERATOR.nextInt(numberOfGroups);
                   }                                                             
                   aPopulation[aFocalPlayer].setStrategyForGroup(
                           newStrategy, replacementGroup);
                } //end if
                averages[j] += getCooperationForTimeStep();
            }//end game rounds
        }//end samples
        
        //calculate averages
        for(int i = 0; i < averages.length; i++){
            averages[i] /= SAMPLES;
        }
        displayStatistics(numberOfGroups, averages);
        System.out.println("Final fC : " + averages[GAME_ROUNDS-1]);
        return averages;
        //return averageCooperation;
    }
    
    /**
     * Displays performance statistics
     */
    private void displayStatistics(int numberOfGroups, 
            double[] averages){
        //local variables
        double totalFitnessAll = 0,
               totalFitnessDefectors = 0,
               totalFitnessCooperators,
               averageFitnessDefectors,
               averageFitnessCooperators;
        
        for(int aNode = 0; aNode < aPopulation.length; aNode++){
            totalFitnessAll += getCumulativePayoff(aNode);
        }
        
        System.out.println("Prisoner's Dilemma Game - Well Mixed - "
                + "with strategic groups:\n");
        System.out.println("Size : " + aPopulation.length + " Groups : " 
                + numberOfGroups + ", b = " + DEFECTION_P + ".  ");
        System.out.println("Total fitness all : " + totalFitnessAll);
        System.out.println("Average fitness per node : " + 
                (totalFitnessAll/POPULATION_SIZE));
        System.out.println("Average fitness received per link: " + 
                (totalFitnessAll/POPULATION_SIZE)/(aPopulation.length-1));
        double fractionOfDefection = 1 - averages[GAME_ROUNDS-1];
        totalFitnessDefectors = totalFitnessAll*fractionOfDefection;
        System.out.println("Fraction of defection : " + fractionOfDefection);
        System.out.println("Total fitness defectors : " + totalFitnessDefectors);
        
        averageFitnessDefectors = totalFitnessDefectors/
                (POPULATION_SIZE*fractionOfDefection);
        
        System.out.println("Average fitness p. defectors : " 
                + averageFitnessDefectors);
        totalFitnessCooperators = totalFitnessAll - totalFitnessDefectors;
        
        System.out.println("Total Fitness Cooperators : " 
                + totalFitnessCooperators);
        averageFitnessCooperators = (totalFitnessCooperators/(
                averages[GAME_ROUNDS-1]*100));
        
        System.out.println("Average Fitness p. Cooperator : " + 
                averageFitnessCooperators);
        System.out.println("Pop. fraction of cooperation : " +
                averages[GAME_ROUNDS-1] );
        System.out.println("\n\nPopulation view:\n****************\n");
        //UtilityMethods.print2DMultiStrategyPopulation(aPopulation);
        System.out.println("\n\n"); 
    }
    
    /**
     * Retrieves the fraction of cooperation in real time
     * @return the fraction of cooperation
     */
    public double getCooperationForTimeStep(){
        double tempAvg = 0;
        for(Node n : aPopulation){
            tempAvg += n.getCoopAvgFromStrategies();
        }
        
        return tempAvg / aPopulation.length;
    }
    
    /**
     * Plays the prisoner's dilemma with all opponents
     * @param aSelectedPlayer the selected player
     * @return the cumulative payoff
     */
    public double getCumulativePayoff(int aSelectedPlayer){
        
        boolean[] opponentStrategies = getOpponentStrategies(aSelectedPlayer);
        
        //the cumulative payoff
        double cumulativePayoff = 0;
        //the group the selected player is playing
        int selectedGroup = -1;
        //the strategy its using against this group
        boolean aSelectedPlayerStrategy = false;
        
        int tempGroup = -1;
        
        //play with all opponents
        for(int i = 0; i < aPopulation.length; i++){
         
            // do for all but selected player
            if(i != aSelectedPlayer){
         
                // get the group the opponent is in
                selectedGroup = aPopulation[aSelectedPlayer].getGroupFor(i);
                                                                                //System.out.println("Selected group:" + selectedGroup);
                // first run through
                if(i == 0){
                   // assign initial group to temp
                   tempGroup = selectedGroup; 
                }
                
                if(i > 0 && tempGroup != selectedGroup){
                    // if another group is encountered
                    tempGroup = selectedGroup;
                }
                // get the strategy the player has associated with the the group
                aSelectedPlayerStrategy = 
                        aPopulation[aSelectedPlayer].getStrategyForGroup(
                                selectedGroup);
                
                double payoff = playPD(opponentStrategies[i], 
                        aSelectedPlayerStrategy);
                
                // store payoffs in node, so they can be accessed for strategy
                // update
                if(aSelectedPlayer == aFocalPlayer){
                    aPopulation[aSelectedPlayer].setGroupPayoffs(tempGroup, payoff);
                }
                cumulativePayoff += payoff;
            }
        }
        return cumulativePayoff;
    }
    
    /**
     * Returns an array of all strategies used against a selected player.
     * @param aSelectedPlayer the selected player
     * @return the strategies opponents use against the selected player.
     */
    public boolean[] getOpponentStrategies(int aSelectedPlayer){
        
        // opponent strategies
        boolean[] opponentStrategies = new boolean[aPopulation.length];
        int group;
        for(int i = 0; i < aPopulation.length; i++){
            // get the group aSelectedPlayer is in for this
            group = aPopulation[i].getGroupFor(aSelectedPlayer);
            // add strategy to temporary opponent strategy list
            opponentStrategies[i] = aPopulation[i].getStrategyForGroup(group);
        }
        
        //return opponent strategies
        return opponentStrategies;
    }
    
    /**
     * Plays the Prisoner's Dilemma Game
     * @param aNeighbor strategy
     * @param aSelectedPlayer strategy
     * @return the pairwise payoff
     */
    public double playPD(boolean aNeighbor, boolean aSelectedPlayer){
         //default return for C-D encounter
        double payoff = 0.0;
        // check D-C encounter
        if(!aSelectedPlayer && aNeighbor){
            return DEFECTION_P;
        }
        // check C-C encounter
        if(aSelectedPlayer && aNeighbor){
            return 1.0;
        }
        // check D-D encounter
        if(!aSelectedPlayer && !aNeighbor){
            return EPSILON;
        }
        //return the payoff
        return payoff;
    }
    
    /**
     * Get a valid opponent, who is not the focal player.
     * @param aFocalPlayer position of the focal player
     * @return a valid opponent
     */
    public int getValidOpponent(int aFocalPlayer){
        //set opponent outside of array
        int anOpponent = -1;
        //initialise condition
        boolean isValid = false; //is the selected node valid
        
        while(!isValid){
            //get a random node
            anOpponent = GENERATOR.nextInt(aPopulation.length);
            
            //must not match focal
            if(anOpponent != aFocalPlayer){
                isValid = true;
            }
        }
        
        //return a valid location
       return anOpponent; 
    }
    
    
    /**
     * Main entry point for program
     * @param args 
     */
    public static void main(String[] args){
        
        double[] means;
        //instantiate new game
        StrategicGroups exp = new StrategicGroups();
        String bVal = Double.toString(DEFECTION_P);
        
        //the number of groups
        double numberOfGroups = 2;//POPULATION_SIZE/2;
        
        //declare a file writer
        PrintWriter output;
            double divisor = 2;
         try{

//            while(numberOfGroups >= POPULATION_SIZE/8){
//                means = exp.runExperiment((int)numberOfGroups);
//                output = new PrintWriter(new BufferedWriter(new FileWriter(
//                    "Group_Game_Nodes_" + Double.toString(POPULATION_SIZE) +
//                            "_Groups_" + Integer.toString((int)numberOfGroups) + 
//                            "_b_"+ bVal + ".csv")));
//                //write all to file
//                for(double d : means){
//                    output.append(Double.toString(d) + ", ");
//                    output.flush();
//                }
//                output.close();
//                divisor*=2;
//                numberOfGroups = (int)POPULATION_SIZE/divisor;
//                //System.out.println(numberOfGroups);
//            }
            numberOfGroups = 2;
            output = new PrintWriter(new BufferedWriter(new FileWriter(
                    "Group_Game_Nodes_" + Double.toString(POPULATION_SIZE) +
                            "_Groups_" + Integer.toString((int)numberOfGroups) + 
                            "_b_"+ bVal + ".csv")));
            means = exp.runExperiment((int)numberOfGroups);
            //write all to file
            for(double d : means){
                output.append(Double.toString(d) + ", ");
                output.flush();
            }
            output.close();
            
           } catch(IOException ex){
                System.out.println(ERR_FILENOTCREATED);
          }
    }
    
}

