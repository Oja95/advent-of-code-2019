package ee.aoc.day2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day2Test {

  @Test
  void testGetChecksum() {
    assertEquals(Part1.getChecksum(List.of("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee" ,"ababab")), 12);
  }
}