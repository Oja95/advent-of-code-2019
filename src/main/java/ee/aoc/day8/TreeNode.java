package ee.aoc.day8;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

  private final List<Integer> metadata = new ArrayList<>();
  private final List<TreeNode> children = new ArrayList<>();

  public void addMetaData(Integer data) {
    metadata.add(data);
  }

  public void addChildren(TreeNode node) {
    children.add(node);
  }

  public List<Integer> getMetadata() {
    return metadata;
  }

  public List<TreeNode> getChildren() {
    return children;
  }

  public int getNodeValue() {
    if (children.isEmpty()) {
      return metadata.stream().mapToInt(x -> x).sum();
    }

    int sum = 0;
    for (Integer metadatum : metadata) {
      if (metadatum > 0 && children.size() >= metadatum) {
        TreeNode childNode = children.get(metadatum - 1);
        sum += childNode.getNodeValue();
      }
    }

    return sum;
  }
}
