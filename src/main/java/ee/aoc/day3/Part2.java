package ee.aoc.day3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;
import ee.aoc.day3.InputParserUtil.FabricDimensions;

public class Part2 {
  public static void main(String[] args) {
    System.out.println(getDistinctFabricId(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day3/input.txt")));
  }

  static int getDistinctFabricId(List<String> inputList) {
    // I hope nobody ever sees this wtf O(n^3) 'solution'
    Set[][] wtf = new HashSet[1000][1000];

    List<FabricDimensions> collect = inputList.stream().map(InputParserUtil::parseDimensionsFromString).collect(Collectors.toList());
    for (FabricDimensions fabricDimensions : collect) {
      for (int i = fabricDimensions.startX; i < fabricDimensions.startX + fabricDimensions.width; i++) {
        for (int j = fabricDimensions.startY; j < fabricDimensions.startY + fabricDimensions.height; j++) {
          Set set = wtf[i][j];
          if (set == null) wtf[i][j] = new HashSet<Integer>();
          wtf[i][j].add(fabricDimensions.id);
        }
      }
    }

    for (FabricDimensions fabricDimensions : collect) {
      int area = fabricDimensions.height * fabricDimensions.width;
      int count = 0;
      for (int i = 0; i < wtf.length; i++) {
        for (int j = 0; j < wtf.length; j++) {
          Set set = wtf[i][j];
          if (set != null && set.size() == 1 && set.contains(fabricDimensions.id)) count++;
        }
      }
      if (count == area) return fabricDimensions.id;
    }

    throw new IllegalStateException("Non-overlapping fabric doesn't exist!");
  }
}
