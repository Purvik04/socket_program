package udp;

import java.net.*;

public class DReciever{
    public static void main(String[] args) throws Exception
    {
        var ds = new DatagramSocket(3000);

        byte[] buf = new byte[1024];

        var dp = new DatagramPacket(buf, 1024);

        ds.receive(dp);

        var str = new String(dp.getData(), 0, dp.getLength());

        System.out.println(str);

        ds.close();
    }
}
