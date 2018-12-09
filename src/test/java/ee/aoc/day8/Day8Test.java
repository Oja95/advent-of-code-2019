package ee.aoc.day8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ee.aoc.InputFileReaderUtil;

public class Day8Test {

  @Test
  void testTreeMetaDataSum() {
    Assertions.assertEquals(41849, Part1.getMetaDataSum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day8/input.txt")));
  }
}
