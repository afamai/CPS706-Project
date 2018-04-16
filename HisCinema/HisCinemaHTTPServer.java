package HisCinema;
import com.sun.net.httpserver.*;
import config.MainConfig;
import java.io.*;
import java.net.*;

public class HisCinemaHttpServer {
  public static InetAddress ip;
  public static int port;
  public static InetSocketAddress socket;
  public static void main(String[] args) throws Exception {
        ip =  MainConfig.getIP("HIS_CINEMA_SERVER_IP");
        port = MainConfig.getPort("HIS_CINEMA_SERVER_PORT");
        if(ip == null){
          socket = new InetSocketAddress(port);
        }
        else{
          socket = new InetSocketAddress(ip, port);
        }
        HttpServer server = HttpServer.create(socket, 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("HISCINEMA WEB SERVER");
        System.out.println("Listening for client connections on " + socket.toString() + "...");
    }
  
  static class MyHandler implements HttpHandler {
      @Override
      public void handle(HttpExchange t) throws IOException 
      {
        System.out.println("Received client connection!");
        OutputStream os = t.getResponseBody();
        File file = new File("HisCinema/index.html");
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
        System.out.println("Sent index.html file to client!");

        os.close();
    }
  }

}
