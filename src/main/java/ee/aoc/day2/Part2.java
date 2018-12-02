package ee.aoc.day2;

import java.util.List;

import ee.aoc.InputFileReaderUtil;

public class Part2 {

  public static void main(String[] args) {
    System.out.println(getCorrectBoxesCommonLetters(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day2/input.txt")));
  }

  static String getCorrectBoxesCommonLetters(List<String> idList) {
    for (int i = 0; i < idList.size() - 1; i++) {
      for (int j = 0; j < i + 1; j++) {
        String s = idList.get(i);
        String stringsIntersection = getStringsIntersection(s, idList.get(j));
        if (stringsIntersection.length() == s.length() - 1) return stringsIntersection;
      }
    }

    throw new IllegalStateException("Correct boxes not found!");
  }


  private static String getStringsIntersection(String first, String second) {
    var sb = new StringBuilder();
    for (int i = 0; i < Math.min(first.length(), second.length()); i++) {
      if (first.charAt(i) == second.charAt(i)) sb.append(first.charAt(i));
    }

    return sb.toString();
  }

}
