package ethernet;

import java.util.*;

public class Main {

  public static void main(String[] args) {
    int size;
    Scanner scanner = new Scanner(System.in);

    System.out.print("Length of the array: ");
    while (true) {
      try {
        size = Integer.parseInt(scanner.nextLine());
        if (size < 1) {
          System.err.print("Size must be a positive number ");
        } else {
          break;
        }
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
      }
    }

    System.out.print("Where to set the sources?: ");
    Set<Integer> sources = new HashSet<>();
    for (String word : scanner.nextLine().split(" ")) {
      try {
        int number = Integer.parseInt(word);
        if (0 <= number && number < size) {
          sources.add(number);
        }
      } catch (NumberFormatException ignored) {
      }
    }

    System.out.print("Show the history? [y/n]: ");
    String input;
    do {
      input = scanner.next();
    } while (!input.equals("yes")
        && !input.equals("y")
        && !input.equals("no")
        && !input.equals("n"));
    Ether ether = new Ether(size, sources, input.equals("yes") || input.equals("y"));
    ether.startSendingMessagesFromSources(2 * size);
    while (true) {
      ether.turn();
    }
  }
}
