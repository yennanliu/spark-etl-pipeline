#!/bin/sh

echo '>>>> DOCKER BUILD...'
docker build . -t spark_env
echo '>>>> RUN SCALA SPARK WORLD COUNT BUILD...'
docker run  --mount \
type=bind,\
source="$(pwd)"/.,\
target=/spark-etl-pipeline \
-i -t spark_env \
/bin/bash  -c "cd ../spark-etl-pipeline && sbt clean compile && sbt assembly && spark-submit /spark-word-count/target/scala-2.11/spark-etl-pipeline-assembly-1.0.jar"
