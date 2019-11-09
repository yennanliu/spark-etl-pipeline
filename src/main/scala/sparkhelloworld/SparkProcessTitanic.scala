package sparkhelloworld 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, FloatType}
import org.apache.spark.sql._
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

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
        val dataScheme = (new StructType)
                        .add("PassengerId", IntegerType)
                        .add("Survived", IntegerType)
                        .add("Pclass", IntegerType)
                        .add("Name", StringType)
                        .add("Sex", StringType)
                        .add("Age", FloatType)
                        .add("SibSp", IntegerType)
                        .add("Parch", IntegerType)
                        .add("Ticket", StringType)
                        .add("Fare", FloatType)
                        .add("Cabin", StringType)
                        .add("Embarked", StringType)

        var filePath = "data/titanic_train.csv"
        val df = spark.read.format("csv")
                      .schema(dataScheme)
                      .option("header", "true")
                      .option("inferSchema", "true")
                      .option("delimiter", ",")
                      .load(filePath)

        // show the df schema 
        df.printSchema()

        // >>>>>>>>>>>>> UDF 
        val embarked: (String => String) = {
          case "" => "S"
          case null =>"S"
          case a  => a
            }
        val embarkedUDF = udf(embarked)

        // >>>>>>>>>>>>> SQL 
        df.createOrReplaceTempView("titanic")
        var sample_df =  spark.sql("SELECT * FROM titanic limit 10")
        sample_df.show()
        spark.sql("SELECT Sex, COUNT(*) FROM titanic GROUP BY 1").show()

        // >>>>>>>>>>>>> DF 
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
        df_passenger_pclass_age_fare_agg.show()

        //calculate average age for filling gaps in dataset
        val averageAge = df.select("Age")
          .agg(avg("Age"))
          .collect() match {
          case Array(Row(avg: Double)) => avg
          case _ => 0
        }

        //calculate average fare for filling gaps in dataset
        val averageFare = df.select("Fare")
          .agg(avg("Fare"))
          .collect() match {
          case Array(Row(avg: Double)) => avg
          case _ => 0
        } 

        println ("averageAge : " + averageAge)
        println ("averageFare : " + averageFare)

        // filter df 
        val filledDF = df.na.fill(Map("Fare" -> averageFare, "Age" -> averageAge)) // fill null with avg value 
        val filledDF2 = filledDF.withColumn("Embarked", embarkedUDF(filledDF.col("Embarked")))  
        val Array(trainingData, testData) = filledDF2.randomSplit(Array(0.7, 0.3))

        // >>>>>>>>>>>>> RDD 
        var passenger_pclass_age_fare_rdd = df_passenger_pclass_age_fare.rdd 
        var age_fare_count = passenger_pclass_age_fare_rdd.map( x => (x(2), x(3)))
        age_fare_count.take(20).foreach(println)

        var age = passenger_pclass_age_fare_rdd.map( x => (x(2)))
        age.take(20).foreach(println)

        print (">>>>>>>>>> write to csv...")
        var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now)
        var file_name = "output/SparkHelloWorld/output_" + current_time
        age.saveAsTextFile(file_name)

        sc.stop() 
}
 } 