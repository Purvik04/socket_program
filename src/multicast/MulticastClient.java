package multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient {
    public static void main(String[] args)
    {
        String multicastAddress = "230.0.0.1";
        // Must match server
        int port = 4446;

        try (var socket = new MulticastSocket(port))
        {
            var group = InetAddress.getByName(multicastAddress);

            socket.joinGroup(group);

            System.out.println("Joined multicast group. Waiting for messages...");

            while (true)
            {
                byte[] buffer = new byte[256];

                var packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                var message = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Received: " + message);

                if (message.equalsIgnoreCase("exit"))
                {
                    System.out.println("Server shut down. Leaving group...");

                    socket.leaveGroup(group);

                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
