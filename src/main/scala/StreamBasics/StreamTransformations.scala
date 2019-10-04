package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import util.Twitter

object StreamTransformations {
    def main(args: Array[String]): Unit = {
        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "StreamTransformations", Seconds(1))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val tweets = TwitterUtils.createStream(ssc, None)

        println(">>>>> start stream...")

        println(">>>>> run stream transformation...")

        tweets.filter(_.getContributors.length < 3)
            .map(tweet => tweet.isRetweet -> tweet.getText.length)
            .reduceByKey((pair1, pair2) => (pair1 + pair2) / 2)
            .print

        ssc.start
        ssc.awaitTermination()
    }
}