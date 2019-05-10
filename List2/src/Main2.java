import java.util.ArrayList;

/*
{0, 4, 5, 3, 2, 3, 2, 2, 3, 3}
{1, 0, 2, 3, 3, 3, 3, 5, 3, 2}
{1, 4, 0, 3, 2, 3, 2, 3, 3, 2}
{2, 2, 5, 0, 3, 1, 3, 3, 3, 4}
{1, 4, 5, 3, 0, 3, 3, 0, 3, 3}
{1, 5, 1, 3, 3, 0, 3, 3, 3, 0}
{1, 4, 5, 3, 3, 3, 0, 3, 2, 1}
{5, 1, 5, 3, 3, 2, 3, 0, 3, 3}
{1, 4, 1, 3, 3, 0, 3, 3, 0, 2}
{1, 4, 5, 3, 3, 2, 3, 3, 3, 0}
 */
public class Main2 {
  private static final double RELIABILITY = 0.9;
  private static final int CAPACITY = 55_000;
  private static final int AVERAGE_PACKAGE_SIZE = 1000;
  private static final double MAX_AVERAGE_LATENCY = 0.1;
  private static final int EXPERIMENTS = 1_000_000;

  public static void main(String[] args) {
    int unconnectedGraphs = 0;
    int overflows = 0;
    int overLatencies = 0;
    int passedTests = 0;
    for (int j = 0; j < EXPERIMENTS; j++) {
      IntensityMatrix intensityMatrix = new IntensityMatrix(10);
      intensityMatrix.setRow(0, new int[] {0, 4, 5, 3, 1, 3, 2, 2, 3, 3});
      intensityMatrix.setRow(1, new int[] {1, 0, 2, 3, 3, 3, 3, 1, 3, 2});
      intensityMatrix.setRow(2, new int[] {1, 4, 0, 3, 2, 3, 2, 3, 3, 2});
      intensityMatrix.setRow(3, new int[] {1, 2, 5, 0, 3, 1, 3, 3, 3, 4});
      intensityMatrix.setRow(4, new int[] {1, 4, 5, 3, 0, 3, 3, 0, 3, 3});
      intensityMatrix.setRow(5, new int[] {1, 5, 1, 3, 3, 0, 3, 3, 3, 0});
      intensityMatrix.setRow(6, new int[] {1, 4, 5, 3, 3, 3, 0, 3, 2, 1});
      intensityMatrix.setRow(7, new int[] {1, 1, 5, 1, 3, 2, 3, 0, 3, 3});
      intensityMatrix.setRow(8, new int[] {1, 4, 1, 3, 3, 0, 3, 3, 0, 2});
      intensityMatrix.setRow(9, new int[] {1, 4, 5, 3, 3, 2, 3, 3, 3, 0});
      Network network = new Network(intensityMatrix, AVERAGE_PACKAGE_SIZE);
      ArrayList<Node> nodes = network.getNodes();
      for (int i = 0; i < 10; i++) {
        nodes.add(new Node());
      }
      ArrayList<Edge> edges = network.getEdges();
      for (int i = 0; i <= 7; i++) {
        edges.add(new Edge(nodes.get(i), nodes.get((i + 1) % 8), RELIABILITY, CAPACITY));
      }
      for (int i = 3; i <= 8; i++) {
        edges.add(new Edge(nodes.get(9), nodes.get(i), RELIABILITY, CAPACITY));
      }
      for (int i = 0; i <= 3; i++) {
        edges.add(new Edge(nodes.get(8), nodes.get(i), RELIABILITY, CAPACITY));
      }
      edges.add(new Edge(nodes.get(7), nodes.get(8), RELIABILITY, CAPACITY));
      network.interval();
      double latency;
      try {
        latency = network.countAverageLatency();
        if (latency < MAX_AVERAGE_LATENCY) {
          passedTests++;
        } else {
          overLatencies++;
        }
      } catch (EdgeOverflowException ex) {
        overflows++;
      } catch (UnconnectedGraphException ex) {
        unconnectedGraphs++;
      }
    }
    System.out.println(
        "Unconnected graphs: "
            + unconnectedGraphs
            + "/"
            + EXPERIMENTS
            + ", "
            + (double) 100 * unconnectedGraphs / EXPERIMENTS
            + "%");
    System.out.println(
        "Overflows: "
            + overflows
            + "/"
            + EXPERIMENTS
            + ", "
            + (double) 100 * overflows / EXPERIMENTS
            + "%");
    System.out.println(
        "Over latencies: "
            + overLatencies
            + "/"
            + EXPERIMENTS
            + ", "
            + (double) 100 * overLatencies / EXPERIMENTS
            + "%");
    System.out.println(
        "Passed tests: "
            + passedTests
            + "/"
            + EXPERIMENTS
            + ", "
            + (double) 100 * passedTests / EXPERIMENTS
            + "%");
  }
}
