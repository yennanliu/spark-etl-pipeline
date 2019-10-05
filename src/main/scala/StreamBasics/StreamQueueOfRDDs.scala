package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object StreamQueueOfRDDs {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext("local[*]", "StreamQueueOfRDDs", Seconds(1))

    Logger.getRootLogger.setLevel(Level.ERROR)

    val queue = new mutable.Queue[RDD[String]]
    val rdd = ssc.sparkContext.parallelize(Seq("received", "some", "data"))
    queue.enqueue(rdd)

    println(">>>>> start stream...")

    val stream = ssc.queueStream(queue)
    stream.print()

    ssc.start

    println(">>>>> start stream queue...")

    for (i <- 1 to 10) {
        queue.enqueue(ssc.sparkContext.parallelize(Seq("received", "more", "data", i.toString)))
        Thread.sleep(2000)
    }
    ssc.awaitTermination()
  }
}