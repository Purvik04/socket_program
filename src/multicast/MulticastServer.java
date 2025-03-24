package multicast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MulticastServer {
    public static void main(String[] args)
    {
        String multicastAddress = "230.0.0.1"; // Multicast group IP

        int port = 4446;

        try (var socket = new DatagramSocket())
        {
            var group = InetAddress.getByName(multicastAddress);

            var scanner = new Scanner(System.in);

            System.out.println("Multicast Server Started. Type messages to send:");

            while (true)
            {
                var message = scanner.nextLine();

                byte[] buffer = message.getBytes();

                var packet = new DatagramPacket(buffer, buffer.length, group, port);

                socket.send(packet);

                if (message.equalsIgnoreCase("exit"))
                {
                    System.out.println("Server shutting down...");

                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

