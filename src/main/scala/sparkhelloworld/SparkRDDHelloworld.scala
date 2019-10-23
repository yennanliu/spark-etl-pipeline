package sparkhelloworld 

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 

object SparkRDDHelloworld{

    def main(args: Array[String]){

        val conf = new SparkConf().setMaster("local[*]").setAppName("SparkRDDHelloworld")

        // var spark : SparkSession = SparkSession.builder
        //                                     .config(conf)
        //                                     .config("spark.master", "local")
        //                                     .getOrCreate()
        var sc = new SparkContext(conf)
        sc.setLogLevel("ERROR")
        print (">>>>>>>>>>")
        var filePath = "data/pic_data.txt"
        val lines = sc.textFile(filePath)
        val words = lines.flatMap(line => line.split(" "))
        val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
        counts.collect().foreach(println)
        sc.stop()
        print ("SparkRDDHelloworld")
        print (">>>>>>>>>>")
}
 } 