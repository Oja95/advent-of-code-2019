package ee.aoc.day1;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ee.aoc.InputFileReaderUtil;

public class Day1Test {

  @Test
  void testGetFrequencySum() {
    Assertions.assertEquals(Part1.getFrequencySum(List.of("+1", "-2", "+3", "+1")), 3);
    Assertions.assertEquals(Part1.getFrequencySum(List.of("+1", "+1", "+1")), 3);
    Assertions.assertEquals(Part1.getFrequencySum(List.of("+1", "+1", "-2")), 0);
    Assertions.assertEquals(Part1.getFrequencySum(List.of("-1", "-2", "-3")), -6);

    Assertions.assertEquals(Part1.getFrequencySum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day1/input.txt")), 536);
  }

  @Test
  void testGetTwiceAchievedFrequency() {
    Assertions.assertEquals(Part2.getTwiceAchievedFrequency(List.of("+1", "-2", "+3", "+1")), 2);
    Assertions.assertEquals(Part2.getTwiceAchievedFrequency(List.of("+1", "-1")), 0);
    Assertions.assertEquals(Part2.getTwiceAchievedFrequency(List.of("+3", "+3", "+4", "-2", "-4")), 10);
    Assertions.assertEquals(Part2.getTwiceAchievedFrequency(List.of("-6", "+3", "+8", "+5", "-6")), 5);
    Assertions.assertEquals(Part2.getTwiceAchievedFrequency(List.of("+7", "+7", "-2", "-7", "-4")), 14);

    Assertions.assertEquals(Part2.getTwiceAchievedFrequency(
        InputFileReaderUtil.getLinesFromClassPathResourceFile("/day1/input.txt")), 75108);
  }
}
