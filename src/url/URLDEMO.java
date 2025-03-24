package url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class URLDEMO {
    public static void main(String[] args)
    {

        try
        {
            var url= new URL("https://www.tpointtech.com/URLConnection-class");

            var urlCon = url.openConnection();

            var stream = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

            System.out.println(url.getFile());

            String i;

            while((i=stream.readLine())!= null)
            {
                System.out.println(i);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
