import java.util.*;

class Network {
  private ArrayList<Node> nodes = new ArrayList<>();
  private ArrayList<Edge> edges = new ArrayList<>();
  private IntensityMatrix intensityMatrix = null;
  private int averagePackageSize;

  Network() {}

  Network(IntensityMatrix intensityMatrix, int averagePackageSize) {
    this.intensityMatrix = intensityMatrix;
    this.averagePackageSize = averagePackageSize;
  }

  ArrayList<Node> getNodes() {
    return nodes;
  }

  ArrayList<Edge> getEdges() {
    return edges;
  }

  private void visitNode(Node node) {
    node.setVisited(true);
    for (Edge edge : node.getEdges()) {
      Node nextNode = edge.getWay(node);
      if (nextNode == null) {
        continue;
      }
      if (nextNode.isNotVisited()) {
        visitNode(nextNode);
      }
    }
  }

  boolean deepFirstSearch() {
    for (Node node : nodes) {
      node.setVisited(false);
    }
    if (nodes.size() == 0) {
      return true;
    }
    visitNode(nodes.get(0));
    for (Node node : nodes) {
      if (node.isNotVisited()) {
        return false;
      }
    }
    return true;
  }

  ArrayList<Edge> breadthFirstSearch(Node sourceNode, Node sinkNode) {
    ArrayList<Node> queue = new ArrayList<>();
    HashMap<Node, Edge> edgeToParent = new HashMap<>();
    for (Node node : nodes) {
      node.setVisited(false);
      edgeToParent.put(node, null);
    }
    queue.add(sourceNode);
    while (!queue.isEmpty()) {
      Node node = queue.remove(0);
      if (node == sinkNode) {
        ArrayList<Edge> way = new ArrayList<>();
        Edge edge = edgeToParent.get(node);
        while (node != sourceNode) {
          way.add(edge);
          node = edge.getWay(node);
          edge = edgeToParent.get(node);
        }
        return way;
      }
      for (Edge edge : node.getEdges()) {
        Node nextNode = edge.getWay(node);
        if (nextNode.isNotVisited()) {
          nextNode.setVisited(true);
          edgeToParent.replace(nextNode, edge);
          queue.add(nextNode);
        }
      }
    }
    return null;
  }

  void interval() {
    for (Edge edge : edges) {
      edge.testDurability();
    }
  }

  /**
   * Returns false if there is no way to find flows for each edge. In other words, if network isn't
   * connected. In this case, flow for each edge will be set as 0. Returns true if there is a way to
   * find flows for each edge. In other words, if network is connected.
   *
   * @return true if is possible to count all flows.
   */
  private boolean countFlows() {
    for (Edge edge : getEdges()) {
      edge.resetFlow();
    }
    if (!deepFirstSearch()) {
      return false;
    }
    for (int i = 0; i < getNodes().size(); i++) {
      for (int j = 0; j < getNodes().size(); j++) {
        if (i == j) {
          continue;
        }
        for (Edge edge : breadthFirstSearch(getNodes().get(i), getNodes().get(j))) {
          edge.increaseFlow(intensityMatrix.getIntensity(i, j));
        }
      }
    }
    return true;
  }

  double countAverageLatency() throws UnconnectedGraphException, EdgeOverflowException {
    if (!countFlows()) {
      throw new UnconnectedGraphException("Graph must be connected to check the latency.");
    }
    double delay = 0.0;
    for (Edge edge : getEdges()) {
      double replenishment = ((double) edge.getCapacity() / averagePackageSize) - edge.getFlow();
      if (replenishment <= 0) {
        throw new EdgeOverflowException("Flow on edge is greater than capacity of that edge.");
      }
      delay += edge.getFlow() / replenishment;
    }
    delay /= intensityMatrix.getSum();
    return delay;
  }
}
