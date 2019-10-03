package mydevclass

import java.io.File
import com.typesafe.config.ConfigFactory

object load_creds_dev{ 
    def main(args: Array[String]){ 
        // this can be set into the JVM environment variables, you can easily find it on google
        //val config = ConfigFactory.load()
        val config = ConfigFactory.parseFile(new File("config/twitter.config"))

        config.getString("TWITTER_COSUMER_KEY")
        println (config.getString("TWITTER_COSUMER_KEY"))
        println (config.getString("TWITTER_COSUMER_SECRET"))
        println (config.getString("TWITTER_TOKEN"))
        println (config.getString("TWITTER_TOKEN_SECRET"))    } 
} 