package SparkStreamingBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object Stream2SQL {
    def main(args: Array[String]): Unit = {

        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "SqlOperations", Seconds(10))

        Logger.getRootLogger.setLevel(Level.ERROR)

        //val stream = RedditUtils.createPageStream(Reddit.auth, List("sports"), ssc, pollingPeriodInSeconds=10)

        val stream = TwitterUtils.createStream(ssc, None)

        stream
            .map(p => (p.getId , p.getUser.getName, p.getCreatedAt.getTime))
            .foreachRDD { rdd =>
                val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
                import spark.implicits._

                // Convert RDD to DataFrame
                val df = rdd.toDF("id", "username", "time")

                // Create a temporary view
                df.createOrReplaceTempView("twitter")

                spark.sql("select id, username, from_unixtime(time) AS created from twitter").show()
            }

        ssc.start
        ssc.awaitTermination()
    }
}