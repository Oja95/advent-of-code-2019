package ee.aoc.day8;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import ee.aoc.InputFileReaderUtil;

public class Part1 {
  public static void main(String[] args) {
    System.out.println(getMetaDataSum(InputFileReaderUtil.getLinesFromClassPathResourceFile("/day8/input.txt")));
  }

  static int getMetaDataSum(List<String> input) {
    TreeNode root = TreeUtil.readTreeFromStringInput(input.get(0));

    Queue<TreeNode> nodeQueue = new ArrayDeque<>();
    nodeQueue.add(root);

    int sum = 0;
    while (!nodeQueue.isEmpty()) {
      TreeNode node = nodeQueue.poll();
      nodeQueue.addAll(node.getChildren());

      sum += node.getMetadata().stream().mapToInt(x -> x).sum();
    }

    return sum;
  }


}
