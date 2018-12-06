package ee.aoc.day6;

class Day6Util {
  static int getManhattanDistanceToPoint(Coordinates start, Coordinates end) {
    return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
  }

  static Coordinates stringToCoords(String input) {
    String[] split = input.split(",");
    return Coordinates.of(Integer.valueOf(split[0].strip()), Integer.valueOf(split[1].strip()));
  }
}
