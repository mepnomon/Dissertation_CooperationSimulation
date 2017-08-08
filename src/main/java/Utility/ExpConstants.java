package Utility;

/**
 * GLOBAL CONSTANTS USED IN EXPERIMENTS
 * EDIT PARAMETERS HERE INSTEAD OF INDIVIDUAL CLASS FILES
 * @author D.B. Dressler
 */
public class ExpConstants {
    
    //the size of the population
    public static final double POPULATION_SIZE = 1024; //129 for groups 4,8,16,32
    //how many rounds the experiment runs for
    public static final int GAME_ROUNDS = 100000;
    //the cost for the public goods game
    //can be set individually through constructor too
    public static final double COST = 2;
    //the reward for the public goods game
    public static final double REWARD = 1.9;
    //deflection potential for PD
    public static final double DEFECTION_P = 3; // DP > 1 //experiments with 2
    //Epsilon value for PD
    public static final double EPSILON = 0.01;
    
    
    //run groups 128/4 next for all group sizes
    
    //array with initial cooperation value for a ndoe population of 10
    //used for tests
   public static boolean[] TEST_STRATEGIES = {true, false, true, false, true, 
       false, false, false, true, true};
   
   public static boolean[] TEST_STRATEGIES_TWO = {true, true, true, true, true, 
       false, false, true, false, false};
   
   //array of integers
   public static final int[] TEST_INDICES = {5,8,8,7,0,3,5,5,8,1,7,3,9,2,1,0,3,9,
       3,0,4,3,3,4,3,2,3,3,1,7,8,4,5,5,7,1,1,2,7,3,6,8,8,4,0,2,4,0,9,0,8,1,6,9,3
           ,2,9,5,0,8,2,5,1,5,1,1,8,5,5,3,6,2,2,5,7,6,1,8,5,7,7,5,0,4,7,4,0,6,0,
           3,1,1,3,4,2,9,3,9,1,4};
}