package ee.aoc.day1;

import java.io.IOException;
import java.net.URISyntaxException;

import ee.aoc.InputFileReaderUtil;

public class Part1 {
  public static void main(String[] args) throws URISyntaxException, IOException {
    var result = InputFileReaderUtil.getLinesStreamFromClassPathResourceFile("/day1/input.txt")
        .mapToInt(Integer::valueOf).sum();

    System.out.println(result);
  }
}
