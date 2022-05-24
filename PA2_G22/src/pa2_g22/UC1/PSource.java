/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa2_g22.UC1;

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
    private Client client;
    
    public PSource(String filename, String hostname, int port) {
        file = new File(filename);
        client = new Client(hostname, port);
    }
    
    /**
     * Read lines from the file and send to the producer.
     */
    private void readData() {
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
                    if(client.createSocket())
                        client.writeObject("CONTENT-ID:"+content[0]+" TEMP:"+content[1]+" TIME:"+content[2]);
                    count=0;
                }
                else{
                    String cont = sc.nextLine();
                    //System.out.println(content);
                    content[count] = cont;
                    count++;
                }
            }
            sc.close(); 
            
            // End
            if(client.createSocket())
                client.writeObject("END");
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        /*catch (InterruptedException e) {
            System.out.println("thread 2 interrupted");
        }*/
    }
    
    /**
     * PSource life cycle.
     */
    @Override
    public void run() {
        readData();
    }
}
