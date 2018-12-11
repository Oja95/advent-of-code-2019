package ee.aoc.day11;

import static ee.aoc.day11.Part1.calcPowerLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Day11Tests {

  @Test
  void testPowerLevelCalculating() {
    assertEquals(4, calcPowerLevel(8, 3, 5));
    assertEquals(-5, calcPowerLevel(57, 122, 79));
    assertEquals(0, calcPowerLevel(39, 217, 196));
    assertEquals(4, calcPowerLevel(71, 101, 153));
  }

  @Test
  void name() {

  }
}
