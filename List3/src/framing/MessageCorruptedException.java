package framing;

class MessageCorruptedException extends Exception {
  MessageCorruptedException(String message) {
    super(message);
  }
}
