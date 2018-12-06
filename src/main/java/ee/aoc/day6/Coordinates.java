package ee.aoc.day6;

class Coordinates {
  final int x;
  final int y;

  private Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  static Coordinates of(int i, int j) {
    return new Coordinates(i, j);
  }
}
