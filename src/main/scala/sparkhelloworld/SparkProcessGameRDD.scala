package sparkhelloworld 

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode

object SparkProcessGameRDD{

    def main(args: Array[String]){

        val conf = new SparkConf().setMaster("local[*]").setAppName("SparkRDDHelloworld")
        var sc = new SparkContext(conf)
        val spark = SparkSession
                  .builder
                  .appName("Spark sql demo")
                  .config("spark.master", "local")
                  .getOrCreate()

        import spark.implicits._
        sc.setLogLevel("ERROR")
        print (">>>>>>>>>>")
        var filePath = "data/war_data.json"
        val df = spark.read.json(filePath)
        df.show()
        // show the df schema 
        df.printSchema()
        // val lines = sc.textFile(filePath)
        // val words = lines.flatMap(line => line.split(" "))
        // val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
        //counts.collect().foreach(println)
        sc.stop()
        print ("SparkRDDHelloworld")
        print (">>>>>>>>>>")
}
 } 