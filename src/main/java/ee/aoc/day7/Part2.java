package ee.aoc.day7;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.C;

import ee.aoc.InputFileReaderUtil;

@SuppressWarnings("ALL")
public class Part2 {

  public static final Comparator<Character> COMPARATOR = new Comparator<>() {
    @Override
    public int compare(Character o1, Character o2) {
      return o1 - o2;
    }
  };

  public static void main(String[] args) throws CloneNotSupportedException {
    System.out.println(getConurrentExecutionTime(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day7/input.txt"), 60, 5));
  }

  static int getConurrentExecutionTime(List<String> input, int extraSeconds, int executors) {
    Graph<Character> characterGraph = new Graph<>();
    for (String s : input) {
      String[] splitInputLine = s.split(" ");
      Character firstVertex = splitInputLine[1].charAt(0);
      Character secondVertex = splitInputLine[7].charAt(0);
      characterGraph.addVertex(firstVertex);
      characterGraph.addVertex(secondVertex);
      characterGraph.addEdge(firstVertex, secondVertex);
    }

    int totalTasks = characterGraph.getVertices().size();
    var finishedTasks = new HashSet<Character>();
    var inProgressTasks = new HashSet<Character>();
    var availableExecutors = executors;
    int time = 0;

    List<Character> verticesWithoutIncomingEdge = new ArrayList<>(characterGraph.getVerticesWithoutIncomingEdge());
    verticesWithoutIncomingEdge.sort(COMPARATOR);
    while (totalTasks != finishedTasks.size()) {
      for (int i = 0; i < Math.min(verticesWithoutIncomingEdge.size(), availableExecutors); i++) {
        availableExecutors--;
        Character character = verticesWithoutIncomingEdge.get(i);
        inProgressTasks.add(character);
      }

      time++;
    }


    return time;
  }
  /*
  List<V> res = new ArrayList<>();

    List<V> verticesWithoutIncomingEdge = new ArrayList<>(getVerticesWithoutIncomingEdge());
    verticesWithoutIncomingEdge.sort(comparator);
    while (!verticesWithoutIncomingEdge.isEmpty()) {
      V v = verticesWithoutIncomingEdge.get(0);
      verticesWithoutIncomingEdge.remove(v);

      res.add(v);
      Set<V> outgoingAdjacentVertices = getOutgoingAdjacentVertices(v);
      for (V incomingAdjacentVertex : new HashSet<>(outgoingAdjacentVertices)) {
        removeEdge(v, incomingAdjacentVertex);
        if (getIncomingAdjacentVertices(incomingAdjacentVertex).isEmpty()) {
          verticesWithoutIncomingEdge.add(incomingAdjacentVertex);
          verticesWithoutIncomingEdge.sort(comparator);
        }
      }
    }

   */


  private static int mapTaskToSeconds(int secondCompensation, Character task) {
    return (int) task - 64 + secondCompensation;
  }

}
