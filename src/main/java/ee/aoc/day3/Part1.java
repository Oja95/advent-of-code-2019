package ee.aoc.day3;

import java.util.List;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {

  public static void main(String[] args) {
    System.out.println(getOverlappingSquares(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day3/input.txt")));
  }

  static int getOverlappingSquares(List<String> inputList) {
    var matrix = new int[1000][1000];

    List<FabricDimensions> collect = inputList.stream().map(Part1::parseDimensionsFromString).collect(Collectors.toList());
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

  private static FabricDimensions parseDimensionsFromString(String inputString) {
    // #1 @ 1,3: 4x4
    String idStrippedInput = inputString.split("@")[1].strip();
    String[] dimensionString = idStrippedInput.split(":");
    String startPositions = dimensionString[0].strip();
    String sizes = dimensionString[1].strip();

    String[] startPositionsArray = startPositions.split(",");
    int startX = Integer.valueOf(startPositionsArray[0]);
    int starty = Integer.valueOf(startPositionsArray[1]);

    String[] sizesArray = sizes.split("x");
    int width = Integer.valueOf(sizesArray[0]);
    int height = Integer.valueOf(sizesArray[1]);

    return new FabricDimensions(startX, starty, width, height);
  }

  static class FabricDimensions {
    private final int startX;
    private final int startY;
    private final int width;
    private final int height;

    FabricDimensions(int startX, int startY, int width, int height) {
      this.startX = startX;
      this.startY = startY;
      this.width = width;
      this.height = height;
    }
  }
}
