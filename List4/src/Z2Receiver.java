import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Z2Receiver {
  private static final int datagramSize = 50;
  private InetAddress localHost;
  private int destinationPort;
  private DatagramSocket socket;
  private HashMap<Integer, Z2Packet> map;
  private int counter;
  private ReceiverThread receiver;

  private Z2Receiver(int myPort, int destPort) throws Exception {
    localHost = InetAddress.getByName("127.0.0.1");
    destinationPort = destPort;
    socket = new DatagramSocket(myPort);
    receiver = new ReceiverThread();
    map = new HashMap<>();
    counter = 0;
  }

  class ReceiverThread extends Thread {
    public void run() {
      Z2Packet packet = null;
      try {
        while (true) {
          byte[] data = new byte[datagramSize];
          DatagramPacket datagramPacket = new DatagramPacket(data, datagramSize);
          socket.receive(datagramPacket);
          Z2Packet newPacket = new Z2Packet(datagramPacket.getData());
          if (packet == null) {
              packet = newPacket;
          }
          if (newPacket.getIntAt(0) >= counter) {
              map.put(newPacket.getIntAt(0), newPacket);
          }
          while (map.containsKey(counter)) {
            packet = map.remove(counter);
            System.out.println("R:" + packet.getIntAt(0) + ": " + (char) packet.data[4]);
            counter++;
          }
          // WYSLANIE POTWIERDZENIA
          DatagramPacket outPacket =
              new DatagramPacket(packet.data, packet.data.length, localHost, destinationPort);
          socket.send(outPacket);
        }
      } catch (Exception e) {
        System.out.println("Z2Receiver.ReceiverThread.run: " + e);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Z2Receiver receiver = new Z2Receiver(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    receiver.receiver.start();
  }
}
