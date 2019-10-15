package PerformanceAndEventQueues

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import org.apache.spark.streaming.kafka._
import kafka.serializer.StringDecoder

object KafkaIntegration {
    def main(args: Array[String]): Unit = {
        val ssc = new StreamingContext("local[*]", "KafkaIntegration", Seconds(2))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")

        val topics = List("userEvents").toSet

        // Kafka stream contains (topic, message) pairs
        val events = KafkaUtils
            .createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
            .map(_._2)

        // Message format: eventName:userId:userName:jsonEventData
        events.filter(_.startsWith("order:"))
            .map(_.split(":"))
            .map(e => "User " + e(2) + " ordered a product!")
                .print

        ssc.checkpoint("kafkaIntegration")
        ssc.start
        ssc.awaitTermination()
    }
}