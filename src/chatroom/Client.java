package chatroom;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args)
    {
        String serverAddress = "localhost";

        int port = 3000;

        try (Socket socket = new Socket(serverAddress, port))
        {
            var in = new DataInputStream(socket.getInputStream());

            var out = new DataOutputStream(socket.getOutputStream());

            var console = new BufferedReader(new InputStreamReader(System.in));

            System.out.print(in.readUTF());// "Enter your username: "

            String username = console.readLine();

            out.writeUTF(username);

            // Thread to receive messages
            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        System.out.println(message);
                    }
                }
                catch (IOException e) {
                    System.out.println("Disconnected from server.");

                    System.exit(0);
                }
            });

            receiveThread.start();

            // Sending messages
            while (true)
            {
                String message = console.readLine();

                out.writeUTF(message);

                if (message.equalsIgnoreCase("exit"))
                {
                    receiveThread.interrupt();

                    break;
                }
            }
        }
        catch (IOException e) {
            System.out.println("Connection closed.");
        }
    }
}

