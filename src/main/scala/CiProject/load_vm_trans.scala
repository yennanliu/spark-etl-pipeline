package CiProject 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, FloatType}
import org.apache.spark.sql.Row
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object load_vm_trans{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("load_vm_trans")

        var sc = new SparkContext(conf)

        val spark = SparkSession
                    .builder
                    .appName("Spark sql demo")
                    .config("spark.master", "local")
                    .getOrCreate()

        import spark.implicits._

        val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
        val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
        sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
        sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)

        // load s3 data 
        var s3_file_path = "s3a://suntory-data/filtered_10_vm_transaction/filtered_10_vm_transaction_2019.csv"
        val df = spark.read.format("csv")
                      .option("header", "true")
                      .option("inferSchema", "true")
                      .option("delimiter", ",")
                      .load(s3_file_path)
        df.printSchema()
        df.createOrReplaceTempView("transaction")
        //var df_ =  spark.sql("SELECT * FROM transaction limit 10")
        spark.sql("SELECT * FROM transaction limit 10").show()
        sc.stop()
}
 } 