package ee.aoc.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part2 {

  private static final int INITIAL_VALUE = 0;
  private static final int MAX_TRIES = 100_000;

  public static void main(String[] args) {
    System.out.println(getTwiceAchievedFrequency(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day1/input.txt")));
  }

  static int getTwiceAchievedFrequency(List<String> frequencies) {
    var reachedFrequencies = new HashSet<>(Set.of(INITIAL_VALUE));

    var inputList = frequencies.stream()
        .map(Integer::valueOf)
        .collect(Collectors.toUnmodifiableList());

    var currentValue = INITIAL_VALUE;
    for (int i = 0; i < MAX_TRIES; i++) {
      for (Integer integer : inputList) {
        currentValue += integer;
        if (reachedFrequencies.contains(currentValue)) {
          return currentValue;
        }
        reachedFrequencies.add(currentValue);
      }
    }

    throw new IllegalStateException("No twice achieved frequency found!");
  }
}
