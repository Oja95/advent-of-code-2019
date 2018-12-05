package ee.aoc.day5;

import java.util.List;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {
  public static void main(String[] args) {
    System.out.println(getReactedPolymerSize(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day5/input.txt").get(0)));
  }

  public static int getReactedPolymerSize(String input) {
    List<Character> collect = input.chars().mapToObj(x -> (char) x).collect(Collectors.toList());

    Character previous = 0;
    int previousId = 0;

    int i = 0;
    while (i < collect.size()) {
      if (collect.get(i).equals('.')) {
        i++;
        continue;
      }

      if (previous != collect.get(i) && Character.toUpperCase(previous) == Character.toUpperCase(collect.get(i))) {
        collect.set(i, '.');
        collect.set(previousId, '.');
        previous = 0;
        previousId = 0;
        i = 0;
        continue;
      }

      previousId = i;
      previous = collect.get(i);
      i++;
    }


    return ((int) collect.stream().filter(x -> x != '.').count());
  }
}
