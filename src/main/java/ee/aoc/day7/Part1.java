package ee.aoc.day7;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;

public class Part1 {

  public static void main(String[] args) {
    System.out.println(getTopologicalOrdering(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day7/input.txt")));
  }

  public static final Comparator<Character> COMPARATOR = new Comparator<>() {
    @Override
    public int compare(Character o1, Character o2) {
      return o1 - o2;
    }
  };

  static String getTopologicalOrdering(List<String> input) {
    Graph<Character> characterGraph = new Graph<>();
    for (String s : input) {
      String[] splitInputLine = s.split(" ");
      Character firstVertex = splitInputLine[1].charAt(0);
      Character secondVertex = splitInputLine[7].charAt(0);
      characterGraph.addVertex(firstVertex);
      characterGraph.addVertex(secondVertex);
      characterGraph.addEdge(firstVertex, secondVertex);
    }

    return characterGraph.getTopologicalOrdering(COMPARATOR).stream().map(x -> Character.toString(x)).collect(Collectors.joining(""));
  }

}
