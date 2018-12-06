package ee.aoc.day6;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day6Test {
  @Test
  void testPart1() {
    Assertions.assertEquals(Part1.getLargestNonInfiniteAreaSize(List.of("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9")), 17);
  }

  @Test
  void testPart2() {
    Assertions.assertEquals(Part2.getLargestAreaWithMaxDistanceToAllPoints(List.of("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9"), 32), 16);
  }
}
