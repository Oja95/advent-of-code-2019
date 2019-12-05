package ee.aoc.day1;

import java.util.List;

import ee.aoc.InputFileReaderUtil;

public class Main {
  public static void main(String[] args) {
    List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day1.txt");
    partOne(linesFromClassPathResourceFile);
    partTwo(linesFromClassPathResourceFile);
  }

  private static void partTwo(List<String> linesFromClassPathResourceFile) {
    System.out.println(linesFromClassPathResourceFile.stream()
            .mapToInt(Integer::parseInt)
            .map(Main::getFuelForModule)
            .sum());
  }

  private static int getFuelForModule(int input) {
    int i = calculateFuelMass(input);
    return i > 0 ? i + getFuelForModule(i) : 0;
  }

  private static void partOne(List<String> linesFromClassPathResourceFile) {
    System.out.println(linesFromClassPathResourceFile.stream()
            .mapToInt(Integer::parseInt)
            .map(Main::calculateFuelMass)
            .sum());
  }

  private static int calculateFuelMass(int x) {
    return x / 3 - 2;
  }
}
