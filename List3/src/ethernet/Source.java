package ethernet;

import java.util.Random;

class Source {
  private boolean interrupted = false;
  private int remainMessageLength = 0;
  private int wholeMessageLength = 0;
  private int attempts = 0;
  private int delay;
  private static int etherSize;
  private Random random = new Random();

  static void setEtherSize(int etherSize) {
    if (etherSize > 0) {
      Source.etherSize = etherSize;
    }
  }

  void startSendingMessage(int messageLength) {
    if (messageLength > 0) {
      wholeMessageLength = messageLength;
      remainMessageLength = messageLength;
    }
  }

  Message sendMessage() {
    if (!interrupted) {
      if (remainMessageLength > 0) {
        remainMessageLength--;
        return new Message(Direction.LEFT_AND_RIGHT, false);
      } else {
        wholeMessageLength = 0;
      }
    }
    return null;
  }

  void resumeSendingMessage() {
    if (interrupted) {
      if (delay > 0) {
        delay--;
      } else {
        remainMessageLength = wholeMessageLength;
        interrupted = false;
      }
    }
  }

  void stopSendingMessage() {
    if (interrupted && delay > 0) {
      delay--;
    } else if (!interrupted && wholeMessageLength > 0) {
      interrupted = true;
      if (attempts < 15) {
        attempts++;
      } else {
        throw new EtherOverloadedException();
      }
      delay = etherSize * random.nextInt((int) Math.pow(2, attempts <= 10 ? attempts : 10));
    }
  }
}
