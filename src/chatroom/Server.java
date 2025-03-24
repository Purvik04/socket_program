package chatroom;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
    private static final int PORT = 3000;

    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args)
    {
        try (var serverSocket = new ServerSocket(PORT))
        {
            System.out.println("Server started. Waiting for clients...");

            while (true)
            {
                var socket = serverSocket.accept();

                System.out.println("New client connected: " + socket.getInetAddress());

                // Create a new thread for each client
                new Thread(new ClientHandler(socket)).start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    static class ClientHandler implements Runnable {

        private Socket socket;

        private DataInputStream in;

        private DataOutputStream out;

        private String username;

        public ClientHandler(Socket socket)
        {
            this.socket = socket;

            try
            {
                in = new DataInputStream(socket.getInputStream());

                out = new DataOutputStream(socket.getOutputStream());

                // Ask for username
                out.writeUTF("Enter your username: ");

                this.username = in.readUTF();

                synchronized (clients) {
                    clients.put(username, this);
                }

                broadcast("User " + username + " has joined the chat!");

                out.writeUTF("Welcome, " + username + "! Type @username message to chat.");
            } catch (IOException e) {
                closeConnection();
            }
        }

        @Override
        public void run()
        {
            try
            {
                while (true)
                {
                    var message = in.readUTF();

                    if (message.equalsIgnoreCase("exit"))
                    {
                        break;
                    }

                    if (message.startsWith("@"))
                    {
                        String[] parts = message.split(" ", 2);

                        if (parts.length < 2)
                        {
                            out.writeUTF("Invalid format! Use @username message");

                            continue;
                        }

                        var recipient = parts[0].substring(1);

                        var msg = parts[1];

                        sendMessage(recipient, msg);
                    }
                    else
                    {
                        out.writeUTF("Invalid format! Use @username message");
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println(username + " disconnected.");
            }
            finally
            {
                closeConnection();
            }
        }

        private void sendMessage(String recipient, String msg)
        {
            synchronized (clients)
            {
                var client = clients.get(recipient);

                if (client != null)
                {
                    try
                    {
                        client.out.writeUTF(username + " (private): " + msg);
                    }
                    catch (IOException e)
                    {
                        System.out.println("Error sending message to " + recipient);
                    }
                }
                else
                {
                    try
                    {
                        out.writeUTF("User " + recipient + " not found.");
                    }
                    catch (IOException e)
                    {
                        System.out.println("Error sending message to " + username);
                    }
                }
            }
        }

        private void broadcast(String message)
        {
            synchronized (clients)
            {
                for (var client : clients.values())
                {
                    try
                    {
                        client.out.writeUTF(message);
                    }
                    catch (IOException e)
                    {
                        System.out.println("Error broadcasting message.");
                    }
                }
            }
        }

        private void closeConnection()
        {
            synchronized (clients)
            {
                clients.remove(username);
            }

            broadcast("User " + username + " has left the chat.");

            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                System.out.println("Error closing socket.");
            }
        }
    }
}

