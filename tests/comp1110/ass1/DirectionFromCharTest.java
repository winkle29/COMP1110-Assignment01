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
public class DirectionFromCharTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private void test(char dir, Direction expected) {
        Direction out = Direction.fromChar(dir);

        assertTrue("Expected " + expected + " for input char" + dir +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        test('N', NORTH);
        test('E', EAST);
    }

    @Test
    public void testSimple2() {
        test('N', NORTH);
        test( 'S', SOUTH);
    }

    @Test
    public void testSimple3() {
        test('N', NORTH);
        test('W', WEST);
    }
}
