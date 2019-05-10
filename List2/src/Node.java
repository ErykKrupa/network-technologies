import java.util.HashSet;

class Node {
  private HashSet<Edge> edges = new HashSet<>();
  private boolean visited = false;

  void addEdge(Edge edge) {
    edges.add(edge);
  }

  HashSet<Edge> getEdges() {
    return edges;
  }

  boolean isNotVisited() {
    return !visited;
  }

  void setVisited(boolean visited) {
    this.visited = visited;
  }

  void removeEdge(Edge edge) {
    edges.remove(edge);
  }
}
