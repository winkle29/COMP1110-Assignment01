package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import javax.swing.*;

import static comp1110.ass1.Objective.OBJECTIVES;
import static comp1110.ass1.TileName.*;
import static org.junit.Assert.assertTrue;

/*
 * IMPORTANT: THIS CODE MUST NOT BE MODIFIED.
 *
 * You may write your own tests, but they need to be in separate files.
 */
public class CanTransitTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private void test(Tile instance, Tile other, boolean expected) {
        boolean out = instance.canTransit(other);

        assertTrue("Expected " + expected +
                        " for Tile instance " + instance.toString() +
                        " in position " + instance.position +
                        ", and other tile " + other.toString() +
                        " in position " + other.position +
                        ", but got " + out + ".",
                out == expected);

    }

    Tile[][] VALID_TRANSIT = {
            //Green tiles
            {new Tile(PLUS, "S1"), new Tile(SQUARE, "W0")}, // West
            {new Tile(SQUARE, "S3"), new Tile(EQUALS, "N0")}, //North
            {new Tile(PLUS, "E4"), new Tile(EQUALS, "E7")}, //South
            //Brown tiles
            {new Tile(CROSS, "S6"), new Tile(CIRCLE, "N7")}, //East
            {new Tile(CIRCLE, "W2"), new Tile(TRIANGLE, "E5")},
            //Stair tiles
            {new Tile(STAR, "W5"), new Tile(SQUARE, "N2")}, //Stair north to Green
            {new Tile(DIAMOND, "S6"), new Tile(CIRCLE, "E7")},
            {new Tile(STAR, "N0"), new Tile(DIAMOND, "S1")}
    };

    Tile[][] INVALID_TRANSIT = {
            //Green tiles
            {new Tile(PLUS, "S4"), new Tile(SQUARE, "E3")}, // incorrect orientations
            {new Tile(SQUARE, "S3"), new Tile(EQUALS, "W4")},
            {new Tile(PLUS, "E4"), new Tile(EQUALS, "E0")}, // not adjacent
            //Brown tiles
            {new Tile(CROSS, "S6"), new Tile(EQUALS, "N7")}, // Invalid colours
            {new Tile(SQUARE, "W2"), new Tile(TRIANGLE, "E5")},
            //Stair tiles
            {new Tile(STAR, "W5"), new Tile(CROSS, "N2")}, //
            {new Tile(DIAMOND, "S6"), new Tile(SQUARE, "E7")},
            {new Tile(STAR, "N0"), new Tile(DIAMOND, "N1")}

    };




    @Test
    public void testInvalid() {
        for (int i = 0; i < 5; i++) {
            test(INVALID_TRANSIT[i][0], INVALID_TRANSIT[i][1], false);
        }
    }

    @Test
    public void testValid() {
        for (int i = 0; i < 5; i++) {
            test(VALID_TRANSIT[i][0], VALID_TRANSIT[i][1], true);
        }
    }

    @Test
    public void testInvalidStairs() {
        for (int i = 0; i < 3; i++) {
            test(INVALID_TRANSIT[i+5][0], INVALID_TRANSIT[i+5][1], false);
        }
    }

    @Test
    public void testValidStairs() {
        for (int i = 0; i < 3; i++) {
            test(VALID_TRANSIT[i+5][0], VALID_TRANSIT[i+5][1], true);
        }
    }
}
