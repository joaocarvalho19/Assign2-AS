/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa2_g22.UC6;

import java.time.Duration;
import java.util.Arrays;
import java.util.ArrayList;
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
    // List of temps
    private final ArrayList<Double> temps_list = new ArrayList<>();
    
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
        
        // The minimum amount of data the server should return for a fetch request.
        properties.put("fetch.min.bytes", 1);    // Default value
        
        // If true the consumer's offset will be periodically committed in the background.
        properties.put("enable.auto.commit", true); // Default value
        
        // What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
        properties.put("auto.offset.reset", "latest");  // If the consumer crashes or is shut down, its partitions will be re-assigned to another member, which will begin consumption from the last committed offset of each partition.
        
        //properties.put("partition", id);
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
                        
                        Double currTemp = Double.parseDouble(rec.value().split("TEMP:")[1].split(" ")[0]);
                        temps_list.add(currTemp);   // Add current Temp to list
                        
                        String sensorID = rec.value().split("ID:")[1].split(" ")[0];
                        consGUI.incrSensorRecordsNum(Integer.parseInt(sensorID));      // update number of records by sensor ID
                        
                    }
                    Double temp_sum = 0.0;
                    if(!this.temps_list.isEmpty()){
                        for(Double temp : this.temps_list){
                            temp_sum += temp;
                            
                        }
                        Double average = temp_sum / this.temps_list.size();
                        consGUI.updateAvgTemp(String.valueOf(average));
                    }
                    
                    //consumer.commitSync(); 
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
