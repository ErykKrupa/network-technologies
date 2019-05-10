package framing;

class NotBitsException extends Exception {
  NotBitsException() {
    super("Message can contains only \"1\" and \"0\".");
  }
}
