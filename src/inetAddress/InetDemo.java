package inetAddress;

import java.net.InetAddress;

public class InetDemo {

    public static void main(String[] args) {
        try {
            var address = InetAddress.getByName("www.google.com");
            var get_all = InetAddress.getAllByName("www.google.com");
            InetAddress loopbackAddress = InetAddress.getLoopbackAddress();

            System.out.println(get_all[0] + " " + get_all[1]);
            System.out.println("Host Name: " + address.getHostName());
            System.out.println("IP Address: " + address.getHostAddress());
            System.out.println("Canonical Host Name: " + address.getCanonicalHostName());

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
