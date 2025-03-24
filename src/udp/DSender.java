package udp;

import java.net.*;

public class DSender{
    public static void main(String[] args) throws Exception
    {
        var ds = new DatagramSocket();

        String str = "Welcome java";

        var ip = InetAddress.getByName("127.0.0.1");

        var dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);

        ds.send(dp);

        ds.close();
    }
}
