package ethernet;

class Fragment {
  Message message = null;
  final Source source;

  Fragment(Source source) {
    this.source = source;
  }

  Fragment() {
    this(null);
  }

  void startSendingMessageFromSource(int messageLength) {
    if (source != null) {
      source.startSendingMessage(messageLength);
    }
  }

  void sendMessageFromSource() {
    if (source != null) {
      if (message == null) {
        source.resumeSendingMessage();
        message = source.sendMessage();
      } else {
        source.stopSendingMessage();
      }
    }
  }
}
