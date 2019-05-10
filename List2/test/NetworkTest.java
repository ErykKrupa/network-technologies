import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {
  @Test
  void shouldCheckConsistencyCorrectly() {
    for (int i = 0; i <= 1000; i++) {
      Network network = new Network();
      ArrayList<Node> nodes = network.getNodes();
      for (int j = 0; j < i; j++) {
        nodes.add(new Node());
      }
      ArrayList<Edge> edges = network.getEdges();
      for (int j = 0; j < i - 1; j++) {
        edges.add(new Edge(nodes.get(j), nodes.get(j + 1), 0.95));
      }
      network.interval();
      boolean isConsistency = true;
      for (Edge edge : edges) {
        if (!edge.isUndamaged()) {
          isConsistency = false;
          break;
        }
      }
      assertEquals(network.deepFirstSearch(), isConsistency);
    }
  }

  @Test
  void shouldCheckTheShortestWaysCorrectly() {
    Network network = new Network();
    ArrayList<Node> nodes = network.getNodes();
    for (int i = 0; i < 10; i++) {
      nodes.add(new Node());
    }
    ArrayList<Edge> edges = network.getEdges();
    for (int i = 0; i <= 7; i++) {
      edges.add(new Edge(nodes.get(i), nodes.get((i + 1) % 8), 1));
    }
    for (int i = 3; i <= 8; i++) {
      edges.add(new Edge(nodes.get(9), nodes.get(i), 1));
    }
    for (int i = 0; i <= 3; i++) {
      edges.add(new Edge(nodes.get(8), nodes.get(i), 1));
    }
    edges.add(new Edge(nodes.get(7), nodes.get(8), 1));
    for (int i = 0; i < nodes.size(); i++) {
      for (int j = 0; j < nodes.size(); j++) {
        assertEquals(
            network.breadthFirstSearch(nodes.get(i), nodes.get(j)).size(),
            network.breadthFirstSearch(nodes.get(j), nodes.get(i)).size());
        assertTrue(4 > network.breadthFirstSearch(nodes.get(j), nodes.get(i)).size());
      }
    }
    assertEquals(0, network.breadthFirstSearch(nodes.get(0), nodes.get(0)).size());
    assertEquals(0, network.breadthFirstSearch(nodes.get(9), nodes.get(9)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(2), nodes.get(3)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(5), nodes.get(4)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(9), nodes.get(8)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(0), nodes.get(7)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(3), nodes.get(9)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(8), nodes.get(2)).size());
    assertEquals(1, network.breadthFirstSearch(nodes.get(5), nodes.get(9)).size());
    assertEquals(2, network.breadthFirstSearch(nodes.get(9), nodes.get(2)).size());
    assertEquals(2, network.breadthFirstSearch(nodes.get(0), nodes.get(3)).size());
    assertEquals(3, network.breadthFirstSearch(nodes.get(5), nodes.get(1)).size());
    assertEquals(3, network.breadthFirstSearch(nodes.get(6), nodes.get(2)).size());
    assertEquals(3, network.breadthFirstSearch(nodes.get(0), nodes.get(4)).size());
  }
}
