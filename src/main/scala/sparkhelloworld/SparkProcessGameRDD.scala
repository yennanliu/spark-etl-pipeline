package sparkhelloworld 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.functions._
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

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
        // >>>>>>>>>> DF
        val df_agg = df.groupBy("user_id").agg(
        sum($"usd_cost") as "usd_sum",
        count($"battle_id") as "battle_count")
        df_agg.take(30).foreach(println)

        // >>>>>>>>>> RDD
        // game df rdd 
        var dfRDD = df.rdd
        dfRDD.take(10).foreach(println)
        // battle id 
        var battle_id = dfRDD.map(x => x(1)).filter(x => x != null) 
        battle_id.take(30).foreach(println)
        // event timestamp
        var event_timestamp = dfRDD.map(x => x(4)).filter(x => x != null)
        event_timestamp.take(30).foreach(println)

        val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
        var event_date = dfRDD.map(x => x(4))//.map(x => x.substring(0,10))
        event_date.take(30)
        // platform count
        var platform_count = dfRDD.map(x => (x(8),1)).reduceByKey{case (x, y) => x + y}
        platform_count.collect().foreach(println)

        // player payment aggregate (USD) via RDD  
        var player  = dfRDD.map(x => (x(18),x(16))).filter{case (x,y) => y != null}//.map{ case (x,y) => (x, y.toFloat)}
        player.take(30).foreach(println)

        // player payment aggregate (USD) via SQL 
        df.createOrReplaceTempView("war_data")
        spark.sql("SELECT user_id, sum(usd_cost) FROM war_data group by 1 order by 2 desc limit 3").collect().foreach(println)
    
        // sql UDF 
       // def string2float(s: String): String = {s.toFloat}
        def plushundred(s: Float): Float = {s.toFloat + 100}
        //val string2floatUdf = udf(string2float)
        //val plushundredUdf = udf(plushundred _)
        val plushundredUdf = udf[Float, Float](plushundred)
        df.filter($"usd_cost".isNotNull).select(plushundredUdf(col("usd_cost"))).collect().foreach(println)

        sc.stop()
        print ("SparkRDDHelloworld")
        print (">>>>>>>>>>")
}
 } 