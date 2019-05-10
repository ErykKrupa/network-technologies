import java.util.ArrayList;
import java.util.Random;

public class Main1 {
  private static Random random = new Random();
  private static final int INTERVALS = 6;
  private static final int EXPERIMENTS = 1_000;

  public static void main(String[] args) {
    for (int k = 1; k <= 4; k++) {
      int[] consistent = new int[INTERVALS];
      for (int i = 0; i < EXPERIMENTS; i++) {
        Network network = new Network();
        ArrayList<Node> nodes = network.getNodes();
        for (int j = 0; j < 20; j++) {
          nodes.add(new Node());
        }
        ArrayList<Edge> edges = network.getEdges();
        for (int j = 0; j < 19; j++) {
          edges.add(new Edge(nodes.get(j), nodes.get(j + 1), 0.95));
        }
        if (k >= 2) {
          edges.add(new Edge(nodes.get(0), nodes.get(19), 0.95));
        }
        if (k >= 3) {
          edges.add(new Edge(nodes.get(0), nodes.get(9), 0.8));
          edges.add(new Edge(nodes.get(4), nodes.get(14), 0.7));
        }
        if (k == 4) {
          for (int j = 0; j < 4; j++) {
            Node firstNode;
            Node secondNode;
            do {
              firstNode = nodes.get(random.nextInt(20));
              secondNode = nodes.get(random.nextInt(20));
            } while (firstNode == secondNode);
            edges.add(new Edge(firstNode, secondNode, 0.4));
          }
        }
        for (int j = 0; j < INTERVALS; j++) {
          network.interval();
          if (network.deepFirstSearch()) {
            consistent[j]++;
          } else {
            break;
          }
        }
      }
      System.out.println("Subpoint " + k + ".");
      for (int i = 0; i < INTERVALS; i++) {
        System.out.println(
            "Interval "
                + (i + 1)
                + ": "
                + ((double) consistent[i] * 100 / EXPERIMENTS)
                + "% of consistency.");
      }
    }
  }
}
