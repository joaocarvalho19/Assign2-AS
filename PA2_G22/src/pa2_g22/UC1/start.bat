:: Start the ZooKeeper service
cd ..
cd kfk

ECHO "Start ZooKeeper...\n"
start .\\bin\\windows\\zookeeper-server-start.bat config/zookeeper.properties 

timeout 5

:: Start the Kafka broker service
ECHO "Start Kafka broker...\n"

start .\\bin\\windows\\kafka-server-start.bat config\\server1.properties 


timeout 5

:: Create topic Sensor
.\\bin\\windows\\kafka-topics.bat --create --topic Sensor --partitions 1 --bootstrap-server localhost:9092