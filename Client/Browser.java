import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Browser extends JFrame{
    private JTextField urlField;
    private JEditorPane editorPane;
    private JPanel panel;
//    private List<Record> browserRecordTable;

    public Browser(){

      panel = new JPanel();
//      browserRecordTable = new ArrayList<Record>();
        urlField = new JTextField(35);
        urlField.addKeyListener(new KeyAdapter(){
          public void keyReleased(KeyEvent k){
            if(k.getKeyCode() == KeyEvent.VK_ENTER){
              try{
                gotoLocalDNS();
              }catch(Exception e){
                e.printStackTrace();
              }
            }
          }
        });
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        panel.add(editorPane);

        getContentPane().setLayout(new BorderLayout());
        add(urlField, BorderLayout.NORTH);
        add(editorPane, BorderLayout.CENTER);
        
    }

    /*load html to editor pane*/
    public void load(String html){
        editorPane.setText(html);
        
    }
    
    /*add records to the record table*/
//    public void addRecord(Record record){
//      browserRecordTable.add(record);
//    }
    
    public void gotoLocalDNS() throws Exception{
      //Create client UDP socket 
      DatagramSocket clientSocket = new DatagramSocket();
      //Assuming that url value will be www.hiscinema.com
      String url = urlField.getText().replace("www.", "");
          
      //Uses local IP address
      InetAddress ipAddr = InetAddress.getByName(MainConfiguration.localdnsIP());
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
          
      sendData = url.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,ipAddr,MainConfiguration.udpPort());
          
      System.out.println("Contacting local DNS using UDP to resolve: "+url);
//      clientSocket.send(sendPacket);

      //Receives UDP packet
//      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//      clientSocket.receive(receivePacket);
          
      //contains IP of hiscinema.com
//      String modifiedSentence = new String(receivePacket.getData());
//      System.out.println("FROM SERVER:" + modifiedSentence);
      clientSocket.close();
          
      //getting the IP address of hiscinema.com
//      Socket TCPclientSocket = new Socket("modifiedSentence",MainConfiguration.hisCinemaServerPort());
      String hisCinemaResolvedIP = "localhost";
      URL urlLink = new URL("http://"+hisCinemaResolvedIP+":"+MainConfiguration.hisCinemaServerPort()+"/");
    HttpURLConnection con = (HttpURLConnection)urlLink.openConnection();
    con.setRequestMethod("GET");
    
    int status = con.getResponseCode();
    if(status == 200)
    {
      InputStream is = con.getInputStream();
      FileOutputStream fos = new FileOutputStream("receivedIndex.html");
      int bytesRead = -1;
      int BUFFER_SIZE = 4096;
      byte[] buffer = new byte[BUFFER_SIZE];
      while((bytesRead = is.read(buffer)) != -1)
      {
        fos.write(buffer,0,bytesRead);
      }
      
      fos.close();
      is.close();
      System.out.println("Successfully received file.");
    }
    con.disconnect();
    
      
      StringBuilder sb = new StringBuilder();
      File indexHTML = new File ("receivedIndex.html");
      BufferedReader br = new BufferedReader(new FileReader (indexHTML));
      String line = null;
      while((line = br.readLine()) != null)
      { 
        sb.append(line);
      }
      
      load(sb.toString());
      
      HyperlinkListener listener = new HyperlinkListener(){

      @Override
      public void hyperlinkUpdate(HyperlinkEvent e) {
        // TODO Auto-generated method stub
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
          retrieveSelectedVideo(e.getURL().toString());
        }
      }
        
      };
      
      editorPane.addHyperlinkListener(listener);
    }
    
    //The url parameter will hold the string of format: http://video.hiscinema.com/Fi where i is an integer
    public void retrieveSelectedVideo(String url)
    {
      //TODO: insert dns lookup code here
      
      String resolvedHerCdnIp = "localhost";//Replace this with the result from DNS lookup
      
      String fileRequested = url.substring(url.length()-2,url.length());
      try
      {
        URL urlLink = new URL("http://"+resolvedHerCdnIp+":"+MainConfiguration.herCinemaServerPort()
          +"/"+fileRequested);
      HttpURLConnection con = (HttpURLConnection)urlLink.openConnection();
      con.setRequestMethod("GET");
      
      int status = con.getResponseCode();
      if(status == 200)
      {
        InputStream is = con.getInputStream();
        FileOutputStream fos = new FileOutputStream("receivedVideo.mp4");
        int bytesRead = -1;
        int BUFFER_SIZE = 4096;
        byte[] buffer = new byte[BUFFER_SIZE];
        while((bytesRead = is.read(buffer)) != -1)
        {
          fos.write(buffer,0,bytesRead);
        }
        
        fos.close();
        is.close();
        System.out.println("Successfully received the video file.");
      }
      con.disconnect();
      Desktop.getDesktop().open(new File ("receivedVideo.mp4"));
      }
      catch(Exception e)
      {
        
      }
    }
    
    public static void main(String[] args){
        Browser client = new Browser();
        
        client.setTitle("Client Application");
        client.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        client.setSize( 900, 500 );
        client.setVisible( true );
    }

    
}