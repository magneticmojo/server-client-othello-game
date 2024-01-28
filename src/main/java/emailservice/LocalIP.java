package emailservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalIP {

    public static String getLocalIP() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
        return inetAddress.getHostAddress();
    }
}
