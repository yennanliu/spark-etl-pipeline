package util

import java.io.File
import com.typesafe.config.ConfigFactory

// object Twitter {
//     def initialize(): Unit = {
//         System.setProperty("twitter4j.oauth.consumerKey", sys.env("TWITTER_COSUMER_KEY"))
//         System.setProperty("twitter4j.oauth.consumerSecret", sys.env("TWITTER_COSUMER_SECRET"))
//         System.setProperty("twitter4j.oauth.accessToken", sys.env("TWITTER_TOKEN"))
//         System.setProperty("twitter4j.oauth.accessTokenSecret", sys.env("TWITTER_TOKEN_SECRET"))
//     }
// }

object Twitter {
    def initialize(): Unit = {
        val config = ConfigFactory.parseFile(new File("config/twitter.config"))
        System.setProperty("twitter4j.oauth.consumerKey", config.getString("TWITTER_COSUMER_KEY"))
        System.setProperty("twitter4j.oauth.consumerSecret", config.getString("TWITTER_COSUMER_SECRET"))
        System.setProperty("twitter4j.oauth.accessToken", config.getString("TWITTER_TOKEN"))
        System.setProperty("twitter4j.oauth.accessTokenSecret", config.getString("TWITTER_TOKEN_SECRET"))
    }
}
