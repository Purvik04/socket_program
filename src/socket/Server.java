package socket;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 3000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (message.equalsIgnoreCase("exit")) {
                            System.out.println("Client disconnected.");
                            break;
                        }
                        System.out.println("Client: " + message);
                    }
                    socket.close();
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                    System.exit(0);
                }
            });

            receiveThread.start();

            while (true) {
                String message = console.readLine();
                out.writeUTF(message);
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Server shutting down.");
                    socket.close();
                    System.exit(0);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
