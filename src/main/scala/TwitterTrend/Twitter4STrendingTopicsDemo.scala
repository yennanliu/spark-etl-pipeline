  package TwitterTrend

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.LocationTrends
import com.danielasfregola.twitter4s.entities.{HashTag, Tweet}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import scala.concurrent.ExecutionContext.Implicits.global

object Twitter4STrendingTopicsDemo extends App {

  val consumerToken = ConsumerToken(key = "my-consumer-key", secret = "my-consumer-secret")
  val accessToken = AccessToken(key = "my-access-key", secret = "my-access-secret")
  
  val client = TwitterRestClient()

  def printTrendingTopics(locationTrends: Seq[LocationTrends]): Unit =
    locationTrends.foreach { locationTrend =>
      println()
      println("Trends for: " + locationTrend.locations.map(_.name).mkString("/"))
      locationTrend.trends.take(10).foreach(t => println(t.name))
    }

  lazy val globalTrends = for {
    globalTrendsResult <- client.globalTrends().map(_.data)
  } yield printTrendingTopics(globalTrendsResult)

  lazy val ukTrends = for {
    locations <- client.locationTrends.map(_.data)
    ukWoeid = locations.find(_.name == "United Kingdom").map(_.woeid)
    if ukWoeid.isDefined
    ukTrendsResult <- client.trends(ukWoeid.get).map(_.data)
  } yield printTrendingTopics(ukTrendsResult)

  lazy val nearMeTrends = for {
    locationNearestMe <- client.closestLocationTrends(43.6426, -79.3871).map(_.data)
    woeId = locationNearestMe.headOption.map(_.woeid)
    if woeId.isDefined
    locationNearestMeResult <- client.trends(woeId.get).map(_.data)
  } yield printTrendingTopics(locationNearestMeResult)

  val allTheTrends = for {
    _ <- globalTrends
    _ <- ukTrends
    _ <- nearMeTrends
  } yield ()

}