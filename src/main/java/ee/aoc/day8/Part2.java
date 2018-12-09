package ee.aoc.day8;

import java.util.List;

import ee.aoc.InputFileReaderUtil;

@SuppressWarnings("ALL")
public class Part2 {
  public static void main(String[] args) {
    System.out.println(getTreeRootNodeValue(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day8/input.txt")));
  }

  static int getTreeRootNodeValue(List<String> input) {
    return TreeUtil.readTreeFromStringInput(input.get(0)).getNodeValue();
  }
}
