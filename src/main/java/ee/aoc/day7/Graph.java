package ee.aoc.day7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<V> {
  private Set<V> vertices;
  private Map<V, Set<V>> edges;

  public Graph() {
    this.vertices = new HashSet<>();
    this.edges = new HashMap<>();
  }

  public void addVertex(V vertex) {
    vertices.add(vertex);
  }

  public void addEdge(V from, V to) {
    edges.computeIfAbsent(from, key -> new HashSet<>()).add(to);
  }

  public Set<V> getOutgoingAdjacentVertices(V vertex) {
    return edges.getOrDefault(vertex, Collections.emptySet());
  }

  public Set<V> getIncomingAdjacentVertices(V vertex) {
    return vertices.stream().filter(v -> edges.getOrDefault(v, new HashSet<>()).contains(vertex)).collect(Collectors.toSet());
  }

  public Set<V> getVertices() {
    return vertices;
  }

  public Map<V, Set<V>> getEdges() {
    return edges;
  }

  public void removeEdge(V from, V to) {
    edges.getOrDefault(from, new HashSet<>()).remove(to);
  }

  public List<V> getTopologicalOrdering(Comparator<V> comparator) {
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

    return res;
  }

  public Set<V> getVerticesWithoutIncomingEdge() {
    return vertices.stream().filter(vertex -> getIncomingAdjacentVertices(vertex).isEmpty()).collect(Collectors.toSet());
  }

}