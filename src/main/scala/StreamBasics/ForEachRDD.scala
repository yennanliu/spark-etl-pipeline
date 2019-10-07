package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object ForEachRDD {
    def main(args: Array[String]): Unit = {
        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "CountByValueAndWindow", Seconds(1))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val tweets = TwitterUtils.createStream(ssc, None)

        ssc.checkpoint("checkpoints")

        println(">>>>> ForEachRDD...")

        tweets.map(status => status.getText).print 

        tweets.map(status => status.getText)
              .foreachRDD{
                rdd => if (rdd.count() > 0 ) 
                { println (" RDD count() > 0")}
        }

        ssc.start
        ssc.awaitTermination()
    }
}