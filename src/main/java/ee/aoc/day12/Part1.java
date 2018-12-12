package ee.aoc.day12;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ee.aoc.InputFileReaderUtil;

public class Part1 {
  public static void main(String[] args) {
    System.out.println(getIndicesSum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day12/input.txt")));
  }

  static int getIndicesSum(List<String> input) {
    String initialState = input.get(0);
    Pattern compile = Pattern.compile("initial state: (.*)");
    Matcher matcher1 = compile.matcher(initialState);
    if (matcher1.find()) {
      initialState = matcher1.group(1);
    }
    else {
      throw new RuntimeException("Garbage input!");
    }


    String padding = ".".repeat(20);
    String state = padding + initialState + padding;

    List<PotRotation> potRotations = new ArrayList<>();

    Pattern inputPattern = Pattern.compile("(.....) => (.)");
    for (int i = 2; i < input.size(); i++) {
      Matcher matcher = inputPattern.matcher(input.get(i));
      if (matcher.find()) {
        potRotations.add(new PotRotation(matcher.group(1), matcher.group(2).charAt(0)));
      }
      else {
        throw new RuntimeException("Garbage input!");
      }
    }
    for (long i = 0; i < 20L; i++) {

      char[] chars = state.toCharArray();
      for (int j = 2; j < state.length() - 2; j++) {
        String currentPattern = new String(new char[]{state.charAt(j - 2), state.charAt(j - 1), state.charAt(j), state.charAt(j + 1), state.charAt(j + 2)});
        char replacement = '.';
        for (PotRotation potRotation : potRotations) {
          if (currentPattern.equals(potRotation.pattern)) {
            replacement = potRotation.result;
          }
        }
        chars[j] = replacement;
      }
      state = new String(chars);

    }

    System.out.println(state);

    int sum = 0;
    for (int i = 0; i < state.length(); i++) {
      if (state.charAt(i) == '#') {
        sum += i - 20;
      }
    }

    System.out.println(sum);
    return 0;
  }

  static class PotRotation {
    private String pattern;
    private char result;

    public PotRotation(String pattern, char result) {
      this.pattern = pattern;
      this.result = result;
    }
  }
}
