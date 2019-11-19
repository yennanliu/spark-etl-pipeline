package util

import java.io.File
import com.typesafe.config.ConfigFactory

object S3Loader {
    def initialize(): Unit = {
        val config = ConfigFactory.parseFile(new File("config/s3.config"))
        System.setProperty("AWS_ACCESS_KEY_ID", config.getString("AWS_ACCESS_KEY_ID"))
        System.setProperty("AWS_SECRET_ACCESS_KEY", config.getString("AWS_SECRET_ACCESS_KEY"))
    }

    def get_AWS_ACCESS_KEY_ID(): String = {
        val config = ConfigFactory.parseFile(new File("config/s3.config"))
        ConfigFactory.parseFile(new File("config/s3.config")).getString("AWS_ACCESS_KEY_ID")
    }

    def get_AWS_SECRET_ACCESS_KEY(): String = {
        val config = ConfigFactory.parseFile(new File("config/s3.config"))
        ConfigFactory.parseFile(new File("config/s3.config")).getString("AWS_SECRET_ACCESS_KEY")
    }
}
