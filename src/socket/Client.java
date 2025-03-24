package socket;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args)
    {
        String serverAddress = "localhost";

        int port = 3000;

        try (    var socket = new Socket(serverAddress, port);

                 var in = new DataInputStream(socket.getInputStream());

                 var out = new DataOutputStream(socket.getOutputStream());

                 var console = new BufferedReader(new InputStreamReader(System.in)))
        {
            System.out.println("Connected to server.");

            // Thread to receive messages from the server
            Thread receiveThread = new Thread(() -> {
                try
                {
                    while (true)
                    {
                        String message = in.readUTF();

                        if (message.equalsIgnoreCase("exit"))
                        {
                            System.out.println("Server disconnected.");
                            break;
                        }
                        System.out.println("Server: " + message);
                    }
                }
                catch (IOException e)
                {
                    System.out.println("Connection closed.");
                }
                finally
                {
                    System.exit(0);
                }
            });

            receiveThread.start();

            // Sending messages to server
            while (true)
            {
                String message = console.readLine();

                out.writeUTF(message);

                if (message.equalsIgnoreCase("exit"))
                {
                    System.out.println("Client disconnecting...");

                    socket.close();

                    break;
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
