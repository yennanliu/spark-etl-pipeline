package SparkStreamingBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object StreamSave2SQL {
    def main(args: Array[String]): Unit = {

        Twitter.initialize()

        val ssc = new StreamingContext("local[*]", "SqlOperations", Seconds(10))

        Logger.getRootLogger.setLevel(Level.ERROR)

        //val stream = RedditUtils.createPageStream(Reddit.auth, List("sports"), ssc, pollingPeriodInSeconds=10)

        val stream = TwitterUtils.createStream(ssc, None)

        stream
            .map(p => (p.getId , 
                       p.getUser.getName, 
                       (if (p.getPlace == null) "" else p.getPlace.getName), 
                       p.getInReplyToScreenName,
                       p.getCreatedAt.getTime, 
                       p.getText.length, 
                       p.getHashtagEntities.map(_.getText).headOption.getOrElse(""))
                       )
            .foreachRDD { rdd =>
                val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
                import spark.implicits._

                // Convert RDD to DataFrame
                val df = rdd.toDF("id","username","place","reply_screen_name","time","text_length", "hasg_tag")

                // Create a temporary view
                df.createOrReplaceTempView("twitter")

                spark.sql("select * from twitter").show()

                //spark.sql("select text_length, count(*) from twitter group by 1 order by 2 desc").show()
            }

        ssc.start
        ssc.awaitTermination()
    }
}