package DataGenerator

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Random

/** Represents a page view on a website with associated dimension data. */
class PageView(val url: String, val status: Int, val zipCode: Int, val userID: Int)
    extends Serializable {
  override def toString(): String = {
    "%s\t%s\t%s\t%s\n".format(url, status, zipCode, userID)
  }
}

object PageView extends Serializable {
  def fromString(in: String): PageView = {
    val parts = in.split("\t")
    new PageView(parts(0), parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
}
// scalastyle:off
/**
 * Generates streaming events to simulate page views on a website.
 *
 * This should be used in tandem with PageViewStream.scala. Example:
 *
 * To run the generator
 * `$ bin/run-example org.apache.spark.examples.streaming.clickstream.PageViewGenerator 44444 10`
 * To process the generated stream
 * `$ bin/run-example \
 *    org.apache.spark.examples.streaming.clickstream.PageViewStream errorRatePerZipCode localhost 44444`
 *
 */
// scalastyle:on
object PageViewDataGenerator {
  val pages = Map("http://foo.com/" -> 0.5,
                  "http://foo.com/news" -> 0.2,
                  "http://foo.com/newsx" -> 0.1,
                  "http://foo.com/contact" -> 0.1,
                  "http://foo.com/contactx" -> 0.1)
  val httpStatus = Map(200 -> .95,
                       404 -> .05)
  val userZipCode = Map(94709 -> .5,
                        94117 -> .5)
  val userID = Map((1 to 100).map(_ -> .01): _*)

  def pickFromDistribution[T](inputMap: Map[T, Double]): T = {
    val rand = new Random().nextDouble()
    var total = 0.0
    for ((item, prob) <- inputMap) {
      total = total + prob
      if (total > rand) {
        return item
      }
    }
    inputMap.take(1).head._1 // Shouldn't get here if probabilities add up to 1.0
  }

  def getNextClickEvent(): String = {
    val id = pickFromDistribution(userID)
    val page = pickFromDistribution(pages)
    val status = pickFromDistribution(httpStatus)
    val zipCode = pickFromDistribution(userZipCode)
    new PageView(page, status, zipCode, id).toString()
  }

  def main(args: Array[String]) {
    // work around : hard code the env arg here, but not via args input 
    // please check Line 81 (this script)

    // if (args.length != 2) {
    //   System.err.println("Usage: PageViewGenerator <port> <viewsPerSecond>")
    //   System.exit(1)
    // }

    // val port = args(0).toInt
    // val viewsPerSecond = args(1).toFloat
    // val sleepDelayMs = (1000.0 / viewsPerSecond).toInt
    // val listener = new ServerSocket(port)
    // hard code env arg here 
    val port = 44444
    val viewsPerSecond = 10 
    val sleepDelayMs = (1000.0 / viewsPerSecond).toInt
    val listener = new ServerSocket(port)
    println(s"Listening on port: $port")

    while (true) {
      val socket = listener.accept()
      new Thread() {
        override def run(): Unit = {
          println(s"Got client connected from: ${socket.getInetAddress}")
          val out = new PrintWriter(socket.getOutputStream(), true)

          while (true) {
            Thread.sleep(sleepDelayMs)
            out.write(getNextClickEvent())
            out.flush()
          }
          socket.close()
        }
      }.start()
    }
  }
}