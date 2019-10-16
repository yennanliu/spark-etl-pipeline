package StructuredStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object StructuredStreamingOperations {
    def main(args: Array[String]): Unit = {
        
        val spark = SparkSession
            .builder
            .appName("StructuredStreamingOperations")
            .master("local[*]")
            .getOrCreate()

        import spark.implicits._

        Logger.getRootLogger.setLevel(Level.ERROR)

        println(">>>>> read stream from kafka...")

        val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")

        val df = spark
            .readStream
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", "tweets")
            .load()

        println(">>>>> kafka stream to df...")

        println(">>>>> query df...")

        val query = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
            .as[(String, String)]
            .map(_._2.split(":")) // Message format: id:userName:place:replyToScreenName:createdAt:length:firstHashtag
            .toDF("id", "userName", "place", "replyToScreenName", "createdAt", "length", "firstHashtag")
            .dropDuplicates("id")
            .select("place", "id", "length")
            .groupBy("place")
            .agg(count("id"), avg("length"))
            .writeStream.format("console")
            .queryName("streamingOperationsOutput").start

        query.awaitTermination()
    }
}