package Game_Models;

import static Utility.ExpConstants.DEFECTION_P;
import static Utility.ExpConstants.EPSILON;
import static Utility.ExpConstants.GAME_ROUNDS;
import static Utility.ExpConstants.POPULATION_SIZE;
import static Utility.Notifications.ERR_FILENOTCREATED;
import Utility.UtilityMethods;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * --THIS DATA IS CURRENTLY IN USE -- Implement cost etc
 *
 * @author D.B. Dressler
 */
public class SingleStrategy2DStructured {

    //global variables
    Node[][] aPopulation; //the population
    Random generator; //init random number generator
    int focalPlayerX, focalPlayerY; //focal player location
    int modelPlayerX, modelPlayerY; //model player location
    final int POP_SIZE = (int) POPULATION_SIZE; //define population size
    final int MIN; //MAX_X, MAX_Y; //store boundaries
    int max_x, max_y;
    boolean modelStrategy; //model strategies

    /**
     * Constructor
     */
    public SingleStrategy2DStructured() {
        //instantiate variables
        aPopulation = new Node[POP_SIZE][POP_SIZE];
        generator = new Random();
        MIN = 0;
    }

    /**
     * Perform th experiment
     *
     * @return an array of values
     */
    public double[] runExperiment() {

        final int SAMPLES = 100;
        double focalSum, modelSum;
        double[] averages = new double[(int) GAME_ROUNDS];

        for (int i = 0; i < SAMPLES; i++) {
            //create new population
            //provide linear size
            aPopulation = UtilityMethods.setup2DPopulation((int) POPULATION_SIZE);
            max_x = UtilityMethods.getMAX_X() - 1;
            max_y = UtilityMethods.getMAX_Y() - 1;
            for (int j = 0; j < GAME_ROUNDS; j++) {
                int updates = 0; //counter for updates
                double cumulativePayoffFocal, cumulativePayoffModel;
                //select focal player x,y coordinates
                focalPlayerX = generator.nextInt(max_x);
                focalPlayerY = generator.nextInt(max_y);
                //select model player
                selectModel();

                //store cumulative payoffs
                cumulativePayoffFocal = playPDInNeighborhood(focalPlayerX, focalPlayerY);
                cumulativePayoffModel = playPDInNeighborhood(modelPlayerX, modelPlayerY);

                //check if strategy update occurs
                if (cumulativePayoffModel > cumulativePayoffFocal) {
                    ++updates;
                    //use the wrong strategy in 10% of all updates, i.e. noise
                    if (updates % GAME_ROUNDS * 0.1 == 0) {
                        //adopt a random, wrong strategy
                        modelStrategy = generator.nextBoolean();
                    }
                    aPopulation[focalPlayerX][focalPlayerY].setCoopStatus(modelStrategy);
                }
                //store fC
                averages[j] += UtilityMethods.
                        fractionOfCooperationSingle2D(aPopulation)*10;
            }//end inner loop
        }//end outer loops

        //calculate averages
        for (int i = 0; i < GAME_ROUNDS; i++) {
            averages[i] /= SAMPLES;
        }
        //display performance statistics
        displayPerformanceStatistics(averages);
        //print fraction of cooperation for single strategy
        System.out.println("fC : " + UtilityMethods.fractionOfCooperationSingle2D(aPopulation)*10);
        System.out.println("Coops: "  + UtilityMethods.get2DCoopCount(aPopulation));
        return averages;
    }//end runExperiment

    /**
     * Displays the performance statistics
     *
     * @param averages
     */
    private void displayPerformanceStatistics(double[] averages) {
        double totalFitnessAll = 0,
                averageFitnessAll = 0,
                totalFitnessDefectors = 0,
                totalFitnessCooperators,
                averageFitnessDefectors,
                averageFitnessCooperators,
                tempFitness;

        //calculate total fitness
        for (int x = 0; x < UtilityMethods.getMAX_X(); x++) {
            for (int y = 0; y < UtilityMethods.getMAX_Y(); y++) {
                totalFitnessAll += playPDInNeighborhood(x, y);
            }
        }

        System.out.println("Prisoner's Dilemma Game with Single Strategies in 2D Structure Lattice\n");
        System.out.println("Linear Size : " + POP_SIZE + ", b = " + DEFECTION_P + ".  ");
        System.out.println("Total fitness all : " + totalFitnessAll);
        System.out.println("Average fitness per node : " + totalFitnessAll / (UtilityMethods.getMAX_X() * UtilityMethods.getMAX_Y()));
        double fractionOfDefection = 1 - averages[GAME_ROUNDS - 1];
        totalFitnessDefectors = totalFitnessAll * fractionOfDefection;
        System.out.println("Total fitness defectors : " + totalFitnessDefectors);
        //divide the Total Fitness for defectors by it
        System.out.println("Fraction of defection : " + fractionOfDefection);
        //averageFitnessDefectors = totalFitnessDefectors/(fractionOfDefection*100);
        averageFitnessDefectors = totalFitnessDefectors / ((UtilityMethods.getMAX_X() * UtilityMethods.getMAX_Y()) * fractionOfDefection);
        System.out.println("Average fitness p. defectors : " + averageFitnessDefectors);
        totalFitnessCooperators = totalFitnessAll - totalFitnessDefectors;
        System.out.println("Total Fitness Cooperators : " + totalFitnessCooperators);
        averageFitnessCooperators = (totalFitnessCooperators / (averages[GAME_ROUNDS - 1] * 10));
        System.out.println("Average Fitness p. Cooperator : " + averageFitnessCooperators);
        System.out.println("Pop. fraction of cooperation : " + averages[GAME_ROUNDS - 1]);
        System.out.println("\n\nPopulation view:\n****************\n");
        UtilityMethods.print2DPopulation(aPopulation);
        System.out.println("\n\n");
    }

