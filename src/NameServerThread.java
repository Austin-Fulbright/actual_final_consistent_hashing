import java.util.*;
import java.io.*;
import java.net.*;


public class NameServerThread extends Thread{
    
    public ServerSocket nameListenServer = null;
    ServerInfo serverinfo = null;
    public HashMap<Integer, String> map;
    public Socket socket = null;

    public NameServerThread(ServerInfo si, HashMap<Integer, String> map){
        serverinfo = si;
        this.map = map;
    }

    public void takeInfo(DataInputStream instream, ServerInfo sinfo){
        try{
        sinfo.id = Integer.parseInt(instream.readUTF());
        sinfo.listeningPort = Integer.parseInt(instream.readUTF());
        sinfo.ip = instream.readUTF();
        }catch(Exception e){

        }
    }

    public void updateInfo(ServerInfo info, ServerInfo otherInfo){
        otherInfo.successorid = info.id;
        otherInfo.successorport = info.listeningPort;
        otherInfo.successorip = info.ip;
        otherInfo.predissesorid = info.predissesorid;
        otherInfo.predissesorPort = info.predissesorPort;
        otherInfo.predisessorip = info.predisessorip;
        info.predissesorid = otherInfo.id;
        info.predissesorPort = otherInfo.listeningPort;
        info.predisessorip = otherInfo.ip;
    }

    public void sendKeys(DataOutputStream ostream, int ID, int otherID){
    try{
        ID++;
        for(int i = ID; i<=otherID; i++){
            if(map.containsKey(i)){
                ostream.writeUTF(Integer.toString(i));
                ostream.writeUTF(map.get(i));
                map.remove(i);
            }
        }
        ostream.writeUTF("-1");
    }catch(Exception e){

    }
    }
    
    public void recieveKeys(DataInputStream istreamRecieve){
        try{
            String msg = "";
            int i = 0;
            String v = ""; 

            while(true){
                msg = istreamRecieve.readUTF();
                if(msg.equals("-1")){
                    break;
                }
                i = Integer.parseInt(msg);
                msg = istreamRecieve.readUTF();
                v = msg;
                map.put(i, v);
            }

        }catch(Exception e){

        }
    }
    public void takeAndSend(DataInputStream istreamRecieve, DataOutputStream ostreamSend){
        try{
            
            String msgRS = "";
            while(true){
    
                msgRS = istreamRecieve.readUTF();
                if(msgRS.equals("-1")){
                    break;
                }
                ostreamSend.writeUTF(msgRS);
                msgRS = istreamRecieve.readUTF();
                ostreamSend.writeUTF(msgRS);
            }
            ostreamSend.writeUTF("-1");
            }catch(Exception e){
    
            }
        } 

