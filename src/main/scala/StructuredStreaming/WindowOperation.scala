package StructuredStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.streaming.Seconds

object WindowOperation {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("WindowOperations")
            .master("local[*]")
            .getOrCreate()

        import spark.implicits._

        Logger.getRootLogger.setLevel(Level.ERROR)

        println(">>>>> load the scrape twitter json...")
        println(">>>>> if there is error, please check : ")
        println(">>>>> 1) the file output/tweetFiles/ exist")
        println(">>>>> 2) Re-run twitter json scrapping script : src/main/scala/StructuredStreaming/TweetProducer.scala")

        spark.read.json("output/tweetFiles/").printSchema()

        val schema = spark.read
            .format("csv")
            .option("header", value = false)
            .option("inferSchema", value = true)
            .load("output/tweetFiles/")
            .schema

        val records = spark.readStream
            .schema(schema)
            .format("csv")
            .option("header", value = false)
            .load("output/tweetFiles/")

        // the way select columns from spark dataframe
        println(">>>>> select df columns...")

        val query = records.toDF("id", "user_name", "place", "reply_to_screen_name", "created_at", "length", "first_hashtag")
            .select(to_timestamp(from_unixtime($"created_at")).as("created"), $"first_hashtag")
            .where("first_hashtag is not null")
            .withWatermark("created", "2 minutes")
            .groupBy(window($"created", "3 minutes", "20 seconds"), $"first_hashtag")
            .count()
            .writeStream.format("console")
            .queryName("someOutput")
            .start

        query.awaitTermination()
    }
}