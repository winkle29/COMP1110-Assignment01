package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;

import static org.junit.Assert.assertTrue;

/*
 * IMPORTANT: THIS CODE MUST NOT BE MODIFIED.
 *
 * You may write your own tests, but they need to be in separate files.
 */
public class GetSolutionsTest {
  @Rule
  public Timeout globalTimeout = Timeout.millis(5000);

  private void test(Objective obj, SampleSolution expected) {
    TempleTrap game = new TempleTrap(obj);
    Set<String> out = game.getSolutions();

    assertTrue("Expected getSolutions() to find " + expected.setLength + " solutions for objective number " + obj.getProblemNumber() + ", but only found " + out.size() + " solutions",
            expected.setLength == out.size());
    assertTrue("Expected getSolutions() to find the solution " + expected.exampleString + ", but it was not there",
            out.contains(expected.exampleString));
  }

  int[] quick = {2, 3, 16, 17, 19};
  int[] medium = {1, 6, 26, 41, 22};
  int[] longer = {4, 8, 13, 32, 33};

  @Test
  public void testSimple() {
    test(Objective.getObjective(19), SampleSolution.SOLUTIONS[19]);
  }

  @Test
  public void testQuick() {
    for (int i : quick)
      test(Objective.getObjective(i), SampleSolution.SOLUTIONS[i]);
  }

  @Test
  public void testMedium() {
    for (int i : medium)
      test(Objective.getObjective(i), SampleSolution.SOLUTIONS[i]);
  }
}
