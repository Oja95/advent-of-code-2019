package ee.aoc.day3;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ee.aoc.InputFileReaderUtil;

public class Day3Test {

  @Test
  void testGetOverlappingSquares() {
    Assertions.assertEquals(Part1.getOverlappingSquares(List.of("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")), 4);
    Assertions.assertEquals(Part1.getOverlappingSquares(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day3/input.txt")), 116920);
  }

  @Test
  void testGetDistinctNonOverlappingFabricId() {
    Assertions.assertEquals(Part2.getDistinctFabricId(List.of("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")), 3);
    Assertions.assertEquals(Part2.getDistinctFabricId(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day3/input.txt")), 382);
  }
}
