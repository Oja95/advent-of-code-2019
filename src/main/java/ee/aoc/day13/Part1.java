package ee.aoc.day13;

import java.util.List;
import java.util.Map;

import ee.aoc.InputFileReaderUtil;

@SuppressWarnings("Duplicates")
public class Part1 {
  private static List<Character> CART_DIRS = List.of('^', '>', 'v', '<');
  private static final Map<Character, Character> CART_REPLACEMENT = Map.of('>', '-', '<', '-', '^', '|', 'v', '|');

  public static void main(String[] args) {
    getCollisionCoords(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day13/input.txt"));
  }
  
  private static void getCollisionCoords(List<String> linesFromClassPathResourceFile) {
    GridElement[][] matrix = new GridElement[150][150];
    char[][] initialStateMatrix = new char[150][150];
    for (int i = 0; i < linesFromClassPathResourceFile.size(); i++) {
      char[] chars = linesFromClassPathResourceFile.get(i).toCharArray();
      for (int j = 0; j < chars.length; j++) {
        matrix[i][j] = new GridElement(chars[j]);
        initialStateMatrix[i][j] = CART_REPLACEMENT.getOrDefault(chars[j], chars[j]);
      }
    }
    
    // what have i done! dont try this at home
    while (true) {

      int remainingCarts = getRemainingCarts(matrix);
      if (remainingCarts < 2) break;

      for (int i = 0; i < 150; i++) {
        for (int j = 0; j < 150; j++) {
          GridElement current = matrix[i][j];
          if (current.moved) {
            current.moved = false;
            continue;
          }
          if (current.element == '>') {
            GridElement next = matrix[i][j + 1];
            if (next.element == '-') {
              matrix[i][j + 1].element = '>';
              matrix[i][j + 1].moved = true;
              matrix[i][j + 1].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '/') {
              matrix[i][j + 1].element = '^';
              matrix[i][j + 1].moved = true;
              matrix[i][j + 1].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '\\') {
              matrix[i][j + 1].element = 'v';
              matrix[i][j + 1].moved = true;
              matrix[i][j + 1].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (isCart(next)) {
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
              matrix[i][j + 1].element = initialStateMatrix[i][j + 1];
              matrix[i][j + 1].cartDirection = 0;
              System.out.println("collision at " + (j + 1) + " " + i);
            }
            else if (next.element == '+') {
              int directionIndex = CART_DIRS.indexOf(current.element);
              matrix[i][j].element = initialStateMatrix[i][j];
              if (current.cartDirection == 0) {
                matrix[i][j + 1].element = CART_DIRS.get(directionIndex - 1 == -1 ? 3 : directionIndex - 1);
              }
              else if (current.cartDirection == 1) {
                matrix[i][j + 1].element = CART_DIRS.get(directionIndex);
              }
              else {
                matrix[i][j + 1].element = CART_DIRS.get((directionIndex + 1) % 4);
              }
              matrix[i][j + 1].cartDirection = (current.cartDirection + 1) % 3;
              matrix[i][j + 1].moved = true;
              matrix[i][j].cartDirection = 0;
            }
            else {
              if (next.element != ' ' && next.element != '|') throw new RuntimeException("wat1");
            }
          }
          else if (current.element == '^') {
            GridElement next = matrix[i - 1][j];
            if (next.element == '|') {
              matrix[i - 1][j].element = '^';
              matrix[i - 1][j].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '/') {
              matrix[i - 1][j].element = '>';
              matrix[i - 1][j].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '\\') {
              matrix[i - 1][j].element = '<';
              matrix[i - 1][j].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (isCart(next)) {
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
              matrix[i - 1][j].element = initialStateMatrix[i - 1][j];
              matrix[i - 1][j].cartDirection = 0;
              System.out.println("collision at " + (i - 1) + " " + j);
            }
            else if (next.element == '+') {
              int directionIndex = CART_DIRS.indexOf(current.element);
              matrix[i][j].element = initialStateMatrix[i][j];
              if (current.cartDirection == 0) {
                matrix[i - 1][j].element = CART_DIRS.get(directionIndex - 1 == -1 ? 3 : directionIndex - 1);
              }
              else if (current.cartDirection == 1) {
                matrix[i - 1][j].element = CART_DIRS.get(directionIndex);
              }
              else {
                matrix[i - 1][j].element = CART_DIRS.get((directionIndex + 1) % 4);
              }
              matrix[i - 1][j].cartDirection = (current.cartDirection + 1) % 3;
              matrix[i][j].cartDirection = 0;
            }
            else {
              if (next.element != ' ' && next.element != '-') throw new RuntimeException("wat2");
            }
          }
          else if (current.element == '<') {
            GridElement next = matrix[i][j - 1];
            if (next.element == '-') {
              matrix[i][j - 1].element = '<';
              matrix[i][j - 1].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '/') {
              matrix[i][j - 1].element = 'v';
              matrix[i][j - 1].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '\\') {
              matrix[i][j - 1].element = '^';
              matrix[i][j - 1].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (isCart(next)) {
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
              matrix[i][j - 1].element = initialStateMatrix[i][j - 1];
              matrix[i][j - 1].cartDirection = 0;
              System.out.println("collision at " + (j - 1) + " " + i);
            }
            else if (next.element == '+') {
              int directionIndex = CART_DIRS.indexOf(current.element);
              matrix[i][j].element = initialStateMatrix[i][j];
              if (current.cartDirection == 0) {
                matrix[i][j - 1].element = CART_DIRS.get(directionIndex - 1 == -1 ? 3 : directionIndex - 1);
              }
              else if (current.cartDirection == 1) {
                matrix[i][j - 1].element = CART_DIRS.get(directionIndex);
              }
              else {
                matrix[i][j - 1].element = CART_DIRS.get((directionIndex + 1) % 4);
              }
              matrix[i][j - 1].cartDirection = (current.cartDirection + 1) % 3;
              matrix[i][j].cartDirection = 0;
            }
            else {
              if (next.element != ' ' && next.element != '|') throw new RuntimeException("wat2");
            }
          }
          else if (current.element == 'v') {
            GridElement next = matrix[i + 1][j];
            if (next.element == '|') {
              matrix[i + 1][j].element = 'v';
              matrix[i + 1][j].moved = true;
              matrix[i + 1][j].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '/') {
              matrix[i + 1][j].element = '<';
              matrix[i + 1][j].moved = true;
              matrix[i + 1][j].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (next.element == '\\') {
              matrix[i + 1][j].element = '>';
              matrix[i + 1][j].moved = true;
              matrix[i + 1][j].cartDirection = matrix[i][j].cartDirection;
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
            }
            else if (isCart(next)) {
              matrix[i][j].element = initialStateMatrix[i][j];
              matrix[i][j].cartDirection = 0;
              matrix[i + 1][j].element = initialStateMatrix[i + 1][j];
              matrix[i + 1][j].cartDirection = 0;
              System.out.println("collision at " + j + " " + (i + 1));
            }
            else if (next.element == '+') {
              int directionIndex = CART_DIRS.indexOf(current.element);
              matrix[i][j].element = initialStateMatrix[i][j];
              if (current.cartDirection == 0) {
                matrix[i + 1][j].element = CART_DIRS.get(directionIndex - 1 == -1 ? 3 : directionIndex - 1);
              }
              else if (current.cartDirection == 1) {
                matrix[i + 1][j].element = CART_DIRS.get(directionIndex);
              }
              else {
                matrix[i + 1][j].element = CART_DIRS.get((directionIndex + 1) % 4);
              }
              matrix[i + 1][j].cartDirection = (current.cartDirection + 1) % 3;
              matrix[i + 1][j].moved = true;
              matrix[i][j].cartDirection = 0;
            }
            else {
              if (next.element != ' ' && next.element != '-') throw new RuntimeException("wat2");
            }
          }
        }
      }
    }

    int lastXCoord = 0;
    int lastYCoord = 0;
    
    for (int i = 0; i < 150; i++) {
      for (int j = 0; j < 150; j++) {
        if (CART_DIRS.contains(matrix[i][j].element)) {
          lastXCoord = j;
          lastYCoord = i;
        }
        System.out.print(matrix[i][j]);
      }
      System.out.println();
    }

    System.out.println(lastXCoord);
    System.out.println(lastYCoord);
    

  }

  private static int getRemainingCarts(GridElement[][] matrix) {
    int count = 0;
    for (int i = 0; i < 150; i++) {
      for (int j = 0; j < 150; j++) {
        if (CART_DIRS.contains(matrix[i][j].element)) {
          count++;
        }
      }
    }
    return count;
  }
  
  private static boolean isCart(GridElement next) {
    return next.element == '<' || next.element == '^' || next.element == 'v' || next.element == '>';
  }


  static class GridElement {
    private char element;
    private int cartDirection; // if it is a cart this decides whether to turn or not.
    private boolean moved;

    public GridElement(char element) {
      this.element = element;
    }

    @Override
    public String toString() {
      return Character.toString(element);
    }
  }
}
