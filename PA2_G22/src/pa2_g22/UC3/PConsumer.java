/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa2_g22.UC3;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * @author joaoc
 */
public class PConsumer extends Thread{
    // Topic name.
    private final String TOPIC = "Sensor";
    // Group name.
    private final String GROUP = "ConsumerGroup"; 
    
    Properties properties = new Properties();
    
    private final PConsumerGUI consGUI;
    
    KafkaConsumer<String, String> consumer;
    
    public PConsumer(int id) {
        
        //Open Gui
        consGUI = new PConsumerGUI();
        consGUI.setVisible(true);
        consGUI.updateTitle(id+1);
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", GROUP);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        properties.put("partition", id);
        // The minimum amount of data the server should return for a fetch request.
        properties.put("fetch.min.bytes", 100000);    // Improve throughput
        
        // If true the consumer's offset will be periodically committed in the background.
        properties.put("enable.auto.commit", false); // Data can be reprocessed
        
        // What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
        properties.put("auto.offset.reset", "latest");  // Default value
        
    }
    
    @Override
    public void run() {  
        try{            
            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(TOPIC));       
            while(true){
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> rec : records){
                        System.out.println("----Consumer: "+rec.value());
                        consGUI.appendRecord(rec.value());  // append Record to interface
                        consGUI.incrTotalRecordsNum();      // update total number of Records
                            
                        String sensorID = rec.value().split("ID:")[1].split(" ")[0];
                        consGUI.incrSensorRecordsNum(Integer.parseInt(sensorID));      // update number of records by sensor ID
                        
                    }
                    consumer.commitSync(); 
                }
            }
            
        catch(Exception ex){
                System.out.println(ex);
        }
        finally{
                //consumer.commitSync();
                consumer.close();
        }
    }

}
