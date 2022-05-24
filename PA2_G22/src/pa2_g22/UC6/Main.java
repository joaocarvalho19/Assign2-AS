/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa2_g22.UC6;

import pa2_g22.UC5.*;
import pa2_g22.UC3.*;
import pa2_g22.UC2.*;
import java.io.IOException;

/**
 *
 * @author joaoc
 */
public class Main{
    //private static final String filePath = System.getProperty("user.dir") + "/src/Data/sensor.txt";
    
    // Sensor data.
    private static final String FILENAME = "src\\pa2_g22\\data\\sensor_small.txt";
    
    //private static final File newFile = new File(parentDir,"/data/sensor_small.txt");
    
    // Host name of the producer.
    private static final String HOSTNAME = "localhost";
    
    public static void main(String args[]) throws IOException {
    
        final int NUMBER_PRODUCERS = 1;
        final int NUMBER_CONSUMERS = 3;
        
        final PConsumer[] consumers = new PConsumer[NUMBER_CONSUMERS];
        final PProducer[] producers = new PProducer[NUMBER_PRODUCERS];
        
        for(int i = 0; i < NUMBER_CONSUMERS; i++){
            consumers[i] = new PConsumer(i);
            consumers[i].start();
        } 
        
        for(int i = 0; i < NUMBER_PRODUCERS; i++){
            producers[i] = new PProducer(i);
            producers[i].start();
        }
        
        final PSource source = new PSource(FILENAME, HOSTNAME, NUMBER_PRODUCERS);
        source.start();
        
        /*try {
            for ( int i = 0; i < NUMBER_PRODUCERS; i++ )
                producers[i].join();
            for ( int i = 0; i < NUMBER_CONSUMERS; i++ )
                consumers[i].join();
            source.join();
            
        } catch ( Exception ex ) {} */
     }
}
