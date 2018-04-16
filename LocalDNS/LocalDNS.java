package LocalDNS;
import config.*;
import DNS.*;
import java.net.*;

public class LocalDNS{

    public static void main(String[] args) throws Exception {
        DNSServer server = new DNSServer(MainConfig.getIP("LOCAL_DNS_IP"),
                                         MainConfig.getPort("LOCAL_DNS_PORT"), false);
        server.addRecord(new Record(new Name("herCDN.com"), "NSherCDN.com", Type.NS));
        server.addRecord(new Record(new Name("NSherCDN.com"), 
        MainConfig.getIP("HER_CDN_DNS_IP").toString().substring(1), Type.A));
        server.addRecord(new Record(new Name("hiscinema.com"), "NShiscinema.com", Type.NS));
        server.addRecord(new Record(new Name("NShiscinema.com"), 
        MainConfig.getIP("HIS_CINEMA_DNS_IP").toString().substring(1), Type.A));

        System.out.println("LOCAL DNS");
        server.start();
    }
}