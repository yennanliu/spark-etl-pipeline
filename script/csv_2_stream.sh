#!/bin/sh
#################################################################
# SCRIPT TRANSFORM CSV FILE AS KAFKA STREAM    
#################################################################

transform_csv_stream(){
cat data/log_demo.csv | kafka-console-producer --broker-list localhost:9092 --topic new_topic

}

loop_transform_csv_stream(){
for i in {1..100}; 
do 
    echo $i
    cat data/log_demo.csv | kafka-console-producer --broker-list localhost:9092 --topic new_topic
    sleep 5
done 
}

loop_transform_csv_stream