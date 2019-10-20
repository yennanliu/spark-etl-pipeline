package TwitterTrend

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object PrintStreamTwitterTrend {
  def main(args: Array[String]): Unit = {
    
    Twitter.initialize()

    val ssc = new StreamingContext("local[*]", "PrintTwitterStream", Seconds(10))

    Logger.getRootLogger.setLevel(Level.ERROR)

    val stream = TwitterUtils.createStream(ssc, None)

        stream
            .map(p => (p.getUser.getName, 
                       (if (p.getPlace == null) "" else p.getPlace.getName), 
                       p.getCreatedAt.getTime, 
                       p.getHashtagEntities.map(_.getText).headOption.getOrElse(""))
                       )
            .foreachRDD { rdd =>
                val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
                import spark.implicits._

                val df = rdd.toDF("username","place","time", "hasg_tag")
                df.createOrReplaceTempView("twitter")
                spark.sql("select * from twitter").show()
            }

    ssc.start
    ssc.awaitTermination()
                                }
                                                }