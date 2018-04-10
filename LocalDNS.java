import DNS.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class LocalDNS{

    public static void main(String[] args) throws Exception {
        DNSServer server = new DNSServer(53);
        server.addRecord(new Record(new Name("herCDN.com"), "NSherCDN.com", Record.Type.NS));
        server.addRecord(new Record(new Name("NSherCDN.com"), "SOME IP", Record.Type.A));
        server.addRecord(new Record(new Name("hiscinema.com"), "NShiscinema.com", Record.Type.NS));
        server.addRecord(new Record(new Name("NShiscinema.com"), "SOME IP", Record.Type.A));

        server.start();
    }
}