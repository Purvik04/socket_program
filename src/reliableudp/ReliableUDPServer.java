package reliableudp;

import java.net.*;

public class ReliableUDPServer {
    public static void main(String[] args)
    {
        int port = 4445;

        try (var socket = new DatagramSocket())
        {
            var clientAddress = InetAddress.getByName("localhost");

            socket.setSoTimeout(3000);  // Timeout for ACK (3 seconds)

            String message = "Hello, Client!";

            byte[] buffer = message.getBytes();

            var packet = new DatagramPacket(buffer, buffer.length, clientAddress, port);

            var ackReceived = false;

            var attempts = 0;

            var maxAttempts = 3;

            while (!ackReceived && attempts < maxAttempts)
            {
                System.out.println("Sending: " + message);

                socket.send(packet);

                try
                {
                    // Wait for ACK
                    byte[] ackBuffer = new byte[10];

                    var ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);

                    socket.receive(ackPacket);

                    var ack = new String(ackPacket.getData(), 0, ackPacket.getLength());

                    if (ack.equals("ACK"))
                    {
                        System.out.println("ACK received. Message delivered.");

                        ackReceived = true;
                    }
                }
                catch (SocketTimeoutException e)
                {
                    attempts++;

                    System.out.println("No ACK received. Retrying... (" + attempts + ")");
                }
            }

            if (!ackReceived)
            {
                System.out.println("Failed to deliver message after " + maxAttempts + " attempts.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

