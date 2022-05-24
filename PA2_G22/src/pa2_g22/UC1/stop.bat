cd ..
cd kfk

ECHO "Stop ZooKeeper...\n"
start .\\bin\\windows\\kafka-server-stop.bat 

timeout 3

:: Stop the Kafka broker service
ECHO "Stop Kafka broker...\n"
start .\\bin\\windows\\zookeeper-server-stop.bat

:: Clean up
@RD /S /Q "kafka-logs1"

@RD /S /Q "zookeeper"