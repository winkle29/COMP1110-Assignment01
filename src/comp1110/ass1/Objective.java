package comp1110.ass1;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An objective is a certain challenge that the player must solve to complete a
 * game of Temple Trap. An objective has two fields:
 *
 *   1. The objective's problem number. There are 60 objectives, each with a
 *      problem number between 1 and 60 inclusive. Generally, as the problem
 *      number increases, so does the difficulty of the objective.
 *   2. The initial state of the objective. This defines the location and
 *      orientation of every tile on the board, as well as the location of the
 *      peg.
 *
 * This class contains one task that you need to complete.
 */
public class Objective {
    private int problemNumber;          // The problem number from the original board game
    private String initialState;        // The list of initial tile placements

    /**
     * This array defines a set of 60 pre-defined puzzle objectives.
     *
     * There are 5 categories of objective, according to 5 difficulty levels, organized within the array as follows:
     *
     * Starter: 1-12
     * Junior: 13-24
     * Expert: 25-36
     * Master: 37-48
     * Wizard: 49-60
     *
     * Each objective is encoded in terms of the initial state of the board.
     * 'direction''position' of each tile in order, then the position of the tile.
     */
    public static Objective[] OBJECTIVES = {
            // STARTER
            new Objective("S0S6W3N8N2E5S7S18", 1),
            new Objective("N7N0S3N8S2S6E1S51", 2),
            new Objective("W8S6E2S5N0E4W1S31", 3),
            new Objective("S8S7W0E5S4N2W3S13", 4),
            new Objective("S1N2W0E7S6S8W3W58", 5),
            new Objective("W8N6E7S4N0W1N2S32", 6),
            new Objective("S2N0W1E8S7W4W6W38", 7),
            new Objective("S6N8W5E4W7W1S3N23", 8),
            new Objective("S3S7W6S4W1E5N2S05", 9),
            new Objective("S0S1N2N7S6W3N4S57", 10),
            new Objective("E2N8W7N1S6E4S3S06", 11),
            new Objective("N1S3W0W7N5E8E6S28", 12),
            // JUNIOR
            new Objective("S4S3E5W1E2W7S0W62", 13),
            new Objective("W1N7N2N0E5N8W6E30", 14),
            new Objective("S1N6N2N4N5S7S3W04", 15),
            new Objective("S0N5E4W7E2N8N6E11", 16),
            new Objective("W5N3W4W1N0S6E2W86", 17),
            new Objective("S0W8E7S3W2S6S1N55", 18),
            new Objective("W0S1N2N7N5W8S4N33", 19),
            new Objective("W2S6W3N8N0S1S7S57", 20),
            new Objective("W5W0N1S8S6E7W3W43", 21),
            new Objective("S6S0N1S7N5E8N3S47", 22),
            new Objective("W8S0N1E7W4E5W2S66", 23),
            new Objective("N1W7N5S3W0E2W8N40", 24),
            // EXPERT
            new Objective("W2E5N1N4S3E6E7W00", 25),
            new Objective("S3S1N0N4W5E7E8W24", 26),
            new Objective("W0N1S3N8S7W4S6N55", 27),
            new Objective("W7S3W0N2N5S6S4S15", 28),
            new Objective("S7S6W3E5S1N2S8S48", 29),
            new Objective("N7S6N1S5W2E0E3S82", 30),
            new Objective("S3N2E1E8W7N5S4N67", 31),
            new Objective("N8W6N0E5N2E7W3W47", 32),
            new Objective("W7S8N0N1W3S4S6N51", 33),
            new Objective("W0N1E2W6N5S7W4N88", 34),
            new Objective("E0N2S8W3N4W5W1E77", 35),
            new Objective("W8E6N7N2S5W1E0W35", 36),
            // MASTER
            new Objective("E2S6E8N4W0N1E5S70", 37),
            new Objective("E1S0N2S6W5E4E8S36", 38),
            new Objective("E3W2N1E7W4W5W0S66", 39),
            new Objective("N6W2N0S7W3E5W4S87", 40),
            new Objective("N0S6N2E4W3S8N7S13", 41),
            new Objective("S7E8N2W0N1S4N5N60", 42),
            new Objective("S3S0N6N1W4N5W2E84", 43),
            new Objective("E1N5S0S6E2N3N7S83", 44),
            new Objective("E8S6N3E5W0S7N1S27", 45),
            new Objective("W8W6N7N2W1E4W0E52", 46),
            new Objective("W4S7N1N6N2N5N0S88", 47),
            new Objective("N5W4S8E1N3E6S2W77", 48),
            // WIZARD
            new Objective("N4W3E5W0E1S8S7W66", 49),
            new Objective("W0S7N2E4W1S6S3N83", 50),
            new Objective("N7N1E4W2E5S3S8N68", 51),
            new Objective("N6S4N1E8W3S7N0S58", 52),
            new Objective("N2W4E7W8E0S1S5N65", 53),
            new Objective("W0W1N2E5W4E7W6W34", 54),
            new Objective("N7W0N2W3W1S8S5N45", 55),
            new Objective("W3W8S4W6N0W2S5E15", 56),
            new Objective("S2S5N0W3W6S7S4E84", 57),
            new Objective("S3N6E0W7E4S2N1N84", 58),
            new Objective("W4S7N3E1E6N2S5N05", 59),
            new Objective("W7W1E4N0E8N3S5N65", 60)

    };


