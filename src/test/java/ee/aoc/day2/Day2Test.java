package ee.aoc.day2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

import ee.aoc.InputFileReaderUtil;

class Day2Test {

  @Test
  void testGetChecksum() {
    assertEquals(Part1.getChecksum(List.of("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee" ,"ababab")), 12);
    assertEquals(Part1.getChecksum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day2/input.txt")), 6944);
  }

  @Test
  void testGetCorrectBoxIdIntersection() {
    assertEquals(Part2.getCorrectBoxesCommonLetters(List.of("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye" ,"wvxyz")), "fgij");
    assertEquals(Part2.getCorrectBoxesCommonLetters(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day2/input.txt")),
        "srijafjzloguvlntqmphenbkd");
  }
}