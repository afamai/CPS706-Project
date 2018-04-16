package HerCDN;
import com.sun.net.httpserver.*;

import config.MainConfig;

import java.io.*;
import java.net.*;
public class HerCDNHTTPServer {
    public static InetAddress ip;
    public static int port;
    public static InetSocketAddress socket;
    public static void main(String[] args) throws Exception {
        ip =  MainConfig.getIP("HER_CDN_SERVER_IP");
        port = MainConfig.getPort("HER_CDN_SERVER_PORT");
        if(ip == null){
        socket = new InetSocketAddress(port);
        }
        else{
        socket = new InetSocketAddress(ip, port);
        }
        HttpServer server = HttpServer.create(socket, 0);
        server.createContext("/F1", new MyHandler());
        server.createContext("/F2", new MyHandler());
        server.createContext("/F3", new MyHandler());
        server.createContext("/F4", new MyHandler());
        server.setExecutor(null); 
        server.start();
        System.out.println("HERCDN WEB SERVER");
        System.out.println("Listening for client connections on" + socket.toString() + "...");
    }
	
	static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException 
        {
        	System.out.println("Received client connection!");
            String uri = t.getRequestURI().toString().replace("/", "").trim();
            System.out.println(uri);
            String response400 = "Not valid file name.";
            OutputStream os = t.getResponseBody();

            if(uri.equals("F1") || uri.equals("F2") || uri.equals("F3") || uri.equals("F4"))
            {
            	File file = new File("HerCDN/" + uri+".mp4");
            	String fileLength = ""+(int)file.length();
               	t.sendResponseHeaders(200, file.length());
            	FileInputStream fis = new FileInputStream(file);
            	BufferedInputStream bis = new BufferedInputStream(fis);
				DataInputStream dis = new DataInputStream(bis);
				
            	byte[] buffer = new byte[(int)file.length()];
            	int read;
            	while((read = dis.read(buffer)) != -1)
            		os.write(buffer, 0, read);
            	
            	fis.close();
				bis.close();
				dis.close();
				System.out.println("Sent file "+uri);
            }
            else
            {
            	t.sendResponseHeaders(400, response400.length());
            	os.write(response400.getBytes());
            }
            os.close();
        }
    }

}