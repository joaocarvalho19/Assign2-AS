package pa2_g22.UC5;

import pa2_g22.UC3.*;
import java.net.*;
import java.io.*;

/**
 * Thread to read messages from a client connected to the OCC server.
 * @author Rafael Sá (104552), Luís Laranjeira (81526)
 */
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    PProducer prod;
  
        // Constructor
        public ClientHandler(Socket socket, PProducer prod){
            this.clientSocket = socket;
            this.prod = prod;
        }
        
        @Override
        public void run(){
            ObjectInputStream in = null;
    
            ObjectOutputStream out = null;
            
            try {
                    
                  // get the outputstream of client
                out = new ObjectOutputStream(clientSocket.getOutputStream());
  
                  // get the inputstream of client
                in = new ObjectInputStream(clientSocket.getInputStream());
  
                Object obj = in.readObject();
                //while ((obj = in.readObject()) != null) {
  
                    // writing the received message from client
                    String msg = obj.toString();
                    
                    //System.out.printf(" Sent from the client: %s\n",obj);
                    if(msg.contains("CONTENT")){
                        String content = msg.split("-")[1];
                        String sensorID = content.split("ID:")[1].split(" ")[0];
                        
                        prod.appendRecord(content);
                        prod.incrTotalRecordsNum();
                        prod.incrSensorRecordsNum(Integer.parseInt(sensorID));

                        // content that Producer will send
                        prod.sendToTopic(content);
                        
                    }
                    else if(msg.equals("END")){
                        System.out.println("EXIT");
                    }
                    Thread.sleep(500);
                    out.writeObject("RSPONSE: OK!");
                    

                //}
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
            System.out.println("thread 2 interrupted");
        }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}