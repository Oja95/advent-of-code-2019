package ee.aoc.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part2 {

  private static final int INITIAL_VALUE = 0;
  private static final int MAX_TRIES = 100_000;

  public static void main(String[] args) throws URISyntaxException, IOException {
    var reachedFrequencies = new HashSet<>(Set.of(INITIAL_VALUE));

    var inputList = InputFileReaderUtil.getLinesStreamFromClassPathResourceFile("/day1/input.txt")
        .map(Integer::valueOf)
        .collect(Collectors.toUnmodifiableList());

    var currentValue = INITIAL_VALUE;
    for (int i = 0; i < MAX_TRIES; i++) {
      for (Integer integer : inputList) {
        currentValue += integer;
        if (reachedFrequencies.contains(currentValue)) {
          System.out.println(currentValue);
          System.exit(0);
        }
        reachedFrequencies.add(currentValue);
      }
    }

    throw new IllegalStateException("No solution found!");
  }
}
