package sparkhelloworld 

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode

object SparkProcessGameRDD{

    def main(args: Array[String]){

        val conf = new SparkConf()
                       .setMaster("local[*]")
                       .setAppName("SparkRDDHelloworld")
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
        var dfRDD = df.rdd
        dfRDD.take(10).foreach(println)
        var battle_id = dfRDD.map(x => x(1)).filter(x => x != null) 
        battle_id.take(30).foreach(println)
        var event_timestamp = dfRDD.map(x => x(4)).filter(x => x != null)
        event_timestamp.take(30).foreach(println)
        //var platform_count = dfRDD.map(x => x(8)).filter(x => x != null).map(x => (x(8),1))
        var platform_count = dfRDD.map(x => (x(8),1))
        platform_count.take(30).foreach(println)


        // val lines = sc.textFile(filePath)
        // val words = lines.flatMap(line => line.split(" "))
        // val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
        //counts.collect().foreach(println)
        sc.stop()
        print ("SparkRDDHelloworld")
        print (">>>>>>>>>>")
}
 } 