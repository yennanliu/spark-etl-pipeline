package sparkhelloworld 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, FloatType}
import org.apache.spark.sql.Row
import org.apache.spark.sql.DataFrame
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object SparkTitanicETL{

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
        // S3 
        val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
        val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
        sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
        sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)

        sc.setLogLevel("ERROR")

        // UDF 
        def modify_pclass()(df: DataFrame): DataFrame = {
          df.withColumn("Pass_class", lit("Pclass"))
        }

        def modify_ticket()(df: DataFrame): DataFrame = {
          df.withColumn("Ticket_type", lit("Ticket"))
        }   

        def modify_df()(df: DataFrame): DataFrame = {
          df.transform(modify_pclass())
            .transform(modify_ticket())
        }

        // UDF 

        print (">>>>>>>>>>")
        val dataSchema = (new StructType)
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
                      .schema(dataSchema)
                      .option("header", "true")
                      .option("inferSchema", "true")
                      .option("delimiter", ",")
                      .load(filePath)

        val df_repartition = df.repartition(1000)
        var df_repartition_modified =  df_repartition.transform(modify_df())

        // show the df schema / df 
        df.printSchema()
        df_repartition.show()
        df_repartition_modified.show()

        println (">>>>>>>>>> write to csv...")
        //var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-mm").format(LocalDateTime.now)
        var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm").format(LocalDateTime.now)
        var file_name = "output/SparkTitanicETL/output_" + current_time
        //df_repartition.saveAsTextFile(file_name)

        println (">>>>>>>>>> write to s3...")
        var s3_file_name = "s3a://suntory-data/etl_output/SparkTitanicETL_output_" + current_time
        df_repartition_modified.coalesce(1).write
                .format("com.databricks.spark.csv")
                .option("header", "true")
                .save(s3_file_name)

        sc.stop() 
}
 } 