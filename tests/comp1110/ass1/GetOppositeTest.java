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
public class GetOppositeTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private void test(Direction dir, Direction expected) {
        Direction out = dir.getOpposite();

        assertTrue("Expected " + expected + " for input Direction" + dir +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        test(SOUTH, NORTH);
        test(WEST, EAST);
    }

    @Test
    public void testSimple2() {
        test(SOUTH, NORTH);
        test( NORTH, SOUTH);
    }

    @Test
    public void testSimple3() {
        test(SOUTH, NORTH);
        test(EAST, WEST);
    }
}
