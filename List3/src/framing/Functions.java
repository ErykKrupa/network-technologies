package framing;

import java.io.*;
import java.util.zip.CRC32;

class Functions {
  private static final String limiter = "01111110";

  static String fetchBits(String fileName) {
    String bits;
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      StringBuilder statementBuilder = new StringBuilder();
      String line = reader.readLine();
      while (line != null) {
        statementBuilder.append(line);
        line = reader.readLine();
      }
      bits = statementBuilder.toString();
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    try {
      convertMessageToByteArray(bits);
    } catch (NotBitsException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return bits;
  }

  static String makeMessage(String statement) {
    if (statement == null) {
      throw new NullPointerException();
    }
    String message = null;
    try {
      message = limiter + distend(statement + getCRC(statement)) + limiter;
    } catch (NotBitsException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return message;
  }

  static String getCRC(String statement) {
    if (statement == null) {
      throw new NullPointerException();
    }
    CRC32 crc = new CRC32();
    try {
      crc.update(convertMessageToByteArray(statement));
    } catch (NotBitsException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    String checkSum = Long.toBinaryString(crc.getValue());
    StringBuilder checkSumBuilder = new StringBuilder();
    for (int i = 0; i < 32 - checkSum.length(); i++) {
      checkSumBuilder.append("0");
    }
    return checkSumBuilder.toString() + checkSum;
  }

  static boolean saveBits(String message, String fileName) {
    if (message == null || fileName == null) {
      throw new NullPointerException();
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
      writer.write(message);
    } catch (IOException ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  static String getContent(String message) throws MessageCorruptedException, NotBitsException {
    if (message == null) {
      throw new NullPointerException();
    }
    StringBuilder content = new StringBuilder();
    char[] bits = message.toCharArray();
    int bitsCounter = 0;
    if (!message.substring(0, limiter.length()).equals(limiter)) {
      throw new MessageCorruptedException("Message contains wrong sequence at the beginning.");
    }
    for (int i = limiter.length(); i < bits.length; i++) {
      if (bits[i] == '1' && bitsCounter != 5) {
        bitsCounter++;
      } else if (bits[i] == '0' && bitsCounter != 5) {
        bitsCounter = 0;
      } else if (bits[i] == '0') {
        bitsCounter = 0;
        continue;
      } else if (bits[i] == '1' && i + 1 < bits.length) {
        if (bits[i + 1] == '0') {
          if (content.length() < 6) {
            throw new MessageCorruptedException("Message contains illegal sequence.");
          } else {
            return content.subSequence(0, content.length() - 6).toString();
          }
        } else if (bits[i + 1] == '1') {
          throw new MessageCorruptedException("Message contains illegal sequence.");
        } else {
          throw new NotBitsException();
        }
      } else if (bits[i] == '1') {
        break;
      } else {
        throw new NotBitsException();
      }
      content.append(bits[i]);
    }
    throw new MessageCorruptedException("Message contains wrong sequence at the end.");
  }

  // TODO testy
  private static String distend(String bits) throws NotBitsException {
    if (bits == null) {
      throw new NullPointerException();
    }
    StringBuilder distendedBits = new StringBuilder();
    int bitsCounter = 0;
    for (char bit : bits.toCharArray()) {
      if (bit == '1') {
        bitsCounter++;
      } else if (bit == '0') {
        bitsCounter = 0;
      } else {
        throw new NotBitsException();
      }
      distendedBits.append(bit);
      if (bitsCounter == 5) {
        distendedBits.append("0");
        bitsCounter = 0;
      }
    }
    return distendedBits.toString();
  }

  private static byte[] convertMessageToByteArray(String message) throws NotBitsException {
    if (message == null) {
      throw new NullPointerException();
    }
    byte[] bytes = new byte[message.length()];
    for (int i = 0; i < bytes.length; i++) {
      char bit = message.charAt(i);
      if (bit == '1') {
        bytes[i] = 1;
      } else if (bit == '0') {
        bytes[i] = 0;
      } else {
        throw new NotBitsException();
      }
    }
    return bytes;
  }
}
