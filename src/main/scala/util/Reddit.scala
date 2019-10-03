package util

import com.github.catalystcode.fortis.spark.streaming.reddit.RedditAuth

object Reddit {
    lazy val auth = RedditAuth(
        applicationId = System.getenv("REDDIT_APPLICATION_ID"),
        secret = System.getenv("REDDIT_APPLICATION_SECRET")
    )
}