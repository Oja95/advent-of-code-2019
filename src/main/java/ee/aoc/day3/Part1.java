package ee.aoc.day3;

import static ee.aoc.day3.InputParserUtil.*;

import java.util.List;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {

  public static void main(String[] args) {
    System.out.println(getOverlappingSquares(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day3/input.txt")));
  }

  static int getOverlappingSquares(List<String> inputList) {
    var matrix = new int[1000][1000];

    List<FabricDimensions> collect = inputList.stream().map(InputParserUtil::parseDimensionsFromString).collect(Collectors.toList());
    for (FabricDimensions fabricDimensions : collect) {
      for (int i = fabricDimensions.startX; i < fabricDimensions.startX + fabricDimensions.width; i++) {
        for (int j = fabricDimensions.startY; j < fabricDimensions.startY + fabricDimensions.height; j++) {
          matrix[i][j]++;
        }
      }
    }

    int count = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        if (matrix[i][j] > 1) count++;
      }
    }

    return count;
  }
}
