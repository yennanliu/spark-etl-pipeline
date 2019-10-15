package AdvancedStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object StreamDatasetJoin {
    def main(args: Array[String]): Unit = {

        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "StreamDatasetJoin", Seconds(4))

        Logger.getRootLogger.setLevel(Level.ERROR)

        //val redditStream = RedditUtils.createPageStream(Reddit.auth, List("sports"), ssc, pollingPeriodInSeconds = 2)

        val tweetStream = TwitterUtils.createStream(ssc, None)

        val countTexts = ssc.sparkContext.parallelize(Seq(1 -> "One", 2 -> "Two", 3 -> "Three", 4 -> "Four or more"))

        tweetStream
            .window(Minutes(1), Seconds(12))
            .map(r => (r.getText.length, 1))
            .transform( _.reduceByKey(_ + _)
            .map(r => if (r._2 > 3) (4, r._1) else r.swap)
            .join(countTexts)
            .sortByKey(ascending = false)
            .map(_._2))
            .print

        ssc.start
        ssc.awaitTermination()
    }
}