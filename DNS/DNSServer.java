package DNS;
import java.io.*;
import java.net.*;
import java.util.List;

import DNS.DNSMessage;
import DNS.Name;
import DNS.Record;

import java.util.ArrayList;

public class DNSServer{
    private List<Record> recordTable;
    private int port;
    private InetAddress ipAddr;
    private boolean auth;

    public DNSServer(InetAddress ip, int port, boolean auth){
        recordTable = new ArrayList<Record>();
        ipAddr = ip;
        this.port = port;
        this.auth = auth;
    }

    public void start() throws Exception{
        DatagramSocket serverSocket = new DatagramSocket(port, ipAddr);
        System.out.println("Listening on " + serverSocket.getLocalAddress() + " " + port);
        byte[] receiveData = new byte[1024];

        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            InetAddress ip = receivePacket.getAddress();

            int recPort = receivePacket.getPort();

            System.out.println("RECEIVED:" + ip.toString() + " " + recPort);
            DNSMessage message = new DNSMessage(receivePacket.getData());
            System.out.println(message.toString());

            DNSMessage response = lookup(message);

            response.getHeader().setFlag(3, 1);
            if(auth)
                response.getHeader().setFlag(2, 1);
            
            System.out.println(response.toString());

            byte[] data = response.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, recPort); 
            System.out.println("SEND:" + ip.toString() + " " + recPort + "\n" + response.toString());
            serverSocket.send(sendPacket);
        }
    }

    public DNSMessage lookup(DNSMessage message) throws Exception{

        DNSMessage resp = new DNSMessage(message);
        Record question = resp.getQuestion();
        Name qname = question.getName();
        boolean done = false;
        System.out.println("Looking for " + qname.toString() + " TYPE " + question.getType().toString());

        //loop through the name label by label
        //(Ex. na.abc.example.com -> abc.example.com -> example.com -> etc...)
        int i = 0;
        int max = qname.labels();
        while( i < max - 1 && !done){

            Name tname = qname.offset(i);

            //if i is 0 that means we have not remove a label yet
            boolean isExact = (i == 0);
            i++;

            for(int j = 0; j < recordTable.size(); j++){
                Record record = recordTable.get(j);
                System.out.printf("Comparing... " + tname.toString() + " " + record.toString() + " ");
                if(tname.equals(record.getName())){
                    if(isExact){
                        Type rtype = record.getType();
                        if(rtype == question.getType()){
                            //if the name and type match with the record we found an answer
                            resp.addRecord(record, 1);
                            done = true;
                            System.out.printf("ANSWER FOUND!");
                        }
                        else if(rtype == Type.CN || rtype == Type.R){
                            //if it is type CN or R then restart the search with new name
                            resp.addRecord(record, 1);
                            qname = new Name(record.getValue());
                            System.out.printf("MATCH!\n");
                            i = 0;
                            max = qname.labels();
                            break;
                        }
                    }
                    if(record.getType() == Type.NS && question.getType() != Type.NS){
                        //store any NS records if found
                        System.out.printf("MATCH!");
                        resp.addRecord(record, 2);
                    }
                }
                System.out.printf("\n");
            }
        }

        //if there are auth records, search for their ip addresses
        if(resp.getCount(2) > 0){
            //get all authoritative nameserver records
            List<Record> NSRecords = resp.getRecords(2);
            
            //search through the record table
            for(int k = 0; k < NSRecords.size(); k++){
                //convert String to name object
                Name tname = new Name(NSRecords.get(k).getValue());
                System.out.println("Looking for " + tname.toString());

                for(int l = 0; l < recordTable.size(); l++){
                    //search for the type A record for nameserver
                    Record record = recordTable.get(l);

                    System.out.printf("Comparing... " + tname.toString() + " " + record.toString() + " ");

                    if(tname.equals(record.getName()) && record.getType() == Type.A){
                        resp.addRecord(record, 3);
                        System.out.println("MATCH!");
                        break;
                    }
                    System.out.printf("\n");
                }
            }
        }
        System.out.println("CURRENT RESPONSE\n" + resp.toString());
        //check auth servers for answer, if available
        if(!auth && !done && resp.getCount(2) > 0 ){
            List<Record> ADRecords = resp.getRecords(3);

            //try every name server available
            for( int m = 0; m < ADRecords.size(); m++){
                Record record = ADRecords.get(m);

                if(record.getType() == Type.A){
                    //initialize UDP client to send and receive DNS messages
                    InetAddress ip = InetAddress.getByName(record.getValue());
                    UDPClient client = new UDPClient();

                    //send
                    client.sendPacket(ip, 53, message.toByteArray());
                    System.out.println("\nSENDING " + ip.toString() + " " + 53 + "\n" + message.toString());
                    
                    //receive
                    DNSMessage response = new DNSMessage(client.receivePacket());
                    System.out.println("\nRECEIVED\n" + response.toString());

                    if(response.hasAnswer(question.getType())){
                        System.out.println("ANSWER FOUND!!!");
                        List<Record> answers = response.getRecords(1);
                        for(int n = 0; n < answers.size(); n++){
                            resp.addRecord(answers.get(n), 1);
                        }
                        done = true;
                        break;
                    }
                    if(response.hasAnswer(Type.R)){
                        //add record into answer section
                        Record answer = response.getAnswer(Type.R);
                        resp.addRecord(answer, 1);
                        
                        //forward the request by recusively calling this method
                        DNSMessage request = new DNSMessage(message);
                        request.getQuestion().setName(new Name(answer.getValue()));
                        DNSMessage result = lookup(request);

                        //add sections (answer, nameserver, additional)
                        for(int n = 1; n < 4; n++){
                            resp.addRecords(result.getRecords(n), n);
                        }
                        return resp;
                    }
                }
            }
        }
        return resp;
    }
    

    public void addRecord(Record record){
        recordTable.add(record);
    }

}