package ethernet;

class EtherOverloadedException extends RuntimeException {
  EtherOverloadedException() {
    super("Ether is overloaded");
  }
}
