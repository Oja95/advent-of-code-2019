package ee.aoc.day3;

public class InputParserUtil {
  static FabricDimensions parseDimensionsFromString(String inputString) {
    // #1 @ 1,3: 4x4
    String[] splitInput = inputString.split("@");
    int id = Integer.valueOf(splitInput[0].strip().split("#")[1]);
    String[] dimensionString = splitInput[1].strip().split(":");
    String startPositions = dimensionString[0].strip();
    String sizes = dimensionString[1].strip();

    String[] startPositionsArray = startPositions.split(",");
    int startX = Integer.valueOf(startPositionsArray[0]);
    int starty = Integer.valueOf(startPositionsArray[1]);

    String[] sizesArray = sizes.split("x");
    int width = Integer.valueOf(sizesArray[0]);
    int height = Integer.valueOf(sizesArray[1]);

    return new FabricDimensions(id, startX, starty, width, height);
  }

  static class FabricDimensions {
    final int id;
    final int startX;
    final int startY;
    final int width;
    final int height;

    FabricDimensions(int id, int startX, int startY, int width, int height) {
      this.id = id;
      this.startX = startX;
      this.startY = startY;
      this.width = width;
      this.height = height;
    }
  }
}
