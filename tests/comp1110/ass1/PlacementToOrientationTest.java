package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static comp1110.ass1.Direction.*;
import static org.junit.Assert.assertTrue;

/*
 * IMPORTANT: THIS CODE MUST NOT BE MODIFIED.
 *
 * You may write your own tests, but they need to be in separate files.
 */
public class PlacementToOrientationTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private void test(String pl, Direction expected) {
        Direction out = Tile.placementToOrientation(pl);

        assertTrue("Expected " + expected + " for input placement " + pl +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        test("N0", NORTH);
        test("E5", EAST);
    }

    @Test
    public void testSimple2() {
        test("S7", SOUTH);
        test("W8", WEST);
    }

    @Test
    public void testSimple3() {
        test("S2", SOUTH);
        test("N4", NORTH);
        test("E6", EAST);
        test("W1", WEST);
    }

}
