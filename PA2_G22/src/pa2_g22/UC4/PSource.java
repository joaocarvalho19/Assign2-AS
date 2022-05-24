/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa2_g22.UC4;

import pa2_g22.UC3.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import pa2_g22.Communication.Client;

/**
 *
 * @author joaoc
 */
public class PSource extends Thread{
    
    // File with the data.
    private final File file; 
    // Communication client.
    private Client[] clients;
    // Socket default port for the producer.
    private final int PORT = 4000;
    // Number of Producers
    private final int producersNum;
    
    public PSource(String filename, String hostname, int producersNum) {
        file = new File(filename);
        this.producersNum = producersNum;
        clients = new Client[producersNum];
        for(int i = 0; i<producersNum; i++){
            clients[i] = new Client(hostname, PORT+i);
        }
        
    }
    
    /**
     * PSource life cycle.
     */
    @Override
    public void run() {
        Scanner sc;
        String[] content = new String[3];
        for(int i = 0; i<3; i++)
            content[i] = "";
        int count=0;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                if(count==2){
                    content[count] = sc.nextLine();
                    
                    // Send msg to producer
                    int sensorID = Integer.parseInt(content[0]);
                    if(clients[sensorID-1].createSocket())
                        clients[sensorID-1].writeObject("CONTENT-ID:"+content[0]+" TEMP:"+content[1]+" TIME:"+content[2]);
                    count=0;
                }
                else{
                    String cont = sc.nextLine();
                    content[count] = cont;
                    count++;
                }
            }
            sc.close(); 
            
            // End
            for(int i = 0; i<this.producersNum; i++){
                if(clients[i].createSocket())
                    clients[i].writeObject("END");
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        /*catch (InterruptedException e) {
            System.out.println("thread 2 interrupted");
        }*/
    }
   
}
