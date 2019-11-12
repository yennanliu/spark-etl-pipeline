package util

import java.io.File
import com.typesafe.config.ConfigFactory

object S3Loader {
    def initialize(): Unit = {
        val config = ConfigFactory.parseFile(new File("config/s3.config"))
        System.setProperty("accessKeyId", config.getString("AWS_ACCESS_KEY_ID"))
        System.setProperty("secretAccessKey", config.getString("AWS_SECRET_ACCESS_KEY"))
    }
}