    /**
     * Plays the prisoner's dilemma game with each node in the player's
     * neighborhood.
     *
     * @param x the player's x location
     * @param y the player's y location
     * @return the cumulative payoff
     */
    public double playPDInNeighborhood(int x, int y) {

        double sum;
        int below, above, left, right;
        double sumA, sumB, sumC, sumD;

        //check boundary cases
        if (x == max_x) {
            right = MIN;
        } else {
            right = x + 1;
        }

        if (x == MIN) {
            left = max_x;
        } else {
            left = x - 1;
        }

        if (y == MIN) {
            above = max_y;
        } else {
            above = y - 1;
        }

        if (y == max_y) {
            below = MIN;
        } else {
            below = y + 1;
        }

        //store theese invidiaul values in variables
        //get strategies from players
        sumA = playPD(aPopulation[left][y].getCoopStatus(),
                aPopulation[x][y].getCoopStatus());

        sumB = playPD(aPopulation[right][y].getCoopStatus(),
                aPopulation[x][y].getCoopStatus());

        sumC = playPD(aPopulation[x][above].getCoopStatus(),
                aPopulation[x][y].getCoopStatus());

        sumD = playPD(aPopulation[x][below].getCoopStatus(),
                aPopulation[x][y].getCoopStatus());

        //store individual contributions in node's memory
        aPopulation[x][y].setContributions(sumA, sumB, sumC, sumD);
        sum = sumA + sumB + sumC + sumD;
        //System.out.println("Sum from game:" + sum);
        //return the sum
        return sum;
    }

    /**
     * Calculates the pairwise payoff from 2 nodes interacting
     *
     * @param neighbor the neighbor's strategy
     * @param selectedPlayer the player's strategy
     * @return the pairwise payoff
     */
    public double playPD(boolean neighbor, boolean selectedPlayer) {

        double payoff = 0.0;
        // check D-C encounter
        if (!selectedPlayer && neighbor) {
            return DEFECTION_P;
        }
        // check C-C encounter
        if (selectedPlayer && neighbor) {
            return 1.0;
        }
        // check D-D encounter
        if (!selectedPlayer && !neighbor) {
            return EPSILON;
        }
        //return the payoff
        return payoff;
    }

    /**
     * Selects the model opponent
     */
    public void selectModel() {

        //select a neighbor
        int neighbor = generator.nextInt(4);
        //select appropriate neighbor in 2D structure
        //0=left, 1 right, 2 above, 3, below
        switch (neighbor) {

            case 0: //left opponent
                //System.out.println("Select left neighbor as model");
                //check if left player on opposite side
                if (focalPlayerX == MIN) {
                    modelPlayerX = max_x;
                } else {
                    modelPlayerX = focalPlayerX - 1;
                }
                modelPlayerY = focalPlayerY;
                modelStrategy
                        = aPopulation[modelPlayerX][modelPlayerY].getCoopStatus();
                break;

            case 1: //right opponent
                //System.out.println("Select right neighbor as model");
                //check boundary
                if (focalPlayerX == max_x) {
                    modelPlayerX = MIN;
                } else {
                    modelPlayerX = focalPlayerX + 1;
                }
                modelPlayerY = focalPlayerY;
                modelStrategy
                        = aPopulation[modelPlayerX][modelPlayerY].getCoopStatus();
                break;

            //opponent above
            case 2:
                //check boundary
                //System.out.println("Select neighbor above as model");
                if (focalPlayerY == MIN) {
                    modelPlayerY = max_y;
                } else {
                    modelPlayerY = focalPlayerY - 1;
                }
                modelPlayerX = focalPlayerX;
                modelStrategy
                        = aPopulation[modelPlayerX][modelPlayerY].getCoopStatus();
                break;

            //opponent below
            case 3:
                //check boundary
                //System.out.println("Select neighbor below as model");
                if (focalPlayerY == max_y) {
                    modelPlayerY = MIN;
                } else {
                    modelPlayerY = focalPlayerY + 1;
                }
                modelPlayerX = focalPlayerX;
                modelStrategy
                        = aPopulation[modelPlayerX][modelPlayerY].getCoopStatus();
                break;
        }
        //System.out.println("Model Strategy: " + modelStrategy)
    }

    /**
     * Main entry point for program
     *
     * @param args
     */
    public static void main(String[] args) {

        PrintWriter output;
        double[] averages;
        String popSize = Double.toString(POPULATION_SIZE);
        String bVal = Double.toString(DEFECTION_P);
        SingleStrategy2DStructured exp = new SingleStrategy2DStructured();
        averages = exp.runExperiment();
        try {
            output = new PrintWriter(new BufferedWriter(
                    new FileWriter("CBR_ " + bVal + "_Structured_2D_Single_PopulationSize_ "
                            + popSize + ".csv")));
            output.append("Linear Size : " + popSize + ", b = " + Double.toString(DEFECTION_P));
            for (int i = 0; i < averages.length; i++) {
                output.append(Double.toString(averages[i]) + ", ");
                output.flush();
            }
            output.close();
        } catch (IOException ex) {
            System.out.println(ERR_FILENOTCREATED);
        }
    }
}
