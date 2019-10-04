package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object WindowOperation {
    def main(args: Array[String]): Unit = {
        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "WindowOperation", Seconds(1))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val tweets = TwitterUtils.createStream(ssc, None)

        println(">>>>> start stream...")
        println(">>>>> run WindowOperation...")

        tweets.filter(_.getHashtagEntities.length > 0)
            .map(_.getHashtagEntities.head.getText)
            .window(Seconds(5))
            .print

        ssc.start
        ssc.awaitTermination()
    }
}