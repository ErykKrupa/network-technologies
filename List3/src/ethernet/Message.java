package ethernet;

class Message {
  final Direction direction;
  boolean corrupted;

  Message(Direction direction, boolean corrupted) {
    if (direction == null) {
      throw new NullPointerException();
    }
    this.direction = direction;
    this.corrupted = corrupted;
  }
}
