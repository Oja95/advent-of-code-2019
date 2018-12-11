package ee.aoc.day11;

@SuppressWarnings("ALL")
public class Part1 {
  public static void main(String[] args) {

    System.out.println(getHighestPowerLevelMatrixCoords(5093));
    System.out.println(getHighestPowerLevelMatrixCoordsPart2(5093));
//    System.out.println(calcPowerLevel(8, 3, 5));
//    System.out.println(calcPowerLevel(57, 122, 79));
//    System.out.println(calcPowerLevel(39, 217, 196));
//    System.out.println(calcPowerLevel(71, 101, 153));


  }

  private static Coords getHighestPowerLevelMatrixCoords(int serialNumber) {
    int[][] matrix = new int[301][301];
    // note to self, coords start at 1

    for (int i = 1; i < 301; i++) {
      for (int j = 1; j < 301; j++) {
        matrix[i][j] = calcPowerLevel(serialNumber, i, j);
      }
    }

    int largestSum = 0;
    Coords bestCoords = null;

    for (int i = 1; i < 298; i++) {
      for (int j = 1; j < 298; j++) {
        int sum = 0;
        for (int k = i; k < i + 3; k++) {
          for (int l = j; l < j + 3; l++) {
            sum += matrix[k][l];
          }
        }
        if (sum > largestSum) {
          largestSum = sum;
          bestCoords = new Coords(i, j);
        }
      }
    }
    return bestCoords;
  }

  private static Coords getHighestPowerLevelMatrixCoordsPart2(int serialNumber) {
    int[][] matrix = new int[301][301];
    // note to self, coords start at 1

    for (int i = 1; i < 301; i++) {
      for (int j = 1; j < 301; j++) {
        matrix[i][j] = calcPowerLevel(serialNumber, i, j);
      }
    }

    int largestSum = 0;
    Coords bestCoords = null;
    int bestSquareSize = 0;

    for (int i = 1; i < 301; i++) {
      for (int j = 1; j < 301; j++) {
        for (int k = 1; k < 301 - Math.max(i, j); k++) { // square size


          int sum = 0;
          for (int l = i; l < i + k; l++) {
            for (int m = j; m < j + k; m++) {
              sum += matrix[l][m];
            }
          }

          if (sum > largestSum) {
            largestSum = sum;
            bestCoords = new Coords(i, j);
            bestSquareSize = k;
          }
        }
      }
    }

    System.out.println(bestSquareSize);
    return bestCoords;
  }

  private static int calcPowerLevel(int serialNumber, int i, int j) {
    int rackId = i + 10;
    int powerLevel = (rackId * j) + serialNumber;
    powerLevel *= rackId;
    powerLevel = (powerLevel / 100) % 10;
    powerLevel -= 5;
    return powerLevel;
  }

  static class Coords {
    private int x;
    private int y;

    public Coords(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return String.format("<%d, %d>", x, y);
    }
  }
}
