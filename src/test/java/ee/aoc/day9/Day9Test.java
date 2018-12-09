package ee.aoc.day9;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Day9Test {
  @Test
  void testPart1first() {
    assertEquals(8317, Part1.getWinningElfScore("10 players; last marble is worth 1618 points"));
    assertEquals(146373, Part1.getWinningElfScore("13 players; last marble is worth 7999 points"));
    assertEquals(54718, Part1.getWinningElfScore("21 players; last marble is worth 6111 points"));
    assertEquals(37305, Part1.getWinningElfScore("30 players; last marble is worth 5807 points"));
  }
}
