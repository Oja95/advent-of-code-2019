package ee.aoc.day5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day5Test {

  @Test
  void testPart1() {
    Assertions.assertEquals(Part1.getReactedPolymerSize("dabAcCaCBAcCcaDA"), 10);
  }
}
