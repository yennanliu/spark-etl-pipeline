package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object TransformOperation {
    def main(args: Array[String]): Unit = {
        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "TransformOperation", Seconds(2))

        Logger.getRootLogger.setLevel(Level.ERROR)

        println(">>>>> start stream...")
        println(">>>>> stream transform operation...")

        val tweets = TwitterUtils.createStream(ssc, None)
        val langNames = ssc.sparkContext.parallelize(Seq("de" -> "German", "en" -> "English", "es" -> "Spanish", "fr" -> "French", "jp"->"Japanese"))

        tweets.map(tweet => tweet.getLang -> tweet.getText)
            .transform(_.join(langNames))
                .print

        ssc.start
        ssc.awaitTermination()
    }
}