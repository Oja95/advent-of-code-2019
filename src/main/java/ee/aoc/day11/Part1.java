package ee.aoc.day11;

public class Part1 {
  public static void main(String[] args) {
    System.out.println(getHighestPowerLevelMatrixCoords(5093));
    System.out.println(getHighestPowerLevelMatrixCoordsPart2(5093));
  }

  private static RectCoords getHighestPowerLevelMatrixCoords(int serialNumber) {
    int[][] matrix = getPowerLevelMatrix(serialNumber);

    int largestSum = 0;
    RectCoords bestCoords = null;

    for (int i = 1; i < 298; i++) {
      for (int j = 1; j < 298; j++) {
        int sum = getSum(matrix, i, j, 3);
        if (sum > largestSum) {
          largestSum = sum;
          bestCoords = new RectCoords(i, j, 3);
        }
      }
    }
    return bestCoords;
  }


  private static RectCoords getHighestPowerLevelMatrixCoordsPart2(int serialNumber) {
    int[][] matrix = getPowerLevelMatrix(serialNumber);

    int largestSum = 0;
    RectCoords bestCoords = null;

    for (int i = 1; i < 301; i++) {
      for (int j = 1; j < 301; j++) {
        for (int k = 1; k < 301 - Math.max(i, j); k++) { // square size
          int sum = getSum(matrix, i, j, k);
          if (sum > largestSum) {
            largestSum = sum;
            bestCoords = new RectCoords(i, j, k);
          }
        }
      }
    }

    return bestCoords;
  }

  private static int getSum(int[][] matrix, int i, int j, int k) {
    int sum = 0;
    for (int l = i; l < i + k; l++) {
      for (int m = j; m < j + k; m++) {
        sum += matrix[l][m];
      }
    }
    return sum;
  }


  private static int[][] getPowerLevelMatrix(int serialNumber) {
    int[][] matrix = new int[301][301]; // note to self, coords start at 1

    for (int i = 1; i < 301; i++) {
      for (int j = 1; j < 301; j++) {
        matrix[i][j] = calcPowerLevel(serialNumber, i, j);
      }
    }
    return matrix;
  }

  static int calcPowerLevel(int serialNumber, int i, int j) {
    int rackId = i + 10;
    int powerLevel = ((rackId * j) + serialNumber) * rackId;
    return ((powerLevel / 100) % 10) - 5;
  }

  static class RectCoords {
    private final int x;
    private final int y;
    private final int squareSize;

    public RectCoords(int x, int y, int squareSize) {
      this.x = x;
      this.y = y;
      this.squareSize = squareSize;
    }

    @Override
    public String toString() {
      return String.format("%d,%d,%d", x, y, squareSize);
    }
  }
}
