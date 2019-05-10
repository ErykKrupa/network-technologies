package framing;

import static framing.Functions.*;

public class Decipher {
  public static void main(String[] args) {
    String message = fetchBits("message.txt");
    String content = null;
    try {
      content = getContent(message);
    } catch (NotBitsException | MessageCorruptedException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    if (content.length() < 32) {
      new MessageCorruptedException("Message contains illegal sequence.").printStackTrace();
      System.exit(1);
    }
    String statement = content.substring(0, content.length() - 32);
    String checkSum = content.substring(content.length() - 32);
    if (getCRC(statement).equals(checkSum)) {
      saveBits(statement, "encrypted_statement.txt");
      System.out.println("Message OK");
    } else {
      new MessageCorruptedException("Check sum is incorrect. Message was corrupted.")
          .printStackTrace();
    }
  }
}
