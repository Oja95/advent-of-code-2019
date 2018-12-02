package ee.aoc.day1;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ee.aoc.InputFileReaderUtil;

class Day1Test {

  @Test
  void testGetFrequencySum() {
    assertEquals(Part1.getFrequencySum(List.of("+1", "-2", "+3", "+1")), 3);
    assertEquals(Part1.getFrequencySum(List.of("+1", "+1", "+1")), 3);
    assertEquals(Part1.getFrequencySum(List.of("+1", "+1", "-2")), 0);
    assertEquals(Part1.getFrequencySum(List.of("-1", "-2", "-3")), -6);

    assertEquals(Part1.getFrequencySum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day1/input.txt")), 536);
  }

  @Test
  void testGetTwiceAchievedFrequency() {
    assertEquals(Part2.getTwiceAchievedFrequency(List.of("+1", "-2", "+3", "+1")), 2);
    assertEquals(Part2.getTwiceAchievedFrequency(List.of("+1", "-1")), 0);
    assertEquals(Part2.getTwiceAchievedFrequency(List.of("+3", "+3", "+4", "-2", "-4")), 10);
    assertEquals(Part2.getTwiceAchievedFrequency(List.of("-6", "+3", "+8", "+5", "-6")), 5);
    assertEquals(Part2.getTwiceAchievedFrequency(List.of("+7", "+7", "-2", "-7", "-4")), 14);

    assertEquals(Part2.getTwiceAchievedFrequency(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day1/input.txt")), 75108);
  }
}