    public void run(){
    try{
        System.out.println("thread ID: "+serverinfo.id);
        System.out.println("187 = "+map.get(187));
        System.out.println("288 = "+map.get(288));
        System.out.println("325 = "+map.get(325));
        System.out.println("434 = "+map.get(434));
        System.out.println("502 = "+map.get(502));
        String msg = "";
        ServerSocket server = new ServerSocket(serverinfo.listeningPort);
        while(true){

            socket = server.accept();
            DataInputStream istream = new DataInputStream(socket.getInputStream());
            DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
            msg = istream.readUTF();
            if(msg.equals("enter")){
                ServerInfo svi = new ServerInfo();
                takeInfo(istream, svi);
                if(svi.id<serverinfo.id){
                    updateInfo(serverinfo, svi);
                    sendKeys(ostream, svi.predissesorid, svi.id);
                    ostream.writeUTF(Integer.toString(svi.predissesorid));//predID
                    ostream.writeUTF(Integer.toString(svi.predissesorPort));//predPort
                    ostream.writeUTF(svi.predisessorip);
                    ostream.writeUTF(Integer.toString(svi.successorid));//send new successor first
                    ostream.writeUTF(Integer.toString(svi.successorport));//port
                    ostream.writeUTF(svi.successorip);
                    ostream.writeUTF("update your successor");
                    System.out.println("thread ID: "+serverinfo.id);
                    System.out.println("187 = "+map.get(187));
                    System.out.println("288 = "+map.get(288));
                    System.out.println("325 = "+map.get(325));
                    System.out.println("434 = "+map.get(434));
                    System.out.println("502 = "+map.get(502));
                }
                else{
                    Socket sucSocket = new Socket(serverinfo.successorip, serverinfo.successorport);
                    DataInputStream istreams = new DataInputStream(sucSocket.getInputStream());
                    DataOutputStream ostreams = new DataOutputStream(sucSocket.getOutputStream());
                    ostreams.writeUTF("enter");
                    ostreams.writeUTF(Integer.toString(svi.id));
                    ostreams.writeUTF(Integer.toString(svi.listeningPort));
                    ostreams.writeUTF(svi.ip);
                    takeAndSend(istreams, ostream);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    System.out.println("thread ID: "+serverinfo.id);
                    System.out.println("187 = "+map.get(187));
                    System.out.println("288 = "+map.get(288));
                    System.out.println("325 = "+map.get(325));
                    System.out.println("434 = "+map.get(434));
                    System.out.println("502 = "+map.get(502));
                    if(msg.equals("update your successor")){
                        serverinfo.successorid=svi.id;
                        serverinfo.successorport=svi.listeningPort;
                        serverinfo.successorip=svi.ip;
                    }
                    ostream.writeUTF("Dont update your successor");

                    

                }
            }
            if(msg.equals("exit1")){
                ostream.writeUTF(Integer.toString(serverinfo.successorid));
                ostream.writeUTF(Integer.toString(serverinfo.successorport));
                ostream.writeUTF(serverinfo.ip);
                Socket sucSocket = new Socket(serverinfo.successorip, serverinfo.successorport);
                DataInputStream istreams = new DataInputStream(sucSocket.getInputStream());
                DataOutputStream ostreams = new DataOutputStream(sucSocket.getOutputStream());
                ostreams.writeUTF("predissesor exiting");
                ostreams.writeUTF(Integer.toString(serverinfo.predissesorid));
                ostreams.writeUTF(Integer.toString(serverinfo.predissesorPort));
                ostreams.writeUTF(serverinfo.ip);
                sendKeys(ostreams, serverinfo.predissesorid, serverinfo.id);
                String exitMessage = "nameserver: "+serverinfo.id+" exiting, nameserver: "+serverinfo.successorid+" now responsible for range: ["+(serverinfo.predissesorid+1)+", "+serverinfo.id+"]";
            
                ostream.writeUTF(exitMessage);
                break;
            }
            if(msg.equals("exit2")){
                System.out.println("connected to bootstrap");
                ostream.writeUTF(Integer.toString(serverinfo.predissesorid));
                ostream.writeUTF(Integer.toString(serverinfo.predissesorPort));
                ostream.writeUTF(serverinfo.predisessorip);
                System.out.println("predissesor port is still this: "+serverinfo.predissesorPort);
                Socket predSocket = new Socket("127.0.0.1", serverinfo.predissesorPort);
                DataInputStream istreams = new DataInputStream(predSocket.getInputStream());
                DataOutputStream ostreams = new DataOutputStream(predSocket.getOutputStream());
                ostreams.writeUTF("successor exiting");
                ostreams.writeUTF(Integer.toString(serverinfo.successorid));
                ostreams.writeUTF(Integer.toString(serverinfo.successorport));
                ostreams.writeUTF(serverinfo.successorip);
                sendKeys(ostream, serverinfo.predissesorid, serverinfo.id);
                String exitMessage = "nameserver: "+serverinfo.id+" exiting, nameserver: "+serverinfo.successorid+" now responsible for range: ["+(serverinfo.predissesorid+1)+", "+serverinfo.id+"]";
               
                ostream.writeUTF(exitMessage);
                break;
            }
            if(msg.equals("exit3")){
                System.out.println("connected to bootstrap");
                Socket predSocket = new Socket(serverinfo.predisessorip, serverinfo.predissesorPort);
                DataInputStream istreamp = new DataInputStream(predSocket.getInputStream());
                DataOutputStream ostreamp = new DataOutputStream(predSocket.getOutputStream());
                ostreamp.writeUTF("successor exiting");
                ostreamp.writeUTF(Integer.toString(serverinfo.successorid));
                ostreamp.writeUTF(Integer.toString(serverinfo.successorport));
                ostreamp.writeUTF(serverinfo.successorip);
                Socket sucSocket = new Socket(serverinfo.successorip, serverinfo.successorport);
                DataInputStream istreams = new DataInputStream(sucSocket.getInputStream());
                DataOutputStream ostreams = new DataOutputStream(sucSocket.getOutputStream());
                ostreams.writeUTF("predissesor exiting");
                ostreams.writeUTF(Integer.toString(serverinfo.predissesorid));
                ostreams.writeUTF(Integer.toString(serverinfo.predissesorPort));
                ostreams.writeUTF(serverinfo.predisessorip);
                sendKeys(ostreams, serverinfo.predissesorid, serverinfo.id);
                String exitMessage = "nameserver: "+serverinfo.id+" exiting, nameserver: "+serverinfo.successorid+" now responsible for range: ["+(serverinfo.predissesorid+1)+", "+serverinfo.id+"]";
                ostream.writeUTF(exitMessage);
                break;
            }
            if(msg.equals("successor exiting")){
                msg = istream.readUTF();
                serverinfo.successorid = Integer.parseInt(msg);
                msg = istream.readUTF();
                serverinfo.successorport = Integer.parseInt(msg);
                msg = istream.readUTF();
                serverinfo.successorip = msg;
            }
            if(msg.equals("predissesor exiting")){
                System.out.println("502 = "+map.get(502));
                msg = istream.readUTF();
                serverinfo.predissesorid = Integer.parseInt(msg);
                msg = istream.readUTF();
                serverinfo.predissesorPort = Integer.parseInt(msg);
                msg = istream.readUTF();
                serverinfo.predisessorip = msg;
                recieveKeys(istream);
            }
            if(msg.equals("update info")){
                msg = istream.readUTF();
                serverinfo.successorid = Integer.parseInt(msg);
                msg = istream.readUTF();
                serverinfo.successorport = Integer.parseInt(msg);
                msg = istream.readUTF();
                serverinfo.successorip = msg;
            }
            if(msg.equals("lookup")){
                msg = istream.readUTF();
                if(Integer.parseInt(msg)>serverinfo.id){
                    Socket sucSocket = new Socket(serverinfo.successorip, serverinfo.successorport);
                    DataInputStream istreams = new DataInputStream(sucSocket.getInputStream());
                    DataOutputStream ostreams = new DataOutputStream(sucSocket.getOutputStream());
                    ostreams.writeUTF("lookup");
                    ostreams.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    msg = msg+serverinfo.id+" ";
                    ostream.writeUTF(msg);
                }
                else{
                    if(map.containsKey(Integer.parseInt(msg))){
                        ostream.writeUTF(map.get(Integer.parseInt(msg)));
                    }
                    else{
                        ostream.writeUTF("-1");
                    }
                    ostream.writeUTF("Visited: "+serverinfo.id+" ");
                }
            }
            if(msg.equals("insert")){
                msg = istream.readUTF();
                if(Integer.parseInt(msg)>serverinfo.id){
                    System.out.println("sending this bitch over to you");
                    Socket sucSocket = new Socket(serverinfo.successorip, serverinfo.successorport);
                    DataInputStream istreams = new DataInputStream(sucSocket.getInputStream());
                    DataOutputStream ostreams = new DataOutputStream(sucSocket.getOutputStream());
                    ostreams.writeUTF("insert");
                    ostreams.writeUTF(msg);
                    msg = istream.readUTF();
                    ostreams.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = msg+serverinfo.id+" ";
                    ostream.writeUTF(msg);
                }
                else{
                    String valuee = istream.readUTF();
                    map.put(Integer.parseInt(msg), valuee);
                    }
                    ostream.writeUTF("key succesfully inserted into "+serverinfo.id);
                    ostream.writeUTF("Visited: "+serverinfo.id+" ");
                }
            if(msg.equals("remove")){
                msg = istream.readUTF();
                if(Integer.parseInt(msg)>serverinfo.id){
                    Socket sucSocket = new Socket(serverinfo.successorip, serverinfo.successorport);
                    DataInputStream istreams = new DataInputStream(sucSocket.getInputStream());
                    DataOutputStream ostreams = new DataOutputStream(sucSocket.getOutputStream());
                    ostreams.writeUTF("remove");
                    ostreams.writeUTF(msg);
                    msg = istreams.readUTF();
                    ostream.writeUTF(msg);
                    msg = istreams.readUTF();
                    msg = msg+serverinfo.id+" ";
                    ostream.writeUTF(msg);
                }
                else{
                    int keyy = Integer.parseInt(msg);
                    if(map.containsKey(keyy)){
                    map.remove(keyy);
                    ostream.writeUTF("key succesfully removed from "+serverinfo.id);
                    ostream.writeUTF("Visited: "+serverinfo.id+" ");
                    }
                    else{
                        ostream.writeUTF("key not found");
                        ostream.writeUTF("Visited: "+serverinfo.id+" ");
                    }

            }
        }
    }
        server.close();
     }catch(Exception e){
         System.out.println(e);
     }

    }



}