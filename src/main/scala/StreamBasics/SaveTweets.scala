package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object SaveTweets {
    def main(args: Array[String]): Unit = {
        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "SaveTweets", Seconds(1))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val tweets = TwitterUtils.createStream(ssc, None)

        println(">>>>> start stream...")

        println(">>>>> save stream...")
        tweets.repartition(1)
            .saveAsTextFiles("output/SaveTweets/batch", "json")

        ssc.start
        ssc.awaitTermination()
    }

}