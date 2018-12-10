package ee.aoc.day10;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {

  private static final Pattern INPUT_PATTERN = Pattern.compile("<(\\s*-?\\d+),\\s*(-?\\d+)>.*<\\s*(-?\\d+),\\s*(-?\\d+)>");
  private static final int I_HAVE_OTHER_STUFF_TO_DO_CANT_WAIT_TILL_INFINITY = 1_000_000;

  public static void main(String[] args) {
//    findMessage(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day10/test-input.txt"));
    findMessage(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day10/input.txt"));
  }

  static void findMessage(List<String> vectors) {
    List<Vector> vectorList = vectors.stream().map(Part1::getVectorFromInput).collect(Collectors.toList());
    long lowestBoundingRectArea = getAllCoordsBorderingRectangleArea(vectorList);
    List<Vector> lowestRectAreaVectorState = vectorList;
    int lowestRectAreaSecond = 0;

    for (int i = 1; i < I_HAVE_OTHER_STUFF_TO_DO_CANT_WAIT_TILL_INFINITY; i++) {
      performMove(vectorList);
      long allCoordsBorderingRectangleArea = getAllCoordsBorderingRectangleArea(vectorList);
      if (allCoordsBorderingRectangleArea < lowestBoundingRectArea) {
        lowestRectAreaVectorState = deepCopyVectorList(vectorList);
        lowestBoundingRectArea = allCoordsBorderingRectangleArea;
        lowestRectAreaSecond = i;
      }
    }

    Integer minX = lowestRectAreaVectorState.stream().map(x -> x.x).min(Comparator.naturalOrder()).get();
    Integer minY = lowestRectAreaVectorState.stream().map(x -> x.y).min(Comparator.naturalOrder()).get();
    Integer maxX= lowestRectAreaVectorState.stream().map(x -> x.x).max(Comparator.naturalOrder()).get();
    Integer maxY = lowestRectAreaVectorState.stream().map(x -> x.y).max(Comparator.naturalOrder()).get();

    for (int i = minY; i <= maxY; i++) {
      for (int j = minX; j <= maxX; j++) {
        if (isExistingCoord(j, i, lowestRectAreaVectorState)) {
          System.out.print("#");
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }

    System.out.println(lowestRectAreaSecond);
  }

  private static boolean isExistingCoord(int x, int y, List<Vector> vectors) {
    return vectors.stream().anyMatch(vector -> vector.x == x && vector.y == y);
  }

  private static void performMove(List<Vector> vectors) {
    vectors.forEach(Part1::moveVector);
  }

  private static void moveVector(Vector vector) {
    vector.y += vector.yVelocity;
    vector.x += vector.xVelocity;
  }

  private static List<Vector> deepCopyVectorList(List<Vector> vectors) {
    return vectors.stream().map(x -> new Vector(x.x, x.y, x.xVelocity, x.yVelocity)).collect(Collectors.toList());
  }

  private static long getAllCoordsBorderingRectangleArea(List<Vector> vectors) {
    long minX = vectors.stream().map(x -> (long) x.x).min(Comparator.naturalOrder()).get();
    long minY = vectors.stream().map(x -> (long) x.y).min(Comparator.naturalOrder()).get();
    long maxX= vectors.stream().map(x -> (long) x.x).max(Comparator.naturalOrder()).get();
    long maxY = vectors.stream().map(x -> (long) x.y).max(Comparator.naturalOrder()).get();

    return (maxX - minX) * (maxY - minY);
  }

  private static Vector getVectorFromInput(String input) {
    Matcher matcher = INPUT_PATTERN.matcher(input);
    if (matcher.find()) {
      return new Vector(Integer.valueOf(matcher.group(1).strip()), Integer.valueOf(matcher.group(2).strip()),
          Integer.valueOf(matcher.group(3).strip()), Integer.valueOf(matcher.group(4).strip()));
    }
    throw new RuntimeException("Garbage input!");
  }

  private static class Vector {
    private int x;
    private int y;
    private int xVelocity;
    private int yVelocity;

    private Vector(int x, int y, int xVelocity, int yVelocity) {
      this.x = x;
      this.y = y;
      this.xVelocity = xVelocity;
      this.yVelocity = yVelocity;
    }
  }
}
