package sparksqldemo

import org.apache.spark.sql.SaveMode
// $example on:init_session$
import org.apache.spark.sql.SparkSession

object SparkSQLDemo {
  def main(args: Array[String]) {

    val spark = SparkSession
      .builder
      .appName("Spark sql demo")
      .config("spark.master", "local")
      .getOrCreate()

    import spark.implicits._

    // load the txt file 
    val filePath = "data/example.json" 
    val df = spark.read.json(filePath)
    // show the df
    df.show()
    // show the df schema 
    df.printSchema()
    spark.stop()
  }

}