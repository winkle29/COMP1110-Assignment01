package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

/*
 * IMPORTANT: THIS CODE MUST NOT BE MODIFIED.
 *
 * You may write your own tests, but they need to be in separate files.
 */
public class PlacementToPositionTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private void test(String pl, int expected) {
        int out = Tile.placementToPosition(pl);

        assertTrue("Expected: "  + expected +
                        " for input placement " + pl +
                ", but got: " + out+ ".",
                out == expected);
    }

    @Test
    public void testSimple1() {
        test("N0", 0);
        test("N1", 1);
    }

    @Test
    public void testSimple2() {
        test("S8", 8);
        test("S6", 6);
    }

    @Test
    public void testSimple3() {
        test("W4", 4);
        test("W3", 3);
    }

    @Test
    public void testSimple4() {
        test("E2", 2);
        test("E5", 5);
        test("S7", 7);
    }
}
