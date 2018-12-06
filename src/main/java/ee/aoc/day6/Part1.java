package ee.aoc.day6;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {
  public static void main(String[] args) {
    System.out.println(getLargestNonInfiniteAreaSize(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day6/input.txt")));
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

  static int getLargestNonInfiniteAreaSize(List<String> inputStrings) {
    var inputCoords = inputStrings.stream().map(Part1::stringToCoords).collect(Collectors.toList());
    int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    // 0 as undefined

    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {

        var distances = new TreeMap<Integer, Integer>(); // key -> distance
        for (int k = 0; k < inputCoords.size(); k++) {
          distances.put(k + 1, getManhattanDistanceToPoint(new Coordinates(i, j), inputCoords.get(k)));
        }

        var smallestDistances = distances.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());
        if (smallestDistances.get(0).getValue().equals(smallestDistances.get(1).getValue())) {
          continue;
        }

        int nearestCoordIndex;
        if (smallestDistances.get(0).getValue() < smallestDistances.get(1).getValue()) {
          nearestCoordIndex = smallestDistances.get(0).getKey();
        } else {
          nearestCoordIndex = smallestDistances.get(1).getKey();
        }

        matrix[i][j] = nearestCoordIndex;
      }
    }


    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        System.out.print(matrix[j][i] + " ");
      }
      System.out.println();
    }

    // edges are infinity
    var infinityIndices = new HashSet<Integer>();
    for (int i = 0; i < MATRIX_SIZE; i++) {
      infinityIndices.add(matrix[0][i]);
      infinityIndices.add(matrix[i][0]);
      infinityIndices.add(matrix[i][MATRIX_SIZE-1]);
      infinityIndices.add(matrix[MATRIX_SIZE-1][i]);
    }
    System.out.println(infinityIndices);

    var patchAreas = new HashMap<Integer, Integer>();
    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        int currentIndex = matrix[i][j];
        if (!infinityIndices.contains(currentIndex)) {
          patchAreas.put(currentIndex, patchAreas.getOrDefault(currentIndex, 0) + 1);
        }
      }
    }

    List<Integer> collect = patchAreas.values().stream().sorted().collect(Collectors.toList());
    return collect.get(collect.size() - 1);
  }

  private static int getManhattanDistanceToPoint(Coordinates start, Coordinates end) {
    return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
  }

  private static Coordinates stringToCoords(String input) {
    String[] split = input.split(",");
    return new Coordinates(Integer.valueOf(split[0].strip()), Integer.valueOf(split[1].strip()));
  }
}
