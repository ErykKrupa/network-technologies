package ethernet;

import java.io.IOException;
import java.util.*;

class Ether {
  private Fragment[] ether;
  private Set<Integer> sourcesPositions;
  private boolean showHistory;

  Ether(int size, Set<Integer> sources, boolean showHistory) {
    ether = new Fragment[size];
    sourcesPositions = new HashSet<>();
    for (int i = 0; i < size; i++) {
      if (sources.contains(i)) {
        ether[i] = new Fragment(new Source());
        sourcesPositions.add(i);
      } else {
        ether[i] = new Fragment();
      }
    }
    Source.setEtherSize(size);
    this.showHistory = showHistory;
  }

  private void sendMessagesFromSources() {
    for (int position : sourcesPositions) {
      ether[position].sendMessageFromSource();
    }
  }

  void startSendingMessagesFromSources(int messageLength) {
    for (int position : sourcesPositions) {
      ether[position].startSendingMessageFromSource(messageLength);
    }
  }

  void turn() {
    sendMessagesFromSources();
    trans();
    draw();
  }

  private void trans() {
    Message[] updatedMessages = new Message[ether.length];
    for (int i = 0; i < ether.length; i++) {
      Message message = ether[i].message;
      trans(updatedMessages, message, i);
    }
    for (int i = 0; i < ether.length; i++) {
      ether[i].message = updatedMessages[i];
    }
  }

  private void trans(Message[] updatedMessages, Message message, int i) {
    if (message != null) {
      if (message.direction == Direction.LEFT_AND_RIGHT) {
        trans(updatedMessages, new Message(Direction.LEFT, message.corrupted), i);
        trans(updatedMessages, new Message(Direction.RIGHT, message.corrupted), i);
      } else {
        if (message.direction == Direction.LEFT && 0 <= i - 1) {
          i--;
        } else if (message.direction == Direction.RIGHT && i + 1 < ether.length) {
          i++;
        } else {
          return;
        }
        Message nextMessage = updatedMessages[i];
        if (nextMessage == null) {
          updatedMessages[i] = new Message(message.direction, message.corrupted);
        } else if (nextMessage.direction == message.direction) {
          updatedMessages[i] = new Message(message.direction, true);
        } else {
          updatedMessages[i] = new Message(Direction.LEFT_AND_RIGHT, true);
        }
      }
    }
  }

  private void draw() {
    if (!showHistory) {
      try {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } catch (IOException | InterruptedException ex) {
        ex.printStackTrace();
        System.exit(2);
      }
    }
    for (int i = 0; i < ether.length; i++) {
      if (ether[i].source == null) {
        Message message = ether[i].message;
        if (message == null) {
          System.out.print(". ");
        } else {
          char sign;
          if (message.corrupted) {
            sign = '#';
          } else {
            sign = '-';
          }
          if (message.direction == Direction.LEFT) {
            System.out.print("<" + sign);
          } else if (message.direction == Direction.RIGHT) {
            System.out.print("" + sign + ">");
          } else if (message.direction == Direction.LEFT_AND_RIGHT) {
            System.out.print("" + sign + sign);
          } else {
            System.out.print(". ");
          }
        }
      } else {
        System.out.print(i);
      }
    }
    System.out.println();
    try {
      Thread.sleep(50);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
