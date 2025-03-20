package socket;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 3000;

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to server.");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (message.equalsIgnoreCase("exit")) {
                            System.out.println("Server disconnected.");
                            break;
                        }
                        System.out.println("Server: " + message);
                    }
                    socket.close();
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            receiveThread.start();

            while (true) {
                String message = console.readLine();
                out.writeUTF(message);
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnecting...");
                    socket.close();
                    System.exit(0);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
