package StreamHelloworld

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import util.Twitter


object Stream2SQL {
  def main(args: Array[String]): Unit = {
    Twitter.initialize()

    val ssc = new StreamingContext("local[*]", "PrintTwitterStream", Seconds(1))

    val conf = new SparkConf().setAppName("Simple sql app")

    val sc = SparkContext.getOrCreate(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    import sqlContext.implicits._

    Logger.getRootLogger.setLevel(Level.ERROR)

    val tweets = TwitterUtils.createStream(ssc, None)

    println(">>>>> start stream...")

    tweets.map(status => status.getText).print

    val tweet_words = tweets.map(status => status.getText)
                      .flatMap(line => line.split(" "))
                      .map(word => (word, 1))

    // val tweets_info = tweets.foreachRDD{
    //                     rdd => if (rdd.count() > 0){
    //                         rdd.map( t => (t.getId, t.getUser.getName, t.getCreatedAt.getTime))
    //                                                 }
    //                                     }

    // val data = tweets.map { status =>
    // val tags = status.getHashtagEntities.map(_.getText.toLowerCase)
    // (status.getText, tags)
    // }

    // data.foreachRDD { rdd =>
    // rdd.toDF().registerTempTable("tmp")
    // }

    // sqlContext.sql("select * from tmp").show()
    
    tweet_words.print
    //tweets_info
    
    ssc.start
    ssc.awaitTermination()
  }
}