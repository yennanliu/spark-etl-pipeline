package StreamHelloworld

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import util.Twitter

object PrintTwitterStream {
  def main(args: Array[String]): Unit = {
    
    Twitter.initialize()

    val ssc = new StreamingContext("local[*]", "PrintTwitterStream", Seconds(1))

    Logger.getRootLogger.setLevel(Level.ERROR)

    val tweets = TwitterUtils.createStream(ssc, None)

    println(">>>>> start stream...")

    tweets.map(status => status.getText).print

    ssc.start
    ssc.awaitTermination()
  }
}