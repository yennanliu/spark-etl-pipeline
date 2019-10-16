package StructuredStreaming

import java.nio.file.{Files, StandardCopyOption}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

import scala.reflect.io.File

object TweetProducer {
    def main(args: Array[String]): Unit = {

        Twitter.initialize()

        val ssc = new StreamingContext("local[2]", "TweetProducer", Seconds(1))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val tweets = TwitterUtils.createStream(ssc, None)
        val tmpDirectory = "output/tweetFiles_tmp/"
        val directory = "output/tweetFiles/"

        println(">>>>> start stream and save as temp-file, file...")

        tweets.filter(_.getLang == "en")
              .foreachRDD{rdd =>
            if (rdd.count() > 0) {
                val fileName = "tweets_" + System.currentTimeMillis()
                val tmpPath = new java.io.File(tmpDirectory + fileName)
                val path = new java.io.File(directory + fileName)
                File(tmpPath.toString())
                    .writeAll( rdd
                    .map(t => t.getId + "," + t.getUser.getName + "," + (if (t.getPlace == null) "" else t.getPlace.getName)
                              + "," + t.getInReplyToScreenName + "," + t.getCreatedAt.getTime
                              + "," + t.getText.length + "," + t.getHashtagEntities.map(_.getText).headOption.getOrElse(""))
                    .collect()
                    .mkString("\n") )

                Files.move(tmpPath.toPath, path.toPath, StandardCopyOption.ATOMIC_MOVE)
                }
            }

        ssc.start
        ssc.awaitTermination()
    }
}