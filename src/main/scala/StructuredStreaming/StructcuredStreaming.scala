package StructuredStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object StructcuredStreaming {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("StructuredStreaming")
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

        val lines = spark.readStream
            .schema(schema)
            .format("csv")
            .option("header", value = false)
            .load("output/tweetFiles/")

        val query = lines.toDF("id", "user_name", "place", "reply_to_screen_name", "created_at", "length", "first_hashtag")
            .select("user_name", "length", "first_hashtag")
            .where("first_hashtag is not null")
            .writeStream.format("console").queryName("someOutput").start

        query.awaitTermination()
        }
                        }