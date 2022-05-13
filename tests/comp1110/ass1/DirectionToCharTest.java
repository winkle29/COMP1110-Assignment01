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
public class DirectionToCharTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private void test(Direction dir, char expected) {
        char out = dir.toChar();
        assertTrue("Expected " + expected + " for input Orientation" + dir +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        test(Direction.NORTH, 'N');
        test(Direction.EAST, 'E');
    }

    @Test
    public void testSimple2() {
        test(Direction.NORTH, 'N');
        test(Direction.SOUTH, 'S');
    }

    @Test
    public void testSimple3() {
        test(Direction.NORTH, 'N');
        test(Direction.WEST, 'W');
    }
}
