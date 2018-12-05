package ee.aoc.day5;

import java.util.List;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

@SuppressWarnings("Duplicates")
public class Part2 {
  public static void main(String[] args) {
    System.out.println(getReactedPolymerSize(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day5/input.txt").get(0)));
  }

  public static int getReactedPolymerSize(String input) {
    List<Character> chars = input.chars().mapToObj(x -> (char) x).collect(Collectors.toList());

    int minsize = Integer.MAX_VALUE;

    List<Character> uniqueChars = chars.stream().map(Character::toUpperCase).distinct().collect(Collectors.toList());
    for (Character uniqueChar : uniqueChars) {
      List<Character> uniqueCharStrippedInput = chars.stream().filter(x -> Character.toUpperCase(x) != uniqueChar).collect(Collectors.toList());
      int strippedPolymerminSize = getStrippedPolymerminSize(uniqueCharStrippedInput);
      if (strippedPolymerminSize < minsize) {
        minsize = strippedPolymerminSize;
      }
    }

    return minsize;
  }

  private static int getStrippedPolymerminSize(List<Character> chars) {
    Character previous = 0;
    int previousId = 0;

    int i = 0;
    while (i < chars.size()) {
      if (chars.get(i).equals('.')) {
        i++;
        continue;
      }

      if (previous != chars.get(i) && Character.toUpperCase(previous) == Character.toUpperCase(chars.get(i))) {
        chars.set(i, '.');
        chars.set(previousId, '.');
        previous = 0;
        previousId = 0;
        i = 0;
        continue;
      }

      previousId = i;
      previous = chars.get(i);
      i++;
    }


    return ((int) chars.stream().filter(x -> x != '.').count());
  }
}

