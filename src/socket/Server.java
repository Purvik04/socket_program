package socket;

import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 3000;

    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            System.out.println("Server started. Waiting for clients...");

            while (true)
            {
                Socket socket = serverSocket.accept();

                System.out.println("Client connected: " + socket.getInetAddress());

                // Handle client communication in a separate thread
                new ClientHandler(socket).start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread
{
    private Socket socket;

    private DataInputStream in;

    private DataOutputStream out;

    public ClientHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            in = new DataInputStream(socket.getInputStream());

            out = new DataOutputStream(socket.getOutputStream());

            var console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Communicating with client...");

            // Thread for receiving messages from the client
            Thread receiveThread = new Thread(() -> {

                try
                {
                    while (true)
                    {
                        String message = in.readUTF();

                        if (message.equalsIgnoreCase("exit"))
                        {
                            System.out.println("Client disconnected.");

                            break;
                        }

                        System.out.println("Client: " + message);
                    }
                }
                catch (IOException e)
                {
                    System.out.println("Client connection lost.");
                }
                finally
                {
                    closeConnection();
                }
            });

            receiveThread.start();

            // Thread for sending messages to the client
            while (true)
            {
                String message = console.readLine();

                out.writeUTF(message);

                if (message.equalsIgnoreCase("exit"))
                {
                    System.out.println("Closing connection with client...");

                    closeConnection();

                    System.exit(0);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void closeConnection()
    {
        try
        {
            if (socket != null) socket.close();

            if (in != null) in.close();

            if (out != null) out.close();
        }
        catch (IOException e)
        {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
