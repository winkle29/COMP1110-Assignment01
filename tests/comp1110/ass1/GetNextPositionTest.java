package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static comp1110.ass1.TempleTrap.FINISH_POSITION;
import static comp1110.ass1.TempleTrap.OFF_BOARD;
import static org.junit.Assert.assertTrue;

/*
 * IMPORTANT: THIS CODE MUST NOT BE MODIFIED.
 *
 * You may write your own tests, but they need to be in separate files.
 */
public class GetNextPositionTest {
  @Rule
  public Timeout globalTimeout = Timeout.millis(2000);

  private void test(int start, Direction direction, int expected) {
    int out = TempleTrap.getNextPosition(start, direction);

    assertTrue("Expected " + expected +
                    " for position " + start +
                    ", and direction " + direction +
                    ", but got " + out + ".",
            out == expected
    );
  }


  @Test
  public void testSimple() {
    test(0, Direction.EAST, 1);
    test(3, Direction.EAST, 4);
    test(6, Direction.EAST, 7);
    test(2, Direction.WEST, 1);
    test(5, Direction.WEST, 4);
    test(8, Direction.WEST, 7);
    test(0, Direction.SOUTH, 3);
    test(1, Direction.SOUTH, 4);
    test(2, Direction.SOUTH, 5);
    test(6, Direction.NORTH, 3);
    test(7, Direction.NORTH, 4);
    test(8, Direction.NORTH, 5);
  }

  @Test
  public void testBoundary() {
    test(3, Direction.WEST, OFF_BOARD);
    test(6, Direction.WEST, OFF_BOARD);
    test(2, Direction.EAST, OFF_BOARD);
    test(5, Direction.EAST, OFF_BOARD);
    test(8, Direction.EAST, OFF_BOARD);
    test(0, Direction.NORTH, OFF_BOARD);
    test(1, Direction.NORTH, OFF_BOARD);
    test(2, Direction.NORTH, OFF_BOARD);
    test(6, Direction.SOUTH, OFF_BOARD);
    test(7, Direction.SOUTH, OFF_BOARD);
    test(8, Direction.SOUTH, OFF_BOARD);
  }

  @Test
  public void testCompletion() {
    test(0, Direction.WEST, FINISH_POSITION);
  }
}

