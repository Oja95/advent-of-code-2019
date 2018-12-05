package ee.aoc.day4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ee.aoc.InputFileReaderUtil;

public class Day4Test {

  @Test
  void testSleep() {
    Assertions.assertEquals(Part1.getMostSleptIdTimesSleptMinutes(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day4/test-input.txt"))
    ,240);
  }
}
