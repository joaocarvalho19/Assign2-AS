:: Start the ZooKeeper service
cd ..
cd kfk

ECHO "Start ZooKeeper...\n"
start .\\bin\\windows\\zookeeper-server-start.bat config/zookeeper.properties 

timeout 5

:: Start the Kafka broker service
ECHO "Start Kafka broker...\n"
for /l %%x in (1, 1, 6) do (
   start .\\bin\\windows\\kafka-server-start.bat config\\server%%x.properties 
   
)

timeout 5

:: Create topic Sensor
.\\bin\\windows\\kafka-topics.bat --create --topic Sensor --replication-factor 3 --partitions 6 --config min.insync.replicas=2 --bootstrap-server localhost:9092