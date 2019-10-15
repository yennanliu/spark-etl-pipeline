package AdvancedStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object Accumulators {
    def main(args: Array[String]): Unit = {
        val ssc = new StreamingContext("local[*]", "Accumulators", Seconds(5))

        Logger.getRootLogger.setLevel(Level.ERROR)

        val accum = ssc.sparkContext.longAccumulator("My Accumulator")

        Twitter.initialize()
        val tweets = TwitterUtils.createStream(ssc, None)

        tweets.filter(_.getUser.getLocation != null)
            .map(_ => accum.add(1))
            .foreachRDD{rdd =>
                rdd.collect() // necessary to ensure the code in our map function above is actually executed
                println("Total count of user locations: " + accum.sum)
            }

        tweets.filter(_.getPlace != null)
            .map(_ => accum.add(1))
            .foreachRDD{rdd =>
                rdd.collect() // necessary to ensure the code in our map function above is actually executed
                println("Total count of user places: " + accum.sum)
            }

        ssc.start
        ssc.awaitTermination()
    }
}