package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import util.Twitter

object StreamWordCount {
    def main(args: Array[String]): Unit = {
        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "StreamTransformations", Seconds(1))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val tweets = TwitterUtils.createStream(ssc, None)

        println(">>>>> start stream...")

        println(">>>>> run stream word count...")

        //tweets.map(status => status.getText).print

        tweets.map(status => status.getText)
              .flatMap(line => line.split(" "))
              .map(word => (word, 1))
              .print

        ssc.start
        ssc.awaitTermination()
    }
}