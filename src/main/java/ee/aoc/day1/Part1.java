package ee.aoc.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import ee.aoc.InputFileReaderUtil;

public class Part1 {
  public static void main(String[] args) throws URISyntaxException, IOException {
    System.out.println(getFrequencySum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day1/input.txt")));
  }

  static int getFrequencySum(List<String> frequencies) {
    return frequencies.stream().mapToInt(Integer::valueOf).sum();
  }
}
