package HerCDN;
import DNS.*;
import config.MainConfig;
import java.net.*;
public class HerCDNAuthDNS{
    public static void main(String[] args) throws Exception {
        DNSServer server = new DNSServer(MainConfig.getIP("HER_CDN_DNS_IP"), 
                                        MainConfig.getPort("HER_CDN_DNS_PORT"), true);
        server.addRecord(new Record(new Name("herCDN.com"), "www.herCDN.com", Type.CN));
        server.addRecord(new Record(new Name("www.herCDN.com"),
        MainConfig.getIP("HER_CDN_SERVER_IP").toString().substring(1), Type.V));

        System.out.println("HERCDN AUTH DNS");
        server.start();
    }
}