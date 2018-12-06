package ee.aoc.day6;

import static ee.aoc.day6.Day6Util.getManhattanDistanceToPoint;

import java.util.Collections;
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
    System.out.println(getLargestNonInfiniteAreaSize(
        InputFileReaderUtil.getLinesFromClassPathResourceFile("/day6/input.txt"))
    );
  }

  private static final int MATRIX_SIZE = 500;

  static int getLargestNonInfiniteAreaSize(List<String> inputStrings) {
    var inputCoords = inputStrings.stream().map(Day6Util::stringToCoords).collect(Collectors.toList());
    int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {

        var distances = new TreeMap<Integer, Integer>();
        for (int k = 0; k < inputCoords.size(); k++) {
          distances.put(k + 1, getManhattanDistanceToPoint(Coordinates.of(i, j), inputCoords.get(k)));
        }

        var smallestDistances = distances.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());

        Map.Entry<Integer, Integer> firstNearestCoord = smallestDistances.get(0);
        Map.Entry<Integer, Integer> secondNearestCoord = smallestDistances.get(1);
        if (firstNearestCoord.getValue().equals(secondNearestCoord.getValue())) continue;

        matrix[i][j] = firstNearestCoord.getValue() < secondNearestCoord.getValue() ?
            firstNearestCoord.getKey() : secondNearestCoord.getKey();
      }
    }

    var infinityIndices = new HashSet<Integer>();
    for (int i = 0; i < MATRIX_SIZE; i++) {
      infinityIndices.add(matrix[0][i]);
      infinityIndices.add(matrix[i][0]);
      infinityIndices.add(matrix[i][MATRIX_SIZE-1]);
      infinityIndices.add(matrix[MATRIX_SIZE-1][i]);
    }

    var nonInfiniteAreas = new HashMap<Integer, Integer>();
    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        int currentIndex = matrix[i][j];
        if (!infinityIndices.contains(currentIndex)) {
          nonInfiniteAreas.put(currentIndex, nonInfiniteAreas.getOrDefault(currentIndex, 0) + 1);
        }
      }
    }

    return Collections.max(nonInfiniteAreas.values().stream().sorted().collect(Collectors.toList()));
  }
}
