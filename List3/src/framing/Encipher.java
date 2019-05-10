package framing;

import static framing.Functions.*;

public class Encipher {
  public static void main(String[] args) {
    saveBits(makeMessage(fetchBits("statement.txt")), "message.txt");
  }
}
