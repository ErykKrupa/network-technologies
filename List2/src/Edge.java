import java.util.Random;

class Edge {
  private Node firstNode;
  private Node secondNode;
  private double reliability;
  private boolean undamaged = true;
  private int capacity = 0;
  private int flow = 0;
  private static Random random = new Random();

  Edge(Node firstNode, Node secondNode, double reliability) {
    if (firstNode == secondNode) {
      throw new NotDifferentNodesException("Cannot create path without two different nodes.");
    }
    this.firstNode = firstNode;
    this.secondNode = secondNode;
    this.reliability = reliability;
    firstNode.addEdge(this);
    secondNode.addEdge(this);
  }

  Edge(Node firstNode, Node secondNode, double reliability, int capacity) {
    this(firstNode, secondNode, reliability);
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    this.capacity = capacity;
  }

  Node getWay(Node node) {
    if (node == null) {
      return null;
    }
    if (node != firstNode && node != secondNode) {
      throw new IllegalNodeException("Only node that is connected with edge can pass.");
    }
    if (node == firstNode) {
      return secondNode;
    } else {
      return firstNode;
    }
  }

  void testDurability() {
    if (undamaged && random.nextDouble() > reliability) {
      firstNode.removeEdge(this);
      secondNode.removeEdge(this);
      firstNode = null;
      secondNode = null;
      undamaged = false;
    }
  }

  boolean isUndamaged() {
    return undamaged;
  }

  int getCapacity() {
    return capacity;
  }

  int getFlow() {
    return flow;
  }

  void increaseFlow(int influx) {
    if (influx < 0) {
      throw new IllegalArgumentException();
    }
    flow += influx;
  }

  void resetFlow() {
    flow = 0;
  }
}
