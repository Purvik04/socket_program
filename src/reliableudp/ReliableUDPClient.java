package reliableudp;

import java.net.*;

public class ReliableUDPClient {
    public static void main(String[] args)
    {
        int port = 4445;

        try (var socket = new DatagramSocket(port))
        {
            byte[] buffer = new byte[256];

            var packet = new DatagramPacket(buffer, buffer.length);

            System.out.println("Waiting for message...");

            socket.receive(packet);

            var received = new String(packet.getData(), 0, packet.getLength());

            System.out.println("Received: " + received);

            // Send ACK back to the server
            byte[] ackData = "ACK".getBytes();

            var ackPacket = new DatagramPacket(
                    ackData, ackData.length, packet.getAddress(), packet.getPort()
            );

            socket.send(ackPacket);

            System.out.println("ACK sent.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

