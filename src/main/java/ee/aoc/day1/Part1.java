package ee.aoc.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Part1 {
  public static void main(String[] args) throws URISyntaxException, IOException {
    var result = Files.lines(Paths.get(Part1.class.getResource("/day1/input.txt").toURI()))
        .mapToInt(Integer::valueOf).sum();

    System.out.println(result);
  }
}
