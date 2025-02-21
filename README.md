<h1 align="center">SPARK-ETL-PIPELINE</h1>
<h4 align="center">demo various data fetch/transform process via Spark Scala </h4>

<p align="center">
<!--- travis -->
<a href="https://travis-ci.com/yennanliu/spark-etl-pipeline"><img src="https://travis-ci.com/yennanliu/spark-etl-pipeline.svg?branch=master"></a>
<!--- PR -->
<a href="https://github.com/yennanliu/spark-etl-pipeline/pulls"><img src="https://img.shields.io/badge/PRs-welcome-6574cd.svg"></a>
<!--- notebooks mybinder -->
<a href="https://mybinder.org/v2/gh/yennanliu/spark-etl-pipeline/master"><img src="https://mybinder.org/badge_logo.svg"></a>
<!--- hit count -->
<a href="http://hits.dwyl.io/yennanliu/spark-etl-pipeline"><img src="http://hits.dwyl.io/yennanliu/spark-etl-pipeline.svg"></a>
</p>

## Scala Projects 
* [spark_emr_dev](https://github.com/yennanliu/spark_emr_dev) - Demo of submitting Hadoop ecosystem jobs to AWS EMR
* [spark-etl-pipeline](https://github.com/yennanliu/spark-etl-pipeline) - Demo of various Spark ETL processes
* [utility_Scala](https://github.com/yennanliu/utility_Scala) - Scala/Spark programming basic demo 

## File structure

```
# ├── Dockerfile         : Dockerfile make scala spark env 
# ├── README.md
# ├── archived           : legacy spark scripts in python/java...
# ├── build.sbt          : (scala) sbt file build spark scala dependency 
# ├── config             : config for various services. e.g. s3, DB, hive..
# ├── data               : sample data for some spark scripts demo
# ├── output             : where the spark stream/batch output to  
# ├── project            : (scala) other sbt setting : plugins.sbt, build.properties...
# ├── python             : helper python script 
# ├── run_all_process.sh : script demo run minimum end-to-end spark process
# ├── script             : helper shell script
# ├── src                : (scala) MAIN SCALA SPARK TESTS/SCRIPTS 
# ├── target             : where the final complied jar output to  (e.g. target/scala-2.11/spark-etl-pipeline-assembly-1.0.jar)
# └── travis_build.sh    : travis build file
```

## Prerequisites 

1. Modify [config](https://github.com/yennanliu/spark-etl-pipeline/tree/master/config) with yours and rename them (e.g. `twitter.config.dev` -> `twitter.config`) to access services like data source, file system.. and so on. 
2. Install SBT as scala dependency management tool 
3. Install Java, Spark 
4. Modify [build.sbt](https://github.com/yennanliu/spark-etl-pipeline/blob/master/build.sbt) aligned your dev env
5. Check the spark etl scripts : [src](https://github.com/yennanliu/spark-etl-pipeline/tree/master/src/main/scala) 


## Process 

```
sbt clean compile -> sbt test -> sbt run -> sbt assembly -> spark-submit <spark-script>.jar
```

## Quick Start

```bash
$ git clone https://github.com/yennanliu/spark-etl-pipeline.git && cd spark-etl-pipeline && bash run_all_process.sh
```

## Quick Start Manually

<details>
<summary>Quick Start Manually</summary>

```bash
# STEP 0) 
$ cd ~ && git clone https://github.com/yennanliu/spark-etl-pipeline.git && cd spark-etl-pipeline

# STEP 1) download the used dependencies.
$ sbt clean compile

# STEP 2) print twitter via spark stream  via sbt run`
$ sbt run

# # STEP 3) create jars from spark scala scriots 
$ sbt assembly
$ spark-submit spark-etl-pipeline/target/scala-2.11/spark-etl-pipeline-assembly-1.0.jar

```

```bash
# get fake page view event data 

# run the script generate page view 
$ sbt package
$ spark-submit \
  --class DataGenerator.PageViewDataGenerator \
  target/scala-2.11/spark-etl-pipeline_2.11-1.0.jar

# open the other terminal to receive the event
$ curl 127.0.0.1:44444

```
</details> 

## Quick Start Docker

<details>
<summary>Quick Start Docker</summary>

```bash 
# STEP 0) 
$ git clone https://github.com/yennanliu/spark-etl-pipeline.git

# STEP 1) 
$ cd spark-etl-pipeline

# STEP 2) docker build 
$ docker build . -t spark_env

# STEP 3) ONE COMMAND : run the docker env and sbt compile and sbt run and assembly once 
$ docker run  --mount \
type=bind,\
source="$(pwd)"/.,\
target=/spark-etl-pipeline \
-i -t spark_env \
/bin/bash  -c "cd ../spark-etl-pipeline && sbt clean compile && && sbt assembly && spark-submit spark-etl-pipeline/target/scala-2.11/spark-etl-pipeline-assembly-1.0.jar"

# STEP 3') : STEP BY STEP : access docker -> sbt clean compile -> sbt run -> sbt assembly -> spark-submit 
# docker run 
$ docker run  --mount \
type=bind,\
source="$(pwd)"/.,\
target=/spark-etl-pipeline \
-i -t spark_env \
/bin/bash 
# inside docker bash 
root@942744030b57:~ cd ../spark-etl-pipeline && sbt clean compile && sbt run 

root@942744030b57:~ cd ../spark-etl-pipeline && spark-submit spark-etl-pipeline/target/scala-2.11/spark-etl-pipeline-assembly-1.0.jar

```
</details>

## Ref 

<details>
<summary>Ref</summary>

- Stream via python socket 
	- https://pythonprogramming.net/buffering-streaming-data-sockets-tutorial-python-3/
- Install spark + yarn + hadoop via docker 
	- https://medium.com/@thiagolcmelo/submitting-a-python-job-to-apache-spark-on-docker-b2bd19593a06
	- https://www.svds.com/develop-spark-apps-on-yarn-using-docker/
</details>

## Dataset 

<details>
<summary>Dataset</summary>

- Twitch API (`stream`)
	- https://dev.twitch.tv/docs/v5/reference/streams/
- Dota2 API (`stream`)
	- https://docs.opendota.com/#section/Authentication
- NYC TLC Trip Record dataset (taxi) (`large dataset`)
	- https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page
- Amazon Customer Reviews Dataset  (`large dataset`)
	- https://registry.opendata.aws/amazon-reviews/
- Github repo dataset (`large dataset`)
	- https://www.kaggle.com/github/github-repos
- Hacker news dataset (`large dataset`)
 	- https://www.kaggle.com/hacker-news/hacker-news
- Stackoverflow dataset (`large dataset`)
	- https://www.kaggle.com/stackoverflow/stackoverflow
- Yelp dataset (`large dataset`)
	- https://www.kaggle.com/yelp-dataset/yelp-dataset
- Relational dataset (RDBMS online free dataset)
	- https://relational.fit.cvut.cz/search
- Awesome public streaming date
	- https://github.com/ColinEberhardt/awesome-public-streaming-datasets
- NYC SUBWAY REALTIME API
	- http://datamine.mta.info/
	- https://erikbern.com/2016/04/04/nyc-subway-math.html
	- https://github.com/erikbern/mta

- Github mirror data 
	- https://ghtorrent.org/downloads.html
</details> 