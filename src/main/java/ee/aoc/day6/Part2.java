package ee.aoc.day6;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

@SuppressWarnings("Duplicates")
public class Part2 {
  public static void main(String[] args) {
    System.out.println(
        getLargestNonInfiniteAreaSize(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day6/input.txt"), 10000));
  }

  static class Coordinates {
    private final int x;
    private final int y;

    Coordinates(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private static final int MATRIX_SIZE = 500;

  static int getLargestNonInfiniteAreaSize(List<String> inputStrings, int maxDistance) {
    var inputCoords = inputStrings.stream().map(Part2::stringToCoords).collect(Collectors.toList());
    int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    // 0 as undefined

    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        int sum = 0;
        for (Coordinates inputCoord : inputCoords) {
           sum += getManhattanDistanceToPoint(new Coordinates(i, j), inputCoord);
        }
        matrix[i][j] = sum;
      }
    }

    int count = 0;
    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        int currentIndex = matrix[i][j];
        if (currentIndex < maxDistance) count++;
      }
    }
    return count;
  }

  private static int getManhattanDistanceToPoint(Coordinates start, Coordinates end) {
    return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
  }

  private static Coordinates stringToCoords(String input) {
    String[] split = input.split(",");
    return new Coordinates(Integer.valueOf(split[0].strip()), Integer.valueOf(split[1].strip()));
  }
}
