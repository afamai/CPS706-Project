package HisCinema;
import DNS.*;
import config.MainConfig;
import java.net.*;
public class HisCinemaAuthDNS{
    public static void main(String[] args) throws Exception {
        DNSServer server = new DNSServer(MainConfig.getIP("HIS_CINEMA_DNS_IP"), 
                                        MainConfig.getPort("HIS_CINEMA_DNS_PORT"), true);
        server.addRecord(new Record(new Name("video.hiscinema.com"), "herCDN.com", Type.R));
        server.addRecord(new Record(new Name("hiscinema.com"), "www.hiscinema.com", Type.CN));
        server.addRecord(new Record(new Name("www.hiscinema.com"), 
        MainConfig.getIP("HIS_CINEMA_SERVER_IP").toString().substring(1), Type.A));
        
        System.out.println("HISCINEMA AUTH DNS");
        server.start();
    }
}