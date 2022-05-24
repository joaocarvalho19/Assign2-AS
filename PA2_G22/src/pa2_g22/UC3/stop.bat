cd ..
cd kfk

ECHO "Stop ZooKeeper...\n"
start .\\bin\\windows\\kafka-server-stop.bat 

timeout 3

:: Stop the Kafka broker service
ECHO "Stop Kafka broker...\n"
start .\\bin\\windows\\zookeeper-server-stop.bat

:: Clean up
for /l %%x in (1, 1, 6) do (
   @RD /S /Q "kafka-logs%%x"
   
)
@RD /S /Q "zookeeper"