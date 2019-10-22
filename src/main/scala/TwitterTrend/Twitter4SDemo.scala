package TwitterTrend

// https://github.com/DanielaSfregola/twitter4s-demo/blob/master/src/main/scala/rest/MyTopHashtags.scala

//import util.Twitter
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{HashTag, Tweet}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import scala.concurrent.ExecutionContext.Implicits.global
 
object Twitter4SDemo extends App {
 
  def getTopHashtags(tweets: Seq[Tweet], n: Int = 10): Seq[(String, Int)] = {
    val hashtags: Seq[Seq[HashTag]] = tweets.map { tweet =>
      tweet.entities.map(_.hashtags).getOrElse(Seq.empty)
    }
    val hashtagTexts: Seq[String] = hashtags.flatten.map(_.text.toLowerCase)
    val hashtagFrequencies: Map[String, Int] = hashtagTexts.groupBy(identity).mapValues(_.size)
    hashtagFrequencies.toSeq.sortBy { case (entity, frequency) => -frequency }.take(n)
  }
 
  //Twitter.initialize()
 
  val consumerToken = ConsumerToken(key = "my-consumer-key", secret = "my-consumer-secret")
  val accessToken = AccessToken(key = "my-access-key", secret = "my-access-secret")
  
  val client = new TwitterRestClient(consumerToken, accessToken)
 
  val user = "Trump"
  val result = client.homeTimeline(count = 200).map { ratedData =>
  val tweets  = ratedData.data
  val topHashtags: Seq[((String, Int), Int)] = getTopHashtags(tweets).zipWithIndex
  val rankings = topHashtags.map {case ((entity, frequency), idx) => s"[${idx + 1}] $entity (found $frequency times)"}
  println("MY TOP HASHTAGS:")
  println(rankings.mkString("\n"))

  }
 
}