    /**
     * The objective is a string encoding of the initial tile placements and the peg placement.
     * Each tile is represented by a char, followed by a number that indicates its orientation (0-3) and a number
     * representing it's location on the board. The peg is represented by 'Px' where x is a number from 0-8,
     * representing the location of the peg on the board.
     * Eg: "N0S6W3N8N2E5S7S18" can be interpreted in the following way:
     * Tile 'a' is in Orientation North at position 0. "N0"
     * Tile 'b' is in Orientation South at position 6. "S6"
     * Tile 'c' is in Orientation West at position 3.  "W3"
     * Tile 'd' is in Orientation North at position 8. "N8"
     * Tile 'e' is in Orientation North at position 2. "N2"
     * Tile 'f' is in Orientation East at position 5.  "E5"
     * Tile 'g' is in Orientation South at position 7. "S7"
     * Tile 'h' is in Orientation South at position 1. "S1"
     * The peg is in position 8.
     *
     * @param initialState  A string representing the list of initial piece placements
     * @param problemNumber The problem number from the original board game,
     *                      a value from 1 to 60
    */

    public Objective( String initialState, int problemNumber) {

        assert problemNumber >= 1 && problemNumber <= 60;

        this.initialState = initialState;
        this.problemNumber = problemNumber;
    }

    /**
     * Choose a new objective, given a difficulty level.
     *
     * The method should select a randomized objective from the 60 pre-defined solutions,
     * being sure to select an objective with the correct level of difficulty.
     *
     * (See the comments above the declaration of the OBJECTIVES array.)
     *
     * So, for example, if the difficulty is 0 (starter), then this function should use a randomized
     * index between 1 and 12 (inclusive) to return an objective from the OBJECTIVES array that is
     * level 0 difficulty.  On the other hand, if the difficulty is 3 (master), then this function
     * should use a randomized index between 37 and 48 (inclusive) to return an objective from the
     * OBJECTIVES array that is level 3 difficulty.
     *
     * The original code below simply returns OBJECTIVES[0], which neither respects the difficulty
     * (it always returns a level 0 objective), nor is it randomized (it always returns the same
     * objective).
     *
     * @param difficulty The difficulty of the game (0 - starter, 1 - junior, 2 - expert, 3 - master, 4 - wizard)
     *
     * @return An objective at the appropriate level of difficulty.
     */
    public static Objective newObjective(int difficulty) {
        assert difficulty >= 0 && difficulty <= 4;

        //Initialisation
        Random r=new Random();
        int level=0;

        //Conditions for level selection
        if(difficulty==0)
            level= ThreadLocalRandom.current().nextInt(1, 12);
        else if(difficulty==1)
            level= ThreadLocalRandom.current().nextInt(13, 24);
        else if(difficulty==2)
            level= ThreadLocalRandom.current().nextInt(25, 36);
        else if(difficulty==3)
            level= ThreadLocalRandom.current().nextInt(37, 48);
        else if(difficulty==4)
            level= ThreadLocalRandom.current().nextInt(49, 60);

        return OBJECTIVES[level];//Done Task 5
    }

    public String getInitialState() {
        return initialState;
    }
    public int getProblemNumber() {
        return problemNumber;
    }

    public static Objective getObjective(int index) {
        return OBJECTIVES[index];
    }
    public static Objective[] getOBJECTIVES() {
        return OBJECTIVES;
    }
}
