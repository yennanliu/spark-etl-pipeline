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

object aggre_vm_trans{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("aggre_vm_trans")

        var sc = new SparkContext(conf)

        val spark = SparkSession
                    .builder
                    .appName("aggre_vm_trans")
                    .config("spark.master", "local")
                    .getOrCreate()

        import spark.implicits._

        val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
        val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
        sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
        sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)

        // load s3 data 
        var s3_file_path = "s3a://suntory-data/etl_test_data/filtered_10_vm_transaction_2019.csv"

        var df = spark.read
                      .format("csv") 
                      .option("header", "true")       
                      .option("delimiter", ",") 
                      .load(s3_file_path)
                      
        df.printSchema()

        println (">>>>>>>>>> write to s3...")
        df.createOrReplaceTempView("transaction")
        var df_ = spark.sql("SELECT sales_date as sales_date, sum(sales_quantity) as day_sale_quantity FROM transaction group by 1 order by 1 limit 10")
        var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm").format(LocalDateTime.now)
        var s3_file_name = "s3a://suntory-data/etl_output/aggre_vm_trans_" + current_time
        df_.coalesce(1)
           .write
           .format("com.databricks.spark.csv")
           .option("header", "true")
           .save(s3_file_name)

        sc.stop()
}
 } 