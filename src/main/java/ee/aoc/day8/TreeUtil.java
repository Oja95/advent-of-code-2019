package ee.aoc.day8;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtil {

  public static TreeNode readTreeFromStringInput(String inputString) {
    List<Integer> inputNums = Arrays.stream(inputString.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
    TreeNode root = readNode(inputNums.iterator());
    return root;
  }

  private static TreeNode readNode(Iterator<Integer> iterator) {
    Integer childCount = iterator.next();
    Integer metaDataCount = iterator.next();

    TreeNode treeNode = new TreeNode();
    for (int i = 0; i < childCount; i++) {
      TreeNode childNode = readNode(iterator);
      treeNode.addChildren(childNode);
    }

    for (int i = 0; i < metaDataCount; i++) {
      Integer metaDataEntry = iterator.next();
      treeNode.addMetaData(metaDataEntry);
    }

    return treeNode;
  }
}
