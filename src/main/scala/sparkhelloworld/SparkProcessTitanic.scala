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

object SparkProcessTitanic{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("SparkProcessTitanic")

        var sc = new SparkContext(conf)

        val spark = SparkSession
                    .builder
                    .appName("Spark sql demo")
                    .config("spark.master", "local")
                    .getOrCreate()

        import spark.implicits._

        sc.setLogLevel("ERROR")
        print (">>>>>>>>>>")
        var filePath = "data/titanic_train.csv"
        val df = spark.read.format("csv")
                      .option("header", "true")
                      .option("inferSchema", "true")
                      .option("delimiter", ",")
                      .load(filePath)

        // show the df schema 
        df.printSchema()

        // SQL 
        df.createOrReplaceTempView("titanic")
        var sample_df =  spark.sql("SELECT * FROM titanic limit 10")
        sample_df.show()
        spark.sql("SELECT Sex, COUNT(*) FROM titanic GROUP BY 1").show()

        // DF 
        var df_passenger_pclass_age_fare = spark.sql(
            """SELECT PassengerId, 
                CAST(Pclass AS FLOAT), 
                CAST(Age AS FLOAT),
                CAST(Fare AS FLOAT)
                FROM titanic 
                WHERE Pclass IS NOT NULL
                AND Age IS NOT NULL
                AND Fare IS NOT NULL""")
        df_passenger_pclass_age_fare.printSchema()
        df_passenger_pclass_age_fare.show()
        var df_passenger_pclass_age_fare_agg = df_passenger_pclass_age_fare.groupBy("PassengerId").agg(
                                                 sum($"Age") as "sum_Age",
                                                 count($"Age") as "count_Age")
        df_passenger_pclass_age_fare_agg.take(30).foreach(println)

        sc.stop() 
}
 } 