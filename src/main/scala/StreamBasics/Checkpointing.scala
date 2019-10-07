package StreamBasics

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import util.Twitter

object Checkpointing {
    val checkpointDirectory = "checkpointing"

    def main(args: Array[String]): Unit = {
        // Get StreamingContext from data stored in the checkpoint directory or create a new one
        val ssc = StreamingContext.getOrCreate(checkpointDirectory, functionToCreateContext)

        Logger.getRootLogger.setLevel(Level.ERROR)

        ssc.start
        ssc.awaitTermination()
    }

    private def functionToCreateContext(): StreamingContext = {
        val ssc = new StreamingContext("local[*]", "Checkpointing", Seconds(10))

        Twitter.initialize()
        val tweets = TwitterUtils.createStream(ssc, None)

        println(">>>>> start stream...")

        tweets.map(t => (t.getUser.getName, 1))
            .updateStateByKey(updateFunction)
            .transform(_.sortBy(_._2, ascending = false))
            .print

        println(">>>>> spark checkpoint...")

        ssc.checkpoint(checkpointDirectory)
        ssc
    }

    private def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
        val count = runningCount.getOrElse(0) + newValues.sum
        Some(count)
    }
}