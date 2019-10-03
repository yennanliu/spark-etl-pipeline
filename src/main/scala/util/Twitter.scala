package util

object Twitter {
    def initialize(): Unit = {
        System.setProperty("twitter4j.oauth.consumerKey", sys.env("TWITTER_COSUMER_KEY"))
        System.setProperty("twitter4j.oauth.consumerSecret", sys.env("TWITTER_COSUMER_SECRET"))
        System.setProperty("twitter4j.oauth.accessToken", sys.env("TWITTER_TOKEN"))
        System.setProperty("twitter4j.oauth.accessTokenSecret", sys.env("TWITTER_TOKEN_SECRET"))
    }
}