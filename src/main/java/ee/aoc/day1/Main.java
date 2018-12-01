package ee.aoc.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) throws URISyntaxException, IOException {
    var result = Files.lines(Paths.get(Main.class.getResource("/day1/input.txt").toURI()))
        .mapToInt(Integer::valueOf).sum();

    System.out.println(result);
  }
}
