package ee.aoc.day6;

import static ee.aoc.day6.Day6Util.getManhattanDistanceToPoint;

import java.util.List;

import ee.aoc.InputFileReaderUtil;

public class Part2 {
  private static final int MATRIX_SIZE = 500;

  public static void main(String[] args) {
    System.out.println(getLargestAreaWithMaxDistanceToAllPoints(
        InputFileReaderUtil.getLinesFromClassPathResourceFile("/day6/input.txt"), 10000)
    );
  }

  static int getLargestAreaWithMaxDistanceToAllPoints(List<String> inputStrings, int maxDistance) {

    int count = 0;
    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        final var coords = Coordinates.of(i, j);
        var distance = inputStrings.stream()
            .map(Day6Util::stringToCoords)
            .map(x -> getManhattanDistanceToPoint(coords, x))
            .mapToInt(Integer::valueOf).sum();
        if (distance < maxDistance) count++;
      }
    }
    return count;
  }
}
