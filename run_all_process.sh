#!/bin/sh

#################################################################
# SCRIPT RUN ALL PROCESSES AT ONCE 
#################################################################

git clone https://github.com/yennanliu/spark-etl-pipeline.git
cd spark-etl-pipeline
docker build . -t spark_env
docker run  --mount \
type=bind,\
source="$(pwd)"/.,\
target=/spark-etl-pipeline \
-i -t spark_env \
/bin/bash  -c "cd ../spark-etl-pipeline && sbt clean compile && sbt assembly && spark-submit /spark-etl-pipeline/target/scala-2.11/spark-etl-pipeline-assembly-1.0.jar"
